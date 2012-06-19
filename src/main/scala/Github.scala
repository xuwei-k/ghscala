package com.github.xuwei_k.ghscala

import java.io._
import net.liftweb.json._

trait GhScala{
  val BASE = "https://api.github.com/"

  def getJson(url:String*)(params:(String,String)*):JValue =
    ScalajHttp(BASE + url.mkString("/")).params(params.toList){ in =>
      JsonParser.parse(new BufferedReader(new InputStreamReader(in)))
    }

  def json2list[A](json:JValue)(implicit j:FromJValue[A]):List[A] = {
    val JArray(list) = json
    list.map(j.pure)
  }

  def getFromArray[A](url:String*)(params:(String,String)*)(implicit j:FromJValue[A]):List[A] =
    json2list[A](getJson(url:_*)(params:_*))

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

  implicit val issueSearch2Json = new FromJValue[IssueSearch]{
    def pure(j:JValue) = j.extract[IssueSearch]
  }

  implicit val downloadJson = new FromJValue[Download]{
    def pure(j:JValue) = j.extract[Download]
  }

  def repo(user:String,repo:String):Repo = reposJson pure getJson("repos",user,repo)()

  def repos(user:String):List[Repo] = getFromArray[Repo]("users",user,"repos")()

  def refs(user:String,repo:String):List[Ref] = getFromArray[Ref]("repos",user,repo,"git/refs")()

  def followers(user:String):List[User] = getFromArray[User]("users",user,"followers")()

  def searchRepo(query:String):List[SearchRepo] = {
    val json = getJson("legacy/repos/search",query)()
    json2list[SearchRepo](json \ "repositories")
  }

  def searchIssues(user:String,repo:String,query:String,state:IssueState = Open):List[IssueSearch] = {
    val json = getJson("legacy/issues/search",user,repo,state.name,query)()
    json2list[IssueSearch](json \ "issues")
  }

  def commits(user:String,repo:String,sha:String):CommitResponse = commitResJson pure getJson("repos",user,repo,"commits",sha)()

  def trees(user:String,repo:String,sha:String):TreeResponse = treeResJson pure getJson("repos",user,repo,"git/trees",sha)()

  // TODO parameters http://developer.github.com/v3/issues/
  def issues(user:String,repo:String,state:IssueState = Open):List[Issue] =
    getFromArray[Issue]("repos",user,repo,"issues")("state" -> state.name)

  def issueEvents(user:String,repo:String,number:Long):List[IssueEvent] =
    getFromArray[IssueEvent]("repos",user,repo,"issues",number.toString,"events")()

  def issueEvents(user:String,repo:String):List[IssueEvent2] =
    getFromArray[IssueEvent2]("repos",user,repo,"issues/events")()

  def downloads(user:String,repo:String):List[Download] =
    getFromArray[Download]("repos",user,repo,"downloads")()

  def download(user:String,repo:String,id:Long):Download = downloadJson pure getJson("repos",user,repo,"downloads",id.toString)()

  def forks(user:String,repo:String):List[Repo] = getFromArray[Repo]("repos",user,repo,"forks")()
}

trait FromJValue[A]{
  def pure(j:JValue):A
}

case class User(
  login        :String,
  id           :Long,
  avatar_url   :String,
  gravatar_id  :String,
  url          :String
)

object GhScala extends GhScala

