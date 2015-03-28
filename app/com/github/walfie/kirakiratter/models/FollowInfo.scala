package com.github.walfie.kirakiratter.models

import play.api.libs.json.{Json, Format}
import org.joda.time.DateTime

import reactivemongo.bson._

case class FollowInfo (
    followingIds: List[String] = List.empty,
    followerIds: List[String] = List.empty,
    updatedAt: DateTime = new DateTime(0))

object FollowInfo {
  implicit val followInfoFormat: Format[FollowInfo] = Json.format[FollowInfo]

  implicit object FollowInfoBSONReader extends BSONDocumentReader[FollowInfo] {
    def read(doc: BSONDocument): FollowInfo = FollowInfo(
      followingIds = doc.getAs[List[String]]("followingIds").get,
      followerIds = doc.getAs[List[String]]("followerIds").get,
      updatedAt = new DateTime(doc.getAs[BSONDateTime]("updatedAt").get.value))
  }

  implicit object InteractionBSONWriter extends BSONDocumentWriter[FollowInfo] {
    def write(followInfo: FollowInfo): BSONDocument = BSONDocument(
      "followingIds" -> followInfo.followingIds,
      "followerIds" -> followInfo.followerIds,
      "updatedAt" -> BSONDateTime(followInfo.updatedAt.getMillis))
  }
}


