package controllers

import scala.concurrent._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.libs.iteratee.Done
import reactivemongo.api._
import reactivemongo.bson.BSONObjectID
import play.modules.reactivemongo._
import play.modules.reactivemongo.json.collection.JSONCollection
import play.autosource.reactivemongo._
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.Play.current
import reactivemongo.bson.BSONDocument
import com.github.t3hnar.bcrypt._
import play.mvc.Result

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
object Authentication extends ReactiveController[JsObject] {

  object AuthMessages {
    val UserUnauthorized = "Username/Password Incorrect"
  }

  lazy val coll = db.collection[JSONCollection]("users")

  def checkPassword(requestPassword: String, hashPassword: JsObject): Boolean = {
    val hashPass: String = (hashPassword \ "password").as[String]

    return requestPassword.isBcrypted(hashPass)

  }

  /**
   * We can do some stuff such as create a session for the user and
   * even add stats to find out how many people logged in today etc.
   *
   */
  def loggedInAction(username: String): SimpleResult = {
    Ok("Authorized").withSession("username" -> username)
  }

  def logout = Action {
    Ok("logged out").withNewSession
  }

  def login = Action.async(parse.json) { request =>

    val paramVal = request.body;

    val username: String = (paramVal \ "username").as[String]
    val password: String = (paramVal \ "password").as[String]

      val cursor = coll.find(Json.obj("username" -> username)).cursor[JsObject]
      val futureJsValue: Future[Option[JsObject]] = cursor.headOption

      futureJsValue onFailure {
        case t => InternalServerError("An unexpected server error has occured: " + t.getMessage)
      }

      futureJsValue.map {
        case Some(x) => if (checkPassword(password, x)) loggedInAction(username) else Unauthorized(AuthMessages.UserUnauthorized)
        case None => Unauthorized(AuthMessages.UserUnauthorized)
    }
  }
}
