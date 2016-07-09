package ghscala

import argonaut.DecodeJson
import httpz._
import scalaz.{Free, Inject, NonEmptyList}

sealed abstract class Command[A](val f: String => Request)(implicit val decoder: DecodeJson[A]){
  final def request: httpz.Request =
    requestWithURL(Github.baseURL)

  final def requestWithURL(baseURL: String): httpz.Request =
    f(baseURL)

  final def actionWithURL(baseURL: String): httpz.Action[A] =
    Core.json[A](requestWithURL(baseURL))(decoder)

  final def action: httpz.Action[A] =
    actionWithURL(Github.baseURL)

  final def lift[F[_]](implicit I: Inject[Command, F]): Free[F, A] =
    Free.liftF(I.inj(this))

  final def actionEOps: ActionEOps[httpz.Error, A] =
    new ActionEOps(action)

  final def nel: ActionE[NonEmptyList[httpz.Error], A] =
    actionEOps.nel
}

object Command {

  private[ghscala] def get(url: String, opt: Config = httpz.emptyConfig): String => Request = {
    baseURL => opt(Request(url = baseURL + url, params = Map("per_page" -> "100")))
  }

  case object Emojis extends Command[Map[String, String]](
    get("emojis")
  )

  final case class Tags(owner: String, repo: String) extends Command[List[ghscala.Tag]](
    get(s"repos/$owner/$repo/tags")
  )

  final case class Branches(owner: String, repo: String) extends Command[List[ghscala.Branch]](
    get(s"repos/$owner/$repo/branches")
  )

  final case class User(name: String) extends Command[ghscala.User](
    get(s"users/$name")
  )

  final case class Contributors(owner: String, repo: String) extends Command[List[ghscala.User]](
    get(s"repos/${owner}/${repo}/contributors")
  )

  final case class Followers(user: String) extends Command[List[ghscala.User]](
    get(s"users/${user}/followers")
  )

  final case class Following(user: String) extends Command[List[ghscala.User]](
    get(s"users/${user}/following")
  )

  final case class Blob(user: String, repo: String, sha: String) extends Command[ghscala.Blob](
    get(s"repos/${user}/${repo}/git/blobs/${sha}")
  )

  final case class Trees(user: String, repo: String, sha: String) extends Command[ghscala.Trees](
    get(s"repos/${user}/${repo}/git/trees/${sha}")
  )

  final case class Repos(user: String) extends Command[List[ghscala.Repo]](
    get(s"users/${user}/repos")
  )

  final case class OrgRepos(org: String) extends Command[List[ghscala.Repo]](
    get(s"orgs/${org}/repos")
  )

  final case class Repo(user: String, repo: String) extends Command[ghscala.Repo](
    get(s"repos/${user}/${repo}")
  )

  final case class Commits(user: String, repo: String, sha: String) extends Command[ghscala.CommitResponse](
    get(s"repos/${user}/${repo}/commits/${sha}")
  )

  final case class Issues(user: String, repo: String, state: State) extends Command[List[ghscala.Issue]](
    get(s"repos/${user}/${repo}/issues", Request.param("state", state.name))
  )

  final case class RepoEvents(owner: String, repo: String) extends Command[List[ghscala.RepoEvent]](
    get(s"repos/${owner}/${repo}/events")
  )

  final case class IssueEvents(user: String, repo: String) extends Command[List[ghscala.IssueEvent2]](
    get(s"repos/${user}/${repo}/issues/events")
  )

  final case class IssueEvent(user: String, repo: String, number: Long) extends Command[List[ghscala.IssueEvent]](
    get(s"repos/${user}/${repo}/issues/${number}/events")
  )

  final case class Comments(user: String, repo: String) extends Command[List[ghscala.Comment]](
    get(s"repos/${user}/${repo}/comments")
  )

  final case class Readme(user: String, repo: String, ref: Option[String]) extends Command[ghscala.Contents](
    get(s"repos/${user}/${repo}/readme", Request.paramOpt("ref", ref))
  )

  final case class Contents(user: String, repo: String, path: String, ref: Option[String]) extends Command[ghscala.Contents](
    get(s"repos/${user}/${repo}/contents/${path}", Request.paramOpt("ref", ref))
  )

  final case class Org(name: String) extends Command[ghscala.Organization](
    get(s"orgs/${name}")
  )

  final case class Orgs(user: String) extends Command[List[ghscala.Org]](
    get(s"users/${user}/orgs")
  )

  final case class Pulls(user: String, repo: String, state: Option[State], baseBranch: Option[String]) extends Command[List[ghscala.Pull]]({
    val p = Request.paramsOpt("state" -> state.map(_.name), "base" -> baseBranch)
    get(s"repos/${user}/${repo}/pulls", p)
  })

  final case class Gists(user: String) extends Command[List[ghscala.Gists]](
    get(s"users/${user}/gists")
  )

  final case class Gist(id: String) extends Command[ghscala.Gist](
    get(s"gists/${id}")
  )

  final case class Subscribers(owner: String, repo: String) extends Command[List[ghscala.User]](
    get(s"repos/${owner}/${repo}/subscribers")
  )

  final case class Keys(user: String) extends Command[List[ghscala.PublicKey]](
    get(s"users/${user}/keys")
  )

  case object Emails extends Command[List[ghscala.Email]](
    get("user/emails")
  )

  case object GitignoreTemplates extends Command[List[String]](
    get("gitignore/templates")
  )

  final case class Gitignore(language: String) extends Command[ghscala.Gitignore](
    get(s"gitignore/templates/${language}")
  )

  case object Public extends Command[List[ghscala.Gists]](
    get("gists/public")
  )

  final case class SearchRepositories(query: String, sort: SearchRepoSort) extends Command[ghscala.SearchRepo]({
    val p = Request.paramOpt("sort", sort.name) andThen Request.param("q", query)
    get("search/repositories", p)
  })

  final case class SearchCode(query: String) extends Command[ghscala.SearchCode](
    get("search/code", Request.param("q", query))
  )

  final case class SearchIssues(query: String) extends Command[ghscala.SearchIssues](
    get("search/issues", Request.param("q", query))
  )
}

object SelfCommand {
  import Command.get

  case object User extends Command[ghscala.User](
    get("user")
  )

  /*
  case object Follow extends Command[List[ghscala.User]](
    get("") // TODO
  )
  */

  case object Followers extends Command[List[ghscala.User]](
    get("user/following")
  )

  case object Following extends Command[List[ghscala.User]](
    get("user/followers")
  )

  case object Repos extends Command[List[ghscala.Repo]](
    get("user/repos")
  )

  case object Orgs extends Command[List[ghscala.Org]](
    get("user/orgs")
  )

  case object Gists extends Command[List[ghscala.Gists]](
    get("gists")
  )

  case object Starred extends Command[List[ghscala.Gists]](
    get("gists/starred")
  )
}
