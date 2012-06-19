package com.github.xuwei_k.ghscala

import org.specs2.Specification
import org.specs2.matcher.MatchResult

class Spec extends Specification{ def is =
  "repos" ! {
    forall(repos){case (user,_) =>
      forall(GhScala.repos(user)){ r =>
        println(r)
        (
          r must not beNull
        )and(
          forall(r.owner.productIterator){_ must not beNull}
        )
      }
    }
  } ^ "repo" ! {
    println(GhScala.repo(testUser,testRepo))
    success
  } ^ "refs" ! {
    check(GhScala.refs)
  } ^ "followers" ! {
    forall(repos){case (user,_) =>
      nullCheck(GhScala.followers(user))
    }
  } ^ "searchRepo" ! {
    nullCheck(GhScala.searchRepo(".g8"))
  } ^ "commits" ! {
    nullCheck(GhScala.commits(testUser,"ghscala",testSHA))
  } ^ "tree" ! {
    nullCheck(GhScala.trees(testUser,"ghscala",testSHA))
  } ^ "issue" ! {
    forall(repos){case (user,repo) =>
      forall(List(Closed,Open)){ state =>
        nullCheck(GhScala.issues(user,repo,state))
      }
    }
  } ^ "an issue events" ! {
    val issues = List(("lift","framework",1254),("unfiltered","unfiltered",29))
    forall(issues){case (user,repo,num) =>
      forall(GhScala.issueEvents(user,repo,num)){ event =>
        nullCheck(event)
      }
    }
  } ^ "repository issue events" ! {
    check(GhScala.issueEvents)
  } ^ "search issues" ! {
    forall(repos){case (user,r) =>
      forall(List(Open,Closed)){ state =>
        forall(GhScala.searchIssues(user,r,"scala",state)){ issue =>
          nullCheck(issue)
        }
      }
    }
  } ^ "downloads" ! {
    val (repo,user) = ("eed3si9n","scalaxb")
    val list = GhScala.downloads(repo,user)

    def checkSingleDownloads() =
      forall(util.Random.shuffle(list.map{_.id}).take(list.size / 4)){
        id => nullCheck(GhScala.download(repo,user,id))
      }

    forall(list){nullCheck} and checkSingleDownloads()
  } ^ "forks" ! {
    check(GhScala.forks)
  } ^ "watchers" ! {
    check(GhScala.watchers)
  } ^ "watched" ! {
    forall(GhScala.watched(testUser)){nullCheck}
  } ^ "collaborators" ! {
    check(GhScala.collaborators)
  } ^ "comments" ! {
    check(GhScala.comments)
  } ^ "comments for single commit" ! {
    forall(GhScala.comments("scala","scala","989c0d0693e27d06d1f70524b66527d1ef12f5a2")){nullCheck}
  } ^ end

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
        println(obj)
        obj must not beNull
    }
  }

  val repos = List(("etorreborre","specs2"),("dispatch","dispatch"),("scalaz","scalaz"),("unfiltered","unfiltered"))
  val testUser = "xuwei-k"
  val testRepo = "sbtend"
  val testSHA = "9cc84362e2487c4bb18e254445cf60a3fb7c5881"
}

