package com.github.xuwei_k.ghscala

case class IssueSearch(
  pull_request_url :Option[String],
  state            :String,
  user             :String,
  updated_at       :String,
  labels           :List[String],
  position         :Double,
  closed_at        :DateTime,
  title            :String,
  gravatar_id      :String,
  votes            :Long,
  number           :Long,
  html_url         :String,
  diff_url         :Option[String],
  comments         :Long,
  patch_url        :Option[String],
  created_at       :DateTime,
  body             :String
)

