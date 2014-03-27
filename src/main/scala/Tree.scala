package ghscala

object Tree {

  implicit val treeCodecJson: CodecJson[Tree] =
    CodecJson.casecodec6(apply, unapply)(
      "url", "sha", "type", "mode", "size", "path"
    )

}

final case class Tree(
  url    :String,
  sha    :String,
  `type` :String,
  mode   :String,
  size   :Option[Long],
  path   :String
){
  lazy val getType:TreeType = `type` match {
    case "blob" => TreeType.Blob
    case "tree" => TreeType.Directory
    case _      => TreeType.Unknown(`type`)
  }
}

sealed abstract trait TreeType extends Any
object TreeType{
  case object Blob                 extends TreeType
  case object Directory            extends TreeType
  case class  Unknown(name:String) extends AnyVal with TreeType
}

case class TreeResponse(
  sha  :String,
  tree :List[Tree],
  url  :String
)

