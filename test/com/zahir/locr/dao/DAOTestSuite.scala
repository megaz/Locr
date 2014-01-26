package com.zahir.locr.dao

import org.scalatest.FunSuite
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.api.MongoDriver
import reactivemongo.api.DB
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
class DAOTestSuite extends FunSuite {

  lazy val driver = new MongoDriver
  val conn = driver.connection(List("localhost"))
  val database = DB("locr_test", conn)

  def createUserDao: UserDAO = {
    new UserDAO {
      val connection = conn
      override lazy val db = database
    }
  }
}