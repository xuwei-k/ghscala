package ghscala

import httpz.Action
import scalaz.~>

private[ghscala] object Interpreter extends (Command ~> Action) {
  override def apply[A](fa: Command[A]) = fa.action
}
