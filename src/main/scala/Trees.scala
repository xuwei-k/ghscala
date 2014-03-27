package ghscala

object Trees {

  implicit val treesCodecJson: CodecJson[Trees] =
    CodecJson.casecodec3(apply, unapply)(
      "sha", "url", "tree"
    )

}

final case class Trees(
  sha: String, url: String, tree: List[Tree]
) extends JsonToString[Trees]

