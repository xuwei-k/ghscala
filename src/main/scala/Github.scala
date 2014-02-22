package ghscala

import scalaz._, Free._
import argonaut._

final case class GhRequest[A](req: scalaj.http.Http.Request, f: (Error \/ String) => A)

object GhRequest {

  implicit def GhRequestFunctor: Functor[GhRequest] =
    new Functor[GhRequest] {
      def map[A, B](fa: GhRequest[A])(f: A => B) =
        fa.copy(f = fa.f andThen f)
    }

}

object API {
  import Github._

  /** [[http://developer.github.com/v3/git/trees]] */
  def trees(user: String, repo: String, sha: String): Result[Trees] =
    get(s"repos/$user/$repo/git/trees/$sha")

  /** [[http://developer.github.com/v3/repos]] */
  def repo(user: String, repo: String): Result[Repo] =
    get(s"repos/$user/$repo")

  /** [[http://developer.github.com/v3/git/commits]] */
  def commits(user: String, repo: String, sha: String): Result[CommitResponse] =
    get(s"repos/$user/$repo/commits/$sha")

  /** [[http://developer.github.com/v3/issues]] */
  def issues(user: String, repo: String, state: State = Open): Result[List[Issue]] =
    get(
      s"repos/$user/$repo/issues",
      ScalajHttp.param("state", state.toString)
    )

  /** [[http://developer.github.com/v3/issues/events/]] */
  def issueEvents(user: String, repo: String, number: Long): Result[List[IssueEvent]] =
    get(s"repos/$user/$repo/issues/$number/events")

  /** [[http://developer.github.com/v3/issues/events/]] */
  def issueEvents(user: String, repo: String): Result[List[IssueEvent2]] =
    get(s"repos/$user/$repo/issues/events")

  /** [[http://developer.github.com/v3/repos/comments]] */
  def comments(user: String, repo: String): Result[List[Comment]] =
    get(s"repos/$user/$repo/comments")



}

object Github {

  type Result[A] = EitherT[Requests, Error, A]

  private[this] val baseURL = "https://api.github.com/"

  type Requests[A] = Free[GhRequest, A]

  private def liftF[S[_], A](value: => S[A])(implicit S: Functor[S]): Free[S, A] =
    Suspend(S.map(value)(Return[S, A]))

  def get[A: DecodeJson](url: String, opt: Endo[scalaj.http.Http.Request] = Endo.idEndo): Result[A] =
    httpRequest(opt(ScalajHttp(baseURL + url)))

  def post[A: DecodeJson](url: String, opt: Endo[scalaj.http.Http.Request] = Endo.idEndo): Result[Json] =
    httpRequest(opt(ScalajHttp.post(baseURL + url)))

  private def httpRequest[A: DecodeJson](req: scalaj.http.Http.Request): Result[A] =
    EitherT[Requests, Error, A](liftF(GhRequest(
      req,
      _.flatMap{x =>
        Parse.decodeWith[Error \/ A, A](
          x,
          \/.right,
          Error.Parse andThen \/.left,
          (msg, history) => -\/(Error.Decode(msg, history))
        )
      }
    )))

  def execute(conf: Config): GhRequest ~> Id.Id =
    new (GhRequest ~> Id.Id){
      def apply[A](a: GhRequest[A]) = {
        import conf.auth
        val x = try {
          \/-(auth.fold(a.req)(x => a.req.auth(x.user, x.pass)).asString)
        } catch {
          case e: scalaj.http.HttpException => -\/(Error.Http(e))
        }
        a.f(x)
      }
    }

  def run[A](requests: Result[A], conf: Config): Error \/ A = requests.run.runM(execute(conf))

  val program = for{
//    a <- API.trees("scalaz", "scalaz", "master")
//    b <- API.repo("scalaz", "scalaz")
//    c <- API.commits("scalaz", "scalaz", "master")
//    d <- API.issues("scalaz", "scalaz")
//    e <- API.comments("scalaz", "scalaz")
//    f <- API.issueEvents("scalaz", "scalaz", 650)
    f <- API.issueEvents("scalaz", "scalaz")
  } yield f

  def runMain(conf: Config = Config(None)) = run(program, conf)

  def main(args: Array[String]){
    val result = args match {
      case Array(user, pass) =>
        runMain(Config(Some(BasicAuth(user, pass))))
      case Array() =>
        runMain()
    }
    println(result)
  }

}

