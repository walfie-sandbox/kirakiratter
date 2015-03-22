package com.github.walfie.kirakiratter.models

import play.api.libs.json.{Json, Format}

import reactivemongo.bson._

case class Interaction (
    toUserId: String = "",
    count: Int = 0)

object Interaction {
  implicit val interactionFormat: Format[Interaction] = Json.format[Interaction]

  implicit object InteractionBSONReader extends BSONDocumentReader[Interaction] {
    def read(doc: BSONDocument): Interaction = Interaction(
      toUserId = doc.getAs[String]("toUserId").get,
      count = doc.getAs[Int]("count").get)
  }

  implicit object InteractionBSONWriter extends BSONDocumentWriter[Interaction] {
    def write(interaction: Interaction): BSONDocument = BSONDocument(
      "toUserId" -> interaction.toUserId,
      "count" -> interaction.count)
  }
}

