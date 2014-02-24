package ghscala

final case class User(
  login        :String,
  id           :Long,
  avatar_url   :String,
  gravatar_id  :Option[String],
  url          :String
) extends JsonToString[User]

object User {

  implicit val userCodecJson: CodecJson[User] =
    CodecJson.casecodec5(apply, unapply)(
      "login", "id", "avatar_url", "gravatar_id", "url"
    )

}

final case class Org(
  login        :String,
  id           :Long,
  avatar_url   :String,
  url          :String
) extends JsonToString[Org]

object Org {

  implicit val orgCodecJson: CodecJson[Org] =
    CodecJson.casecodec4(apply, unapply)(
      "login", "id", "avatar_url", "url"
    )

}

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
) extends JsonToString[Organization]

object Organization {

  implicit val organizationCodecJson: CodecJson[Organization] =
    CodecJson.casecodec16(apply, unapply)(
      "type", "avatar_url", "blog", "company", "created_at", "email", "followers",
      "following", "html_url", "id", "location", "login", "name", "public_gists",
      "public_repos", "url"
    )
}
