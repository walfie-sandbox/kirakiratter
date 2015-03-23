package com.github.walfie.kirakiratter.services

import org.joda.time.DateTime
import twitter4j.{Paging, ResponseList, Status, Twitter}
import scala.collection.JavaConverters._

import com.github.walfie.kirakiratter.models.{Interaction, User}

trait TwitterService {
  def getInteractions(userId: Long, nTweets: Int): List[Interaction]
  def getRelatedIds(userId: Long): List[Long] // Followings and followers
  def getUser(userId: Long): User // With empty interactions list
}

trait TwitterServiceComponent {
  def twitterService: TwitterService
}

class TwitterServiceImpl(val twitter: Twitter) extends TwitterService {
  def getInteractions(userId: Long, nTweets: Int = 200): List[Interaction] = {
    val paging: Paging = new Paging(1, nTweets)
    val statuses: ResponseList[Status] = twitter.getUserTimeline(userId, paging)

    val userIds: List[Long] = for {
      status <- statuses.asScala.toList
      mention <- status.getUserMentionEntities
      id = mention.getId
    } yield id

    userIds.groupBy(identity).map { case (id, ids) =>
      Interaction(toUserId = id.toString, count = ids.length)
    }.toList
  }

  def getRelatedIds(userId: Long): List[Long] = {
    val followerIds: Array[Long] = twitter.getFollowersIDs(userId, -1).getIDs
    val followingIds: Array[Long] = twitter.getFriendsIDs(userId, -1).getIDs

    (followerIds ++ followingIds).distinct.toList
  }

  def getUser(userId: Long): User = {
    var tUser: twitter4j.User = twitter.showUser(userId)
    User(
      id = userId.toString, // is it worth checking if the IDs aren't the same?
      name = tUser.getName,
      iconUrl = tUser.getMiniProfileImageURL,
      updatedAt = DateTime.now
    )
  }

  // TODO: rate limiting checks
}

