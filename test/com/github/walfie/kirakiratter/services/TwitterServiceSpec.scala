package com.github.walfie.kirakiratter.services

import com.github.walfie.kirakiratter.models.{Interaction, User}

import org.joda.time.DateTime
import org.specs2.mock._
import org.specs2.mutable._
import play.api.test.Helpers._
import play.api.test._
import scala.collection.JavaConverters._
import twitter4j.{Paging, ResponseList, Status, Twitter, UserMentionEntity}

class TwitterServiceSpec extends Specification with Mockito {
  "TwitterServiceImpl" >> {
    "getInteractions" should {
      // Creating mocks for twitter4j is heck
      def fakeStatus(toUserIds: List[Long]): Status = {
        val mentions: Array[UserMentionEntity] = toUserIds.map { id =>
          val mention: UserMentionEntity = mock[UserMentionEntity]
          mention.getId returns id
          mention
        }.toArray

        val status: Status = mock[Status]
        status.getUserMentionEntities returns mentions
      }

      "return the correct Interactions" in {
        val twitter: Twitter = mock[Twitter]
        val statuses: java.util.List[Status] = List(
          fakeStatus(List(1)),
          fakeStatus(List(1,2)),
          fakeStatus(List(1,2,3)),
          fakeStatus(List(1,2,3,4)),
          fakeStatus(List(1,2,3,4,5))).asJava
        val responses: ResponseList[Status] = mock[ResponseList[Status]]
        responses.iterator returns statuses.iterator

        val expected: Set[Interaction] = Set(
          Interaction("1", 5),
          Interaction("2", 4),
          Interaction("3", 3),
          Interaction("4", 2),
          Interaction("5", 1))

        twitter.getUserTimeline(any[Long], any[Paging]) returns responses
        val twitterService = new TwitterServiceImpl(twitter)
        twitterService.getInteractions(1, 2).toSet must_== expected
      }
    }

    "getRelatedIds" should {
      "return a list of following/follower IDs" in {
        val twitter: Twitter = mock[Twitter]
        def toIds(xs: List[Int]): twitter4j.IDs = {
          val ids: twitter4j.IDs = mock[twitter4j.IDs]
          ids.getIDs() returns xs.map(_.toLong).toArray
          ids
        }
        twitter.getFollowersIDs(any[Long], any[Int]) returns toIds((1 to 10).toList)
        twitter.getFriendsIDs(any[Long], any[Int]) returns toIds((5 to 15).toList)

        val twitterService = new TwitterServiceImpl(twitter)
        twitterService.getRelatedIds(123).sorted must_== ((1 to 15).toList)
      }
    }

    "getUser" should {
      "return the correct User" in {
        val user: User = User(
          id = "123",
          name = "Hello",
          iconUrl = "http://example.com/image.png")

        val tUser: twitter4j.User = mock[twitter4j.User]
        tUser.getName returns user.name
        tUser.getMiniProfileImageURL returns user.iconUrl

        val twitter: Twitter = mock[Twitter]
        twitter.showUser(any[Long]) returns tUser

        val twitterService = new TwitterServiceImpl(twitter)
        val receivedUser = twitterService.getUser(123)

        // Can't test for case class equality since updatedAt is dynamic
        receivedUser.id must_== user.id
        receivedUser.name must_== user.name
        receivedUser.iconUrl must_== user.iconUrl
        receivedUser.interactions must haveSize(0)
        (DateTime.now.getMillis - receivedUser.updatedAt.getMillis).toInt must be_<(5000)
      }
    }
  }
}

