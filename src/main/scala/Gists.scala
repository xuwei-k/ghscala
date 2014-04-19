package ghscala

final case class Gists(
  files        :Map[String, Gists.File],
  html_url     :String,
  forks_url    :String,
  comments     :Long,
  created_at   :String,
  id           :String,
  owner        :Option[User],
  commits_url  :String,
  git_pull_url :String,
  public       :Boolean,
  updated_at   :String,
  comments_url :String,
  url          :String,
  description  :Option[String],
  git_push_url :String
) extends JsonToString[Gists]

object Gists {

  final case class File(
    filename :String,
    language :Option[String],
    _type    :String,
    raw_url  :String,
    size     :Long
  ) extends JsonToString[File]

  object File {
    implicit val fileCodecJson: CodecJson[File] =
      CodecJson.casecodec5(apply, unapply)(
        "filename", "language", "type", "raw_url", "size"
      )
  }

  implicit val gistsCodecJson: CodecJson[Gists] =
    CodecJson.casecodec15(apply, unapply)(
      "files", "html_url", "forks_url", "comments", "created_at", "id",
      "owner", "commits_url", "git_pull_url", "public", "updated_at",
      "comments_url", "url", "description", "git_push_url"
    )

}

