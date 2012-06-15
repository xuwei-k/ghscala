package com.github.xuwei_k.ghscala

import org.specs2.Specification

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
    forall(GhScala.refs(testUser,testRepo)){ r =>
      println(r)
      r must not beNull
    }
  } ^ "followers" ! {
    forall(GhScala.followers(testUser)){ u =>
      println(u)
      u.productIterator.forall(null !=) must beTrue
    }
  } ^ "searchRepo" ! {
    forall(GhScala.searchRepo(".g8")){ r =>
      println(r)
      r.productIterator.forall(null !=) must beTrue
    }
  } ^ end

  val testUser = "xuwei-k"
  val testRepo = "sbtend"
}


