package com.github.xuwei_k.ghscala

import org.apache.commons.codec.binary.Base64
import pamflet.PamfletDiscounter._

case class Contents(
  sha      :String,
  name     :String,
  path     :String,
  content  :String,
  _links   :Links,
  `type`   :String,
  encoding :String,
  size     :Long
){

  lazy val decoded:String =
    new String(Base64.decodeBase64(content))

  lazy val html = toXHTML(knockoff(decoded))
}


case class Links(
  self :String,
  git  :String,
  html :String
)

