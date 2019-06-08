import build._

enablePlugins(BuildInfoPlugin)

baseSettings

name := "ghscala"

description := "purely functional scala github api client"

libraryDependencies ++= Seq(
  "com.github.xuwei-k" %% "httpz" % httpzVersion,
  "com.github.xuwei-k" %% "httpz-scalaj" % httpzVersion % "test",
  "joda-time" % "joda-time" % "2.10.2",
  "org.joda" % "joda-convert" % "2.2.1",
  "commons-codec" % "commons-codec" % "1.12"
)
