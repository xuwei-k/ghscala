import sbt._, Keys._
import sbtrelease._
import sbtrelease.ReleasePlugin.autoImport._
import ReleaseStateTransformations._
import xerial.sbt.Sonatype._
import com.typesafe.sbt.pgp.PgpKeys
import sbtbuildinfo.BuildInfoPlugin
import sbtbuildinfo.BuildInfoPlugin.autoImport._

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
        val sxrIndexHtml = "-sxr.jar/!/index.html"
        val javadocIndexHtml = "-javadoc.jar/!/index.html"
        val baseURL = s"${sonatypeURL}${snapshotOrRelease}/archive/${org.replace('.', '/')}/${n}_${scalaV}/${v}/${n}_${scalaV}-${v}"
        if(line.contains(javadocIndexHtml)){
          s"- [API Documentation](${baseURL}${javadocIndexHtml})"
        }else if (line.contains(sxrIndexHtml)){
          s"- [sxr](${baseURL}${sxrIndexHtml})"
        }else line
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

  private[this] final val Scala210 = "2.10.6"

  private[this] val unusedWarnings = (
    "-Ywarn-unused" ::
    "-Ywarn-unused-import" ::
    Nil
  )

  val baseSettings = sonatypeSettings ++ Seq(
    commands += Command.command("updateReadme")(updateReadme),
    releaseProcess := Seq[ReleaseStep](
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
    ) ++ PartialFunction.condOpt(CrossVersion.partialVersion(scalaVersion.value)){
      case Some((2, v)) if v >= 11 => unusedWarnings
    }.toList.flatten,
    scalaVersion := Scala210,
    crossScalaVersions := Scala210 :: "2.11.7" :: Nil,
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
  ) ++ Sxr.settings(Compile, "classes.sxr") ++ Seq(Compile, Test).flatMap(c =>
    scalacOptions in (c, console) ~= {_.filterNot(unusedWarnings.toSet)}
  )

  private final val httpzVersion = "0.3.0"

  lazy val ghscala = Project("ghscala", file(".")).enablePlugins(BuildInfoPlugin).settings(
    baseSettings : _*
  ).settings(
    name := "ghscala",
    description := "purely functional scala github api client",
    libraryDependencies ++= Seq(
      "com.github.xuwei-k" %% "httpz" % httpzVersion,
      "com.github.xuwei-k" %% "httpz-scalaj" % httpzVersion % "test",
      "joda-time" % "joda-time" % "2.8.2",
      "org.joda" % "joda-convert" % "1.7",
      "commons-codec" % "commons-codec" % "1.10"
    )
  )

}

