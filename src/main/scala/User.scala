package ghscala

case class User(
  login        :String,
  id           :Long,
  avatar_url   :String,
  gravatar_id  :String,
  url          :String
)

case class Org(
  login        :String,
  id           :Long,
  avatar_url   :String,
  url          :String
)

case class Organization(
  `type`       :String,
  avatar_url   :String,
  blog         :Option[String],
  company      :Option[String],
  created_at   :Option[DateTime],
  email        :Option[String],
  followers    :Long,
  following    :Long,
  html_url     :String,
  id           :Long,
  location     :Option[String],
  login        :String,
  name         :Option[String],
  public_gists :Long,
  public_repos :Long,
  url          :String
)
