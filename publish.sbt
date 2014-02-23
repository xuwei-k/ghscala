import sbtrelease._
import ReleaseStateTransformations._
import com.typesafe.sbt.pgp.PgpKeys

releaseSettings

sonatypeSettings

val sonatypeURL =
"https://oss.sonatype.org/service/local/repositories/"

val updateReadme = { state: State =>
  val extracted = Project.extract(state)
  val scalaV = extracted get scalaBinaryVersion
  val v = extracted get version
  val org =  extracted get organization
  val n = extracted get name
  val snapshotOrRelease = if(extracted get isSnapshot) "snapshots" else "releases"
  val readme = "README.md"
  val readmeFile = file(readme)
  val newReadme = Predef.augmentString(IO.read(readmeFile)).lines.map{ line =>
    val matchReleaseOrSnapshot = line.contains("SNAPSHOT") == v.contains("SNAPSHOT")
    if(line.startsWith("libraryDependencies") && matchReleaseOrSnapshot){
      s"""libraryDependencies += "${org}" %% "${n}" % "$v""""
    }else if(line.contains(sonatypeURL) && matchReleaseOrSnapshot){
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

commands += Command.command("updateReadme")(updateReadme)

val updateReadmeProcess: ReleaseStep = updateReadme

def releaseStepCross[A](key: TaskKey[A]) = ReleaseStep(
  action = state => Project.extract(state).runTask(key, state)._1,
  enableCrossBuild = true
)

val sonatypeReleaseAllTask = taskKey[Unit]("sonatypeReleaseAllTask")

sonatypeReleaseAllTask := SonatypeKeys.sonatypeReleaseAll.toTask("").value

ReleaseKeys.releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  updateReadmeProcess,
  tagRelease,
  releaseStepCross(PgpKeys.publishSigned),
  setNextVersion,
  commitNextVersion,
  updateReadmeProcess,
  releaseStepCross(sonatypeReleaseAllTask),
  pushChanges
)

credentials ++= {(sys.env.get("SONATYPE_USER"), sys.env.get("SONATYPE_PASS")) match {
  case (Some(user), Some(pass)) =>
    Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", user, pass) :: Nil
  case _ =>
    Nil
}}
