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
    forall(repos){case (user,repo) =>
      nullCheck(GhScala.refs(user,repo))
    }
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
    forall(repos){case (user,repo) =>
      forall(GhScala.issueEvents(user,repo)){ event =>
        nullCheck(event)
      }
    }
  } ^ "search issues" ! {
    forall(repos){case (user,r) =>
      forall(List(Open,Closed)){ state =>
        forall(GhScala.searchIssues(user,r,"scala",state)){ issue =>
          nullCheck(issue)
        }
      }
    }
  } ^ end

  def nullCheck[A](obj:Any):MatchResult[Any] = {
    obj match{
      case p:Product =>
        (obj must not beNull) and forall(p.productIterator.toBuffer){nullCheck}
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

