package ghscala

final case class Pull(
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
) extends JsonToString[Pull]

final case class PullLinks(
  self            :PullLinks.Link,
  review_comments :PullLinks.Link,
  issue           :PullLinks.Link,
  html            :PullLinks.Link,
  comments        :PullLinks.Link,
  commits         :PullLinks.Link,
  statuses        :PullLinks.Link
) extends JsonToString[PullLinks]

object PullLinks{
  object Link {
    implicit val linkCodecJson: CodecJson[Link] =
      CodecJson.casecodec1(apply, unapply)("href")
  }

  final case class Link(href :String)

  implicit val pullLinksCodecJson: CodecJson[PullLinks] =
    CodecJson.casecodec7(apply, unapply)(
      "self", "review_comments", "issue", "html",
      "comments", "commits", "statuses"
    )
}

object Pull{
  final case class Ref(
    user  :Option[User],
    label :String,
    sha   :String,
    repo  :Option[Repo],
    ref   :String
  ) extends JsonToString[Ref]

  object Ref {
    implicit val pullRefCodecJson: CodecJson[Ref] =
      CodecJson.casecodec5(apply, unapply)(
        "user", "label", "sha", "repo", "ref"
      )
  }

  implicit val pullCodecJson: CodecJson[Pull] =
    CodecJson.casecodec18(apply, unapply)(
      "updated_at", "head", "title", "id", "created_at", "_links",
      "merged_at", "base", "diff_url", "body", "state", "html_url",
      "issue_url", "user", "url", "patch_url", "number", "closed_at"
    )
}

