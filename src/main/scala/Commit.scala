package ghscala

final case class Commit(
  tree      :Commit.Tree,
  message   :String,
  url       :String,
  author    :Commit.User,
  committer :Commit.User
)

final case class CommitResponse(
  stats     :Stats,
  commit    :Commit,
  url       :String,
  sha       :String,
  author    :User,
  committer :User,
  files     :List[File],
  parents   :List[Commit.Tree]
)

object CommitResponse {

  implicit val commitResponseDecodeJson: DecodeJson[CommitResponse] =
    DecodeJson.jdecode8L(CommitResponse.apply)(
      "stats", "commit", "url", "sha", "author",
      "committer", "files", "parents"
    )

}

final case class File(
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

object File {

  implicit val fileDecodeJson: DecodeJson[File] =
    DecodeJson.jdecode9L(File.apply)(
      "deletions", "changes", "additions", "status", "raw_url",
      "filename", "blob_url", "patch", "sha"
    )

}

object Commit{

  final case class Tree(
    sha :String,
    url :String
  )

  object Tree {
    implicit val treeDecodeJson: DecodeJson[Tree] =
      DecodeJson.jdecode2L(Tree.apply)("sha", "url")
  }

  final case class User(
    email :String,
    date  :String,
    name  :String
  )

  object User {
    implicit val userDecodeJson: DecodeJson[User] =
      DecodeJson.jdecode3L(User.apply)("email", "date", "name")
  }


  implicit val commitDecodeJson: DecodeJson[Commit] =
    DecodeJson.jdecode5L(Commit.apply)(
      "tree", "message", "url", "author", "committer"
    )

}

final case class Stats(
  total     :Long,
  deletions :Long,
  additions :Long
)

object Stats {

  implicit val statsDecodeJson: DecodeJson[Stats] =
    DecodeJson.jdecode3L(Stats.apply)(
      "total", "deletions", "additions"
    )

}

