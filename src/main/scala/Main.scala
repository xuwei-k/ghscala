package ghscala

object Main {

  val program = for{
//    a <- API.trees("scalaz", "scalaz", "master")
//    b <- API.repo("scalaz", "scalaz")
//    c <- API.commits("scalaz", "scalaz", "master")
//    d <- API.issues("scalaz", "scalaz")
//    e <- API.comments("scalaz", "scalaz")
//    f <- API.issueEvents("scalaz", "scalaz", 650)
//    f <- API.issueEvents("scalaz", "scalaz")
//    g <- API.readme("scalaz", "scalaz")
    h <- API.org("scalaz")
  } yield h

  def main(args: Array[String]){
    val result = args match {
      case Array(user, pass) =>
        Github.run(program, Config(Some(BasicAuth(user, pass))))
      case Array() =>
        Github.run(program)
    }
    println(result)
  }

}

