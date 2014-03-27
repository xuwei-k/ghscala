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
) extends JsonToString[CommitResponse]

object CommitResponse {

  implicit val commitResponseCodecJson: CodecJson[CommitResponse] =
    CodecJson.casecodec8(apply, unapply)(
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

  implicit val fileCodecJson: CodecJson[File] =
    CodecJson.casecodec9(apply, unapply)(
      "deletions", "changes", "additions", "status", "raw_url",
      "filename", "blob_url", "patch", "sha"
    )

}

object Commit{

  final case class Tree(
    sha :String,
    url :String
  ) extends JsonToString[Tree]

  object Tree {
    implicit val treeCodecJson: CodecJson[Tree] =
      CodecJson.casecodec2(apply, unapply)("sha", "url")
  }

  final case class User(
    email :String,
    date  :String,
    name  :String
  ) extends JsonToString[User]

  object User {
    implicit val userCodecJson: CodecJson[User] =
      CodecJson.casecodec3(apply, unapply)("email", "date", "name")
  }


  implicit val commitCodecJson: CodecJson[Commit] =
    CodecJson.casecodec5(apply, unapply)(
      "tree", "message", "url", "author", "committer"
    )

}

final case class Stats(
  total     :Long,
  deletions :Long,
  additions :Long
) extends JsonToString[Stats]

object Stats {

  implicit val statsCodecJson: CodecJson[Stats] =
    CodecJson.casecodec3(apply, unapply)(
      "total", "deletions", "additions"
    )

}

