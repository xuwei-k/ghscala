package ghscala
package dispatchclassic

import scalaz.Id.Id

object DispatchInterpreter {

  object sequential {

    val empty: Interpreter[Id] =
      apply(emptyConfig)

    def apply(conf: Config): Interpreter[Id] =
      new Interpreter[Id] {
        def go[A](r: RequestF[A]) = r match {
          case o @ RequestF.One() =>
            try {
              import dispatch.classic._
              val str = Http(request2dispatch(o.req))
              o.decode(o.req, o.parse(o.req, str))
            } catch {
              case e: Throwable =>
                o.error(Error.http(e))
            }
          case t @ RequestF.Two() =>
           t.f(run(t.x), run(t.y))
        }
      }
  }

}

