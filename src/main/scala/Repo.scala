package ghscala

final case class SearchRepo(
  total_count :Long,
  items       :List[Repo]
) extends JsonToString[SearchRepo]

object SearchRepo {
  implicit val searchRepoCodecJson: CodecJson[SearchRepo] =
    CodecJson.casecodec2(apply, unapply)(
      "total_count", "items"
    )
}

final case class Repo(
  has_downloads :Option[Boolean],
  name          :String,
  has_issues    :Boolean,
  forks         :Int,
  `private`     :Boolean,
  size          :Int,
  open_issues   :Int,
  url           :String,
  description   :Option[String],
  pushed_at     :Option[DateTime],
  git_url       :String,
  has_wiki      :Boolean,
  fork          :Boolean,
  id            :Int,
  language      :Option[String],
  homepage      :Option[String],
  created_at    :DateTime,
  html_url      :String,
  updated_at    :DateTime,
  watchers      :Int,
  default_branch:Option[String],
  owner         :User // TODO User or Org

//  full_name     :String,
//  mirror_url    :String,
//  svn_url       :String,
//  clone_url     :String,
//  ssh_url       :String
) extends JsonToString[Repo] {
  def master: String = default_branch.getOrElse("master")
}

object Repo {

  implicit val repoCodecJson: CodecJson[Repo] =
    CodecJson.casecodec22(apply, unapply)(
      "has_downloads",
      "name",
      "has_issues",
      "forks",
      "private",
      "size",
      "open_issues",
      "url",
      "description",
      "pushed_at",
      "git_url",
      "has_wiki",
      "fork",
      "id",
      "language",
      "homepage",
      "created_at",
      "html_url",
      "updated_at",
      "watchers",
      "default_branch",
      "owner"
    )
}
