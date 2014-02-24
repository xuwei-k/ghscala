package ghscala

final case class Comment(
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
) extends JsonToString[Comment]

object Comment {

  implicit val commentCodecJson: CodecJson[Comment] =
    CodecJson.casecodec11(apply, unapply)(
      "updated_at",
      "id",
      "created_at",
      "path",
      "body",
      "html_url",
      "commit_id",
      "user",
      "url",
      "position",
      "line"
    )

}
