import sbt._, Keys._
import complete.DefaultParsers._

sealed abstract class ParseResult(val method: String, params: String *) extends Product{
  final def value: Seq[String] = (method +: params)
}

object ParseResult {
  final case class Contributors(owner: String, repo: String) extends ParseResult("contributors")
  final case class Followers(user: String) extends ParseResult("followers", user)
  final case class Following(user: String) extends ParseResult("following", user)
  final case class Blob(user: String, repo: String, sha: String) extends ParseResult("blob", user, repo, sha)
  final case class Trees(user: String, repo: String, sha: String) extends ParseResult("trees", user, repo, sha)
  final case class Repos(user: String) extends ParseResult("repos", user)
  final case class Repo(user: String, repo: String) extends ParseResult("repo", user, repo)
  final case class Commits(user: String, repo: String, sha: String) extends ParseResult("commits", user, repo, sha)
  final case class Issues(user: String, repo: String) extends ParseResult("issues", user, repo)
  final case class Comments(user: String, repo: String) extends ParseResult("comments")
}

object Github {
  import complete.Parser
  import complete.Parser._
  import ParseResult._

  private val mainClassLoader = TaskKey[ClassLoader]("mainClassLoader")
  private val github = InputKey[Unit]("github")
  private val parser: Parser[ParseResult] = {
    def start(name: String) = token(name) ~> Space
    def t(name: String): Parser[String] = token(StringBasic, name) <~ Space
    def end(name: String): Parser[String] = token(StringBasic, name)
    Space ~> {
      (start("repos") ~> end("user").map(Repos)) |
      (start("blob") ~> (t("user") ~ t("repo") ~ end("sha")).map{case ((u, r), s) => Blob(u, r, s)}) |
      (start("repo") ~> (t("user") ~ end("repo")).map(Repo.tupled)) |
      (start("following") ~> end("user").map(Following)) |
      (start("followers") ~> end("user").map(Followers))
    }
  }

  val setting: Seq[Def.Setting[_]] = Seq(
    mainClassLoader <<= (fullClasspath in Compile, scalaInstance).map((path, instance) =>
      classpath.ClasspathUtilities.makeLoader(path.map(_.data), instance)
    ),
    github := {
      val result = parser.parsed
      val cp = (fullClasspath in Compile).value
      val r = (runner in Compile).value
      r.run(
        "ghscala.Github",
        Attributed.data(cp),
        result.value,
        streams.value.log
      )
/*
      val result = parser.parsed
      val loader = mainClassLoader.value
      invoke[AnyRef](loader, "ghscala.Github", "main", result.p: _*)
*/
      ()
    }
  )

  def invoke[A](loader: ClassLoader, className: String, method: String, params: (Class[_], AnyRef) *): A = {
    invoke(loader.loadClass(className + "$"), method, params: _*)
  }

  def invoke[A](clazz: Class[_], method: String, params: (Class[_], AnyRef) *): A = {
    clazz.getMethod(method, params.map(_._1): _*).invoke(clazz.getField("MODULE$").get(null), params.map(_._2): _*).asInstanceOf[A]
  }

}

