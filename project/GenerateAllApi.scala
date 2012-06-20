import sbt._,Keys._

object GenerateAllApi{

  val m = "repos followers issues watched searchRepo searchIssues  pulls orgs"

  val methods = "refs downloads forks watchers collaborators".split(" ").toList

  val methods2 = "issueEvents comments"

  def task(dir:File):Seq[File] = {
    val str =
    Iterator(
      "package com.github.xuwei_k.ghscala\n",
      "object All{\n",
      methods.map{m =>
        "  val " + m + "All = Common.all(GhScala." + m + ")"
      }.mkString("\n\n"),
      "}"
    ).mkString("\n")
    println(str)
    val file = dir / "all.scala"
    IO.write(file,str)
    Seq(file)
  }

}
