package ghscala

import scalaz._, Free._
import argonaut._

final case class RequestF[A](req: scalaj.http.Http.Request, f: (Error \/ String) => A)

object Github {

  type Result[A] = EitherT[Requests, Error, A]

  private[this] val baseURL = "https://api.github.com/"

  type Requests[A] = Z.FreeC[RequestF, A]

  def get[A: DecodeJson](url: String, opt: Endo[scalaj.http.Http.Request] = Endo.idEndo): Result[A] =
    httpRequest(opt(ScalajHttp(baseURL + url)))

  def post[A: DecodeJson](url: String, opt: Endo[scalaj.http.Http.Request] = Endo.idEndo): Result[A] =
    httpRequest(opt(ScalajHttp.post(baseURL + url)))

  private def httpRequest[A: DecodeJson](req: scalaj.http.Http.Request): Result[A] =
    EitherT[Requests, Error, A](Z.freeC(RequestF(
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

  def execute(conf: Config): RequestF ~> Id.Id =
    new (RequestF ~> Id.Id){
      def apply[A](a: RequestF[A]) = {
        import conf.auth
        val x = try {
          \/-(auth.fold(a.req)(x => a.req.auth(x.user, x.pass)).asString)
        } catch {
          case e: scalaj.http.HttpException => -\/(Error.Http(e))
        }
        a.f(x)
      }
    }

  def run[A](requests: Result[A], conf: Config = Config(None)): Error \/ A =
    Z.interpret(requests.run)(execute(conf))

}

