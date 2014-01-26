package com.zahir.locr.dao

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import play.api.Play.current
import play.api.libs.json.Json
import play.api.libs.json.Json.toJsFieldJsValueWrapper
import play.api.libs.json.Reads
import play.api.libs.json.Writes
import play.modules.reactivemongo.ReactiveMongoPlugin
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.core.commands.LastError

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
abstract class BaseDAO[T](collectionName: String)(implicit reads: Reads[T], writes: Writes[T]) {

  lazy val db = ReactiveMongoPlugin.db
  def collection: JSONCollection = db.collection[JSONCollection](collectionName)

  def find(key: String, value: String): Future[Option[T]] = {
    collection.find(Json.obj(key -> value)).cursor[T].headOption
  }

  def find(t: T): Future[Option[T]] = {
    collection.find(t).cursor[T].headOption
  }

  def insert(t: T): Future[LastError] = collection.insert(t)
 
}