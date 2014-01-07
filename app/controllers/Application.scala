package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._
import scala.concurrent.Future

import reactivemongo.api._

import play.modules.reactivemongo.MongoController
import play.modules.reactivemongo.json.collection.JSONCollection
import scala.concurrent.ExecutionContext.Implicits.global

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
object Application extends Controller with MongoController {

  
  def collection: JSONCollection = db.collection[JSONCollection]("users")

  def create(name: String, age: Int) = Action.async {

    val json = Json.obj(
      "name" -> name,
      "age" -> age,
      "created" -> new java.util.Date().getTime())

    collection.insert(json).map(lastError =>
      Ok("Mongo LastError: %s".format(lastError)))
  }

  def createFromJson = Action.async(parse.json) { request =>
    case class Bod(s: String)
    collection.insert(request.body).map(lastError =>
      Ok("Mongo LastErorr:%s".format(lastError)))
  }
} 
