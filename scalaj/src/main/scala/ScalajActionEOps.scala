package ghscala
package scalajhttp

import scalaz.{One => _, Two => _, _}
import scalaz.concurrent.{Future, Task}
import Z._

final class ScalajActionEOps[E, A](val self: ActionE[E, A]) extends AnyVal {

  def task: Task[E \/ A] =
    ScalajInterpreter.task.empty.run(self)

  def task(conf: Config): Task[E \/ A] =
    ScalajInterpreter.task.apply(conf).run(self)

  def async: Future[E \/ A] =
    ScalajInterpreter.future.empty.run(self)

  def async(conf: Config): Future[E \/ A] =
    ScalajInterpreter.future.apply(conf).run(self)

  def withTime: Times[E \/ A] =
    ScalajInterpreter.times.empty.run(self)

  def withTime(conf: Config): Times[E \/ A] =
    ScalajInterpreter.times.apply(conf).run(self)

  def futureWithTime: Future[(List[Time], E \/ A)] =
    ScalajInterpreter.times.future.empty.run(self).run

  def futureWithTime(conf: Config): Future[(List[Time], E \/ A)] =
    ScalajInterpreter.times.future(conf).run(self).run

  def interpret: E \/ A =
    ScalajInterpreter.sequential.empty.run(self)

  def interpretWith(conf: Config): E \/ A =
    ScalajInterpreter.sequential.apply(conf).run(self)

}
