package com.github.xuwei_k.ghscala

import java.io._
import org.scala_tools.time.Imports._
import net.liftweb.json._

trait GhScala{
  val BASE = "https://api.github.com/"

  type USER_ID = String

  def repos(user:USER_ID):List[Repo] = {
    val json = ScalajHttp(BASE + "users/" + user + "/repos"){ in =>
      JsonParser.parse(new BufferedReader(new InputStreamReader(in)))
    }

    for{
      JArray(list) <- json
      repo         <- list
    } yield Repo(repo)
  }
}

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
  owner         :Owner
//  full_name     :String,
//  mirror_url    :String,
//  svn_url       :String,
//  clone_url     :String,
//  ssh_url       :String
)

case class Owner(
  login        :String,
  id           :Int,
  avatar_url   :String,
  gravatar_id  :String,
  url          :String
)

object GhScala extends GhScala

