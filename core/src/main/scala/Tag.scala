package ghscala

final case class Tag(
  name        :String,
  zipball_url :String,
  tarball_url :String,
  commit      :Tag.Commit
) extends JsonToString[Tag]


object Tag {
  implicit val tagCodecJson: CodecJson[Tag] =
    CodecJson.casecodec4(apply, unapply)(
      "name", "zipball_url", "tarball_url", "commit"
    )

  final case class Commit(
    sha: String, url: String
  ) extends JsonToString[Commit]

  object Commit {
    implicit val commitCodecJson: CodecJson[Commit] =
      CodecJson.casecodec2(apply, unapply)(
        "sha", "url"
      )
  }
}
