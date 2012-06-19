package com.github.xuwei_k.ghscala

import Common._

trait GhScala{

  def repo(user:String,repo:String):Repo = single[Repo]("repos",user,repo)

  def repos(user:String):List[Repo] = list[Repo]("users",user,"repos")

  def refs(user:String,repo:String):List[Ref] = list[Ref]("repos",user,repo,"git/refs")

  def followers(user:String):List[User] = list[User]("users",user,"followers")

  def searchRepo(query:String):List[SearchRepo] = {
    val json = getJson("legacy/repos/search",query)
    json2list[SearchRepo](json \ "repositories")
  }

  def searchIssues(user:String,repo:String,query:String,state:IssueState = Open):List[IssueSearch] = {
    val json = getJson("legacy/issues/search",user,repo,state.name,query)
    json2list[IssueSearch](json \ "issues")
  }

  def commits(user:String,repo:String,sha:String):CommitResponse = single[CommitResponse]("repos",user,repo,"commits",sha)

  def trees(user:String,repo:String,sha:String):TreeResponse = single[TreeResponse]("repos",user,repo,"git/trees",sha)

  // TODO parameters http://developer.github.com/v3/issues/
  def issues(user:String,repo:String,state:IssueState = Open):List[Issue] =
    listWithParams[Issue]("repos",user,repo,"issues")("state" -> state.name)

  def issueEvents(user:String,repo:String,number:Long):List[IssueEvent] =
    list[IssueEvent]("repos",user,repo,"issues",number.toString,"events")

  def issueEvents(user:String,repo:String):List[IssueEvent2] =
    list[IssueEvent2]("repos",user,repo,"issues/events")

  def downloads(user:String,repo:String):List[Download] =
    list[Download]("repos",user,repo,"downloads")

  def download(user:String,repo:String,id:Long):Download = single[Download]("repos",user,repo,"downloads",id.toString)

  def forks(user:String,repo:String):List[Repo] = list[Repo]("repos",user,repo,"forks")

  // TODO error if contains organization
  def watchers(user:String,repo:String):List[User] = list[User]("repos",user,repo,"watchers")

  def watched(user:String):List[Repo] = list[Repo]("users",user,"watched")

  def collaborators(user:String,repo:String):List[User] = list[User]("repos",user,repo,"collaborators")

  def comments(user:String,repo:String):List[Comment] = list[Comment]("repos",user,repo,"comments")

  def comments(user:String,repo:String,sha:String):List[Comment] = list[Comment]("repos",user,repo,"commits",sha,"comments")

  def readme(user:String,repo:String):Contents = single[Contents]("repos",user,repo,"readme")

}

case class User(
  login        :String,
  id           :Long,
  avatar_url   :String,
  gravatar_id  :String,
  url          :String
)

object GhScala extends GhScala

