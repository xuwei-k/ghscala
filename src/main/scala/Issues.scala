package ghscala

sealed abstract class State(private[ghscala] val name:String)
case object Open   extends State("open")
case object Closed extends State("closed")
object State{
  def apply(name:String):State = name match {
    case Open.name   => Open
    case Closed.name => Closed
  }
}

final case class PullRequest(
  patch_url   :Option[String],
  diff_url    :Option[String],
  html_url    :Option[String]
) extends JsonToString[PullRequest]

object PullRequest {

  implicit val pullRequestCodecJson: CodecJson[PullRequest] =
    CodecJson.casecodec3(apply, unapply)(
      "patch_url", "diff_url", "html_url"
    )

}

final case class Label(
  color        :String,
  url          :String,
  name         :String
) extends JsonToString[Label]

object Label {

  implicit val labelCodecJson: CodecJson[Label] =
    CodecJson.casecodec3(apply, unapply)("color", "url", "name")

}

final case class Milestone(
  title         :String,
  closed_issues :Long,
  due_on        :Option[DateTime],
  number        :Long,
  created_at    :DateTime,
  description   :Option[String],
  creator       :User,
  state         :String,
  id            :Long,
  open_issues   :Long,
  url           :String
) extends JsonToString[Milestone] {
  lazy val getState:State = State(state)
}

object Milestone {

  implicit val issueCodecJson: CodecJson[Milestone] =
    CodecJson.casecodec11(apply, unapply)(
      "title",
      "closed_issues",
      "due_on",
      "number",
      "created_at",
      "description",
      "creator",
      "state",
      "id",
      "open_issues",
      "url"
    )
}

final case class Issue(
  comments     :Long,
  user         :User,
  labels       :List[Label],
  state        :String,
  number       :Long,
  pull_request :Option[PullRequest],
  milestone    :Option[Milestone],
  assignee     :Option[User],
  html_url     :String,
  url          :String,
  body         :Option[String],
  closed_at    :Option[DateTime],
  title        :String,
  updated_at   :DateTime,
  created_at   :DateTime
) extends JsonToString[Issue] {

  override def toString = title
  lazy val getState: State = State(state)
}

object Issue {

  implicit val issueCodecJson: CodecJson[Issue] =
    CodecJson.casecodec15(apply, unapply)(
      "comments",
      "user",
      "labels",
      "state",
      "number",
      "pull_request",
      "milestone",
      "assignee",
      "html_url",
      "url",
      "body",
      "closed_at",
      "title",
      "updated_at",
      "created_at"
    )

}

final case class IssueEvent(
  event      :String,
  actor      :User,
  id         :Long,
  created_at :DateTime,
  commit_id  :Option[String],
  url        :String
) extends JsonToString[IssueEvent] {
  val eventType:EventType = EventType(event)
}

object IssueEvent {

  implicit val issueEventCodecJson: CodecJson[IssueEvent] =
    CodecJson.casecodec6(apply, unapply)(
      "event", "actor", "id", "created_at", "commit_id", "url"
    )
}

final case class IssueEvent2(
  event      :String,
  actor      :User,
  issue      :Issue,
  id         :Long,
  commit_id  :Option[String],
  created_at :DateTime,
  url        :String
) extends JsonToString[IssueEvent2] {
  val eventType:EventType = EventType(event)
}

object IssueEvent2 {

  implicit val issueEvent2CodecJson: CodecJson[IssueEvent2] =
    CodecJson.casecodec7(apply, unapply)(
      "event", "actor", "issue", "id", "commit_id", "created_at", "url"
    )
}

sealed abstract class EventType(private[ghscala] val name:String)
object EventType{
  case object Closed     extends EventType("closed")
  case object Reopened   extends EventType("reopened")
  case object Subscribed extends EventType("subscribed")
  case object Merged     extends EventType("merged")
  case object Referenced extends EventType("referenced")
  case object Mentioned  extends EventType("mentioned")
  case object Assigned   extends EventType("assigned")
  case object Unknown    extends EventType("unknown")

  def apply(name:String):EventType = name match{
    case Closed.name     => Closed
    case Reopened.name   => Reopened
    case Subscribed.name => Subscribed
    case Merged.name     => Merged
    case Referenced.name => Referenced
    case Mentioned.name  => Mentioned
    case Assigned.name   => Assigned
    case _               => Unknown
  }
}
