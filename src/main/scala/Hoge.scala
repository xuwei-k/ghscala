package ghscala

import argonaut.HCursor
import shapeless.PolyDefns.~>
import scala.collection.generic.IsTraversableLike

final case class Hoge(a: Int, b: String, c: Long)

object Hoge {
  import argonaut.DecodeJson
  import shapeless._
  import scalaz.std.function._

  private object constNone extends (Id ~> Option){
    override def apply[A](a: A): Option[A] = None
  }

  private object asDecodeResult extends Poly1 {
    implicit def as[A](implicit A: DecodeJson[A]) = at[(String, Option[A])] {
      case (key, _) => (c: HCursor) => (c --\ key).as[A]
    }
  }

  implicit val instance: DecodeJson[Hoge] = {
    val keys = "a" :: "b" :: "c" :: HNil
    val X = Generic[Hoge]
    val len = ops.hlist.HKernel(keys)
    val noneList = Fill.fill[len.Length, Null](null).asInstanceOf[X.Repr].map(constNone)
    val z = shapeless.contrib.scalaz.sequence(keys zip noneList map asDecodeResult)
    DecodeJson[Hoge]{ x =>
      shapeless.contrib.scalaz.sequence(z(x)).map(X.from)
    }
  }

}
