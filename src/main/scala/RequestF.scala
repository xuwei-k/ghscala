package ghscala

import scalaz.\/
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
    def req: ScalajReq
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

  def one[A](req0: ScalajReq, error0: Error => A, f: String => A): RequestF[A] =
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

