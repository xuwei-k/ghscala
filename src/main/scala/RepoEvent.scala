package ghscala

import argonaut.{Json, JsonObject, DecodeJson}

final case class RepoEvent(
  id            :String,
  `type`        :String,
  actor         :User,
  payload       :JsonObject, // TODO https://developer.github.com/v3/activity/events/types/
  public        :Boolean,
  created_at    :DateTime
) extends JsonToString[RepoEvent] {
  def action: Option[String] =
    payload("action").flatMap(_.string)
}

object RepoEvent {

  private[this] implicit val jsonObjectCodecJson: CodecJson[JsonObject] =
    CodecJson(
      Json.jObject,
      DecodeJson.optionDecoder(_.obj, "JsonObject").decode
    )

  implicit val repoEventCodecJson: CodecJson[RepoEvent] =
    CodecJson.casecodec6(apply, unapply)(
      "id", "type", "actor", "payload", "public", "created_at"
    )

}

