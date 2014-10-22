package ghscala

import argonaut.DecodeJson
import httpz.{Config, Request, Core, Action}
import scalaz.~>

private[ghscala] object Interpreter extends (Command ~> Action){
  private[this] final val baseURL = "https://api.github.com/"

  def get[A: DecodeJson](url: String, opt: Config = httpz.emptyConfig): Action[A] =
    Core.json(opt(Request(url = baseURL + url, params = Map("per_page" -> "100"))))

  private[this] val emojis: Action[Map[String, String]] =
    get("emojis")

  override def apply[A](fa: Command[A]) = fa match {
    case Command.Emojis =>
      emojis

    case c: Command.Tags =>
      get[List[Tag]](s"repos/${c.owner}/${c.repo}/tags")

    case c: Command.Branches =>
      get[List[Branch]](s"repos/${c.owner}/${c.repo}/branches")

    case c: Command.User =>
      get[User](s"users/${c.name}")

    case c: Command.Contributors =>
      get[List[User]](s"repos/${c.owner}/${c.repo}/contributors")

    case c: Command.Followers =>
      get[List[User]](s"users/${c.user}/followers")

    case c: Command.Following =>
      get[List[User]](s"users/${c.user}/following")

    case c: Command.Blob =>
      get[Blob](s"repos/${c.user}/${c.repo}/git/blobs/${c.sha}")

    case c: Command.Trees =>
      get[Trees](s"repos/${c.user}/${c.repo}/git/trees/${c.sha}")

    case c: Command.Repos =>
      get[List[Repo]](s"users/${c.user}/repos")

    case c: Command.OrgRepos =>
      get[List[Repo]](s"orgs/${c.org}/repos")

    case c: Command.Repo =>
      get[Repo](s"repos/${c.user}/${c.repo}")

    case c: Command.Commits =>
      get[CommitResponse](s"repos/${c.user}/${c.repo}/commits/${c.sha}")

    case c: Command.Issues =>
      get[List[Issue]](s"repos/${c.user}/${c.repo}/issues", Request.param("state", c.state.name))

    case c: Command.RepoEvents =>
      get[List[RepoEvent]](s"repos/${c.owner}/${c.repo}/events")

    case c: Command.IssueEvents =>
      get[List[IssueEvent2]](s"repos/${c.user}/${c.repo}/issues/events")

    case c: Command.IssueEvent =>
      get[List[IssueEvent]](s"repos/${c.user}/${c.repo}/issues/${c.number}/events")

    case c: Command.Comments =>
      get[List[Comment]](s"repos/${c.user}/${c.repo}/comments")

    case c: Command.Readme =>
      get[Contents](s"repos/${c.user}/${c.repo}/readme", Request.paramOpt("ref", c.ref))

    case c: Command.Contents =>
      get[Contents](s"repos/${c.user}/${c.repo}/contents/${c.path}", Request.paramOpt("ref", c.ref))

    case c: Command.Org =>
      get[Organization](s"orgs/${c.name}")

    case c: Command.Orgs =>
      get[List[Org]](s"users/${c.user}/orgs")

    case c: Command.Pulls =>
      val p = Request.paramsOpt("state" -> c.state.map(_.name), "base" -> c.baseBranch)
      get[List[Pull]](s"repos/${c.user}/${c.repo}/pulls", p)

    case c: Command.Gists =>
      get[List[Gists]](s"users/${c.user}/gists")

    case c: Command.Gist =>
      get[Gist](s"gists/${c.id}")

    case c: Command.Subscribers =>
      get[List[User]](s"repos/${c.owner}/${c.repo}/subscribers")

    case c: Command.Keys =>
      get[List[PublicKey]](s"users/${c.user}/keys")

    case Command.Emails =>
      get[List[Email]]("user/emails")

    case Command.GitignoreTemplates =>
      get[List[String]]("gitignore/templates") // TODO val ?

    case c: Command.Gitignore =>
      get[Gitignore](s"gitignore/templates/${c.language}")

    case Command.Public =>
      get[List[Gists]]("gists/public")

    case c: Command.SearchRepositories =>
      val p = Request.paramOpt("sort", c.sort.name) andThen Request.param("q", c.query)
      get[SearchRepo]("search/repositories", p)

    case c: Command.SearchCode =>
      get[SearchCode]("search/code", Request.param("q", c.query))

    case c: Command.SearchIssues =>
      get[SearchIssues]("search/issues", Request.param("q", c.query))

    case SelfCommand.User =>
      get[User]("user")

    case SelfCommand.Follow =>
      get[List[User]]("")

    case SelfCommand.Following =>
      get[List[User]]("user/following")

    case SelfCommand.Followers =>
      get[List[User]](s"user/followers")

    case SelfCommand.Repos =>
      get[List[Repo]]("user/repos")

    case SelfCommand.Orgs =>
      get[List[Org]]("user/orgs")

    case SelfCommand.Gists =>
      get[List[Gists]]("gists")

    case SelfCommand.Starred =>
      get[List[Gists]]("gists/starred")
  }
}
