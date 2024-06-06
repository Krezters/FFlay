name := "play-ffmpeg"
organization := "rdanilov"

val sharedSettings = List(
  version := "0.1",
  scalaVersion := "2.11.5"
)

lazy val db = Project(id = "db", base = file("modules/db"))
  .settings(sharedSettings :_*)
  .settings(
    libraryDependencies += "com.typesafe" % "config" % "1.4.3",
    libraryDependencies ++= Seq(
      "org.liquibase" % "liquibase-core" % "3.4.2",
      "org.squeryl" %% "squeryl" % "0.9.5-7",
      "com.zaxxer" % "HikariCP" % "4.0.1",
      "org.postgresql" % "postgresql" % "42.3.1"
    )
  )

lazy val di = Project(id = "di", base = file("modules/di"))
  .settings(sharedSettings :_*)
  .settings(libraryDependencies += Dependencies.Guice)

lazy val ffmpeg = Project(id = "ffmpeg", base = file("modules/ffmpeg"))
  .dependsOn(di, db)
  .settings(sharedSettings :_*)

lazy val auth = Project(id = "auth", base = file("modules/auth"))
  .settings(sharedSettings :_*)
  .enablePlugins(PlayScala)

lazy val fflay = Project(id = "fflay", base = file("modules/fflay"))
  .dependsOn(di, ffmpeg, auth, db)
  .settings(sharedSettings :_*)
  .enablePlugins(PlayScala)

lazy val root = (project in file("."))
  .settings(sharedSettings :_*)
  .dependsOn(fflay)
  .aggregate(di, ffmpeg, auth, fflay)
  .enablePlugins(PlayScala)