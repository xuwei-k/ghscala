package ghscala

import scalaz._

final class ActionOps[A](val self: Action[A]) extends AnyVal {

  def mapRequest(f: Config): Action[A] = Action[A](
    Z.mapSuspensionFreeC(self.run, new (RequestF ~> RequestF){
      def apply[A](a: RequestF[A]) = a.mapRequest(f)
    })
  )

}

