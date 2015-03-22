package com.github.walfie.kirakiratter.services

import com.github.walfie.kirakiratter.helpers.DBSpecHelper
import com.github.walfie.kirakiratter.models.User

import org.joda.time.DateTime
import org.specs2.mutable._
import org.specs2.runner._
import org.specs2.specification._
import play.api.libs.json._

import scala.concurrent.Await
import scala.concurrent.duration._

class MongoUsersServiceSpec extends Specification with DBSpecHelper {
  "MongoUsersService" >> {
    val usersService = new MongoUsersService(userDao)

    "save" should {
      "save user" in {
        var user = User(
          id = "1",
          name = "Hello",
          iconUrl = "http://example.com",
          interactions = List.empty,
          updatedAt = new DateTime(0))

        usersService.save(List(user))
        usersService.find(List("1")) must be_==(List(user)).await
        Await.result(userDao.drop(), Duration(1000, MILLISECONDS)) // Put in a `before` block
      }
    }
  }
}

