package ghscala

import argonaut._

case class SearchRepo(
  has_downloads :Option[Boolean],
  name          :String,
  has_issues    :Boolean,
  forks         :Int,
  `private`     :Boolean,
  size          :Int,
  open_issues   :Int,
  url           :String,
  description   :Option[String],
  pushed_at     :DateTime,
  has_wiki      :Boolean,
  fork          :Boolean,
  language      :String,
  homepage      :Option[String],
  created_at    :DateTime,
  updated_at    :DateTime,
  watchers      :Int,
  master_branch :Option[String],
  owner         :String
//  username      :String
//  organization  :Option[String]
)

final case class Repo(
  has_downloads :Option[Boolean],
  name          :String,
  has_issues    :Boolean,
  forks         :Int,
  `private`     :Boolean,
  size          :Int,
  open_issues   :Int,
  url           :String,
  description   :String, // TODO possibly optional ?
  pushed_at     :String, // TODO DateTime,
  git_url       :String,
  has_wiki      :Boolean,
  fork          :Boolean,
  id            :Int,
  language      :Option[String],
  homepage      :String, // TODO possibly optional ?
  created_at    :String, // TODO DateTime,
  html_url      :String,
  updated_at    :String, // TODO DateTime,
  watchers      :Int,
  master_branch :Option[String],
  owner         :User // TODO User or Org

//  full_name     :String,
//  mirror_url    :String,
//  svn_url       :String,
//  clone_url     :String,
//  ssh_url       :String
){
  def master:String = master_branch.getOrElse("master")
}

object Repo {

  implicit val repoDecodeJson: DecodeJson[Repo] =
    DecodeJson.jdecode22L(Repo.apply)(
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
      "master_branch",
      "owner"
    )
}
