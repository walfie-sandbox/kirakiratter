package com.github.walfie.kirakiratter.services

import com.github.walfie.kirakiratter.models.{Interaction, User}
import com.github.walfie.kirakiratter.helpers.DBSpecHelper

import org.joda.time.DateTime
import org.specs2.mutable._
import org.specs2.runner._
import org.specs2.specification._
import play.api.libs.json._

import scala.concurrent.Await
import scala.concurrent.duration._

class MongoUsersServiceSpec extends Specification with DBSpecHelper {
  "MongoUsersService" >> {
    "save and find" should {
      "return the saved user with interactions" in {
        var interactions = List(
          Interaction("12", 34),
          Interaction("56", 78))
        var user = User(
          id = "1",
          name = "Hello",
          iconUrl = "http://example.com",
          interactions = interactions,
          updatedAt = DateTime.now)

        usersService.save(List(user)) must be_==(List(user)).await
        usersService.find(List("1")) must be_==(List(user)).await
      }
    }
  }
}

