package ghscala

import scalaz._
import scalaz.concurrent.Future
import argonaut._

object Core {

  private[this] val baseURL = "https://api.github.com/"

  def get[A: DecodeJson](url: String, opt: Config = emptyConfig): Action[A] =
    httpRequest(opt(ScalajHttp(baseURL + url)))

  def post[A: DecodeJson](url: String, opt: Config = emptyConfig): Action[A] =
    httpRequest(opt(ScalajHttp.post(baseURL + url)))

  private def httpRequest[A: DecodeJson](req: scalaj.http.Http.Request): Action[A] =
    Action(Z.freeC(RequestF.one[Error \/ A](
      req,
      \/.left,
      x =>
        Parse.decodeWith[Error \/ A, A](
          x,
          \/.right,
          Error.parse andThen \/.left,
          (msg, history) => -\/(Error.decode(msg, history))
        )
    )))

  def run[E, A](actions: ActionE[E, A]): E \/ A =
    Z.interpret(actions.run)(Interpreter.sequential.empty)

  def run[E, A](actions: ActionE[E, A], conf: Config): E \/ A =
    Z.interpret(actions.run)(Interpreter.sequential(conf))

  def runAsync[E, A](actions: ActionE[E, A]): Future[E \/ A] =
    Z.interpret(actions.run)(Interpreter.async.empty)

  def runAsync[E, A](actions: ActionE[E, A], conf: Config): Future[E \/ A] =
    Z.interpret(actions.run)(Interpreter.async(conf))

}

