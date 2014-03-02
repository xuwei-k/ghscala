package ghscala

final case class Branch (
  name    :String,
  commit  :Branch.Commit
) extends JsonToString[Branch]

object Branch {
  implicit val branchCodecJson: CodecJson[Branch] =
    CodecJson.casecodec2(apply, unapply)(
      "name", "commit"
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
