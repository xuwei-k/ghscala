package ghscala

import org.apache.commons.codec.binary.Base64

case class Blob(
  content  :String,
  encoding :String,
  sha      :String,
  size     :Long,
  url      :String
){

  lazy val decoded:String =
    new String(Base64.decodeBase64(content))

}

