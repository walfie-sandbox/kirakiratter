package com.github.walfie.kirakiratter.dal

import play.modules.reactivemongo.ReactiveMongoPlugin
import reactivemongo.extensions.json.dao.JsonDao
import reactivemongo.api.DB
import scala.concurrent.ExecutionContext

import com.github.walfie.kirakiratter.models.User

class UserDao(db: DB)(implicit ec: ExecutionContext)
    extends JsonDao[User, String](db, "users")

