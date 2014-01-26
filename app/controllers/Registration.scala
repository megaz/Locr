package controllers

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import models.User
import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc.Action
import play.api.mvc.Controller
import play.api.libs.json.JsError
import com.zahir.locr.dao.UserDAO
import play.api.mvc.SimpleResult
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
object Registration extends Controller {

  val userDAO = new UserDAO

  def createUser = Action.async(parse.json) { request =>
    Logger.debug(s"request contains ${request.body}")
    Future {
      request.body.validate[User].map {
        case (user) => create(user)
      }.recoverTotal {
        e => BadRequest(Json.obj("status" -> "error", "message" -> JsError.toFlatJson(e)))
      }

    }
  }

  private def create(user: User): SimpleResult = {
    //TODO: Check if user exists before creating new user
    Logger.debug(s"User ${user.username} created")
    userDAO.insert(user)
    Ok(s"User ${user.username} created")
  }

}