package com.github.xuwei_k.ghscala

import org.specs2.Specification

class Spec extends Specification{ def is =
  "repos" ! {
    forall(GhScala.repos(testUser)){
      _ must not beNull
    }
    success
  } ^ "repo" ! {
    println(GhScala.repo(testUser,testRepo))
    success
  } ^ "refs" ! {
    forall(GhScala.refs(testUser,testRepo)){
      _ must not beNull
    }
  } ^ "followers" ! {
    forall(GhScala.followers(testUser)){
      _.productIterator.forall(null !=) must beTrue
    }
  } ^ end

  val testUser = "xuwei-k"
  val testRepo = "sbtend"
}


