package ghscala

import Z._

object Main {

  val program1 = ActionNelZipAp.apply12(
    Github.user.nel,
    Github.trees("scalaz", "scalaz", "master").nel,
    Github.repo("scalaz", "scalaz").nel,
    Github.commits("scalaz", "scalaz", "master").nel,
    Github.issues("scalaz", "scalaz").nel,
    Github.comments("scalaz", "scalaz").nel,
    Github.issueEvents("scalaz", "scalaz", 650).nel,
    Github.issueEvents("scalaz", "scalaz").nel,
    Github.readme("scalaz", "scalaz").nel,
    Github.org("scalaz").nel,
    Github.orgs.nel,
    Github.orgs("xuwei-k").nel
  )(Tuple12.apply)

  val program2 = ActionNelZipAp.apply12(
    Github.contributors("scalaz", "scalaz").nel,
    Github.followers.nel,
    Github.followers("xuwei-k").nel,
    Github.following.nel,
    Github.following("xuwei-k").nel,
    Github.repos.nel,
    Github.orgRepos("scalaz").nel,
    Github.pulls("scalaz", "scalaz").nel,
    Github.gists("xuwei-k").nel,
    Github.gists.me.nel,
    Github.gists.public.nel,
    Github.gists.starred.nel
  )(Tuple12.apply)

  val program = program1 zip program2

  def main(args: Array[String]){
    val result = args match {
      case Array(user, pass) =>
        Core.run(program, ScalajHttp.auth(user, pass))
      case Array() =>
        (sys.env.get("TEST_USER_ID"), sys.env.get("TEST_USER_PASSWORD")) match {
          case (Some(user), Some(pass)) =>
            Core.run(program, ScalajHttp.auth(user, pass))
          case _ =>
            Core.run(program)
        }
    }
    result.leftMap(errors => println(errors.size))
    println(result)
    assert(result.isRight)
  }

}

object ActionNelExample {

  def runExample() = {
    val a = Github.repo("invalid user", "invalid repo").nel
    val b = a zip a
    b.interpret
  }

  def main(args: Array[String]){
    val x = runExample
    x.leftMap(errors => println(errors.size))
    println(x)
  }
}
