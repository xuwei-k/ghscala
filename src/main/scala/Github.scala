package com.github.xuwei_k.ghscala

import java.io._
import net.liftweb.json._

trait GhScala{
  val BASE = "https://api.github.com/"

  def getJson(url:String*)(params:(String,String)*):JValue =
    ScalajHttp(BASE + url.mkString("/")).params(params.toList){ in =>
      JsonParser.parse(new BufferedReader(new InputStreamReader(in)))
    }

  def getFromArray[A](url:String*)(params:(String,String)*)(implicit j:FromJValue[A]):List[A] =
    for{
      JArray(list) <- getJson(url:_*)(params:_*)
      obj          <- list
    } yield j.pure(obj)

  private implicit val formats = DefaultFormats

  implicit val reposJson = new FromJValue[Repo]{
    def pure(j:JValue) = j.extract[Repo]
  }

  implicit val refJson = new FromJValue[Ref]{
    def pure(j:JValue) = j.extract[Ref]
  }

  implicit val userJson = new FromJValue[User]{
    def pure(j:JValue) = j.extract[User]
  }

  implicit val searchRepoJson = new FromJValue[SearchRepo]{
    def pure(j:JValue) = j.extract[SearchRepo]
  }

  implicit val commitResJson = new FromJValue[CommitResponse]{
    def pure(j:JValue) = j.extract[CommitResponse]
  }

  implicit val treeResJson = new FromJValue[TreeResponse]{
    def pure(j:JValue) = j.extract[TreeResponse]
  }

  implicit val issueJson = new FromJValue[Issue]{
    def pure(j:JValue) = j.extract[Issue]
  }

  implicit val issueEventJson = new FromJValue[IssueEvent]{
    def pure(j:JValue) = j.extract[IssueEvent]
  }

  implicit val issueEvent2Json = new FromJValue[IssueEvent2]{
    def pure(j:JValue) = j.extract[IssueEvent2]
  }

  implicit val issueSearchOpenJson = new FromJValue[IssueSearchOpen]{
    def pure(j:JValue) = j.extract[IssueSearchOpen]
  }

  def repo(user:String,repo:String):Repo = reposJson pure getJson("repos",user,repo)()

  def repos(user:String):List[Repo] = getFromArray[Repo]("users",user,"repos")()

  def refs(user:String,repo:String):List[Ref] = getFromArray[Ref]("repos",user,repo,"git/refs")()

  def followers(user:String):List[User] = getFromArray[User]("users",user,"followers")()

  def searchRepo(query:String):List[SearchRepo] = getFromArray[SearchRepo]("legacy/repos/search",query)()

  def searchOpenIssues(user:String,repo:String,query:String):List[IssueSearchOpen]
    = getFromArray[IssueSearchOpen]("legacy/issues/search",user,repo,Open.name,query)()

  def commits(user:String,repo:String,sha:String):CommitResponse = commitResJson pure getJson("repos",user,repo,"commits",sha)()

  def trees(user:String,repo:String,sha:String):TreeResponse = treeResJson pure getJson("repos",user,repo,"git/trees",sha)()

  // TODO parameters http://developer.github.com/v3/issues/
  def issues(user:String,repo:String,state:IssueState = Open):List[Issue] =
    getFromArray[Issue]("repos",user,repo,"issues")("state" -> state.name)

  def issueEvents(user:String,repo:String,number:Long):List[IssueEvent] =
    getFromArray[IssueEvent]("repos",user,repo,"issues",number.toString,"events")()

  def issueEvents(user:String,repo:String):List[IssueEvent2] =
    getFromArray[IssueEvent2]("repos",user,repo,"issues/events")()
}

trait FromJValue[A]{
  def pure(j:JValue):A
}

case class Ref(
  ref      :String,
  url      :String,
  `object` :GitObj
){
  lazy val name:String = ref.split('/').last
  lazy val isTag:Boolean = ref.split('/')(1) == "tags"
  lazy val isBranch:Boolean = ! isTag
}

case class GitObj(`type`:String,sha:String,url:String)

case class SearchRepo(
  has_downloads :Boolean,
  name          :String,
  has_issues    :Boolean,
  forks         :Int,
  `private`     :Boolean,
  size          :Int,
  open_issues   :Int,
  url           :String,
  description   :Option[String],
  pushed_at     :DateTime,
  has_wiki      :Boolean,
  fork          :Boolean,
  language      :String,
  homepage      :Option[String],
  created_at    :DateTime,
  updated_at    :DateTime,
  watchers      :Int,
  master_branch :Option[String],
  owner         :String
//  username      :String
//  organization  :Option[String]
)

case class Repo(
  has_downloads :Boolean,
  name          :String,
  has_issues    :Boolean,
  forks         :Int,
  `private`     :Boolean,
  size          :Int,
  open_issues   :Int,
  url           :String,
  description   :String, // TODO possibly optional ?
  pushed_at     :DateTime,
  git_url       :String,
  has_wiki      :Boolean,
  fork          :Boolean,
  id            :Int,
  language      :String,
  homepage      :String, // TODO possibly optional ?
  created_at    :DateTime,
  html_url      :String,
  updated_at    :DateTime,
  watchers      :Int,
  master_branch :Option[String],
  owner         :User
//  full_name     :String,
//  mirror_url    :String,
//  svn_url       :String,
//  clone_url     :String,
//  ssh_url       :String
){
  def master:String = master_branch.getOrElse("master")
}

case class User(
  login        :String,
  id           :Long,
  avatar_url   :String,
  gravatar_id  :String,
  url          :String
)

object GhScala extends GhScala

