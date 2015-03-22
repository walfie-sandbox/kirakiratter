package com.github.walfie.kirakiratter.models

import play.api.libs.json.{Json, Format}

case class Interaction (
    toUserId: String = "",
    count: Int = 0)

object Interaction {
  implicit val interactionFormat: Format[Interaction] = Json.format[Interaction]
}

