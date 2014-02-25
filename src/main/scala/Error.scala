package ghscala

import argonaut._

sealed trait Error extends Any with Product with Serializable {
  import Error._

  final def fold[A](http: scalaj.http.HttpException => A, parse: String => A, decode: (String, CursorHistory) => A): A =
    this match {
      case Http(e)        => http(e)
      case Parse(e)       => parse(e)
      case Decode(m, h)   => decode(m, h)
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
  final case class Decode private[Error] (message: String, history: CursorHistory) extends Error

  val http: scalaj.http.HttpException => Error = Http
  val parse: String => Error = Parse
  val decode: (String, CursorHistory) => Error = Decode
}

