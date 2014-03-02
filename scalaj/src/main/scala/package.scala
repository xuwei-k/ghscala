package ghscala

import scalaz.{One => _, Two => _, _}
import scalaz.concurrent.Future

package object scalajhttp {

  implicit def toScalajActionEOps[E, A](a: ActionE[E, A]) =
    new ScalajActionEOps(a)

  private[ghscala] type FutureTimes[A] = WriterT[Future, List[Time], A]

}

