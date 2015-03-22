package com.github.walfie.kirakiratter.services

import scala.concurrent.Future

import com.github.walfie.kirakiratter.models.User
import com.github.walfie.kirakiratter.dal.UserDao

trait UsersService {
  def save(users: Iterable[User]): Future[List[User]]
  def find(ids: Iterable[String]): Future[List[User]]
}

trait UsersServiceComponent {
  def usersService: UsersService
}

import scala.concurrent.ExecutionContext
import play.api.libs.json.Json
class MongoUsersService(dao: UserDao)(implicit ec: ExecutionContext) extends UsersService {
  import com.github.walfie.kirakiratter.util.Implicits.BSONDateTimeHandler

  def save(users: Iterable[User]): Future[List[User]] = {
    dao.bulkInsert(users).map(_ => users.toList)
  }

  def find(ids: Iterable[String]): Future[List[User]] = {
    dao.findAll(Json.obj("id" -> Json.obj("$in" -> ids)))
  }
}

