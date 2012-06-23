import sbt._,Keys._

object GenerateAllApi{

  val m = "issues watched searchRepo downloads watchers searchIssues pulls" // TODO

  val methods = "downloads followers repos refs forks collaborators orgs".split(" ").toList

  val methods2 = "issueEvents comments" // TODO

  def task(dir:File):Seq[File] = {
    val str =
    Iterator(
      "package com.github.xuwei_k.ghscala\n",
      "trait All{\n",
      methods.map{m =>
        "  val " + m + " = Common.all(Core._" + m + ")"
      }.mkString("\n\n"),
      "}"
    ).mkString("\n")
    println(str)
    val file = dir / "All.scala"
    IO.write(file,str)
    Seq(file)
  }

}
