import java.io.{ ByteArrayOutputStream, File, PrintWriter }
import java.nio.file.{ Files, Paths, StandardCopyOption }

import scala.sys.process.ProcessLogger

/*
 * Copyright (C) 2009-2016 Lightbend Inc. <http://www.lightbend.com>
 */

trait BinCompatTools {
  import scala.sys.process._

  def run(mainClass: String, cps: List[File]*)(implicit scalaVersion: ScalaVersion): Unit = {
    println(s"=== RUNNING $mainClass >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")

    """rm -rf cp/*""".!
    val flatten: List[File] = cps.flatten[File].toList
    flatten.foreach(f => Files.copy(f.toPath, Paths.get(s"cp/${f.getName}"), StandardCopyOption.REPLACE_EXISTING))

    val cpString =
      (scalaVersion.scalaLibrary :: new File("cp/") :: Nil)
        .map(_.toString)
        .toSet
        .mkString(":")

    println(s"java -cp $cpString $mainClass")
    s"java -cp $cpString $mainClass".!(ProcessLogger(
      fout => System.out.println(Console.GREEN + s"  $fout" + Console.RESET),
      ferr => System.out.println(Console.RED + s"  $ferr" + Console.RESET)
    ))
  }

  def cleanWorkbench() = {
    """rm -rf src/before/target/*""".!
    """rm -rf src/after/target/*""".!
  }

  def load(outs: List[File], prefix: String): List[File] = {
    outs.filter(_.getName.startsWith(prefix))
  }
  def compileBefore(name: String)(implicit scalaVersion: ScalaVersion): List[File] = {
    println(s"=== COMPILING $name (BEFORE) =======================================")
    compile("before", name)
  }
  def compileAfter(name: String)(implicit scalaVersion: ScalaVersion): List[File] = {
    println(s"=== COMPILING $name (AFTER) =======================================")
    compile("after", name)
  }
  // TODO make it compile using that version
  def compile(which: String, name: String)(implicit scalaVersion: ScalaVersion): List[File] = {
    val stderrStream = new ByteArrayOutputStream
    val stderrWriter = new PrintWriter(stderrStream)

    val scalacVersion = {
      """scalac -version""".!(ProcessLogger(_ => (), stderrWriter.println))
      stderrWriter.close()
      stderrStream.toString
    }
    require(scalacVersion.contains(scalaVersion.v), s"Found scalac $scalacVersion, expected $scalaVersion!")

    val lang = if (name.endsWith(".scala")) "scala" else "java"
    val path = s"src/$which/$lang/$name"
    val out = s"src/$which/target/"
    s"""scalac -d $out $path""".!
    val compiledFiles = new File(out).listFiles().toList
    compiledFiles.foreach(f => println("    COMPILED: " + f))
    compiledFiles
  }

}
