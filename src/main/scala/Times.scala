package ghscala

import scalaj.http.HttpException

sealed abstract class Time{
  type A
  def request: ScalajReq
  def http: Long
  def result: A
}

object Time {
  final case class Success(
    request: ScalajReq, result: String, http: Long, parse: Long, decode: Long
  ) extends Time {
    type A = String
    override def toString = {
      ("request" -> request.getUrl.toString) +: Seq(
        "http" -> http,
        "parse" -> parse,
        "decode" -> decode
      ).map{case (k, v) => k -> (v / 1000000.0).toString}
    }.map{case (k, v) => s"${k.toString} = ${v.toString}"}.mkString("Time.Success(", ", ", ")")
  }

  final case class Failure(
    request: ScalajReq, result: HttpException, http: Long
  ) extends Time {
    type A = HttpException
    override def toString = s"Time.Failure(request = ${request.getUrl}, http = ${http / 1000000.0})"
  }
}



