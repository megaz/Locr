package controllers

import scala.concurrent.ExecutionContext.Implicits.global

import play.api.libs.json.JsObject
import play.api.mvc.Action
import play.modules.reactivemongo.json.collection.JSONCollection



object Users extends ReactiveController[JsObject] {
    
  lazy val coll = db.collection[JSONCollection]("users")
  
  def findByUsername(username: String) = Action {
    Async {
      find("username", username)
    }
  }

  def findByEmail(email: String) = Action {
    Async {
      find("email", email)
    }
  }
}
