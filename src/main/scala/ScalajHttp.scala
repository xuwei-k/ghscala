package ghscala

import scalaj.http._

object ScalajHttp{

  val OPTIONS = List( HttpOptions.connTimeout(30000) , HttpOptions.readTimeout(30000) )

  def apply(url:String):Http.Request = Http(url).options(OPTIONS)

  def post(url:String):Http.Request = Http.post(url).options(OPTIONS)

}

