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
)

final case class PullLinks(
  self            :PullLinks.Link,
  review_comments :PullLinks.Link,
  issue           :PullLinks.Link,
  html            :PullLinks.Link,
  comments        :PullLinks.Link,
  commits         :PullLinks.Link,
  statuses        :PullLinks.Link
)

object PullLinks{
  object Link {
    implicit val linkDecodeJson: DecodeJson[Link] =
      DecodeJson.jdecode1L(apply)("href")
  }

  final case class Link(href :String)

  implicit val pullLinksDecodeJson: DecodeJson[PullLinks] =
    DecodeJson.jdecode7L(PullLinks.apply)(
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
  )

  object Ref {
    implicit val pullRefDecodeJson: DecodeJson[Ref] =
      DecodeJson.jdecode5L(Ref.apply)(
        "user", "label", "sha", "repo", "ref"
      )
  }

  implicit val pullDecodeJson: DecodeJson[Pull] =
    DecodeJson.jdecode18L(Pull.apply)(
      "updated_at", "head", "title", "id", "created_at", "_links",
      "merged_at", "base", "diff_url", "body", "state", "html_url",
      "issue_url", "user", "url", "patch_url", "number", "closed_at"
    )
}

