import sbt._, Keys._
import sbtrelease._
import xerial.sbt.Sonatype._
import ReleaseStateTransformations._
import com.typesafe.sbt.pgp.PgpKeys
import sbtbuildinfo.Plugin._

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
    val modules = "ghscala" :: Nil
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

  val baseSettings = ReleasePlugin.releaseSettings ++ sonatypeSettings ++ buildInfoSettings ++ Seq(
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
        action = state => Project.extract(state).runTask(PgpKeys.publishSigned, state)._1,
        enableCrossBuild = true
      ),
      setNextVersion,
      commitNextVersion,
      ReleaseStep(state => Project.extract(state).runTask(SonatypeKeys.sonatypeReleaseAll, state)._1),
      updateReadmeProcess,
      pushChanges
    ),
    sourceGenerators in Compile <+= buildInfo,
    buildInfoKeys := Seq[BuildInfoKey](
      organization,
      name,
      version,
      scalaVersion,
      sbtVersion,
      scalacOptions,
      licenses
    ),
    buildInfoKeys ++= Seq[BuildInfoKey](
      "httpzVersion" -> httpzVersion
    ),
    buildInfoPackage := "ghscala",
    buildInfoObject := "BuildInfoGhScala",
    credentials ++= PartialFunction.condOpt(sys.env.get("SONATYPE_USER") -> sys.env.get("SONATYPE_PASS")){
      case (Some(user), Some(pass)) =>
        Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", user, pass)
    }.toList,
    organization := "com.github.xuwei-k",
    homepage := Some(url("https://github.com/xuwei-k/ghscala")),
    licenses := Seq("MIT License" -> url("http://www.opensource.org/licenses/mit-license.php")),
    scalacOptions ++= (
      "-deprecation" ::
      "-unchecked" ::
      "-Xlint" ::
      "-language:existentials" ::
      "-language:higherKinds" ::
      "-language:implicitConversions" ::
      Nil
    ),
    scalacOptions ++= {
      if(scalaVersion.value.startsWith("2.11"))
        Seq("-Ywarn-unused", "-Ywarn-unused-import")
      else
        Nil
    },
    scalaVersion := "2.10.4",
    crossScalaVersions := scalaVersion.value :: "2.11.2" :: Nil,
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
      <scm>
        <url>git@github.com:xuwei-k/ghscala.git</url>
        <connection>scm:git:git@github.com:xuwei-k/ghscala.git</connection>
        <tag>{if(isSnapshot.value) gitHash.getOrElse("master") else { "v" + version.value }}</tag>
      </scm>
    ),
    pomPostProcess := { node =>
      import scala.xml._
      import scala.xml.transform._
      def stripIf(f: Node => Boolean) = new RewriteRule {
        override def transform(n: Node) =
          if (f(n)) NodeSeq.Empty else n
      }
      val stripTestScope = stripIf { n => n.label == "dependency" && (n \ "scope").text == "test" }
      new RuleTransformer(stripTestScope).transform(node)(0)
    },
    showDoc in Compile <<= (doc in Compile, target in doc in Compile) map { (_, out) =>
      java.awt.Desktop.getDesktop.open(out / "index.html")
    }
  )

  private final val httpzVersion = "0.2.13"

  lazy val ghscala = Project("ghscala", file(".")).settings(
    baseSettings : _*
  ).settings(
    name := "ghscala",
    description := "purely functional scala github api client",
    libraryDependencies ++= Seq(
      "com.github.xuwei-k" %% "httpz" % httpzVersion,
      "com.github.xuwei-k" %% "httpz-scalaj" % httpzVersion % "test",
      "joda-time" % "joda-time" % "2.5",
      "org.joda" % "joda-convert" % "1.6",
      "commons-codec" % "commons-codec" % "1.6"
      // latest commons-codec is 1.9 but "httpclient" % "4.3.3" still depends on 1.6
    )
  )

}

