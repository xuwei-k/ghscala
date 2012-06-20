package com.github.xuwei_k.ghscala

import Common._

trait GhScala{

  def repo(user:String,repo:String):Repo = single[Repo]("repos",user,repo)

  val repos = (page:Int) => (user:String) => listRequest[Repo]("users",user,"repos")()(page)

  val refs = (page:Int) => {
    listRequest[Ref]("repos",_:String,_:String,"git/refs")()(page)
  }.tupled

  val followers = (user:String) => listRequest[User]("users",user,"followers")()(_)

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

  val downloads = (page:Int) => {
    listRequest[Download]("repos",_:String,_:String,"downloads")()(page)
  }.tupled

  def download(user:String,repo:String,id:Long):Download = single[Download]("repos",user,repo,"downloads",id.toString)

  val forks = (page:Int) => {
    listRequest[Repo]("repos",_:String,_:String,"forks")()(page)
  }.tupled

  // TODO error if contains organization
  val watchers = (page:Int) => {
    listRequest[User]("repos",_:String,_:String,"watchers")()(page)
  }.tupled

  val watched = (user:String) => list[Repo]("users",user,"watched")

  val collaborators = (page:Int) => {
    listRequest[User]("repos",_:String,_:String,"collaborators")()(page)
  }.tupled

  def comments(user:String,repo:String):List[Comment] = list[Comment]("repos",user,repo,"comments")

  def comments(user:String,repo:String,sha:String):List[Comment] = list[Comment]("repos",user,repo,"commits",sha,"comments")

  def readme(user:String,repo:String,ref:String = null):Contents =
    Option(ref).collect{ case r if ! r.isEmpty =>
      singleWithParams[Contents]("repos",user,repo,"readme")("ref"-> r)
    }.getOrElse{
      single[Contents]("repos",user,repo,"readme")
    }

  def pulls(user:String,repo:String,state:State = Open):List[Pull] = listWithParams[Pull]("repos",user,repo,"pulls")("state" -> state.name )

  val orgs = (page:Int) => {
    listRequest[Org]("users",_:String,"orgs")()(page)
  }

  def org(orgName:String):Organization = single[Organization]("orgs",orgName)
}

object GhScala extends GhScala

