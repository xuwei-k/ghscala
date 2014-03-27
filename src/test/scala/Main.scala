package ghscala

import httpz._, scalajhttp._

import Z._
import scalaz._
import ghscala._

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

  def printResult[A, E](a: ErrorNel \/ A): Unit = {
    a.leftMap(errors => println(errors.size))
    println(a)
  }

  def runProgram[F[_]: Monad, A](
    p: ActionNel[A], interpreter: InterpreterF[F]
  )(f1: F[ErrorNel \/ A] => (ErrorNel \/ A), f2: F[ErrorNel \/ A] => Unit): Unit = {
    val r = Z.interpret(p.run)(interpreter)
    val value = f1(r)
    printResult(value)
    f2(r)
    assert(value.isRight)
  }

  def main(args: Array[String]){
    import scalaz.syntax.equal._, std.anyVal._

    implicit val timesMonad: Monad[Times] =
      scalaz.WriterT.writerMonad[List[Time]](scalaz.std.list.listMonoid)

    val conf = args match {
      case Array(user, pass) =>
        Request.auth(user, pass)
      case Array() =>
        (sys.env.get("TEST_USER_ID"), sys.env.get("TEST_USER_PASSWORD")) match {
          case (Some(user), Some(pass)) =>
            Request.auth(user, pass)
          case _ =>
            emptyConfig
        }
    }

    runProgram(
      program1, ScalajInterpreter.future(conf).interpreter
    )(_.run, identity)

    runProgram(
      program2, ScalajInterpreter.times(conf).interpreter
    )(_.value, x => {
      val log = x.written
      log foreach println
      log.size assert_=== x.value.map(_.productArity).getOrElse(-1)
    })
  }

}

