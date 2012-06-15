package com.github.xuwei_k.ghscala

import java.io._
import org.scala_tools.time.Imports._
import net.liftweb.json._

trait GhScala{
  val BASE = "https://api.github.com/"

  def getJson(url:String*):JValue =
    ScalajHttp(BASE + url.mkString("/")){ in =>
      JsonParser.parse(new BufferedReader(new InputStreamReader(in)))
    }

  def getFromArray[A](url:String*)(implicit j:FromJValue[A]):List[A] =
    for{
      JArray(list) <- getJson(url:_*)
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

  def repo(user:String,repo:String):Repo = reposJson pure getJson("repos",user,repo)

  def repos(user:String):List[Repo] = getFromArray[Repo]("users",user,"repos")

  def refs(user:String,repo:String):List[Ref] = getFromArray[Ref]("repos",user,repo,"git/refs")

  def followers(user:String):List[User] = getFromArray[User]("users",user,"followers")

  def searchRepo(query:String):List[SearchRepo] = getFromArray[SearchRepo]("legacy/repos/search",query)
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
  id           :Int,
  avatar_url   :String,
  gravatar_id  :String,
  url          :String
)

object GhScala extends GhScala

