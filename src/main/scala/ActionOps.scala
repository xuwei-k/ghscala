package ghscala

import scalaz._
import scalaz.concurrent.Future
import Z._

final class ActionEOps[E, A](val self: ActionE[E, A]) extends AnyVal {

  def nel: ActionE[NonEmptyList[E], A] = self.leftMap(NonEmptyList.nel(_, Nil))

  def mapRequest(f: Config): ActionE[E, A] = Action[E, A](
    Z.mapSuspensionFreeC(self.run, new (RequestF ~> RequestF){
      def apply[A](a: RequestF[A]) = a.mapRequest(f)
    })
  )

  def async: Future[E \/ A] =
    Core.runAsync(self)

  def async(conf: Config): Future[E \/ A] =
    Core.runAsync(self, conf)

  def withTime(conf: Config = emptyConfig): Times[E \/ A] =
    Core.runWithTime(self, conf)

  def interpret: E \/ A =
    Core.run(self)

  def interpretWith(conf: Config): E \/ A =
    Core.run(self, conf)

  def interpretBy[F[_]: Monad](f: InterpreterF[F]): F[E \/ A] =
    Z.interpret(self.run)(f)

  def zipWithError[B, C, E1, E2](that: ActionE[E1, B])(f: (E \/ A, E1 \/ B) => E2 \/ C): ActionE[E2, C] =
    Action(Z.freeC(RequestF.two(self, that)(f)))

  def zip[B](that: ActionE[E, B])(implicit E: Semigroup[E]): ActionE[E, (A, B)] =
    zipWith(that)(Tuple2.apply)

  import syntax.apply._

  def zipWith[B, C](that: ActionE[E, B])(f: (A, B) => C)(implicit E: Semigroup[E]): ActionE[E, C] =
    zipWithError(that)((a, b) => (a.validation |@| b.validation)(f).disjunction)

}
