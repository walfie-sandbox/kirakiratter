package com.github.walfie.kirakiratter.controllers

import com.github.walfie.kirakiratter.controllers
import com.github.walfie.kirakiratter.Kirakiratter
import com.github.walfie.kirakiratter.services._
import com.github.walfie.kirakiratter.models._

import play.api._
import play.api.libs.json._
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}
import twitter4j.Twitter

object Application
    extends ApplicationController
    with TwitterServiceComponent
    with UsersServiceComponent {
  lazy val usersService: MongoUsersService = Kirakiratter.usersService
  lazy val twitterService: TwitterService = Kirakiratter.twitterService

  def index = TODO
}

trait ApplicationController extends Controller {
  def user(id: Long): EssentialAction = TODO
}

