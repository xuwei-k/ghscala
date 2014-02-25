package ghscala

import scalaz._, Free._
import argonaut._

final case class RequestF[A](req: scalaj.http.Http.Request, f: (Error \/ String) => A) {
  def mapRequest(config: Config): RequestF[A] = copy(req = config(req))
}

object Core {

  private[this] val baseURL = "https://api.github.com/"

  type Requests[A] = Z.FreeC[RequestF, A]

  def get[A: DecodeJson](url: String, opt: Config = emptyConfig): Action[A] =
    httpRequest(opt(ScalajHttp(baseURL + url)))

  def post[A: DecodeJson](url: String, opt: Config = emptyConfig): Action[A] =
    httpRequest(opt(ScalajHttp.post(baseURL + url)))

  private def httpRequest[A: DecodeJson](req: scalaj.http.Http.Request): Action[A] =
    Action[A](Z.freeC(RequestF(
      req,
      _.flatMap{x =>
        Parse.decodeWith[Error \/ A, A](
          x,
          \/.right,
          Error.parse andThen \/.left,
          (msg, history) => -\/(Error.decode(msg, history))
        )
      }
    )))

  def execute(conf: Config): Interpreter =
    new Interpreter {
      def apply[A](a: RequestF[A]) = {
        a.f(try {
          \/-(conf(a.req).asString)
        } catch {
          case e: scalaj.http.HttpException => -\/(Error.http(e))
        })
      }
    }

  private[ghscala] val emptyConfig: Config = Endo.idEndo

  def run[A](actions: Action[A]): Error \/ A =
    run(actions, emptyConfig)

  def run[A](actions: Action[A], conf: Config): Error \/ A =
    Z.interpret(actions.run)(execute(conf))

}

