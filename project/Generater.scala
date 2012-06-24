import sbt._,Keys._

object Generater{

  val m = "issues watched searchRepo downloads watchers searchIssues pulls" // TODO
  val methods2 = "issueEvents comments" // TODO

  val packageStatement = "package com.github.xuwei_k.ghscala\n"

  def task(dir:File):Seq[File] = Seq(all(dir),instances(dir))

  private def instances(dir:File):File = {
    val types = Seq(
      "Repo","Ref","User","SearchRepo","CommitResponse","TreeResponse","IssueEvent","Issue","Blob",
      "IssueEvent2","IssueSearch","Download","Comment","Contents","Pull","Org","Organization","Label"
    )
    val header = packageStatement +
      """import net.liftweb.json._
        |
        |trait Instances{
        |
        |  protected implicit val formats = DefaultFormats
        |""".stripMargin

    val str = types.map{ n =>
      Iterator(
        "  implicit val " + n.toLowerCase + "Json = new FromJValue[" + n + "]{",
        "    def pure(j:JValue) = j.extract[" + n + "]",
        "  }"
      ).mkString("\n")
    }.mkString(header,"\n\n","\n}\n")
    println(str)
    val file = dir / "Instances.scala"
    IO.write(file,str)
    file
  }

  private def all(dir:File):File = {
    val methods = "downloads followers repos refs forks collaborators orgs".split(" ").toList

    val str =
    Iterator(
      packageStatement,
      "trait All{\n",
      methods.map{m =>
        "  val " + m + " = Common.all(Core._" + m + ")"
      }.mkString("\n\n"),
      "}"
    ).mkString("\n")
    println(str)
    val file = dir / "All.scala"
    IO.write(file,str)
    file
  }

}
