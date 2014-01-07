package controllers

import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.json.JsObject
import play.api.mvc.Action
import play.modules.reactivemongo.json.collection.JSONCollection
import play.api.libs.json.Json
import models.User
import play.api.Logger

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
object Users extends ReactiveController[User] {
     
  lazy val coll = db.collection[JSONCollection]("users")
  
  def findByUsername(username: String) = Action.async { request =>
    Logger.info("findByUsername");
    find("username", username) 
  }

  def findByEmail(email: String) = Action.async { request =>
    Logger.info("findByEmail");
    find("email", email)
  }
  
}
