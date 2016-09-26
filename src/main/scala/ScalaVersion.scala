import java.io.File

/*
 * Copyright (C) 2009-2016 Lightbend Inc. <http://www.lightbend.com>
 */

final case class ScalaVersion(v: String) {
  val IvyHome = System.getProperty("user.home") + "/.ivy2/"
  val scalaLibrary = new File(new File(IvyHome), s"cache/org.scala-lang/scala-library/jars/scala-library-$v.jar")
}
