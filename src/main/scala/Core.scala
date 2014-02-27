package ghscala

import scalaz.{One => _, Two => _, _}, Free._
import scalaz.concurrent.Future
import argonaut._
import RequestF._

sealed abstract class RequestF[A] extends Product with Serializable {
  def mapRequest(config: Config): RequestF[A] = this match {
    case o @ One() => one(config(o.req), o.error, o.success)
    case t @ Two() => two(t.x.mapRequest(config), t.y.mapRequest(config))(t.f)
  }
}

object RequestF {
  import Core._

  abstract case class One[A]() extends RequestF[A] {
    def req: scalaj.http.Http.Request
    def error: Error => A
    def success: String => A
    private[ghscala] def run(conf: Config): A = try {
      success(conf(req).asString)
    } catch {
      case e: scalaj.http.HttpException => error(Error.http(e))
    }
  }

  abstract case class Two[A]() extends RequestF[A] {
    type X
    type Y
    type E1
    type E2
    def x: ActionE[E1, X]
    def y: ActionE[E2, Y]
    def f: (E1 \/ X, E2 \/ Y) => A
  }

  def one[A](req0: scalaj.http.Http.Request, error0: Error => A, f: String => A): RequestF[A] =
    new One[A]{
      def req = req0
      def error = error0
      def success = f
    }

  def two[X0, Y0, EE1, EE2, A](x0: ActionE[EE1, X0], y0: ActionE[EE2, Y0])(f0: (EE1 \/ X0, EE2 \/ Y0) => A): RequestF[A] =
    new Two[A] {
      type X = X0
      type Y = Y0
      type E1 = EE1
      type E2 = EE2
      def x = x0
      def y = y0
      def f = f0
    }

}

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

  def executeAsync(conf: Config): InterpreterF[Future] =
    new InterpreterF[Future] {
      def apply[A](a: RequestF[A]) = a match {
        case o @ One() =>
          Future(o.run(conf))
        case t @ Two() =>
          Nondeterminism[Future].mapBoth(runAsync(t.x, conf), runAsync(t.y, conf))(t.f)
      }
    }

  def execute(conf: Config): Interpreter =
    new Interpreter {
      def apply[A](a: RequestF[A]) = a match {
        case o @ One() =>
          o.run(conf)
        case t @ Two() =>
          t.f(run(t.x, conf), run(t.y, conf))
      }
    }

  private[ghscala] val emptyConfig: Config = Endo.idEndo

  def run[E, A](actions: ActionE[E, A]): E \/ A =
    run(actions, emptyConfig)

  def run[E, A](actions: ActionE[E, A], conf: Config): E \/ A =
    Z.interpret(actions.run)(execute(conf))

  def runAsync[E, A](actions: ActionE[E, A]): Future[E \/ A] =
    runAsync(actions, emptyConfig)

  def runAsync[E, A](actions: ActionE[E, A], conf: Config): Future[E \/ A] =
    Z.interpret(actions.run)(executeAsync(conf))

}

