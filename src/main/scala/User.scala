package ghscala

import argonaut._

final case class User(
  login        :String,
  id           :Long,
  avatar_url   :String,
  gravatar_id  :Option[String],
  url          :String
)

object User {

  implicit val userDecodeJson: DecodeJson[User] =
    DecodeJson.jdecode5L(User.apply)("login", "id", "avatar_url", "gravatar_id", "url")

}

case class Org(
  login        :String,
  id           :Long,
  avatar_url   :String,
  url          :String
)

final case class Organization(
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

object Organization {

  implicit val organizationDecodeJson: DecodeJson[Organization] =
    DecodeJson.jdecode16L(Organization.apply)(
      "type", "avatar_url", "blog", "company", "created_at", "email", "followers",
      "following", "html_url", "id", "location", "login", "name", "public_gists",
      "public_repos", "url"
    )
}
