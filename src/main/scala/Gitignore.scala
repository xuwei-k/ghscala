package ghscala

final case class Gitignore(
  name: String, source: String
) extends JsonToString[Gitignore]

object Gitignore {
  implicit val gitignoreCodecJson: CodecJson[Gitignore] =
    CodecJson.casecodec2(apply, unapply)(
      "name", "source"
    )
}
