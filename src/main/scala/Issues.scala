package com.github.xuwei_k.ghscala

sealed abstract class IssueState(private[ghscala] val name:String)
case object Open   extends IssueState("open")
case object Closed extends IssueState("closed")
object IssueState{
  def apply(name:String):IssueState = name match {
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
//  due_on        :Unknown, TODO ?
  number        :Long,
  created_at    :String,
  description   :Option[String],
  creator       :User,
  state         :String,
  id            :Long,
  open_issues   :Long,
  url           :String
){
  lazy val getState:IssueState = IssueState(state)
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
  lazy val getState:IssueState = IssueState(state)
}

case class IssueEvent(
  event      :String,
  actor      :User,
  id         :Long,
  created_at :DateTime,
  commit_id  :Option[String],
  url        :String
)

case class IssueEvent2(
  event      :String, // TODO ADT ?
  actor      :User,
  issue      :Issue,
  id         :Long,
  commit_id  :Option[String],
  created_at :DateTime,
  url        :String
)
