package com.github.walfie.kirakiratter.services

import play.api.libs.iteratee.Enumerator
import scala.concurrent.{ExecutionContext, Future}
import reactivemongo.api.Cursor
import reactivemongo.bson.{BSON, BSONDocument}
import reactivemongo.api.collections.default.BSONCollection

import com.github.walfie.kirakiratter.models.User
import com.github.walfie.kirakiratter.util.MongoHelpers.SaveFailedException

trait UsersService {
  def save(users: Iterable[User]): Future[List[User]]
  def find(ids: Iterable[String]): Future[List[User]]
}

trait UsersServiceComponent {
  def usersService: UsersService
}

class MongoUsersService(collection: BSONCollection)(implicit ec: ExecutionContext) extends UsersService {
  def save(users: Iterable[User]): Future[List[User]] = {
    val savedUsers: List[Future[User]] = users.toList.map{ user =>
      collection.save(user).map { status =>
        if (status.ok) user
        else throw new SaveFailedException(s"Failed to save $user")
      }
    }
    Future.sequence(savedUsers)
  }

  def find(ids: Iterable[String]): Future[List[User]] = {
    val query = BSONDocument("_id" -> BSONDocument("$in" -> ids))
    val cursor: Cursor[User] = collection.find(query).cursor[User]
    cursor.collect[List]()
  }
}

