package com.github.xuwei_k.ghscala

import Common._

trait GhScala{

  def repo(user:String,repo:String):Repo = reposJson pure getJson("repos",user,repo)

  def repos(user:String):List[Repo] = getFromArray[Repo]("users",user,"repos")

  def refs(user:String,repo:String):List[Ref] = getFromArray[Ref]("repos",user,repo,"git/refs")

  def followers(user:String):List[User] = getFromArray[User]("users",user,"followers")

  def searchRepo(query:String):List[SearchRepo] = {
    val json = getJson("legacy/repos/search",query)
    json2list[SearchRepo](json \ "repositories")
  }

  def searchIssues(user:String,repo:String,query:String,state:IssueState = Open):List[IssueSearch] = {
    val json = getJson("legacy/issues/search",user,repo,state.name,query)
    json2list[IssueSearch](json \ "issues")
  }

  def commits(user:String,repo:String,sha:String):CommitResponse = commitResJson pure getJson("repos",user,repo,"commits",sha)

  def trees(user:String,repo:String,sha:String):TreeResponse = treeResJson pure getJson("repos",user,repo,"git/trees",sha)

  // TODO parameters http://developer.github.com/v3/issues/
  def issues(user:String,repo:String,state:IssueState = Open):List[Issue] =
    getFromArrayWithParams[Issue]("repos",user,repo,"issues")("state" -> state.name)

  def issueEvents(user:String,repo:String,number:Long):List[IssueEvent] =
    getFromArray[IssueEvent]("repos",user,repo,"issues",number.toString,"events")

  def issueEvents(user:String,repo:String):List[IssueEvent2] =
    getFromArray[IssueEvent2]("repos",user,repo,"issues/events")

  def downloads(user:String,repo:String):List[Download] =
    getFromArray[Download]("repos",user,repo,"downloads")

  def download(user:String,repo:String,id:Long):Download = downloadJson pure getJson("repos",user,repo,"downloads",id.toString)

  def forks(user:String,repo:String):List[Repo] = getFromArray[Repo]("repos",user,repo,"forks")

  // TODO error if contains organization
  def watchers(user:String,repo:String):List[User] = getFromArray[User]("repos",user,repo,"watchers")

  def watched(user:String):List[Repo] = getFromArray[Repo]("users",user,"watched")

  def collaborators(user:String,repo:String):List[User] = getFromArray[User]("repos",user,repo,"collaborators")

  def comments(user:String,repo:String):List[Comment] = getFromArray[Comment]("repos",user,repo,"comments")

  def comments(user:String,repo:String,sha:String):List[Comment] = getFromArray[Comment]("repos",user,repo,"commits",sha,"comments")
}

case class User(
  login        :String,
  id           :Long,
  avatar_url   :String,
  gravatar_id  :String,
  url          :String
)

object GhScala extends GhScala

