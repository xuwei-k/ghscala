package ghscala
package dispatchclassic

object DispatchInterpreter extends InterpretersTemplate {

  override protected def request2string(req: ghscala.Request) = {
    import dispatch.classic._
    Http(request2dispatch(req))
  }

}

