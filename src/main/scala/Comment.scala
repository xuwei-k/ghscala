package ghscala

case class Comment(
  updated_at :String,
  id         :Long,
  created_at :String,
  path       :Option[String],
  body       :String,
  html_url   :String,
  commit_id  :String,
  user       :User,
  url        :String,
  position   :Option[Long],
  line       :Option[Long]
)
