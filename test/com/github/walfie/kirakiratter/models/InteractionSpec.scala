package com.github.walfie.kirakiratter.models

import org.specs2.mutable._
import org.specs2.runner._
import org.specs2.specification._
import play.api.libs.json._
import reactivemongo.bson.{BSON, BSONDocument}

class InteractionSpec extends Specification {
  "Interaction" should {
    val interaction: Interaction = Interaction(
      toUserId = "123",
      count = 10)

    "be JSON serializable" in {
      val expected: JsValue = Json.obj(
        "toUserId" -> "123",
        "count" -> 10)

      Json.toJson(interaction) must_== expected
    }

    "be BSON [de]serializable" in {
      val interactionBSON: BSONDocument = BSONDocument(
        "toUserId" -> "123",
        "count" -> 10)

      BSON.writeDocument(interaction) must_== interactionBSON
      BSON.readDocument[Interaction](interactionBSON) must_== interaction
    }
  }
}

