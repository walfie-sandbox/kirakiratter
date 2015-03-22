package com.github.walfie.kirakiratter.models

import org.joda.time.DateTime
import org.specs2.mutable._
import org.specs2.runner._
import org.specs2.specification._
import play.api.libs.json._

class UserSpec extends Specification {
  "User" should {
    "be JSON serializable" in {
      val user: User = User(
        id = "123",
        name = "example",
        iconUrl = "http://example.com/image.png",
        interactions = List.empty,
        updatedAt = new DateTime(123))

      val expected: JsValue = Json.obj(
        "id" -> "123",
        "name" -> "example",
        "iconUrl" -> "http://example.com/image.png",
        "interactions" -> JsArray(),
        "updatedAt" -> 123)

      Json.toJson(user) must_== expected
    }
  }
}

