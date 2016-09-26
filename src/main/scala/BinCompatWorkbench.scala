/*
 * Copyright (C) 2009-2016 Lightbend Inc. <http://www.lightbend.com>
 */

import java.io.{ ByteArrayOutputStream, File, FilenameFilter, PrintWriter }
import java.net.URLClassLoader
import java.nio.file.{ Files, Paths, StandardCopyOption }

object BinCompatWorkbench extends App with BinCompatTools {
  import scala.sys.process._

  implicit val scalaVersion = ScalaVersion("2.11.8")

  cleanWorkbench()
  val beforeOut = compileBefore("Example.scala")
  val afterOut = compileAfter("Example.scala")

  run(
    "RunScala",
    load(beforeOut, "A"),
    load(beforeOut, "RunScala")
  )
  run(
    "RunScala",
    load(afterOut, "A"),
    load(beforeOut, "RunScala")
  )

}
