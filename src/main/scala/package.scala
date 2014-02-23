import java.text.SimpleDateFormat
import scalaz.{EitherT, Endo, \/}

package object ghscala{

  type Action[A] = EitherT[Github.Requests, Error, A]

  def Action[A](a: Github.Requests[Error \/ A]): Action[A] = EitherT(a)

  implicit def toActionOps[A](a: Action[A]) = new ActionOps(a)

  type Config = Endo[scalaj.http.Http.Request]

  type DateTime = org.joda.time.DateTime

  type DecodeJson[A] = argonaut.DecodeJson[A]
  val DecodeJson = argonaut.DecodeJson

  implicit val datetimeDecodeJson: DecodeJson[DateTime] =
    DecodeJson.optionDecoder({
      _.string.map{ str =>
        new DateTime((new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")).parse(str))
      }
    },"DateTime")
}

