package com.github.walfie.kirakiratter.models

import play.api.libs.json.{Json, Format}
import org.joda.time.DateTime
import reactivemongo.bson.{BSONDocumentReader, BSONDocumentWriter, BSONDocument, BSONDateTime}

case class User (
    id: String = "",
    name: String = "",
    iconUrl: String = "",
    interactions: List[Interaction] = List.empty,
    updatedAt: DateTime = new DateTime(0))

object User {
  implicit val userFormat: Format[User] = Json.format[User]

  implicit object UserBSONReader extends BSONDocumentReader[User] {
    def read(doc: BSONDocument): User = User(
      id = doc.getAs[String]("_id").get,
      name = doc.getAs[String]("name").get,
      iconUrl = doc.getAs[String]("iconUrl").get,
      interactions = doc.getAs[List[Interaction]]("interactions").get,
      updatedAt = new DateTime(doc.getAs[BSONDateTime]("updatedAt").get.value))
  }

  implicit object UserBSONWriter extends BSONDocumentWriter[User] {
    def write(user: User): BSONDocument = BSONDocument(
      "_id" -> user.id,
      "name" -> user.name,
      "iconUrl" -> user.iconUrl,
      "interactions" -> user.interactions,
      "updatedAt" -> BSONDateTime(user.updatedAt.getMillis))
  }
}

