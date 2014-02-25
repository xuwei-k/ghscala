package ghscala

import scalaz._, syntax.apply._

final class ActionKOps[A](val self: ActionK[A]) extends AnyVal {

  def zip[B](that: ActionK[B]): ActionK[(A, B)] =
    zipWith(that)(Tuple2.apply)

  def zipWith[B, C](that: ActionK[B])(f: (A, B) => C): ActionK[C] =
    ActionK(interpreter =>
      (self.run(interpreter).validation |@| that.run(interpreter).validation)(f).disjunction
    )

  def interpret: ErrorNel \/ A =
    self.run(Core.execute(Core.emptyConfig))

  def interpretWith(conf: Config): ErrorNel \/ A =
    self.run(Core.execute(conf))

}


