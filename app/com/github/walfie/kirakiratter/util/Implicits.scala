package com.github.walfie.kirakiratter.util

import org.joda.time.{DateTime, DateTimeZone}
import reactivemongo.bson.{BSONHandler, BSONDateTime, Macros}

package object Implicits {
  implicit object BSONDateTimeHandler extends BSONHandler[BSONDateTime, DateTime] {
    def read(time: BSONDateTime) = new DateTime(time.value, DateTimeZone.UTC)
    def write(jdtime: DateTime) = BSONDateTime(jdtime.getMillis)
  }
}

