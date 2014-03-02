package ghscala

import scalaz._
import argonaut._

object Core {

  private[this] val baseURL = "https://api.github.com/"

  def get[A: DecodeJson](url: String, opt: Config = emptyConfig): Action[A] =
    httpRequest(opt(Request(baseURL + url)))

  def post[A: DecodeJson](url: String, opt: Config = emptyConfig): Action[A] =
    httpRequest(opt(Request(url = baseURL + url, method = "POST")))

  private def httpRequest[A](req: Request)(implicit A: DecodeJson[A]): Action[A] =
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

