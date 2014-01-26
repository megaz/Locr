import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "LocrDataAPI"
  val appVersion      = "1.0-SNAPSHOT"

  val mandubianRepo = Seq(
    "Mandubian repository snapshots" at "https://github.com/mandubian/mandubian-mvn/raw/master/snapshots/",
    "Mandubian repository releases" at "https://github.com/mandubian/mandubian-mvn/raw/master/releases/"
  )

  val sonatypeRepo = Seq(
    "Sonatype Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"
  )
  val appDependencies = Seq(
    cache,
    filters
)

  val main = play.Project(appName, appVersion, appDependencies).settings(
    resolvers ++= mandubianRepo ++ sonatypeRepo,
    resolvers += Resolver.url("sbt-plugin-snapshots", new URL("http://repo.scala-sbt.org/scalasbt/sbt-plugin-snapshots/"))(Resolver.ivyStylePatterns),
resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
    libraryDependencies ++= Seq(
      "com.google.code.gson" % "gson" % "2.2.4",
      "org.specs2"        %% "specs2"              % "1.13"        % "test",
      "junit"              % "junit"               % "4.8"         % "test",
      "com.github.t3hnar" % "scala-bcrypt_2.10" % "2.3",
      "com.dongxiguo" % "memcontinuationed_2.10" % "0.3.1",
      "org.reactivemongo" %% "play2-reactivemongo" % "0.10.1",
      "play-autosource" %% "core" % "2.0",
      "org.scalatest" % "scalatest_2.10" % "2.0" % "test"
    )

  )

}
