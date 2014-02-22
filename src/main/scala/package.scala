package object ghscala{
  type DateTime = org.joda.time.DateTime

  type DecodeJson[A] = argonaut.DecodeJson[A]
  val DecodeJson = argonaut.DecodeJson
}

