package com.github.walfie.kirakiratter.models

import org.specs2.mutable._
import org.specs2.runner._
import org.specs2.specification._
import play.api.libs.json._

class InteractionSpec extends Specification {
  "Interaction" should {
    "be JSON serializable" in {
      val interaction: Interaction = Interaction(
        toUserId = "123",
        count = 10)

      val expected: JsValue = Json.obj(
        "toUserId" -> "123",
        "count" -> 10)

      Json.toJson(interaction) must_== expected
    }
  }
}

