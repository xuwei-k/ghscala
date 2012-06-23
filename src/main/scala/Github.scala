package com.github.xuwei_k.ghscala

import Common._
import net.liftweb.json._

trait GhScala{

  def repo(user:String,repo:String):Repo = single[Repo]("repos",user,repo)

  val searchRepo = (query:String) => {
    val json = getJson("legacy/repos/search",query)
    json2list[SearchRepo](json \ "repositories")
  }

  def searchIssues(user:String,repo:String,query:String,state:State = Open) = {
    val json = getJson("legacy/issues/search",user,repo,state.name,query)
    json2list[IssueSearch](json \ "issues")
  }

  def commits(user:String,repo:String,sha:String):CommitResponse = single[CommitResponse]("repos",user,repo,"commits",sha)

  def trees(user:String,repo:String,sha:String):TreeResponse = single[TreeResponse]("repos",user,repo,"git/trees",sha)

  // TODO parameters http://developer.github.com/v3/issues/
  def issues(user:String,repo:String,state:State = Open):List[Issue] =
    listWithParams[Issue]("repos",user,repo,"issues")("state" -> state.name)

  def issueEvents(user:String,repo:String,number:Long):List[IssueEvent] =
    list[IssueEvent]("repos",user,repo,"issues",number.toString,"events")

  def issueEvents(user:String,repo:String):List[IssueEvent2] =
    list[IssueEvent2]("repos",user,repo,"issues/events")

  def download(user:String,repo:String,id:Long):Download = single[Download]("repos",user,repo,"downloads",id.toString)

  val watched = (user:String) => list[Repo]("users",user,"watched")

  def comments(user:String,repo:String):List[Comment] = list[Comment]("repos",user,repo,"comments")

  def comments(user:String,repo:String,sha:String):List[Comment] = list[Comment]("repos",user,repo,"commits",sha,"comments")

  def readme(user:String,repo:String,ref:String = null):Contents =
    Option(ref).collect{ case r if ! r.isEmpty =>
      singleWithParams[Contents]("repos",user,repo,"readme")("ref"-> r)
    }.getOrElse{
      single[Contents]("repos",user,repo,"readme")
    }

  def pulls(user:String,repo:String,state:State = Open):List[Pull] = listWithParams[Pull]("repos",user,repo,"pulls")("state" -> state.name )

  def org(orgName:String):Organization = single[Organization]("orgs",orgName)

  def rateLimit():Option[Long] = {
    for{
      JInt(j) <- getJson("rate_limit") \ "rate" \ "remaining"
    } yield j
  }.headOption.map{_.longValue}

  val watchers: PARAM => (List[User],List[Org]) =
    (params:PARAM ) => {
      val list = Iterator.from(1).map(Core._watchers(_)(params)).takeWhile{case (a,b) => a.size + b.size == DEFAULT_PER_PAGE }.toList
      list.flatMap{_._1} -> list.flatMap{_._2}
    }

  def blob(user:String,repo:String,sha:String):Blob = single[Blob]("repos",user,repo,"git/blobs",sha)

}

object GhScala extends GhScala with All

