package ghscala

import argonaut.EncodeJson

abstract class JsonToString[A <: JsonToString[A]: EncodeJson] { self: A =>

  override def toString =
    implicitly[EncodeJson[A]].apply(self).pretty(argonaut.PrettyParams.spaces2)

}

