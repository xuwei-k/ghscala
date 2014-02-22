package ghscala

import org.apache.commons.codec.binary.Base64

final case class Blob(
  content  :String,
  encoding :String,
  sha      :String,
  size     :Long,
  url      :String
){

  lazy val decoded:String =
    new String(Base64.decodeBase64(content))

}

object Blob {

  implicit val blobDecodeJson: DecodeJson[Blob] =
    DecodeJson.jdecode5L(Blob.apply)(
      "content", "encoding", "sha", "size", "url"
    )

}

