name := "ghscala"

organization := "com.github.xuwei-k"

description := "scala github api client"

homepage := Some(url("https://github.com/xuwei-k/ghscala"))

scmInfo := Some(ScmInfo(
  url("https://github.com/xuwei-k/ghscala"),
  "scm:git:git@github.com/xuwei-k/ghscala.git"
))

licenses := Seq("MIT License" -> url("http://www.opensource.org/licenses/mit-license.php"))

scalacOptions := Seq("-deprecation", "-unchecked", "-Xlint", "-language:_")

resolvers += Opts.resolver.sonatypeReleases

scalaVersion := "2.10.3"

libraryDependencies ++= Seq(
  "io.argonaut" %% "argonaut" % "6.1-M2",
  "org.scalaz" %% "scalaz-concurrent" % "7.1.0-M3",
  "org.scalaj"  %% "scalaj-http" % "0.3.14",
  "joda-time" % "joda-time" % "2.3",
  "org.joda" % "joda-convert" % "1.2",
  "commons-codec" % "commons-codec" % "1.9"
)

def gitHash: Option[String] = scala.util.Try(
  sys.process.Process("git show -s --oneline").lines_!.head.split(" ").head
).toOption

scalacOptions in (Compile, doc) ++= {
  val tag = if(isSnapshot.value) gitHash.getOrElse("master") else { "v" + version.value }
  Seq(
    "-sourcepath", baseDirectory.value.getAbsolutePath,
    "-doc-source-url", s"https://github.com/xuwei-k/ghscala/tree/${tag}€{FILE_PATH}.scala"
  )
}

logBuffered in Test := false

pomExtra := (
<developers>
  <developer>
    <id>xuwei-k</id>
    <name>Kenji Yoshida</name>
    <url>https://github.com/xuwei-k</url>
  </developer>
</developers>
)

val showDoc = TaskKey[Unit]("showDoc")

showDoc in Compile <<= (doc in Compile, target in doc in Compile) map { (_, out) =>
  java.awt.Desktop.getDesktop.open(out / "index.html")
}
