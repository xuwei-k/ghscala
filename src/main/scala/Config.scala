package ghscala

final case class BasicAuth(user: String, pass: String)

final case class Config(auth: BasicAuth)

