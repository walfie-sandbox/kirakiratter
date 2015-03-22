package com.github.walfie.kirakiratter.models

import play.api.libs.json.{Json, Format}
import org.joda.time.DateTime

case class User (
    id: String = "",
    name: String = "",
    iconUrl: String = "",
    interactions: List[Interaction] = List.empty,
    updatedAt: DateTime = new DateTime(0))

object User {
  implicit val userFormat: Format[User] = Json.format[User]
}

