package ghscala

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

case class Repo(
  has_downloads :Option[Boolean],
  name          :String,
  has_issues    :Boolean,
  forks         :Int,
  `private`     :Boolean,
  size          :Int,
  open_issues   :Int,
  url           :String,
  description   :String, // TODO possibly optional ?
  pushed_at     :DateTime,
  git_url       :String,
  has_wiki      :Boolean,
  fork          :Boolean,
  id            :Int,
  language      :Option[String],
  homepage      :String, // TODO possibly optional ?
  created_at    :DateTime,
  html_url      :String,
  updated_at    :DateTime,
  watchers      :Int,
  master_branch :Option[String],
  owner         :User
//  full_name     :String,
//  mirror_url    :String,
//  svn_url       :String,
//  clone_url     :String,
//  ssh_url       :String
){
  def master:String = master_branch.getOrElse("master")
}

