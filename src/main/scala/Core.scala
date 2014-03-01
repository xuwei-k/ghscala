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
    Action(Z.freeC(RequestF.one[Error \/ A](
      req,
      \/.left,
      (request, result) => Parse.parse(result) match {
        case \/-(json) => A.decodeJson(json).result match {
          case r @ \/-(_) => r
          case -\/((msg, history)) => -\/(Error.decode(request, msg, history, json))
        }
        case -\/(e) => -\/(Error.parse(e))
      }
    )))

  def run[E, A](actions: ActionE[E, A]): E \/ A =
    Z.interpret(actions.run)(Interpreters.sequential.empty)

  def run[E, A](actions: ActionE[E, A], conf: Config): E \/ A =
    Z.interpret(actions.run)(Interpreters.sequential(conf))

  def runAsync[E, A](actions: ActionE[E, A]): Future[E \/ A] =
    Z.interpret(actions.run)(Interpreters.async.empty)

  def runAsync[E, A](actions: ActionE[E, A], conf: Config): Future[E \/ A] =
    Z.interpret(actions.run)(Interpreters.async(conf))

  def runTask[E, A](actions: ActionE[E, A], conf: Config): Task[E \/ A] =
    Z.interpret(actions.run)(Interpreters.async.task(conf))

}

