import sbt._, Keys._
import sbtrelease._
import xerial.sbt.Sonatype._
import ReleaseStateTransformations._
import com.typesafe.sbt.pgp.PgpKeys

object build extends Build {

  def gitHash: Option[String] = scala.util.Try(
    sys.process.Process("git rev-parse HEAD").lines_!.head
  ).toOption

  val showDoc = TaskKey[Unit]("showDoc")

  val sonatypeURL = "https://oss.sonatype.org/service/local/repositories/"

  val updateReadme = { state: State =>
    val extracted = Project.extract(state)
    val scalaV = extracted get scalaBinaryVersion
    val v = extracted get version
    val org =  extracted get organization
    val modules = "ghscala-core" :: ("scalaj" :: "apache" :: "dispatch" :: Nil).map("httpz-" + _)
    val snapshotOrRelease = if(extracted get isSnapshot) "snapshots" else "releases"
    val readme = "README.md"
    val readmeFile = file(readme)
    val newReadme = Predef.augmentString(IO.read(readmeFile)).lines.map{ line =>
      val matchReleaseOrSnapshot = line.contains("SNAPSHOT") == v.contains("SNAPSHOT")
      if(line.startsWith("libraryDependencies") && matchReleaseOrSnapshot){
        val i = modules.indexWhere(line.contains)
        s"""libraryDependencies += "${org}" %% "${modules(i)}" % "$v""""
      }else if(line.contains(sonatypeURL) && matchReleaseOrSnapshot){
        val n = "ghscala"
        s"- [API Documentation](${sonatypeURL}${snapshotOrRelease}/archive/${org.replace('.','/')}/${n}_${scalaV}/${v}/${n}_${scalaV}-${v}-javadoc.jar/!/index.html)"
      }else line
    }.mkString("", "\n", "\n")
    IO.write(readmeFile, newReadme)
    val git = new Git(extracted get baseDirectory)
    git.add(readme) ! state.log
    git.commit("update " + readme) ! state.log
    "git diff HEAD^" ! state.log
    state
  }

  val updateReadmeProcess: ReleaseStep = updateReadme

  val baseSettings = ReleasePlugin.releaseSettings ++ sonatypeSettings ++ Seq(
    commands += Command.command("updateReadme")(updateReadme),
    ReleasePlugin.ReleaseKeys.releaseProcess := Seq[ReleaseStep](
      checkSnapshotDependencies,
      inquireVersions,
      runClean,
      runTest,
      setReleaseVersion,
      commitReleaseVersion,
      updateReadmeProcess,
      tagRelease,
      ReleaseStep(
        action = { state =>
          val extracted = Project extract state
          extracted.runAggregated(PgpKeys.publishSigned in Global in extracted.get(thisProjectRef), state)
        },
        enableCrossBuild = true
      ),
      setNextVersion,
      commitNextVersion,
      ReleaseStep{ state =>
        val extracted = Project extract state
        extracted.runAggregated(SonatypeKeys.sonatypeReleaseAll in Global in extracted.get(thisProjectRef), state)
      },
      updateReadmeProcess,
      pushChanges
    ),
    credentials ++= PartialFunction.condOpt(sys.env.get("SONATYPE_USER") -> sys.env.get("SONATYPE_PASS")){
      case (Some(user), Some(pass)) =>
        Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", user, pass)
    }.toList,
    organization := "com.github.xuwei-k",
    homepage := Some(url("https://github.com/xuwei-k/ghscala")),
    scmInfo := Some(ScmInfo(
      url("https://github.com/xuwei-k/ghscala"),
      "scm:git:git@github.com/xuwei-k/ghscala.git"
    )),
    licenses := Seq("MIT License" -> url("http://www.opensource.org/licenses/mit-license.php")),
    scalacOptions := Seq("-deprecation", "-unchecked", "-Xlint", "-language:_"),
    scalaVersion := "2.10.4",
    scalacOptions in (Compile, doc) ++= {
      val tag = if(isSnapshot.value) gitHash.getOrElse("master") else { "v" + version.value }
      Seq(
        "-sourcepath", baseDirectory.value.getAbsolutePath,
        "-doc-source-url", s"https://github.com/xuwei-k/ghscala/tree/${tag}€{FILE_PATH}.scala"
      )
    },
    logBuffered in Test := false,
    pomExtra := (
      <developers>
        <developer>
          <id>xuwei-k</id>
          <name>Kenji Yoshida</name>
          <url>https://github.com/xuwei-k</url>
        </developer>
      </developers>
    ),
    showDoc in Compile <<= (doc in Compile, target in doc in Compile) map { (_, out) =>
      java.awt.Desktop.getDesktop.open(out / "index.html")
    }
  )


