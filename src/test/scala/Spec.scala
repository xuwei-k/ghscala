package com.github.xuwei_k.ghscala

import org.specs2.Specification
import org.specs2.matcher.MatchResult

class Spec extends Specification{ def is =
//  sequential ^
  "repos" ! {
    p("repos"){
    forall(repos){case (user,_) =>
      forall(GhScala.repos(user)){ r =>
        //println(r)
        (
          r must not beNull
        )and(
          forall(r.owner.productIterator){_ must not beNull}
        )
      }
    }
    }
  } ^ "repo" ! {
    p("repo"){
    GhScala.repo(testUser,testRepo)
    success
    }
  } ^ "refs" ! {
    p("refs"){
    check(Function.untupled(GhScala.refs(_)))
    }
  } ^ "followers" ! {
    p("followers"){
    forall(repos){case (user,_) =>
      nullCheck(GhScala.followers(user))
    }
    }
  } ^ "searchRepo" ! {
    p("searchRepo"){
    nullCheck(GhScala.searchRepo(".g8"))
    }
  } ^ "commits" ! {
    p("commits"){
    nullCheck(GhScala.commits(testUser,"ghscala",testSHA))
    }
  } ^ "tree" ! {
    p("tree"){
    nullCheck(GhScala.trees(testUser,"ghscala",testSHA))
    }
  } ^ "issue" ! {
    p("issue"){
    forallWithState(GhScala.issues)
    }
  } ^ "an issue events" ! {
    p("an issue events"){
    val issues = List(("lift","framework",1254),("unfiltered","unfiltered",29))
    forall(issues){case (user,repo,num) =>
      forall(GhScala.issueEvents(user,repo,num)){ event =>
        nullCheck(event)
      }
    }
    }
  } ^ "repository issue events" ! {
    p("repository issue events"){
    check(GhScala.issueEvents)
    }
  } ^ "search issues" ! {
    p("search issues"){
    forallWithState(GhScala.searchIssues(_,_,"scala",_))
    }
  } ^ "downloads" ! {
    p("downloads"){
    val (repo,user) = ("eed3si9n","scalaxb")
    val list = GhScala.downloads(repo,user)

    def checkSingleDownloads() =
      forall(util.Random.shuffle(list.map{_.id}).take(4)){
        id => nullCheck(GhScala.download(repo,user,id))
      }

    forall(list){nullCheck} and checkSingleDownloads()
    }
  } ^ "forks" ! {
    p("forks"){
    check(Function.untupled(GhScala.forks(_)))
    }
  } ^ "watchers" ! {
    p("watchers"){
      forall(repos){case (user,repo) =>
        val (users,orgs) = GhScala.watchers(user,repo)
        forall(users){nullCheck} and forall(orgs){nullCheck}
      }
    }
  } ^ "watched" ! {
    p("watched"){
    forall(GhScala.watched(testUser)){nullCheck}
    }
  } ^ "collaborators" ! {
    p("collaborators"){
    check(Function.untupled(GhScala.collaborators(_)))
    }
  } ^ "comments" ! {
//    check(GhScala.comments)
    success
  } ^ "comments for single commit" ! {
    p("comments for single commits"){
    forall(GhScala.comments("scala","scala","989c0d0693e27d06d1f70524b66527d1ef12f5a2")){nullCheck}
    }
  } ^ "readme" ! {
    p("readme"){
    forall(repos){case (user,repo) =>
      nullCheck(GhScala.readme(user,repo))
    }
    }
  } ^ "readme ref param" ! {
    p("readme ref param"){
    val `0.11.3` = GhScala.readme("harrah","xsbt","v0.11.3")
    val `0.11.2` = GhScala.readme("harrah","xsbt","v0.11.2")

    nullCheck(`0.11.2`) and nullCheck(`0.11.3`) and { `0.11.2` !== `0.11.3` }
    }
  } ^ "pulls" ! {
    p("pulls"){
    forallWithState(GhScala.pulls)
    }
  } ^ "orgs" ! {
    p("orgs"){
    forall(repos.map(_._1)){ user =>
      nullCheck(GhScala.orgs(user))
    }
    }
  } ^ "org" ! {
    p("org"){
    forall(testOrgs){ o =>
      nullCheck(GhScala.org(o))
    }
    }
  } ^ "blob" ! {
    p("blob"){
      nullCheck(GhScala.blob("xuwei-k","sbtend","f4ff7a5afa754e5d2c3ffd60900e2e0d2e744590"))
    }
  } ^ end

  def forallWithState[A](f: (String,String,State) => List[A]) = {
    forall(repos){case (user,repo) =>
      val open   = f(user,repo,Open)
      val closed = f(user,repo,Closed)
      nullCheck(open) and nullCheck(closed) and {forall(open){closed must not contain(_)}}
    }
  }

  def check[A](func:(String,String) => List[A]) =
    forall(repos){case (user,repo) =>
      forall(func(user,repo)){nullCheck}
    }

  def nullCheck(obj:Any):MatchResult[Any] = {
    obj match{
      case coll:collection.GenTraversableOnce[_] =>
        (coll must not beNull) and forall(coll.toBuffer){nullCheck}
      case p:Product =>
        (p must not beNull) and forall(p.productIterator.toBuffer){nullCheck}
      case _ =>
        //println(obj)
        obj must not beNull
    }
  }

  val testOrgs = List("scalajp","unfiltered","sbt","lift","dispatch")
  val repos = List(("etorreborre","specs2"),("dispatch","dispatch"),("scalaz","scalaz"),("unfiltered","unfiltered"))
  val testUser = "xuwei-k"
  val testRepo = "sbtend"
  val testSHA = "9cc84362e2487c4bb18e254445cf60a3fb7c5881"

  def p[A](label:String)(a : => A):A = {
    val start = GhScala.rateLimit
    println("start  " + label + " " + start)
    val r = try{
      a
    }catch{
      case e => e.printStackTrace
      throw e
    }
    val end = GhScala.rateLimit
    println("finish " + label + " " + end + " " + start.flatMap{a => end.map{a - _}})
    r
  }
}

