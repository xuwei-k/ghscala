package ghscala

import Z._

object Main {

  val program = for{
//    a <- Github.trees("scalaz", "scalaz", "master")
//    b <- Github.repo("scalaz", "scalaz")
//    c <- Github.commits("scalaz", "scalaz", "master")
//    d <- Github.issues("scalaz", "scalaz")
//    e <- Github.comments("scalaz", "scalaz")
//    f <- Github.issueEvents("scalaz", "scalaz", 650)
//    f <- Github.issueEvents("scalaz", "scalaz")
//    g <- Github.readme("scalaz", "scalaz")
//    h <- Github.org("scalaz")
//    i <- Github.orgs
    j <- Github.orgs("xuwei-k")
  } yield j

  def main(args: Array[String]){
    val result = args match {
      case Array(user, pass) =>
        Core.run(program, ScalajHttp.auth(user, pass))
      case Array() =>
        Core.run(program)
    }
    println(result)
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
