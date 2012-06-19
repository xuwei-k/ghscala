package com.github.xuwei_k.ghscala

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

