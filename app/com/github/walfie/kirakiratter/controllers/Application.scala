package com.github.walfie.kirakiratter.controllers

import org.joda.time.DateTime
import play.api._
import play.api.libs.json._
import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.{ExecutionContext, Future}
import twitter4j.Twitter

import com.github.walfie.kirakiratter.Kirakiratter
import com.github.walfie.kirakiratter.services._
import com.github.walfie.kirakiratter.models._

object Application
    extends ApplicationController
    with TwitterServiceComponent
    with UsersServiceComponent {
  lazy val usersService: MongoUsersService = Kirakiratter.usersService
  lazy val twitterService: TwitterService = Kirakiratter.twitterService

  def index = TODO
}

trait ApplicationController extends Controller {
  this: UsersServiceComponent with TwitterServiceComponent =>
  def user(id: Long): EssentialAction = Action {
    val users: List[User] = getUsers(id)
    val usersEdited: List[User] = users.map(_.copy(follows = None))

    Ok(Json.toJson(usersEdited))
  }

  def getUsers(id: Long): List[User] = {
    val followings: List[Long] = twitterService.getFollowingIds(id)
    val followers: List[Long] = twitterService.getFollowerIds(id)
    //val ids: List[Long] = (id :: followings ++ followers).distinct
    val ids: List[Long] = (id :: followings).distinct

    // TODO: this is trash
    ids.grouped(100).flatMap { (userIds: List[Long]) =>
      val users: List[User] = twitterService.getUsers(userIds) // TODO: Check mongo first

      users.sortBy(_.id != id.toString).map { (user: User) =>
        val interactions: List[Interaction] = twitterService.getInteractions(user.id.toLong)
        val followInfo: Option[FollowInfo] = if (user.id == id.toString) {
          Option(FollowInfo(
            followingIds = followings.map(_.toString),
            followerIds = followers.map(_.toString),
            updatedAt = user.updatedAt))
        } else None

        val updatedUser: User = user.copy(
          interactions = interactions,
          follows = followInfo)

        println(updatedUser) // DEBUG. TODO: remove

        usersService.save(List(updatedUser))
        updatedUser
      }
    }.toList
  }
}

