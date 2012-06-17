package com.github.xuwei_k.ghscala

case class Commit(
  tree      :Tree,
  message   :String,
  url       :String,
  author    :User2,
  committer :User2
)

case class CommitResponse(
  stats     :Stats,
  commit    :Commit,
  url       :String,
  sha       :String,
  author    :User,
  committer :User,
  files     :List[File],
  parents   :List[Parent]
)

case class User2(
  email :String,
  date  :String,
  name  :String
)

case class Parent(
  sha :String,
  url :String
)

case class File(
  deletions :Long,
  changes   :Long,
  additions :Long,
  status    :String,
  raw_url   :String,
  filename  :String,
  blob_url  :String,
  patch     :String,
  sha       :String
)

case class Tree(
  sha :String,
  url :String
)

case class Stats(
  total     :Long,
  deletions :Long,
  additions :Long
)

