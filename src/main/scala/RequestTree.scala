package ghscala

import scalaz._

sealed abstract class RequestTree {
  import RequestTree._
  import argonaut._, Argonaut._

  def json: Json = this match {
    case Bin(l, r) =>
      Json(
        "left" := l.json,
        "right" := r.json
      )
    case Req(req) =>
      Json("request" := Json(
        "method" := req.method,
        "url" := req.getUrl.toString
      ))
    case Value(v) =>
      Json("value" := v.toString)
  }

  def valuesList: List[Any \/ Any] = this match {
    case Bin(l, r) =>
      l.valuesList ::: r.valuesList
    case a @ Req(_) =>
      Nil
    case Value(a) =>
      a :: Nil
  }

  def requestsList: List[Req] = this match {
    case Bin(l, r) =>
      l.requestsList ::: r.requestsList
    case a @ Req(_) =>
      a :: Nil
    case Value(_) =>
      Nil
  }

  def jsonString: String = json.pretty(PrettyParams.spaces2)
}

object RequestTree {
  final case class Bin(left: RequestTree, right: RequestTree) extends RequestTree
  final case class Req(value: ScalajReq) extends RequestTree{
    def urlString = value.method + " " + value.getUrl
    override def toString = "RequestTree.Req(" + urlString + ")"
  }
  final case class Value[A, E](value: E \/ A) extends RequestTree

  def apply[E, A](action: ActionE[E, A]): RequestTree = action.run.resume match {
    case \/-(a) => Value(a)
    case -\/(a) => a.fi match {
      case o @ RequestF.One() =>
        Req(o.req)
      case t @ RequestF.Two() =>
        Bin(apply(t.x), apply(t.y))
    }
  }
}

