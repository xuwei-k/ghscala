package ghscala

import scalaz._, std.list._

object SbtPluginRanking {

  val repo = """https:\/\/github.com\/([a-zA-Z0-9_\-]+)\/([a-zA-Z0-9_\-]+)""".r
  val url = "https://raw.github.com/sbt/sbt/v0.13.2-M2/src/sphinx/Community/Community-Plugins.rst"

  def main(args: Array[String]): Unit = args match {
    case Array(user, pass) =>
      run(ScalajHttp.auth(user, pass))
    case Array() =>
      run(Endo.idEndo)
  }

  final case class SbtPlugin(user: String, name: String)

  def sbtPlugins: List[SbtPlugin] = {
    val html = io.Source.fromURL(url).mkString

    repo.findAllIn(html).map{
      case repo(user, name) => SbtPlugin(user, name)
    }.toList.distinct
  }

  def run(config: Config): Unit = {
    val (lefts, rights) = MonadPlus[List].separate(
      sbtPlugins.map{ p =>
        Github.repo(p.user, p.name).interpret
      }
    )

    rights.sortBy(_.watchers).reverse.foreach{r =>
      println(r.html_url + " " + r.watchers)
    }

    lefts.foreach(println)
  }

  def run2(config: Config): Unit = {
    import Z._
    val x = Traverse[List].sequence(
      sbtPlugins.map{ p => Github.repo(p.user, p.name) }
    )
    val y = x.interpret
    println(y)
  }

}

