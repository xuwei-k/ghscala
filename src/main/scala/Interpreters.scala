package ghscala

import scalaz.Nondeterminism
import scalaz.concurrent.{Future, Task}
import RequestF._
import Core._

object Interpreters {

  object async {

    def apply(conf: Config): InterpreterF[Future] =
      new InterpreterF[Future] {
        def apply[A](a: RequestF[A]) = a match {
          case o @ One() =>
            Future(o.run(conf))
          case t @ Two() =>
            Nondeterminism[Future].mapBoth(runAsync(t.x, conf), runAsync(t.y, conf))(t.f)
        }
      }

    def task(conf: Config): InterpreterF[Task] =
      new InterpreterF[Task] {
        def apply[A](a: RequestF[A]) = a match {
          case o @ One() =>
            Task(o.run(conf))
          case t @ Two() =>
            Nondeterminism[Task].mapBoth(runTask(t.x, conf), runTask(t.y, conf))(t.f)
        }
      }

    val empty: InterpreterF[Future] =
      new InterpreterF[Future] {
        def apply[A](a: RequestF[A]) = a match {
          case o @ One() =>
            Future(o.run(emptyConfig))
          case t @ Two() =>
            Nondeterminism[Future].mapBoth(runAsync(t.x), runAsync(t.y))(t.f)
        }
      }
  }

  object sequential {

    def apply(conf: Config): Interpreter =
      new Interpreter {
        def apply[A](a: RequestF[A]) = a match {
          case o @ One() =>
            o.run(conf)
          case t @ Two() =>
            t.f(run(t.x, conf), run(t.y, conf))
        }
      }

    val empty: Interpreter =
      new Interpreter {
        def apply[A](a: RequestF[A]) = a match {
          case o @ One() =>
            o.run(emptyConfig)
          case t @ Two() =>
            t.f(run(t.x), run(t.y))
        }
      }
  }

  object times {
    def apply(conf: Config): InterpreterF[Times] =
      new InterpreterF[Times] {
        def apply[A](a: RequestF[A]) = a match {
          case o @ One() =>
            val r = conf(o.req)
            val start = System.nanoTime
            try {
              val str = r.asString
              val httpFinished = System.nanoTime
              val parseResult = o.parse(r, str)
              val parseFinished = System.nanoTime
              val decodeResult = o.decode(r, parseResult)
              val decodeFinished = System.nanoTime - parseFinished
              Times(
                decodeResult,
                Time.Success(
                  r, str, httpFinished - start, parseFinished - httpFinished, decodeFinished
                )
              )
            } catch {
              case e: scalaj.http.HttpException =>
                Times(o.error(Error.http(e)), Time.Failure(r, e, System.nanoTime - start))
            }
          case t @ Two() =>
            timesMonad.apply2(
              runWithTime(t.x, conf),
              runWithTime(t.y, conf)
            )(t.f)
        }
      }
  }
}

