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
abstract class ReactiveController[T](implicit ctx: ExecutionContext, format: Format[T]) extends ReactiveMongoAutoSourceController[T] {

  @deprecated("Use BaseDAO.find function instead", "")
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
