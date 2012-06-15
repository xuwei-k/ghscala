package com.github.xuwei_k.ghscala

import org.specs2.Specification

class Spec extends Specification{ def is =
  "repos" ! {
    println(GhScala.repos("xuwei-k"))
    success
  } ^ "repo" ! {
    println(GhScala.repo("xuwei-k","sbtend"))
    success
  } ^ end
}


