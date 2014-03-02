package ghscala

import scalaj.http._
import scalaz.Endo

object ScalajHttp{

  val OPTIONS = List( HttpOptions.connTimeout(30000) , HttpOptions.readTimeout(30000) )

  def apply(url:String): Http.Request = Http(url).options(OPTIONS)

  def post(url:String): Http.Request = Http.post(url).options(OPTIONS)

  def param(key: String, value: String): Config = Endo(_.param(key, value))

  def params(keyValues: (String, String) *): Config = Endo(_.params(keyValues.toList))

  def auth(user: String, pass: String): Config = Endo(_.auth(user, pass))

  // TODO https://gist.github.com/xuwei-k/5608828

}

