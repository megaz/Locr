package com.zahir.locr.dao

import play.modules.reactivemongo.json.collection.JSONCollection
import models.User
import scala.concurrent.Future
import reactivemongo.core.commands.LastError
import scala.concurrent.ExecutionContext.Implicits.global


/**
 * Copyright 2014 Zahir Abdi (@megaz)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
class UserDAO extends BaseDAO[User]("users") {

  def coll: JSONCollection = db.collection[JSONCollection]("users")

  def findByUsername(username: String): Future[Option[User]] = find("username", username)

  def findByEmail(email: String): Future[Option[User]] = find("email", email)

  def createUser(user: User): Future[LastError] = insert(user)

} 

