package ghscala

final case class SearchIssues(
  total_count   :Long,
  items         :List[Issue]
) extends JsonToString[SearchIssues]

object SearchIssues {
  implicit val searchIssuesCodecJson: CodecJson[SearchIssues] =
    CodecJson.casecodec2(apply, unapply)(
      "total_count", "items"
    )
}

