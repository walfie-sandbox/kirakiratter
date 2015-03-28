package com.github.walfie.kirakiratter

import java.util.concurrent.TimeUnit

import com.typesafe.config.{Config, ConfigFactory}
import org.joda.time.Duration
import play.api.GlobalSettings
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import reactivemongo.api.DB
import reactivemongo.api.collections.default.BSONCollection
import twitter4j.conf.ConfigurationBuilder
import twitter4j.{Twitter, TwitterFactory}

import com.github.walfie.kirakiratter.util.MongoHelpers
import com.github.walfie.kirakiratter.services.{TwitterServiceImpl, MongoUsersService}

object Kirakiratter extends GlobalSettings {
  lazy val config: Config = ConfigFactory.load

  lazy val twitter: Twitter = {
    val c: Config = config.getConfig("twitter.oauth")

    val cb: ConfigurationBuilder = new ConfigurationBuilder()
      .setOAuthConsumerKey(c.getString("consumerKey"))
      .setOAuthConsumerSecret(c.getString("consumerSecret"))
      .setOAuthAccessToken(c.getString("accessToken"))
      .setOAuthAccessTokenSecret(c.getString("accessTokenSecret"));

    val twitter: Twitter = new TwitterFactory(cb.build).getInstance
    twitter
  }

  lazy val db: DB = {
    val uri: String = config.getString("mongodb.uri")
    MongoHelpers.dbFromURI(uri)
  }

  lazy val twitterService: TwitterServiceImpl = new TwitterServiceImpl(twitter)
  lazy val usersService: MongoUsersService = {
    val usersTTL: Duration = Duration.millis(
      config.getDuration("application.userTTL", TimeUnit.MILLISECONDS))

    val usersCollection: BSONCollection = db("users")
    new MongoUsersService(usersCollection, usersTTL)
  }
}

