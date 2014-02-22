package ghscala

case class Commit(
  tree      :Commit.Tree,
  message   :String,
  url       :String,
  author    :Commit.User,
  committer :Commit.User
)

case class CommitResponse(
  stats     :Stats,
  commit    :Commit,
  url       :String,
  sha       :String,
  author    :User,
  committer :User,
  files     :List[File],
  parents   :List[Commit.Tree]
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

object Commit{

  case class Tree(
    sha :String,
    url :String
  )

  case class User(
    email :String,
    date  :String,
    name  :String
  )
}

case class Stats(
  total     :Long,
  deletions :Long,
  additions :Long
)

