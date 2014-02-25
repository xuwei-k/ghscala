package ghscala

import scalaz._
import Z._

final class ActionOps[A](val self: Action[A]) extends AnyVal {

  def mapRequest(f: Config): Action[A] = Action[A](
    Z.mapSuspensionFreeC(self.run, new (RequestF ~> RequestF){
      def apply[A](a: RequestF[A]) = a.mapRequest(f)
    })
  )

  def interpret: Error \/ A =
    Core.run(self)

  def interpretWith(conf: Config): Error \/ A =
    Core.run(self, conf)

  def interpretBy[F[_]: Monad](f: InterpreterF[F]): F[Error \/ A] =
    Z.interpret(self.run)(f)

  def kleisli: ActionK[A] = ActionK(Z.interpret(self.leftMap(NonEmptyList(_)).run))
}

