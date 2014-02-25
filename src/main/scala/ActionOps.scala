package ghscala

import scalaz._
import Z._

final class ActionEOps[E, A](val self: ActionE[E, A]) extends AnyVal {

  def nel: ActionE[NonEmptyList[E], A] = self.leftMap(NonEmptyList.nel(_, Nil))

  def mapRequest(f: Config): ActionE[E, A] = Action[E, A](
    Z.mapSuspensionFreeC(self.run, new (RequestF ~> RequestF){
      def apply[A](a: RequestF[A]) = a.mapRequest(f)
    })
  )

  def interpret: E \/ A =
    Core.run(self)

  def interpretWith(conf: Config): E \/ A =
    Core.run(self, conf)

  def interpretBy[F[_]: Monad](f: InterpreterF[F]): F[E \/ A] =
    Z.interpret(self.run)(f)

  def zip[B](that: ActionE[E, B])(implicit E: Semigroup[E]): ActionE[E, (A, B)] =
    zipWith(that)(Tuple2.apply)

  import syntax.apply._

  def zipWith[B, C](that: ActionE[E, B])(f: (A, B) => C)(implicit E: Semigroup[E]): ActionE[E, C] =
    Action(Z.freeC(RequestF.two(self, that)((a, b) =>
      (a.validation |@| b.validation)(f).disjunction
    )))

}
