watchSources ++= { file("project") ** "*.scala" get }

sourceGenerators in Compile <+= (sourceManaged in Compile).map{Generater.task}

name := "ghscala"

version := "0.1.3-SNAPSHOT"

organization := "com.github.xuwei-k"

description := "scala github api client"

homepage := Some(url("https://github.com/xuwei-k/ghscala"))

licenses := Seq("MIT License" -> url("http://www.opensource.org/licenses/mit-license.php"))

scalacOptions := Seq("-deprecation", "-unchecked")

resolvers += Opts.resolver.sonatypeReleases

crossScalaVersions := Seq("2.9.2","2.10.0")

libraryDependencies ++= Seq("scalaz","ext","native").map{ m =>
  "org.json4s" %% ( "json4s-" + m ) % "3.1.0"
}

libraryDependencies <+= scalaVersion{ v =>
  if(v.startsWith("2.9"))
    "org.specs2" %% "specs2" % "1.12.3" % "test"
  else
    "org.specs2" %% "specs2" % "1.13" % "test"
}

libraryDependencies ++= Seq(
   "org.scalaj"  %% "scalaj-http" % "0.3.6"
  ,"commons-codec" % "commons-codec" % "1.7"
  //,"net.databinder" % "pamflet-knockoff_2.9.1" % "0.4.0"
)

initialCommands in console := {
  Iterator(
    "com.github.xuwei_k.ghscala",
    "net.liftweb.json"
  ).map{"import " + _ + "._"}.mkString(";")
}

publishTo := sys.env.get("MAVEN_DIRECTORY").map{ dir =>
  Resolver.file("gh-pages",file(dir))(Patterns(true, Resolver.mavenStyleBasePattern))
}
