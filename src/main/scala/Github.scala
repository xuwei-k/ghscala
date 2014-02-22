package ghscala

import scalaz._, Free._
import argonaut._

final case class GhRequest[A](req: scalaj.http.Http.Request, f: (String \/ Json) => A)

object GhRequest {

  implicit def GhRequestFunctor: Functor[GhRequest] =
    new Functor[GhRequest] {
      def map[A, B](fa: GhRequest[A])(f: A => B) =
        fa.copy(f = fa.f andThen f)
    }

}

object API {
  import Github._

  def trees(user: String, repo: String, sha: String): Result[DecodeResult[Trees]] =
    getAndMap(s"repos/$user/$repo/git/trees/$sha")

  def repo(user: String, repo: String): Result[DecodeResult[Repo]] =
    getAndMap(s"repos/$user/$repo")

  def commits(user: String, repo: String, sha: String): Result[DecodeResult[CommitResponse]] =
    getAndMap(s"repos/$user/$repo/commits/$sha")

  def issues(user: String, repo: String, state: State = Open): Result[DecodeResult[List[Issue]]] =
    get(
      s"repos/$user/$repo/issues",
      Endo(_.param("state", state.toString))
    ).map(
      implicitly[DecodeJson[List[Issue]]].decodeJson
    )

}

object Github {

  type Result[A] = EitherT[Requests, String, A]

  private[this] val baseURL = "https://api.github.com/"

  type Requests[A] = Free[GhRequest, A]

  def liftF[S[_], A](value: => S[A])(implicit S: Functor[S]): Free[S, A] =
    Suspend(S.map(value)(Return[S, A]))

  def get(url: String, opt: Endo[scalaj.http.Http.Request] = Endo.idEndo): Result[Json] =
    EitherT[Requests, String, Json](liftF(GhRequest(opt(ScalajHttp(baseURL + url)), identity)))

  def getAndMap[A](url: String)(implicit A: DecodeJson[A]): Result[DecodeResult[A]] =
    Github.get(url).map(A.decodeJson)

  def post(url: String): Result[Json] =
    EitherT[Requests, String, Json](liftF(GhRequest(ScalajHttp.post(baseURL + url), identity)))

  def execute(conf: Config): GhRequest ~> Id.Id =
    new (GhRequest ~> Id.Id){
      def apply[A](a: GhRequest[A]) = {
        import conf.auth
        val x = JsonParser.parse(a.req.auth(auth.user, auth.pass).asString)
        println(x.map(_.pretty(PrettyParams.spaces2)))
        a.f(x)
      }
    }

  def run[A](requests: Result[A], conf: Config): String \/ A = requests.run.runM(execute(conf))

  val program = for{
//    a <- API.trees("scalaz", "scalaz", "master")
//    a <- API.repo("scalaz", "scalaz")
    a <- API.commits("scalaz", "scalaz", "master")
  } yield a

  def main(args: Array[String]){
    val result = run(program, Config(BasicAuth(args(0), args(1))))
    println(result)
  }

}

