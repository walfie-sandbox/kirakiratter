package com.github.walfie.kirakiratter.services

import org.joda.time.DateTime
import org.specs2.mutable._
import org.specs2.runner._
import org.specs2.specification._
import play.api.libs.json._
import reactivemongo.api.collections.default.BSONCollection
import scala.concurrent.Future
import scala.concurrent.duration._

import com.github.walfie.kirakiratter.helpers.DBSpecHelper
import com.github.walfie.kirakiratter.models.{Interaction, User}

class MongoUsersServiceSpec extends Specification with DBSpecHelper {
  // TODO: Drop collection after tests
  val usersCollection: BSONCollection = db("users")
  val usersService: MongoUsersService = new MongoUsersService(usersCollection)

  "MongoUsersService" >> {
    "save and find" should {
      "return the saved user with interactions" in {
        var users = List(
          User(
            id = "1",
            name = "Hello",
            iconUrl = "http://example.com/image1.png",
            interactions = List(
              Interaction("12", 34),
              Interaction("56", 78)),
            updatedAt = DateTime.now),
          User(
            id = "2",
            name = "World",
            iconUrl = "http://example.com/image2.png",
            interactions = List.empty,
            updatedAt = new DateTime(0)))

        usersService.save(users) must be_==(users).await
        usersService.find(List("1", "2")) must be_==(users).await
      }
    }

    "find" should {
      "accept a minimum updatedAt date" in {
        var users = List(
          User(id = "100", updatedAt = new DateTime(0)),
          User(id = "101", updatedAt = new DateTime(1)),
          User(id = "102", updatedAt = new DateTime(2)),
          User(id = "103", updatedAt = new DateTime(3)),
          User(id = "104", updatedAt = new DateTime(4)))
        var minUpdatedAt: DateTime = new DateTime(2)
        var userIds = users.map(_.id)

        var result: Future[List[User]] = usersService.save(users).flatMap {
          _ => usersService.find(userIds, minUpdatedAt)
        }

        result must be_==(users.drop(2)).await
      }
    }
  }
}

