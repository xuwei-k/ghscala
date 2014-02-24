package ghscala

import org.apache.commons.codec.binary.Base64
//import pamflet.PamfletDiscounter._ // TODO

final case class Contents(
  sha      :String,
  name     :String,
  path     :String,
  content  :String,
  _links   :Links,
  `type`   :String,
  encoding :String,
  size     :Long,
  url      :String,
  html_url :String,
  git_url  :String
) extends JsonToString[Contents] {

  lazy val decoded: String =
    new String(Base64.decodeBase64(content))

//  lazy val html = toXHTML(knockoff(decoded))
}

object Contents {

  implicit val contentsCodecJson: CodecJson[Contents] =
    CodecJson.casecodec11(apply, unapply)(
      "sha", "name", "path", "content", "_links", "type", "encoding",
      "size", "url", "html_url", "git_url"
    )

}


final case class Links(
  self :String,
  git  :String,
  html :String
) extends JsonToString[Links]

object Links {
  implicit val linksCodecJson: CodecJson[Links] =
    CodecJson.casecodec3(apply, unapply)(
      "self", "git", "html"
    )

}

