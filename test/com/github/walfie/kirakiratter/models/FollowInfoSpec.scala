package com.github.walfie.kirakiratter.models

import org.joda.time.DateTime
import org.specs2.mutable._
import org.specs2.runner._
import org.specs2.specification._
import play.api.libs.json._
import reactivemongo.bson.{BSON, BSONArray, BSONDateTime, BSONDocument}

class FollowInfoSpec extends Specification {
  "FollowInfo" should {
    val followInfo: FollowInfo = FollowInfo(
      followingIds = List("123", "456"),
      followerIds = List("789", "0"),
      updatedAt = new DateTime(31415926))

    "be JSON serializable" in {
      val expected: JsValue = Json.obj(
        "followingIds" -> List("123", "456"),
        "followerIds" -> List("789", "0"),
        "updatedAt" -> 31415926)

      Json.toJson(followInfo) must_== expected
    }

    "be BSON [de]serializable" in {
      val followInfoBSON: BSONDocument = BSONDocument(
        "followingIds" -> BSONArray("123", "456"),
        "followerIds" -> BSONArray("789", "0"),
        "updatedAt" -> BSONDateTime(31415926))

      BSON.writeDocument(followInfo) must_== followInfoBSON
      BSON.readDocument[FollowInfo](followInfoBSON) must_== followInfo
    }
  }
}


