package ghscala

import org.apache.commons.codec.binary.Base64

final case class Blob(
  content  :String,
  encoding :String,
  sha      :String,
  size     :Long,
  url      :String
) extends JsonToString[Blob] {

  lazy val decoded:String =
    new String(Base64.decodeBase64(content))

}

object Blob {

  implicit val blobCodecJson: CodecJson[Blob] =
    CodecJson.casecodec5(apply, unapply)(
      "content", "encoding", "sha", "size", "url"
    )

}

