package com.github.xuwei_k.ghscala

import java.io._
import org.scala_tools.time.Imports._
import net.liftweb.json._

trait GhScala{
  val BASE = "https://api.github.com/"

  def getJson(path:String):JValue =
    ScalajHttp(BASE + path){ in =>
      JsonParser.parse(new BufferedReader(new InputStreamReader(in)))
    }

  def repos(user:String):List[Repo] =
    for{
      JArray(list) <- getJson("users/" + user + "/repos")
      repo         <- list
    } yield Repo(repo)

  def repo(user:String,repo:String):Repo = Repo( getJson("repos/" + user + "/" + repo) )

  def refs(user:String,repo:String):List[Ref] =
    for{
      JArray(list) <- getJson("repos/" + user + "/" + repo + "/git/refs" )
      repo         <- list
    } yield Ref(repo)
}

object Ref{
  def apply(q:JValue):Ref = {
    implicit val formats = DefaultFormats
    q.extract[Ref]
  }
}

case class Ref(
  ref      :String,
  url      :String,
  `object` :Obj
){
  lazy val name:String = ref.split('/').last
  lazy val isTag:Boolean = ref.split('/')(1) == "tags"
  lazy val isBranch:Boolean = ! isTag
}

case class Obj(`type`:String,sha:String,url:String)

object Repo{
  def apply(q:JValue):Repo = {
    implicit val formats = DefaultFormats
    q.extract[Repo]
  }
}

case class Repo(
  has_downloads :Boolean,
  name          :String,
  has_issues    :Boolean,
  forks         :Int,
  `private`     :Boolean,
  size          :Int,
  open_issues   :Int,
  url           :String,
  description   :String,
  pushed_at     :DateTime,
  git_url       :String,
  has_wiki      :Boolean,
  fork          :Boolean,
  id            :Int,
  language      :String,
  homepage      :String,
  created_at    :DateTime,
  html_url      :String,
  updated_at    :DateTime,
  watchers      :Int,
  master_branch :Option[String],
  owner         :Owner
//  full_name     :String,
//  mirror_url    :String,
//  svn_url       :String,
//  clone_url     :String,
//  ssh_url       :String
){
  def master:String = master_branch.getOrElse("master")
}

case class Owner(
  login        :String,
  id           :Int,
  avatar_url   :String,
  gravatar_id  :String,
  url          :String
)

object GhScala extends GhScala

