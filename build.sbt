resolvers += "xuwei-k" at "http://xuwei-k.github.com/mvn"

scalaVersion := "2.9.1"

libraryDependencies ++= {
val liftV = "2.4"
Seq(
   "net.liftweb" %% "lift-json-scalaz" % liftV
  ,"net.liftweb" %% "lift-json-ext" % liftV
  ,"org.scalaj"  %% "scalaj-http" % "0.3.1"
  ,"org.scala-tools.time" % "time_2.9.1" % "0.5"
  ,"org.specs2" %% "specs2" % "1.11" % "test"
)
}

initialCommands in console := {
  Iterator(
    "com.github.xuwei_k.ghscala"
  ).map{"import " + _ + "._"}.mkString(";")
}
