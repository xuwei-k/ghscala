name := "ghscala"

version := "0.2.0-SNAPSHOT"

organization := "com.github.xuwei-k"

description := "scala github api client"

homepage := Some(url("https://github.com/xuwei-k/ghscala"))

licenses := Seq("MIT License" -> url("http://www.opensource.org/licenses/mit-license.php"))

scalacOptions := Seq("-deprecation", "-unchecked", "-Xlint", "-language:_")

resolvers += Opts.resolver.sonatypeReleases

scalaVersion := "2.10.3"

libraryDependencies ++= Seq(
  "io.argonaut" %% "argonaut" % "6.1-M2",
  "org.scalaj"  %% "scalaj-http" % "0.3.14",
  "com.github.nscala-time" %% "nscala-time" % "0.8.0",
  "commons-codec" % "commons-codec" % "1.9"
)