  val descriptionHttpz = description := "purely functional http client"

  lazy val httpz = Project("httpz", file("httpz")).settings(
    baseSettings : _*
  ).settings(
    name := "httpz",
    descriptionHttpz,
    libraryDependencies ++= Seq(
      "org.scalaz" %% "scalaz-concurrent" % "7.1.0-M3",
      "io.argonaut" %% "argonaut" % "6.1-M2"
    )
  )

  lazy val core = Project("core", file("core")).settings(
    baseSettings : _*
  ).settings(
    name := "ghscala-core",
    description := "purely functional scala github api client",
    libraryDependencies ++= Seq(
      "joda-time" % "joda-time" % "2.3",
      "org.joda" % "joda-convert" % "1.2",
      "commons-codec" % "commons-codec" % "1.6"
      // latest commons-codec is 1.9 but "httpclient" % "4.3.3" still depends on 1.6
    )
  ).dependsOn(httpz)

  lazy val scalaj = Project("scalaj", file("scalaj")).settings(
    baseSettings : _*
  ).settings(
    name := "httpz-scalaj",
    descriptionHttpz,
    libraryDependencies ++= Seq(
      "org.scalaj"  %% "scalaj-http" % "0.3.14"
    )
  ).dependsOn(httpz, core % "test->test")

  lazy val dispatch = Project("dispatch", file("dispatch")).settings(
    baseSettings : _*
  ).settings(
    name := "httpz-dispatch",
    descriptionHttpz,
    libraryDependencies ++= Seq(
      "net.databinder" %% "dispatch-http" % "0.8.10"
    )
  ).dependsOn(httpz)

  lazy val apache = Project("apache", file("apache")).settings(
    baseSettings : _*
  ).settings(
    name := "httpz-apache",
    descriptionHttpz,
    libraryDependencies ++= Seq(
      "org.apache.httpcomponents" % "httpclient" % "4.3.3"
    )
  ).dependsOn(httpz)


  lazy val root = {
    import sbtunidoc.Plugin._
    val customPackageKeys = Seq(packageDoc in Compile)

    Project("root", file(".")).settings(
      baseSettings ++ unidocSettings ++ Seq(
        name := "ghscala",
        sources := (sources in UnidocKeys.unidoc in ScalaUnidoc).value,
        resolvers += Classpaths.typesafeReleases,
        packageDoc in Compile := {
          val dir = (UnidocKeys.unidoc in Compile).value
          val files = (dir ** (-DirectoryFilter)).get.map{f =>
            f -> IO.relativize((crossTarget in UnidocKeys.unidoc).value / "unidoc", f).get
          }
          val out = crossTarget.value / s"ghscala-${scalaBinaryVersion.value}-${version.value}-javadoc.jar"
          IO.zip(files, out)
          out
        },
        artifacts <<= sbt.Classpaths.artifactDefs(customPackageKeys),
        packagedArtifacts <<= sbt.Classpaths.packaged(customPackageKeys)
      ): _*
    ).aggregate(httpz, core, scalaj, dispatch, apache)
  }


}

