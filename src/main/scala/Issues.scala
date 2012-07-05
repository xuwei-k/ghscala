package com.github.xuwei_k.ghscala

sealed abstract class State(private[ghscala] val name:String)
case object Open   extends State("open")
case object Closed extends State("closed")
object State{
  def apply(name:String):State = name match {
    case Open.name   => Open
    case Closed.name => Closed
  }
}

case class PullRequest(
  patch_url   :Option[String],
  diff_url    :Option[String],
  html_url    :Option[String]
)

case class Label(
  color        :String,
  url          :String,
  name         :String
)

case class Milestone(
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
){
  lazy val getState:State = State(state)
}

case class Issue(
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
){

  override def toString = title
  lazy val getState:State = State(state)
}

case class IssueEvent(
  event      :String,
  actor      :User,
  id         :Long,
  created_at :DateTime,
  commit_id  :Option[String],
  url        :String
){
  val eventType:EventType = EventType(event)
}

case class IssueEvent2(
  event      :String,
  actor      :User,
  issue      :Issue,
  id         :Long,
  commit_id  :Option[String],
  created_at :DateTime,
  url        :String
){
  val eventType:EventType = EventType(event)
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
