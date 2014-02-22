package ghscala

import scalaz._, Free._
import argonaut._

final case class BasicAuth(user: String, pass: String)
final case class Config(auth: BasicAuth)

sealed abstract class GhRequest[A]

object GhRequest {

  final case class Get[A] (url: String, f: (String \/ Json) => A) extends GhRequest[A]
  final case class Post[A](url: String, f: (String \/ Json) => A) extends GhRequest[A]

  implicit def GhRequestFunctor: Functor[GhRequest] =
    new Functor[GhRequest] {
      def map[A, B](fa: GhRequest[A])(f: A => B) =
        fa match {
          case Get(url, g)  => Get(url, g andThen f)
          case Post(url, g) => Post(url, g andThen f)
        }
    }

}

object Github {

  type Requests[A] = Free[GhRequest, A]

  def liftF[S[_], A](value: => S[A])(implicit S: Functor[S]): Free[S, A] =
    Suspend(S.map(value)(Return[S, A]))

  def get(url: String): Requests[String \/ Json] =
    liftF(GhRequest.Get(url, identity))
  def post(url: String): Requests[String \/ Json] =
    liftF(GhRequest.Post(url, identity))

  def execute(conf: Config): GhRequest ~> Id.Id =
    new (GhRequest ~> Id.Id){
      def apply[A](a: GhRequest[A]) = {
        import conf.auth
        a match {
          case GhRequest.Get(url, f) =>
            f(JsonParser.parse(ScalajHttp(url).auth(auth.user, auth.pass).asString))
          case GhRequest.Post(url, f) =>
            f(JsonParser.parse(ScalajHttp.post(url).auth(auth.user, auth.pass).asString))
        }
      }
    }

  def run[A](requests: Requests[A], conf: Config): A = requests.runM(execute(conf))

  val program = for{
    a <- get("https://api.github.com/repos/scalaz/scalaz/git/trees/master")
  } yield a

  def main(args: Array[String]){
    val result = run(program, Config(BasicAuth(args(0), args(1))))
    println(result)
  }

}

