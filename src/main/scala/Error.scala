package ghscala

import argonaut._

sealed trait Error extends Any with Product with Serializable {
  import Error._

  def httpOr[A](z: => A, f: scalaj.http.HttpException => A): A =
    this match {
      case Http(e) => f(e)
      case _ => z
    }

  def parseOr[A](z: => A, f: String => A): A =
    this match {
      case Parse(e) => f(e)
      case _ => z
    }

  def decodeOr[A](z: => A, f: (scalaj.http.Http.Request, String, CursorHistory, String) => A): A =
    this match {
      case Decode(r, m, h, s) => f(r, m, h, s)
      case _ => z
    }

  final def fold[A](
    http: scalaj.http.HttpException => A,
    parse: String => A,
    decode: (scalaj.http.Http.Request, String, CursorHistory, String) => A
  ): A =
    this match {
      case Http(e) =>
        http(e)
      case Parse(e) =>
        parse(e)
      case Decode(r, m, h, s) =>
        decode(r, m, h, s)
    }
}

object Error {
  final case class Http private[Error] (err: scalaj.http.HttpException) extends AnyVal with Error {
    override def toString = Array(
      "code"     -> err.code,
      "message"  -> err.message,
      "cause"    -> err.cause,
      "body"     -> err.body
    ).mkString("HttpException(", ", ", ")")
  }
  final case class Parse private[Error] (err: String) extends AnyVal with Error
  final case class Decode private[Error] (
    req: scalaj.http.Http.Request, message: String, history: CursorHistory, sourceString: String
  ) extends Error {
    override def toString = Seq(
      "request" -> s"${req.method} ${req.getUrl}",
      "message" -> message,
      "history" -> history,
      "source"  -> sourceString
    ).mkString("DecodeError(",", ",")")
  }

  val http: scalaj.http.HttpException => Error = Http
  val parse: String => Error = Parse
  val decode: (scalaj.http.Http.Request, String, CursorHistory, String) => Error = Decode
}

