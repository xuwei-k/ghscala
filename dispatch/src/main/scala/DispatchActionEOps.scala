package ghscala
package dispatchclassic

import scalaz.\/

final class DispatchActionEOps[E, A](val self: ActionE[E, A]) extends AnyVal {

  def interpret(conf: Config): E \/ A =
    DispatchInterpreter.sequential(conf).run(self)

  def interpret: E \/ A =
    DispatchInterpreter.sequential.empty.run(self)

}

