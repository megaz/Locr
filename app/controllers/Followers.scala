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

object Followers extends ReactiveController[JsObject] {

  lazy val coll = db.collection[JSONCollection]("followers")
  
    override val reader = __.read[JsObject] keepAnd (
      (__ \ "followersid").readNullable[Option[List[String]]])
}