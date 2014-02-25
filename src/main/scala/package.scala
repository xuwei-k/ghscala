import java.text.SimpleDateFormat
import scalaz._

package object ghscala{

  type InterpreterF[F[_]] = RequestF ~> F
  type Interpreter = InterpreterF[Id.Id]

  type ErrorNel = NonEmptyList[Error]

  type Action[A] = EitherT[Core.Requests, Error, A]

  def Action[A](a: Core.Requests[Error \/ A]): Action[A] = EitherT(a)

  type ActionK[A] = Kleisli[({type λ[α] = ErrorNel \/ α})#λ, Interpreter, A]

  val ActionKZipApply: Apply[ActionK] =
    new Apply[ActionK] {
      override def map[A, B](fa: ActionK[A])(f: A => B) =
        fa map f
      override def ap[A, B](fa: => ActionK[A])(f: => ActionK[A => B]) =
        apply2(f, fa)(_ apply _)
      override def apply2[A, B, C](a: => ActionK[A], b: => ActionK[B])(f: (A, B) => C) =
        a.zipWith(b)(f)
    }

  def ActionK[A](f: Interpreter => (ErrorNel \/ A)): ActionK[A] =
    Kleisli[({type λ[α] = ErrorNel \/ α})#λ, Interpreter, A](f)

  implicit def toActionOps[A](a: Action[A]): ActionOps[A] = new ActionOps(a)

  implicit def toActionKOps[A](a: ActionK[A]): ActionKOps[A] = new ActionKOps(a)

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

