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
import reactivemongo.core.commands.LastError
import reactivemongo.core.commands.Count
import reactivemongo.bson.BSONDocument
import reactivemongo.bson.BSONString
import reactivemongo.core.commands.Aggregate
import reactivemongo.core.commands.Match
import reactivemongo.core.commands.Unwind
import play.api.cache.Cache

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
object Following extends ReactiveController[JsObject] {

  lazy val coll = db.collection[JSONCollection]("following")

  override val reader = __.read[JsObject] keepAnd (
    (__ \ "followingid").readNullable[Option[List[String]]])

  def followAction(result: Int, userId: String, followUserId: String): SimpleResult = {
    if (result > 0) {
      Followers.addFollower(userId, followUserId)
      Logger.info("Updated and followed " + result);
      Ok("Followed user " + followUserId)
    } else {
      Ok("No documents updated ")
    }
  }

  def followUser() = Action.async(parse.json) { request =>
    val paramVal = request.body;
    val userId: String = (paramVal \ "userid").as[String]
    val followUserId: String = (paramVal \ "follow").as[String]
    Cache.remove("followcount" + userId)
      coll.update(
        Json.obj("userid" -> userId),
        Json.obj("$addToSet" -> Json.obj("followingId" -> followUserId))).map {

          case (updated) => followAction(updated.n, userId, followUserId)
          case t => {
            BadRequest("Could not follow user " + userId + " cause " + t.getMessage)
          }
    } 
  } 

  def unfollowUser() = Action(parse.json) { request =>

    val paramVal = request.body;
    val userId: String = (paramVal \ "userid").as[String]
    val followUserId: String = (paramVal \ "unfollow").as[String]	
    Followers.removeFollower(userId, followUserId)
    Cache.remove("followcount" + userId)
    coll.update(
      Json.obj("userid" -> userId),
      Json.obj("$pull" -> Json.obj("followingId" -> followUserId)))

    Ok("deleted")
  }

  def userFollowingCount(userId: String) = Action.async  {

      val followingCount = Cache.getOrElse("followcount" + userId, 3600 * 60) {
        Logger.info("Getting from mongo")
        coll.db.command(Aggregate("following", Seq(Unwind("followingId"), Match(BSONDocument("userid" -> userId)))))
      }

      followingCount.map { count =>
        Ok(Json.toJson(count.toList.size))
      }
  }
}
