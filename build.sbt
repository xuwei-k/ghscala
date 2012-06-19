resolvers += "xuwei-k" at "http://xuwei-k.github.com/mvn"

name := "ghscala"

version := "0.1-SNAPSHOT"

organization := "com.github.xuwei-k"

description := "scala github api client"

homepage := Some(url("https://github.com/xuwei-k/ghscala"))

licenses := Seq("MIT License" -> url("http://www.opensource.org/licenses/mit-license.php"))

scalacOptions := Seq("-deprecation", "-unchecked")

externalResolvers ~= { _.filterNot{_.name.contains("Scala-Tools")} }

scalaVersion:= "2.9.2"

libraryDependencies ++= {
val liftV = "2.4"
Seq(
   "net.liftweb" %% "lift-json-scalaz" % liftV
  ,"net.liftweb" %% "lift-json-ext" % liftV
  ,"org.scalaj"  %% "scalaj-http" % "0.3.1"
  ,"org.scala-tools.time" % "time_2.9.1" % "0.5"
  ,"org.specs2" %% "specs2" % "1.11" % "test"
  ,"commons-codec" % "commons-codec" % "1.6"
  ,"net.databinder" % "pamflet-knockoff_2.9.1" % "0.4.0"
)
}

initialCommands in console := {
  Iterator(
    "com.github.xuwei_k.ghscala",
    "net.liftweb.json"
  ).map{"import " + _ + "._"}.mkString(";")
}
