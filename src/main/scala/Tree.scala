package com.github.xuwei_k.ghscala

case class Tree(
  url    :String,
  sha    :String,
  `type` :String,
  mode   :String,
  size   :Option[Long],
  path   :String
){
  lazy val getType:TreeType = `type` match {
    case "blob" => Blob
    case "tree" => Directory
    case _      => Unknown(`type`)
  }
}

sealed abstract class TreeType
case object Blob                 extends TreeType
case object Directory            extends TreeType
case class  Unknown(name:String) extends TreeType

case class TreeResponse(
  sha  :String,
  tree :List[Tree],
  url  :String
)

