package controllers

import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import play.api.libs.json.Format
import play.api.libs.json.JsObject
import play.api.libs.json.Json
import play.api.libs.json.Json.toJsFieldJsValueWrapper
import play.api.mvc.SimpleResult
import play.autosource.reactivemongo.ReactiveMongoAutoSourceController
import reactivemongo.api.Cursor
import play.api.libs.json.JsArray
import scala.util.Failure

abstract class ReactiveController[T](implicit ctx: ExecutionContext, format: Format[T]) extends ReactiveMongoAutoSourceController[T] {

  def find(key: String, value: String): Future[SimpleResult] = {

    val cursor: Cursor[JsObject] = coll.
      find(Json.obj(key -> value)).
      cursor[JsObject]

    val futureList: Future[List[JsObject]] = cursor.collect[List]()

    futureList onFailure {
      case t => InternalServerError("An error has occured using find: " + t.getMessage)
    }

    futureList.map { item => Ok(Json.toJson(item)) }
  } 
  
}