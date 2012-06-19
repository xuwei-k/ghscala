package com.github.xuwei_k.ghscala

case class Contents(
  sha      :String,
  name     :String,
  path     :String,
  content  :String,
  _links   :Links,
  `type`   :String,
  encoding :String,
  size     :Long
)


case class Links(
  self :String,
  git  :String,
  html :String
)

