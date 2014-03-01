package ghscala

import scalaz._
import scalaz.concurrent.{Future, Task}
import argonaut._

object Core {

  private[this] val baseURL = "https://api.github.com/"

  def get[A: DecodeJson](url: String, opt: Config = emptyConfig): Action[A] =
    httpRequest(opt(ScalajHttp(baseURL + url)))

  def post[A: DecodeJson](url: String, opt: Config = emptyConfig): Action[A] =
    httpRequest(opt(ScalajHttp.post(baseURL + url)))

  private def httpRequest[A](req: ScalajReq)(implicit A: DecodeJson[A]): Action[A] =
    Action(Z.freeC(RequestF.one[Error \/ A, Error \/ Json](
      req,
      \/.left,
      (request, result) => Parse.parse(result).leftMap(Error.parse),
      (request, either) => either.flatMap{ json =>
        A.decodeJson(json).result match {
           case r @ \/-(_) => r
           case -\/((msg, history)) => -\/(Error.decode(request, msg, history, json))
        }
      }
    )))

}

