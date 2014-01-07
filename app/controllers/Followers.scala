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
import play.api.cache.Cache
import reactivemongo.core.commands.Aggregate
import reactivemongo.core.commands.Unwind
import reactivemongo.core.commands.Match
import reactivemongo.bson.BSONDocument

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
object Followers extends ReactiveController[JsObject] {

  lazy val coll = db.collection[JSONCollection]("followers")

  override val reader = __.read[JsObject] keepAnd (
    (__ \ "followersid").readNullable[Option[List[String]]])

  def addFollower(userId: String, followId: String): Future[Boolean] = {
    Cache.remove("followerscount" + followId)
    Logger.info("user " + followId + " has a new follower " + userId)
    coll.update(
      Json.obj("userid" -> followId),
      Json.obj("$addToSet" -> Json.obj("followersId" -> userId))).map {

        case (updated) => true
        case t => false
      }
  }

    def removeFollower(userId: String, followId: String): Future[Boolean] = {
    Cache.remove("followerscount" + followId)
    Logger.info("user " + followId + " has a new follower " + userId)
    coll.update(
      Json.obj("userid" -> followId),
      Json.obj("$pull" -> Json.obj("followersId" -> userId))).map {

        case (updated) => true
        case t => false
      }
  }
    
  def userFollowersCount(userId: String) = Action.async  {

      val followingCount = Cache.getOrElse("followerscount" + userId, 3600 * 60) {
        Logger.info("Getting from mongo")
        coll.db.command(Aggregate("followers", Seq(Unwind("followersId"), Match(BSONDocument("userid" -> userId)))))
      }

      followingCount.map { count =>
        Ok(Json.toJson(count.toList.size))
      }
  }

}
