package ghscala

final case class PublicKey(
  id: Long, key: String
) extends JsonToString[PublicKey]

object PublicKey {
  implicit val publicKeyCodecJson: CodecJson[PublicKey] =
    CodecJson.casecodec2(apply, unapply)(
      "id", "key"
    )
}
