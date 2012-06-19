package com.github.xuwei_k.ghscala

import java.io._
import net.liftweb.json._

trait FromJValue[A]{
  def pure(j:JValue):A
}

trait Common{

  val BASE = "https://api.github.com/"

  type PARAM = (String,String)

  def getJson(url:String*):JValue = getJsonWithParams(url:_*)()

  def getJsonWithParams(url:String*)(params:PARAM*):JValue =
    ScalajHttp(BASE + url.mkString("/")).params(params.toList){ in =>
      JsonParser.parse(new BufferedReader(new InputStreamReader(in)))
    }

  def json2list[A](json:JValue)(implicit j:FromJValue[A]):List[A] = {
    val JArray(list) = json
    list.map(j.pure)
  }

  def listWithParams[A](url:String*)(params:PARAM*)(implicit j:FromJValue[A]):List[A] =
    json2list[A](getJsonWithParams(url:_*)(params:_*))

  def list[A](url:String*)(implicit j:FromJValue[A]):List[A] = json2list[A](getJson(url:_*))

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
}

object Common extends Common

