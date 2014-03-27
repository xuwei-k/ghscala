import argonaut.{EncodeJson, DecodeJson}
import java.text.SimpleDateFormat

package object ghscala{

  type DateTime = org.joda.time.DateTime

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

}

