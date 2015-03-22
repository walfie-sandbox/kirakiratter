package com.github.walfie.kirakiratter.models

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
        interactions = List.empty)

      val expected: JsValue = Json.obj(
        "id" -> "123",
        "name" -> "example",
        "iconUrl" -> "http://example.com/image.png",
        "interactions" -> JsArray())

      Json.toJson(user) must_== expected
    }
  }
}

