package ghscala

sealed abstract class Command[A]

object Command {
  case object Emojis extends Command[Map[String, String]]

  final case class Tags(owner: String, repo: String) extends Command[List[ghscala.Tag]]

  final case class Branches(owner: String, repo: String) extends Command[List[ghscala.Branch]]

  final case class User(name: String) extends Command[ghscala.User]

  final case class Contributors(owner: String, repo: String) extends Command[List[ghscala.User]]

  final case class Followers(user: String) extends Command[List[ghscala.User]]

  final case class Following(user: String) extends Command[List[ghscala.User]]

  final case class Blob(user: String, repo: String, sha: String) extends Command[ghscala.Blob]

  final case class Trees(user: String, repo: String, sha: String) extends Command[ghscala.Trees]

  final case class Repos(user: String) extends Command[List[ghscala.Repo]]

  final case class OrgRepos(org: String) extends Command[List[ghscala.Repo]]

  final case class Repo(user: String, repo: String) extends Command[ghscala.Repo]

  final case class Commits(user: String, repo: String, sha: String) extends Command[ghscala.CommitResponse]

  final case class Issues(user: String, repo: String, state: State) extends Command[List[ghscala.Issue]]

  final case class RepoEvents(owner: String, repo: String) extends Command[List[ghscala.RepoEvent]]

  final case class IssueEvents(user: String, repo: String) extends Command[List[ghscala.IssueEvent2]]

  final case class IssueEvent(user: String, repo: String, number: Long) extends Command[List[ghscala.IssueEvent]]

  final case class Comments(user: String, repo: String) extends Command[List[ghscala.Comment]]

  final case class Readme(user: String, repo: String, ref: Option[String]) extends Command[ghscala.Contents]

  final case class Contents(user: String, repo: String, path: String, ref: Option[String]) extends Command[ghscala.Contents]

  final case class Org(name: String) extends Command[ghscala.Organization]

  final case class Orgs(user: String) extends Command[List[ghscala.Org]]

  final case class Pulls(user: String, repo: String, state: Option[State], baseBranch: Option[String]) extends Command[List[ghscala.Pull]]

  final case class Gists(user: String) extends Command[List[ghscala.Gists]]

  final case class Gist(id: String) extends Command[ghscala.Gist]

  final case class Subscribers(owner: String, repo: String) extends Command[List[ghscala.User]]

  final case class Keys(user: String) extends Command[List[ghscala.PublicKey]]

  case object Emails extends Command[List[ghscala.Email]]

  case object GitignoreTemplates extends Command[List[String]]

  final case class Gitignore(language: String) extends Command[ghscala.Gitignore]

  case object Public extends Command[List[ghscala.Gists]]

  final case class SearchRepositories(query: String, sort: SearchRepoSort) extends Command[ghscala.SearchRepo]

  final case class SearchCode(query: String) extends Command[ghscala.SearchCode]

  final case class SearchIssues(query: String) extends Command[ghscala.SearchIssues]
}

object SelfCommand {

  case object User extends Command[ghscala.User]

  case object Follow extends Command[List[ghscala.User]]

  case object Followers extends Command[List[ghscala.User]]

  case object Following extends Command[List[ghscala.User]]

  case object Repos extends Command[List[ghscala.Repo]]

  case object Orgs extends Command[List[ghscala.Org]]

  case object Gists extends Command[List[ghscala.Gists]]

  case object Starred extends Command[List[ghscala.Gists]]
}
