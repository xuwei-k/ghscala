package ghscala

final case class Email (
  email: String, primary: Boolean, verified: Boolean
) extends JsonToString[Email]

object Email {
  implicit val emailCodecJson: CodecJson[Email] =
    CodecJson.casecodec3(apply, unapply)(
      "email", "primary", "verified"
    )
}
