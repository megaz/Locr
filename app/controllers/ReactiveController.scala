package controllers

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.util.Failure

import play.api.libs.json.Format
import play.api.libs.json.JsObject
import play.api.libs.json.Json
import play.api.libs.json.Json.toJsFieldJsValueWrapper
import play.autosource.reactivemongo.ReactiveMongoAutoSourceController
import reactivemongo.api.Cursor

abstract class ReactiveController[T](implicit ctx: ExecutionContext, format: Format[T]) extends ReactiveMongoAutoSourceController[T] {

  def find(key: String, value: String) = Future {
    Async {
      val cursor: Cursor[JsObject] = coll.
        find(Json.obj(key -> value)).
        cursor[JsObject]

      val futureUsersList: Future[List[JsObject]] = cursor.toList()

      futureUsersList onComplete {
        case Failure(t) => Ok("An error has occured: " + t.getMessage)
      }
      futureUsersList.map { users =>
        Ok(Json.toJson(users))
      }
    }
  }
} 