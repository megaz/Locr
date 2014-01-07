package models

import play.api.libs.json.Json
import org.joda.time.DateTime

case class User(username: String, password: String, email: String, name: Option[String], phone : Option[String], registerDate : DateTime)

object User{
  implicit val fmt = Json.format[User]
}
