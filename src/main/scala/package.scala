import java.text.SimpleDateFormat
import scalaz._
import argonaut.{DecodeJson, EncodeJson}

package object ghscala{

  type InterpreterF[F[_]] = RequestF ~> F

  type Requests[A] = Z.FreeC[RequestF, A]

  type ErrorNel = NonEmptyList[Error]

  type ActionE[E, A] = EitherT[Requests, E, A]
  type Action[A] = EitherT[Requests, Error, A]
  type ActionNel[A] = EitherT[Requests, ErrorNel, A]

  def Action[E, A](a: Requests[E \/ A]): ActionE[E, A] = EitherT(a)

  implicit def toActionEOps[E, A](a: ActionE[E, A]): ActionEOps[E, A] = new ActionEOps(a)

  type Config = Endo[ScalajReq]

  type DateTime = org.joda.time.DateTime

  private[ghscala] type ScalajReq = scalaj.http.Http.Request

  type CodecJson[A] = argonaut.CodecJson[A]
  val CodecJson = argonaut.CodecJson

  type Times[A] = Writer[List[Time], A]
  private[ghscala] def Times[A](result: A, time: Time): Times[A] =
    Writer(List(time), result)

  private[ghscala] implicit val timesMonad: Monad[Times] =
    scalaz.WriterT.writerMonad[List[Time]](scalaz.std.list.listMonoid)

  private[ghscala] val emptyConfig: Config = Endo.idEndo

  implicit val RequestsMonad: Monad[Requests] =
    Z.freeCMonad[RequestF]

  def actionEMonad[E]: Monad[({type λ[α] = ActionE[E, α]})#λ] =
    EitherT.eitherTMonad[Requests, E]

  implicit val ActionMonad: Monad[Action] =
    actionEMonad[Error]

  implicit val ActionNelMonad: Monad[ActionNel] =
    actionEMonad[ErrorNel]

  implicit val datetimeCodecJson: CodecJson[DateTime] =
    CodecJson.derived(
      EncodeJson.jencode1(_.toString()),
      DecodeJson.optionDecoder({
        _.string.map{ str =>
          new DateTime((new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")).parse(str))
        }
      },"DateTime")
    )

  def ActionZipAp[E: Semigroup]: Apply[({type λ[α] = ActionE[E, α]})#λ] =
    new Apply[({type λ[α] = ActionE[E, α]})#λ] {
      import Z._
      override def ap[A, B](fa: => ActionE[E, A])(f: => ActionE[E, A => B]) =
        f.zipWith(fa)(_ apply _)

      override def map[A, B](fa: ActionE[E, A])(f: A => B) =
        fa map f

      override def apply2[A, B, C](fa: => ActionE[E, A], fb: => ActionE[E, B])(f: (A, B) => C) =
        fa.zipWith(fb)(f)
    }

  val ActionNelZipAp: Apply[ActionNel] =
    ActionZipAp[ErrorNel]
}

