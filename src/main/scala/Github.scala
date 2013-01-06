package com.github.xuwei_k.ghscala

import org.json4s._

class GhScala(override val isDebug:Boolean) extends Common with All{

  /**
   * @see [[http://developer.github.com/v3/repos]]
   */
  def repo(user:String,repo:String):Repo = single[Repo]("repos",user,repo)

  val searchRepo = (query:String) => {
    val json = getJson("legacy/repos/search",query)
    json2list[SearchRepo](json \ "repositories")
  }

  /**
   * @see [[http://developer.github.com/v3/search/#search-issues]]
   */
  def searchIssues(user:String,repo:String,query:String,state:State = Open) = {
    val json = getJson("legacy/issues/search",user,repo,state.name,query)
    json2list[IssueSearch](json \ "issues")
  }

  /**
   * @see [[http://developer.github.com/v3/git/commits]]
   */
  def commits(user:String,repo:String,sha:String):CommitResponse = single[CommitResponse]("repos",user,repo,"commits",sha)

  /**
   * @see [[http://developer.github.com/v3/git/trees]]
   */
  def trees(user:String,repo:String,sha:String,recursive:java.lang.Integer = null):TreeResponse = {
    val request = singleWithParams[TreeResponse]("repos",user,repo,"git/trees",sha) _
    Option(recursive).map{ r => request(Seq("recursive" -> r.toString)) }.getOrElse(request(Nil))
  }

  /**
   * @see [[http://developer.github.com/v3/issues]]
   * TODO parameters
   */
  def issues(user:String,repo:String,state:State = Open):List[Issue] =
    listWithParams[Issue]("repos",user,repo,"issues")("state" -> state.name)

  /**
   * @see [[http://developer.github.com/v3/issues/events/]]
   */
  def issueEvents(user:String,repo:String,number:Long):List[IssueEvent] =
    list[IssueEvent]("repos",user,repo,"issues",number.toString,"events")

  /**
   * @see [[http://developer.github.com/v3/issues/events/]]
   */
  def issueEvents(user:String,repo:String):List[IssueEvent2] =
    list[IssueEvent2]("repos",user,repo,"issues/events")

  /**
   * @see [[http://developer.github.com/v3/repos/downloads]]
   */
  def download(user:String,repo:String,id:Long):Download = single[Download]("repos",user,repo,"downloads",id.toString)

  val watched = (user:String) => list[Repo]("users",user,"watched")

  /**
   * @see [[http://developer.github.com/v3/repos/comments]]
   */
  def comments(user:String,repo:String):List[Comment] = list[Comment]("repos",user,repo,"comments")

  /**
   * @see [[http://developer.github.com/v3/repos/comments]]
   */
  def comments(user:String,repo:String,sha:String):List[Comment] = list[Comment]("repos",user,repo,"commits",sha,"comments")

  /**
   * @see [[http://developer.github.com/v3/repos/contents]]
   */
  def readme(user:String,repo:String,ref:String = null):Contents =
    refParamOpt[Contents]("repos",user,repo,"readme")(ref)

  /**
   * @see [[http://developer.github.com/v3/pulls]]
   */
  def pulls(user:String,repo:String,state:State = Open):List[Pull] = listWithParams[Pull]("repos",user,repo,"pulls")("state" -> state.name )

  /**
   * @see [[http://developer.github.com/v3/orgs]]
   */
  def org(orgName:String):Organization = single[Organization]("orgs",orgName)

  /**
   * @see [[http://developer.github.com/v3/#rate-limiting]]
   */
  def rateLimit():Option[Long] = {
    for{
      JInt(j) <- getJson("rate_limit") \ "rate" \ "remaining"
    } yield j
  }.headOption.map{_.longValue}

  val watchers: PARAM => (List[User],List[Org]) =
    (params:PARAM ) => {
      val list = Iterator.from(1).map(_watchers(_)(params)).takeWhile{case (a,b) => a.size + b.size == DEFAULT_PER_PAGE }.toList
      list.flatMap{_._1} -> list.flatMap{_._2}
    }

  /**
   * @see [[http://developer.github.com/v3/git/blobs]]
   */
  def blob(user:String,repo:String,sha:String):Blob = single[Blob]("repos",user,repo,"git/blobs",sha)

  /**
   * @see [[http://developer.github.com/v3/repos/contents]]
   */
  def contents(user:String,repo:String,path:String,ref:String = null):Contents =
    refParamOpt[Contents]("repos",user,repo,"contents",path)(ref)

  /**
   * @see [[http://developer.github.com/v3/issues/labels]]
   */
  def labels(user:String,repo:String):List[Label] = list[Label]("repos",user,repo,"labels")

  /**
   * @see [[http://developer.github.com/v3/issues/milestones]]
   */
  def milestones(user:String,repo:String):List[Milestone] = list[Milestone]("repos",user,repo,"milestones")

  private def refParamOpt[A:FromJValue](path:String*)(ref:String):A = {
    Option(ref).collect{ case r if ! r.isEmpty =>
      singleWithParams[A](path:_*)(Seq("ref"-> r))
    }.getOrElse{
      single[A](path:_*)
    }
  }

}

object GhScala extends GhScala(true)
