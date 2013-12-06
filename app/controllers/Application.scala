package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._
import scala.concurrent.Future

import reactivemongo.api._

import play.modules.reactivemongo.MongoController
import play.modules.reactivemongo.json.collection.JSONCollection
import scala.concurrent.ExecutionContext.Implicits.global


object Application extends Controller with MongoController {

  
  def collection: JSONCollection = db.collection[JSONCollection]("users")

  def create(name: String, age: Int) = Action {
    
    Async {
      val json = Json.obj(
        "name" -> name,
        "age" -> age,
        "created" -> new java.util.Date().getTime())

      collection.insert(json).map(lastError =>
        Ok("Mongo LastError: %s".format(lastError)))
    }
  }

  def createFromJson = Action(parse.json) { request =>
    case class Bod(s:String)
    Async {
      collection.insert(request.body).map(lastError =>
        Ok("Mongo LastErorr:%s".format(lastError)))
    }
  }
}