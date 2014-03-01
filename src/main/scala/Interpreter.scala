package ghscala

import scalaz.{Nondeterminism, \/, Monad}
import scalaz.Id.Id
import scalaz.concurrent.{Future, Task}
import RequestF._

abstract class Interpreter[F[_]: Monad] {
  final val interpreter: InterpreterF[F] =
    new InterpreterF[F] {
      def apply[A](a: RequestF[A]) = go(a)
    }

  protected[this] def go[A](a: RequestF[A]): F[A]

  final def run[E, A](a: ActionE[E, A]): F[E \/ A] =
    Z.interpret(a.run)(interpreter)
}

object Interpreter {

  object future {
    val empty: Interpreter[Future] =
      apply(emptyConfig)

    def apply(conf: Config): Interpreter[Future] =
      new Interpreter[Future] {
        def go[A](a: RequestF[A]) = a match {
          case o @ One() =>
            Future(o.run(conf))
          case t @ Two() =>
            Nondeterminism[Future].mapBoth(run(t.x), run(t.y))(t.f)
        }
      }
  }

  object task {
    val empty: Interpreter[Task] =
      apply(emptyConfig)

    def apply(conf: Config): Interpreter[Task] =
      new Interpreter[Task] {
        def go[A](a: RequestF[A]) = a match {
          case o @ One() =>
            Task(o.run(conf))
          case t @ Two() =>
            Nondeterminism[Task].mapBoth(run(t.x), run(t.y))(t.f)
        }
      }
  }

  object sequential {
    val empty: Interpreter[Id] =
      apply(emptyConfig)

    def apply(conf: Config): Interpreter[Id] =
      new Interpreter[Id] {
        def go[A](a: RequestF[A]) = a match {
          case o @ One() =>
            o.run(conf)
          case t @ Two() =>
            t.f(run(t.x), run(t.y))
        }
      }
  }

  object times {
    val empty: Interpreter[Times] =
      apply(emptyConfig)

    def apply(conf: Config): Interpreter[Times] =
      new Interpreter[Times] {
        def go[A](a: RequestF[A]) = a match {
          case o @ One() =>
            go1(o, conf)
          case t @ Two() =>
            timesMonad.apply2(run(t.x), run(t.y))(t.f)
        }
      }

    private def go1[A](o: One[A], conf: Config): Times[A] = {
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
    }

  }
}

