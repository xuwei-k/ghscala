package ghscala

import argonaut._

sealed abstract class Error extends Product with Serializable

object Error {
  final case class Http private[Error] (err: scalaj.http.HttpException) extends Error {
    override def toString = Array(
      "code"     -> err.code,
      "message"  -> err.message,
      "cause"    -> err.cause,
      "body"     -> err.body
    ).mkString("HttpException(", ", ", ")")
  }
  final case class Parse private[Error] (err: String) extends Error
  final case class Decode private[Error] (message: String, history: CursorHistory) extends Error

  val http: scalaj.http.HttpException => Error = Http
  val parse: String => Error = Parse
  val decode: (String, CursorHistory) => Error = Decode
}

