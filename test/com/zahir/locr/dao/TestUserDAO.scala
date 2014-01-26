package com.zahir.locr.dao

import play.api.test.FakeApplication
import play.api.test.Helpers.running
import scala.concurrent.ExecutionContext.Implicits.global
import org.junit.Test
import org.junit.Assert
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.api.DB
import scalaz.std.effect.sql.connection
import reactivemongo.api.MongoDriver
import play.api.Logger
import models.User
import org.scalatest.BeforeAndAfter
import org.scalatest.FunSuite
import reactivemongo.core.commands.Drop
/**
 * Copyright 2013 Zahir Abdi (@megaz)
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
class TestUserDAO extends DAOTestSuite with BeforeAndAfter {

  val userDAO = createUserDao 
    
    new UserDAO {
    override def coll: JSONCollection = db.collection[JSONCollection]("users_test")
    lazy val driver = new MongoDriver
    val connection = driver.connection(List("localhost"))
    override lazy val db = DB("locr_test", connection)
  }

  test("Persist and find User from UserDAO") {
    running(FakeApplication()) {

      val userToPersist = new User("testUsername", "testPassword", "testEmail", None, None, None)
      userDAO.insert(userToPersist)

      userDAO.findByUsername("testUsername") onSuccess {
        case Some(x) => {
          Assert.assertEquals(userToPersist, x)
        }
        case None => {
          Assert.fail("Could not find user persisted")
        }
      }
    }
  }

  after {
    userDAO.coll.drop
  }
}