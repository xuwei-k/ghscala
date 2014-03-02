package ghscala

final case class SearchCode(
  total_count : Long,
  items       : List[SearchCode.Item]
) extends JsonToString[SearchCode]

object SearchCode {

  implicit val searchCodeCodecJson: CodecJson[SearchCode] =
    CodecJson.casecodec2(apply, unapply)(
      "total_count", "items"
    )

  final case class Item(
    name        :String,
    path        :String,
    sha         :String,
    url         :String,
    git_url     :String,
    html_url    :String,
    score       :Double,
    repository  :SearchCode.Repo
  ) extends JsonToString[Item]

  object Item {
    implicit val itemCodecJson: CodecJson[Item] =
      CodecJson.casecodec8(apply, unapply)(
        "name", "path", "sha", "url", "git_url",
        "html_url", "score", "repository"
      )
  }

  final case class Repo(
    id           :Long,
    name         :String,
    full_name    :String,
    owner        :User,
    _private     :Boolean,
    html_url     :String,
    description  :Option[String],
    fork         :Boolean,
    url          :String
  ) extends JsonToString[Repo]

  object Repo{
    implicit val repoCodecJson: CodecJson[Repo] =
      CodecJson.casecodec9(apply, unapply)(
        "id", "name", "full_name", "owner", "private",
        "html_url", "description", "fork", "url"
      )
  }
}

