package com.github.xuwei_k.ghscala

import org.specs2.Specification
import org.specs2.matcher.MatchResult

class Spec extends Specification{ def is =
  "repos" ! {
    forall(GhScala.repos(testUser)){ r =>
      println(r)
      (
        r must not beNull
      )and(
        forall(r.owner.productIterator){_ must not beNull}
      )
    }
  } ^ "repo" ! {
    println(GhScala.repo(testUser,testRepo))
    success
  } ^ "refs" ! {
    nullCheck(GhScala.refs(testUser,testRepo))
  } ^ "followers" ! {
    nullCheck(GhScala.followers(testUser))
  } ^ "searchRepo" ! {
    nullCheck(GhScala.searchRepo(".g8"))
  } ^ "commits" ! {
    nullCheck(GhScala.commits(testUser,"ghscala",testSHA))
  } ^ "tree" ! {
    nullCheck(GhScala.trees(testUser,"ghscala",testSHA))
  } ^ end

  def nullCheck[A](obj:Any):MatchResult[Any] = {
    obj match{
      case p:Product =>
        (obj must not beNull) and forall(p.productIterator){nullCheck}
      case _ =>
        println(obj)
        obj must not beNull
    }
  }

  val testUser = "xuwei-k"
  val testRepo = "sbtend"
  val testSHA = "9cc84362e2487c4bb18e254445cf60a3fb7c5881"
}


