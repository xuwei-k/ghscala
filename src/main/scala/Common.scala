package com.github.xuwei_k.ghscala

import java.io._
import net.liftweb.json._

trait FromJValue[A]{
  def pure(j:JValue):A
}

trait Common{

  val isDebug:Boolean

  def debug[A](obj:A*){
    if(isDebug){
      obj.foreach(println)
    }
  }

  val DEFAULT_PER_PAGE = 100

  def all[A,B](f : Int => A => List[B] ) = { param: A =>
    val stream = Stream.from(1).map(i => i -> f(i)(param))
    val list = stream.takeWhile{case (index,l) =>
      val currentSize = l.size
      (currentSize > 0) && {
        if(index == 1){
          true
        }else{
          stream(index - 2)._2.size == DEFAULT_PER_PAGE
        }
      }
    }.map{_._2}.toList.flatten
    val result = list.distinct
    if(result.size != list.size){throw new Error("overlap !? " + list.mkString("\n") )}
    result
  }

  val BASE = "https://api.github.com/"

  type PARAM = (String,String)

  def getJson(url:String*):JValue = getJsonWithParams(url:_*)()

  def getJsonWithParams(url:String*)(params:PARAM*):JValue = {
    val u = BASE + url.mkString("/")
    val p = params.toList
    debug(u + p.map{case (k,v) => k + "=" + v}.reduceLeftOption(_ + "&" + _).map{"?"+}.getOrElse(""))
    ScalajHttp(u).params(p).process{ c =>
      val in = c.getInputStream()
//      debug(c.getHeaderFields())
      try{
        JsonParser.parse(new BufferedReader(new InputStreamReader(in)))
      }finally{
        in.close()
      }
    }
  }

  def withPageParams(url:String*)(params:PARAM*)(page:Int):JValue =
    getJsonWithParams(url:_*)({Seq("page" -> page.toString,"per_page" -> DEFAULT_PER_PAGE.toString) ++ params} :_*)

  def json2list[A](json:JValue)(implicit j:FromJValue[A]):List[A] = {
    val JArray(list) = json
    list.map(j.pure)
  }

  def json2list2[A:Manifest,B:Manifest](json:JValue):(List[A],List[B]) = {
    val JArray(list) = json
    val listA = list.map{ j => j -> j.extractOpt[A]}
    val listB = listA.collect{case (j,None) => j.extract[B]}
    listA.flatMap{_._2} -> listB
  }

  def listWithParams[A:FromJValue](url:String*)(params:PARAM*):List[A] =
    json2list[A](getJsonWithParams(url:_*)(params:_*))

  def usersAndOrgs[A](url:String*)(params:PARAM*)(page:Int):(List[User],List[Org]) =
    json2list2[User,Org](withPageParams(url:_*)(params:_*)(page))

  def listRequest[A:FromJValue](url:String*)(params:PARAM*)(page:Int):List[A] =
    json2list[A](withPageParams(url:_*)(params:_*)(page))

  def list[A:FromJValue](url:String*):List[A] = json2list[A](getJson(url:_*))

  def single[A](url:String*)(implicit j:FromJValue[A]):A = j pure getJson(url:_*)

  def singleWithParams[A](url:String*)(params:PARAM*)(implicit j:FromJValue[A]):A =
    j pure getJsonWithParams(url:_*)(params:_*)

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

  implicit val commentJson = new FromJValue[Comment]{
    def pure(j:JValue) = j.extract[Comment]
  }

  implicit val contentsJson = new FromJValue[Contents]{
    def pure(j:JValue) = j.extract[Contents]
  }

  implicit val pullJson = new FromJValue[Pull]{
    def pure(j:JValue) = j.extract[Pull]
  }

  implicit val orgJson = new FromJValue[Org]{
    def pure(j:JValue) = j.extract[Org]
  }

  implicit val organizationJson = new FromJValue[Organization]{
    def pure(j:JValue) = j.extract[Organization]
  }
}

object Common extends Common{val isDebug = true}
