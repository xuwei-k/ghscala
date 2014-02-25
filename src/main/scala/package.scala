import java.text.SimpleDateFormat
import scalaz._
import argonaut.{DecodeJson, EncodeJson}

package object ghscala{

  type InterpreterF[F[_]] = RequestF ~> F
  type Interpreter = InterpreterF[Id.Id]

  type Requests[A] = Z.FreeC[RequestF, A]

  type ErrorNel = NonEmptyList[Error]

  type ActionE[E, A] = EitherT[Requests, E, A]
  type Action[A] = EitherT[Requests, Error, A]
  type ActionNel[A] = EitherT[Requests, ErrorNel, A]

  def Action[E, A](a: Requests[E \/ A]): ActionE[E, A] = EitherT(a)

  implicit def toActionEOps[E, A](a: ActionE[E, A]): ActionEOps[E, A] = new ActionEOps(a)

  type Config = Endo[scalaj.http.Http.Request]

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

