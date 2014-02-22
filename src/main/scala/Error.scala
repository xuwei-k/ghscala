package ghscala

import argonaut._

sealed abstract class Error extends Product with Serializable

object Error {
  final case class Http(err: scalaj.http.HttpException) extends Error {
    override def toString = Array(
      "code"     -> err.code,
      "message"  -> err.message,
      "cause"    -> err.cause,
      "body"     -> err.body
    ).mkString("HttpException(", ", ", ")")
  }
  final case class Parse(err: String) extends Error
  final case class Decode(message: String, history: CursorHistory) extends Error
}

