resolvers += "xuwei-k" at "http://xuwei-k.github.com/mvn"

watchSources ++= { file("project") ** "*.scala" get }

sourceGenerators in Compile <+= (sourceManaged in Compile).map{Generater.task}

name := "ghscala"

version := "0.1.1-SNAPSHOT"

organization := "com.github.xuwei-k"

description := "scala github api client"

homepage := Some(url("https://github.com/xuwei-k/ghscala"))

licenses := Seq("MIT License" -> url("http://www.opensource.org/licenses/mit-license.php"))

scalacOptions := Seq("-deprecation", "-unchecked")

resolvers += Opts.resolver.sonatypeReleases

scalaVersion:= "2.9.2"

libraryDependencies ++= {
val liftV = "2.4"
Seq(
   "net.liftweb" %% "lift-json-scalaz" % liftV
  ,"net.liftweb" %% "lift-json-ext" % liftV
  ,"org.scalaj"  %% "scalaj-http" % "0.3.6"
  ,"org.specs2" %% "specs2" % "1.12.3" % "test"
  ,"net.databinder" % "pamflet-knockoff_2.9.1" % "0.4.0"
)
}

initialCommands in console := {
  Iterator(
    "com.github.xuwei_k.ghscala",
    "net.liftweb.json"
  ).map{"import " + _ + "._"}.mkString(";")
}

publishTo := sys.env.get("MAVEN_DIRECTORY").map{ dir =>
  Resolver.file("gh-pages",file(dir))(Patterns(true, Resolver.mavenStyleBasePattern))
}
