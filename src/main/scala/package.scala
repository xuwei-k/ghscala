import argonaut.{EncodeJson, DecodeJson}
import httpz.Action
import java.text.SimpleDateFormat
import scalaz.~>

package object ghscala{

  type DateTime = org.joda.time.DateTime
  private[ghscala] type JsonToString[A <: httpz.JsonToString[A]] =
    httpz.JsonToString[A]

  type CodecJson[A] = argonaut.CodecJson[A]
  val CodecJson = argonaut.CodecJson

  implicit val datetimeCodecJson: CodecJson[DateTime] =
    CodecJson.derived(
      EncodeJson.jencode1(_.toString()),
      DecodeJson.optionDecoder({
        _.string.map{ str =>
          new DateTime((new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")).parse(str))
        }
      },"DateTime")
    )

  val interpreter: Command ~> Action = Interpreter
}

