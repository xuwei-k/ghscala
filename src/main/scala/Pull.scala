package ghscala

case class Pull(
  updated_at :DateTime,
  head       :Pull.Ref,
  title      :String,
  id         :Long,
  created_at :DateTime,
  _links     :PullLinks,
  merged_at  :Option[DateTime],
  base       :Pull.Ref,
  diff_url   :String,
  body       :String,
  state      :String,
  html_url   :String,
  issue_url  :String,
  user       :User,
  url        :String,
  patch_url  :String,
  number     :Long,
  closed_at  :Option[DateTime]
)

case class PullLinks(
  self            :PullLinks.Self,
  review_comments :PullLinks.Review_comments,
  issue           :PullLinks.Issue,
  html            :PullLinks.Html,
  comments        :PullLinks.Comments
)

object PullLinks{
  case class Issue(href :String)
  case class Self( href :String)
  case class Review_comments(href :String)
  case class Comments(href :String)
  case class Html(href :String)
}

object Pull{
  case class Ref(
    user  :Option[User],
    label :String,
    sha   :String,
    repo  :Option[Repo],
    ref   :String
  )
}

