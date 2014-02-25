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

  def run(config: Config): Unit = {
    val html = io.Source.fromURL(url).mkString

    val repoList = repo.findAllIn(html).map{
      case repo(user, name) => user -> name
    }.toList.distinct

    val (lefts, rights) = MonadPlus[List].separate(
      repoList.map{ case (u, r) =>
        Github.repo(u, r).interpret
      }
    )

    rights.sortBy(_.watchers).reverse.foreach{r =>
      println(r.html_url + " " + r.watchers)
    }

    lefts.foreach(println)
  }

}

