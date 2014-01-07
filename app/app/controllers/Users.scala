package controllers

import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.json.JsObject
import play.api.mvc.Action
import play.modules.reactivemongo.json.collection.JSONCollection
import play.api.libs.json.Json
import models.User
import play.api.Logger



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