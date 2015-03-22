package com.github.walfie.kirakiratter.models

import org.joda.time.{DateTime, DateTimeZone}
import org.specs2.mutable._
import org.specs2.runner._
import org.specs2.specification._
import play.api.libs.json._
import reactivemongo.bson.{BSON, BSONArray, BSONDateTime, BSONDocument}

class UserSpec extends Specification {
  "User" should {
    val user: User = User(
      id = "123",
      name = "example",
      iconUrl = "http://example.com/image.png",
      interactions = List.empty,
      updatedAt = new DateTime(31415926))

    "be JSON serializable" in {
      val expected: JsValue = Json.obj(
        "id" -> "123",
        "name" -> "example",
        "iconUrl" -> "http://example.com/image.png",
        "interactions" -> JsArray(),
        "updatedAt" -> 31415926)

      Json.toJson(user) must_== expected
    }

    "be BSON [de]serializable" in {
      val userBSON: BSONDocument = BSONDocument(
        "_id" -> "123",
        "name" -> "example",
        "iconUrl" -> "http://example.com/image.png",
        "interactions" -> BSONArray(),
        "updatedAt" -> BSONDateTime(31415926))

      BSON.writeDocument(user) must_== userBSON
      BSON.readDocument[User](userBSON) must_== user
    }
  }
}

