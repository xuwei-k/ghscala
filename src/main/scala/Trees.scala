package ghscala

import argonaut._

object Trees {

  implicit val treesDecodeJson: DecodeJson[Trees] =
    DecodeJson.jdecode3L(Trees.apply)("sha", "url", "tree")

}

final case class Trees(sha: String, url: String, tree: List[Tree])

