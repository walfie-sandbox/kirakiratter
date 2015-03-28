package com.github.walfie.kirakiratter.services

import com.github.walfie.kirakiratter.models.{Interaction, User}

import org.joda.time.DateTime
import org.specs2.mock._
import org.specs2.mutable._
import play.api.test.Helpers._
import play.api.test._
import scala.collection.JavaConverters._
import twitter4j.{Paging, ResponseList, Status, Twitter, UserMentionEntity}

class TwitterServiceSpec extends Specification
    with Mockito with TwitterServiceSpecHelpers {
  "TwitterServiceImpl" >> {
    "getInteractions" should {
      "return the correct Interactions" in {
        val twitter: Twitter = mock[Twitter]
        val statuses: ResponseList[Status] = fakeResponseList[Status](
          fakeStatus(List(1)),
          fakeStatus(List(1,2)),
          fakeStatus(List(1,2,3)),
          fakeStatus(List(1,2,3,4)),
          fakeStatus(List(1,2,3,4,5)))

        val expected: Set[Interaction] = Set(
          Interaction("1", 5),
          Interaction("2", 4),
          Interaction("3", 3),
          Interaction("4", 2),
          Interaction("5", 1))

        twitter.getUserTimeline(any[Long], any[Paging]) returns statuses
        val twitterService = new TwitterServiceImpl(twitter)
        twitterService.getInteractions(1, 2).toSet must_== expected
      }
    }

    "getFollowingIds" should {
      "return a list of following IDs" in {
        val twitter: Twitter = mock[Twitter]

        twitter.getFriendsIDs(any[Long], any[Int]) returns toTwitterIds(0 to 10)

        val twitterService = new TwitterServiceImpl(twitter)
        twitterService.getFollowingIds(123).sorted must_== ((0 to 10).toList)
      }
    }

    "getFollowerIds" should {
      "return a list of follower IDs" in {
        val twitter: Twitter = mock[Twitter]

        twitter.getFollowersIDs(any[Long], any[Int]) returns toTwitterIds(0 to 10)

        val twitterService = new TwitterServiceImpl(twitter)
        twitterService.getFollowerIds(123).sorted must_== ((0 to 10).toList)
      }
    }

    "getRelatedIds" should {
      "return a list of following/follower IDs" in {
        val twitter: Twitter = mock[Twitter]

        twitter.getFollowersIDs(any[Long], any[Int]) returns toTwitterIds(1 to 10)
        twitter.getFriendsIDs(any[Long], any[Int]) returns toTwitterIds(5 to 15)

        val twitterService = new TwitterServiceImpl(twitter)
        twitterService.getRelatedIds(123).sorted must_== ((1 to 15).toList)
      }
    }

    "getUsers" should {
      "return the correct Users" in {
        val user1: User = User(
          id = "123",
          name = "Hello",
          iconUrl = "http://example.com/image1.png")
        val user2: User = User(
          id = "456",
          name = "World",
          iconUrl = "http://example.com/image2.png")

        val tUsers: ResponseList[twitter4j.User] = fakeResponseList[twitter4j.User](
          fakeTwitterUser(user1),
          fakeTwitterUser(user2))

        val twitter: Twitter = mock[Twitter]
        twitter.lookupUsers(any[Array[Long]]) returns tUsers

        val twitterService = new TwitterServiceImpl(twitter)
        val receivedUsers: List[User] = twitterService.getUsers(List(123, 456))

        // Check that updatedAt date is recent
        receivedUsers must contain { (user: User) =>
          (DateTime.now.getMillis - user.updatedAt.getMillis).toInt must be_<(5000)
        }.forall

        // Can't test for case class equality since updatedAt is dynamic
        receivedUsers.map(_.copy(updatedAt = new DateTime(0))) must_==
          List(user1, user2)
      }
    }
  }
}

trait TwitterServiceSpecHelpers extends Mockito {
  def fakeStatus(toUserIds: List[Long]): Status = {
    val mentions: Array[UserMentionEntity] = toUserIds.map { id =>
      val mention: UserMentionEntity = mock[UserMentionEntity]
      mention.getId returns id
      mention
    }.toArray

    val status: Status = mock[Status]
    status.getUserMentionEntities returns mentions
  }

  def fakeResponseList[T](elements: T*): ResponseList[T] = {
    val rl: ResponseList[T] = mock[ResponseList[T]]
    rl.iterator returns elements.iterator.asJava
    rl
  }

  def toTwitterIds(ids: Iterable[Int]): twitter4j.IDs = {
    val tIds: twitter4j.IDs = mock[twitter4j.IDs]
    tIds.getIDs() returns ids.map(_.toLong).toArray
    tIds
  }

  def fakeTwitterUser(user: User): twitter4j.User = {
    val tUser: twitter4j.User = mock[twitter4j.User]
    tUser.getId returns user.id.toLong
    tUser.getScreenName returns user.name
    tUser.getMiniProfileImageURL returns user.iconUrl
    tUser
  }
}

