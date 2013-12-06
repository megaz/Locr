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

object Following extends ReactiveController[JsObject] {

  lazy val coll = db.collection[JSONCollection]("following")

  override val reader = __.read[JsObject] keepAnd (
    (__ \ "followingid").readNullable[Option[List[String]]])

  def followAction(result: Int, id: String): SimpleResult = {
    if (result > 0) {
      Logger.info("Updated and followed " + result);
      Ok("Followed user " + id)
    } else {
      Ok("No documents updated ")
    }
  }

  def followUser() = Action(parse.json) { request =>
    val paramVal = request.body;
    val id: String = (paramVal \ "userid").as[String]
    val userId: String = (paramVal \ "follow").as[String]
    Async {
      coll.update(
        Json.obj("userid" -> id),
        Json.obj("$addToSet" -> Json.obj("followingId" -> userId))).map {

          case (updated) => followAction(updated.n, id)
          case t => {
            BadRequest("Could not follow user " + id + " cause " + t.getMessage)
          }
        }
    }
  }

  /*
    def userFollowingCount(userId : String) = Action {
    Async {
    	
      val command = Aggregate("following", Seq(
    //  GroupField("followingId")("followingId" -> SumValue(1)),
      Match(BSONDocument("userid" -> userId))
    ))
    val result = coll.db.command(command)
      result.map { value => {
       Ok("got value" + value )
      }

    }
  } 
 } 
   */
  def userFollowingCount(userId: String) = Action {
    Async {

      val followingCount = Cache.getOrElse("followcount" + userId, 5) {
        Logger.info("Getting from mongo")
        coll.db.command(Aggregate("following", Seq(Unwind("followingId"), Match(BSONDocument("userid" -> userId)))))
      }

      followingCount.map { count =>
        Ok(Json.toJson(count.toList.size))
      }
    }
  }

}