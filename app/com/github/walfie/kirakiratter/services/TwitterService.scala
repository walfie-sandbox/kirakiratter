package com.github.walfie.kirakiratter.services

import org.joda.time.DateTime
import twitter4j.{Paging, ResponseList, Status, Twitter}
import scala.collection.JavaConverters._

import com.github.walfie.kirakiratter.models.{Interaction, User}

trait TwitterService {
  def getInteractions(userId: Long, nTweets: Int): List[Interaction]
  def getFollowerIds(userId: Long): List[Long]
  def getFollowingIds(userId: Long): List[Long]
  def getUsers(userIds: Iterable[Long]): List[User]
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

  // TODO: Use cursor for >2k followers
  def getFollowerIds(userId: Long): List[Long] = {
    val followerIds: Array[Long] = twitter.getFollowersIDs(userId, -1).getIDs
    followerIds.toList
  }

  // TODO: Use cursor for >2k followings
  def getFollowingIds(userId: Long): List[Long] = {
    val followingIds: Array[Long] = twitter.getFriendsIDs(userId, -1).getIDs
    followingIds.toList
  }

  def getRelatedIds(userId: Long): List[Long] = {
    (getFollowerIds(userId) ++ getFollowingIds(userId)).distinct.toList
  }

  def getUsers(userIds: Iterable[Long]): List[User] = {
    var tUsers: List[twitter4j.User] = twitter.lookupUsers(userIds.toArray).asScala.toList
    tUsers.map { (tUser: twitter4j.User) =>
      User(
        id = tUser.getId.toString,
        name = tUser.getName,
        iconUrl = tUser.getMiniProfileImageURL,
        updatedAt = DateTime.now
      )
    }
  }

  // TODO: rate limiting checks
}

