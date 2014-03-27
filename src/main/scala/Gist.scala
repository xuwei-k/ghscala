package ghscala

final case class Gist(
  comments     :Long,
  comments_url :String,
  commits_url  :String,
  created_at   :String,
  description  :String,
  files        :Map[String, Gist.File],
  forks        :List[Gist.Fork],
  forks_url    :String,
  git_pull_url :String,
  git_push_url :String,
  history      :List[Gist.History],
  html_url     :String,
  id           :String,
  public       :Boolean,
  updated_at   :String,
  url          :String,
  user         :User
)

object Gist {

  implicit val gistCodecJson: CodecJson[Gist] =
    CodecJson.casecodec17(apply, unapply)(
      "comments", "comments_url", "commits_url", "created_at", "description",
      "files", "forks", "forks_url", "git_pull_url", "git_push_url", "history",
      "html_url", "id", "public", "updated_at", "url", "user"
    )

  final case class History(
    url           :String,
    version       :String,
    user          :User,
    change_status :Gist.ChangeStatus,
    committed_at  :String
  ) extends JsonToString[History]

  object History {
    implicit val historyCodecJson: CodecJson[History] =
      CodecJson.casecodec5(apply, unapply)(
        "url", "version", "user", "change_status", "committed_at"
      )
  }

  final case class ChangeStatus(
    additions :Long,
    deletions :Long,
    total     :Long
  ) extends JsonToString[ChangeStatus]

  object ChangeStatus {
    implicit val changeStatusCodecJson: CodecJson[ChangeStatus] =
      CodecJson.casecodec3(apply, unapply)(
        "additions", "deletions", "total"
      )
  }

  final case class File(
    filename :String,
    language :Option[String],
    _type    :String,
    raw_url  :String,
    size     :Long,
    content  :String
  ) extends JsonToString[File]

  object File {
    implicit val fileCodecJson: CodecJson[File] =
      CodecJson.casecodec6(apply, unapply)(
        "filename", "language", "type", "raw_url", "size", "content"
      )
  }

  final case class Fork(
    id         :String,
    url        :String,
    user       :User,
    created_at :String,
    updated_at :String
  ) extends JsonToString[Fork]

  object Fork {
    implicit val forkJson: CodecJson[Fork] =
      CodecJson.casecodec5(apply, unapply)(
        "id", "url", "user", "created_at", "updated_at"
      )
  }

}

