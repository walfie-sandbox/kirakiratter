package com.github.walfie.kirakiratter.util

import java.text.ParseException

import reactivemongo.api.MongoConnection.ParsedURI
import reactivemongo.api.{MongoDriver, DB, MongoConnection}
import scala.concurrent.ExecutionContext
import scala.util.{Success, Failure}

package object MongoHelpers {
  class SaveFailedException(
      message: String = null,
      cause: Throwable = null)
    extends RuntimeException(message, cause)

  class InvalidConfigurationException(
      message: String = null,
      cause: Throwable = null)
    extends RuntimeException(message, cause)

  private def parsedURIFromString(uri: String): ParsedURI = {
    val parsedURI: ParsedURI = MongoConnection.parseURI(uri) match {
      case Success(parsedURI) if parsedURI.db.isDefined =>
        parsedURI
      case Success(_) =>
        throw new InvalidConfigurationException(s"Missing database name in mongodb.uri '$uri'")
      case Failure(e) =>
        throw new InvalidConfigurationException(s"Invalid mongodb.uri '$uri'", e)
    }

    parsedURI
  }

  def dbFromURI(uri: String)(implicit ec: ExecutionContext): DB = {
    val parsedURI: ParsedURI = parsedURIFromString(uri)

    val driver: MongoDriver = new MongoDriver
    val connection: MongoConnection = driver.connection(parsedURI)
    DB(parsedURI.db.get, connection)
  }
}

