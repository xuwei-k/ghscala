package shapeless

trait Fill[N, A] extends DepFn1[A] { type Out <: HList }

object Fill {

  def fill[B <: Nat, A](elem: A)(implicit fill: Fill[B, A]) : fill.Out = fill(elem)

  def apply[N, A](implicit fill: Fill[N, A]) = fill

  type Aux[N, A, Out0] = Fill[N, A] {type Out = Out0}

  implicit def fill1Zero[A]: Aux[Nat._0, A, HNil] =
    new Fill[Nat._0, A] {
      type Out = HNil

      def apply(elem: A) = HNil
    }

  implicit def fill1Succ[N <: Nat, A]
    (implicit prev: Fill[N, A]): Aux[Succ[N], A, A :: prev.Out] =
      new Fill[Succ[N], A] {
        type Out = A :: prev.Out

        def apply(elem: A) = elem :: prev(elem)
      }
}

