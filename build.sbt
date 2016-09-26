import sbt._
import Keys._

name := "bin-compat-workbench"

val commonSettings =
  Seq(
    organization := "com.typesafe.akka",
    organizationName := "Lightbend",
    startYear := Some(2014),
    licenses := Seq("Apache License 2.0" -> url("http://opensource.org/licenses/Apache-2.0")),
    scalaVersion := "2.11.8",
    crossVersion := CrossVersion.binary,
    scalacOptions ++= Seq(
      "-deprecation",
      "-encoding", "UTF-8", // yes, this is 2 args
      "-unchecked",
      "-Xlint",
      // "-Yno-adapted-args", //akka-http heavily depends on adapted args and => Unit implicits break otherwise
      "-Ywarn-dead-code"
      // "-Xfuture" // breaks => Unit implicits
    ),
    testOptions += Tests.Argument(TestFrameworks.JUnit, "-q", "-v"))

lazy val root = Project(
    id = "bin-compat-workbench",
    base = file(".")
  )
  .settings(commonSettings)
  .settings(
    managedSources := List(
      file("src/before/scala"),
      file("src/before/java"),
      
      file("src/after/scala"),
      file("src/after/java")
    )
  )

shellPrompt := { s => Project.extract(s).currentProject.id + " > " }
