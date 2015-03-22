package com.github.walfie.kirakiratter.models

import play.api.libs.json.{Json, Format}

case class User (
    id: String = "",
    name: String = "",
    iconUrl: String = "",
    interactions: List[Interaction] = List.empty)

object User {
  implicit val userFormat: Format[User] = Json.format[User]
}

