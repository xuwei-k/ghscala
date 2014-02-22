package ghscala

import scalaz._, Free._
import argonaut._

sealed abstract class GhRequest[A]

object GhRequest {

  final case class Get[A] (url: String, f: (String \/ Json) => A) extends GhRequest[A]
  final case class Post[A](url: String, f: (String \/ Json) => A) extends GhRequest[A]
  final case class Const[A](value: A) extends GhRequest[A]

  implicit def GhRequestFunctor: Functor[GhRequest] =
    new Functor[GhRequest] {
      def map[A, B](fa: GhRequest[A])(f: A => B) =
        fa match {
          case Get(url, g)  => Get(url, g andThen f)
          case Post(url, g) => Post(url, g andThen f)
          case Const(value) => Const(f(value))
        }
    }

}

object API {
  import Github._

  def trees(user: String, repo: String, sha: String): Result[DecodeResult[Trees]] =
    getAndMap(s"repos/$user/$repo/git/trees/$sha")

  def repo(user: String, repo: String): Result[DecodeResult[Repo]] =
    getAndMap(s"repos/$user/$repo")

  def commits(user: String, repo: String, sha: String): Result[DecodeResult[CommitResponse]] =
    getAndMap[CommitResponse](s"repos/$user/$repo/commits/$sha")
}

object Github {

  type Result[A] = EitherT[Requests, String, A]

  private[this] val baseURL = "https://api.github.com/"

  type Requests[A] = Free[GhRequest, A]

  def liftF[S[_], A](value: => S[A])(implicit S: Functor[S]): Free[S, A] =
    Suspend(S.map(value)(Return[S, A]))

  def get(url: String): Result[Json] =
    EitherT[Requests, String, Json](liftF(GhRequest.Get(url, identity)))

  def getAndMap[A](url: String)(implicit A: DecodeJson[A]): Result[DecodeResult[A]] =
    Github.get(url).map(A.decodeJson)

  def post(url: String): Result[Json] =
    EitherT[Requests, String, Json](liftF(GhRequest.Post(url, identity)))

  def execute(conf: Config): GhRequest ~> Id.Id =
    new (GhRequest ~> Id.Id){
      def apply[A](a: GhRequest[A]) = {
        import conf.auth
        a match {
          case GhRequest.Get(url, f) =>
            val x = JsonParser.parse(ScalajHttp(baseURL + url).auth(auth.user, auth.pass).asString)
            println(x.map(_.pretty(PrettyParams.spaces2)))
            f(x)
          case GhRequest.Post(url, f) =>
            f(JsonParser.parse(ScalajHttp.post(baseURL + url).auth(auth.user, auth.pass).asString))
          case GhRequest.Const(value) =>
            value
        }
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

