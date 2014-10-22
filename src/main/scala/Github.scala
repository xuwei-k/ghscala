package ghscala

import httpz._
import scalaz.Free.FreeC
import scalaz.{Inject, Free}

object GhScala extends Github[Command, ({type l[a] = FreeC[Command, a]})#l] {
  override protected[this] def f[A](c: Command[A]) = lift(c)
}

object Github extends Github[Command, Action]{
  implicit def instance[F[_]](implicit I: Inject[Command, F]): Github[F, ({type l[a] = FreeC[F, a]})#l] =
    new Github[F, ({type l[a] = FreeC[F, a]})#l] {
      def f[A](c: Command[A]) = lift(c)
    }

  def commands2Action[A](a: FreeC[Command, A]): Action[A] =
    Free.runFC[Command, Action, A](a)(Interpreter)(httpz.ActionMonad)

  protected[this] override def f[A](c: Command[A]) =
    commands2Action(lift(c))

  private[ghscala] final val baseURL = "https://api.github.com/"
}

sealed abstract class Github[F[_], G[_]](implicit I: Inject[Command, F]) {

  final type FreeCF[A] = FreeC[F, A]

  final def lift[A](f: Command[A]): FreeCF[A] =
    Free.liftFC(I.inj(f))

  protected[this] def f[A](c: Command[A]): G[A]

  /** [[https://developer.github.com/v3/emojis/]] */
  final val emojis: G[Map[String, String]] =
    f(Command.Emojis)

  /** [[http://developer.github.com/v3/repos/#list-tags]] */
  def tags(owner: String, repo: String): G[List[Tag]] =
    f(Command.Tags(owner, repo))

  /** [[http://developer.github.com/v3/repos/#list-branches]] */
  def branches(owner: String, repo: String): G[List[Branch]] =
    f(Command.Branches(owner, repo))

  /** [[http://developer.github.com/v3/users/]] */
  final val user: G[User] =
    f(SelfCommand.User)

  /** [[http://developer.github.com/v3/users/]] */
  def user(user: String): G[User] =
    f(Command.User(user))

  /** [[http://developer.github.com/v3/repos/#list-contributors]] */
  def contributors(owner: String, repo: String): G[List[User]] =
    f(Command.Contributors(owner, repo))

  /** [[http://developer.github.com/v3/users/followers/#list-followers-of-a-user]] */
  final val followers: G[List[User]] =
    f(SelfCommand.Followers)

  /** [[http://developer.github.com/v3/users/followers/#list-followers-of-a-user]] */
  def followers(user: String): G[List[User]] =
    f(Command.Followers(user))

  /** [[http://developer.github.com/v3/users/followers/#list-users-followed-by-another-user]] */
  final val following: G[List[User]] =
    f(SelfCommand.Following)

  /** [[http://developer.github.com/v3/users/followers/#list-users-followed-by-another-user]] */
  def following(user: String): G[List[User]] =
    f(Command.Following(user))

  /** [[http://developer.github.com/v3/git/blobs]] */
  def blob(user: String, repo: String, sha: String): G[Blob] =
    f(Command.Blob(user, repo, sha))

  /** [[http://developer.github.com/v3/git/trees]] */
  def trees(user: String, repo: String, sha: String): G[Trees] =
    f(Command.Trees(user, repo, sha))

  /** [[http://developer.github.com/v3/repos/#list-your-repositories]] */
  final val repos: G[List[Repo]] =
    f(SelfCommand.Repos)

  /** [[http://developer.github.com/v3/repos/#list-user-repositories]] */
  def repos(user: String): G[List[Repo]] =
    f(Command.Repos(user))

  /** [[http://developer.github.com/v3/repos/#list-organization-repositories]] */
  def orgRepos(org: String): G[List[Repo]] =
    f(Command.OrgRepos(org))

  /** [[http://developer.github.com/v3/repos/#list-user-repositories]] */
  def repo(user: String, repo: String): G[Repo] =
    f(Command.Repo(user, repo))

  /** [[http://developer.github.com/v3/git/commits]] */
  def commits(user: String, repo: String, sha: String): G[CommitResponse] =
    f(Command.Commits(user, repo, sha))

  /** [[http://developer.github.com/v3/issues]] */
  def issues(user: String, repo: String, state: State = Open): G[List[Issue]] =
    f(Command.Issues(user, repo, state))

  /** [[https://developer.github.com/v3/activity/events/#list-repository-events]] */
  def repoEvents(owner: String, repo: String): G[List[RepoEvent]] =
    f(Command.RepoEvents(owner, repo))

  /** [[http://developer.github.com/v3/issues/events/]] */
  def issueEvents(user: String, repo: String, number: Long): G[List[IssueEvent]] =
    f(Command.IssueEvent(user, repo, number))

  /** [[http://developer.github.com/v3/issues/events/]] */
  def issueEvents(user: String, repo: String): G[List[IssueEvent2]] =
    f(Command.IssueEvents(user, repo))

  /** [[http://developer.github.com/v3/repos/comments]] */
  def comments(user: String, repo: String): G[List[Comment]] =
    f(Command.Comments(user, repo))

  /** [[http://developer.github.com/v3/repos/contents]] */
  def readme(user: String, repo: String, ref: String): G[Contents] =
    f(Command.Readme(user, repo, Option(ref)))

  /** [[http://developer.github.com/v3/repos/contents]] */
  def readme(user: String, repo: String): G[Contents] =
    f(Command.Readme(user, repo, None))

  /** [[http://developer.github.com/v3/repos/contents]] */
  def contents(user: String, repo: String, path: String): G[Contents] =
    f(Command.Contents(user, repo, path, None))

  /** [[http://developer.github.com/v3/repos/contents]] */
  def contents(user: String, repo: String, path: String, ref: String): G[Contents] =
    f(Command.Contents(user, repo, path, Option(ref)))

  /** [[http://developer.github.com/v3/orgs]] */
  def org(orgName: String): G[Organization] =
    f(Command.Org(orgName))

  /** [[http://developer.github.com/v3/orgs]] */
  def orgs(user: String): G[List[Org]] =
    f(Command.Orgs(user))

  /** [[http://developer.github.com/v3/orgs]] */
  final val orgs: G[List[Org]] =
    f(SelfCommand.Orgs)

  /** [[http://developer.github.com/v3/pulls]] */
  def pulls(user: String, repo: String): G[List[Pull]] =
    f(Command.Pulls(user, repo, None, None))

  /** [[http://developer.github.com/v3/pulls]] */
  def pulls(user: String, repo: String, state: State = Open, baseBranch: String = null): G[List[Pull]] =
    f(Command.Pulls(user, repo, Option(state), Option(baseBranch)))

  /** [[http://developer.github.com/v3/gists/#list-gists]] */
  def gists(user: String): G[List[Gists]] =
    f(Command.Gists(user))

  /** [[http://developer.github.com/v3/gists/#get-a-single-gist]] */
  def gist(id: String): G[Gist] =
    f(Command.Gist(id))

  def markdown(text: String): ActionE[Throwable, String] = {
    import argonaut.Json
    Core.string(Request(
      url = Github.baseURL + "markdown",
      method = "POST",
      body = Some(
        Json.obj("text" -> Json.jString(text)).toString.getBytes
      )
    ))
  }

  /** [[https://developer.github.com/v3/activity/watching/#list-watchers]] */
  def subscribers(owner: String, repo: String): G[List[User]] =
    f(Command.Subscribers(owner, repo))

  /** [[https://developer.github.com/v3/users/keys/#list-public-keys-for-a-user]] */
  def keys(user: String): G[List[PublicKey]] =
    f(Command.Keys(user))

  /** [[https://developer.github.com/v3/users/emails/#list-email-addresses-for-a-user]] */
  final val emails: G[List[Email]] =
    f(Command.Emails)

  object gitignore {
    final val templates: G[List[String]] =
      f(Command.GitignoreTemplates)

    def apply(language: String): G[Gitignore] =
      f(Command.Gitignore(language))
  }

  object gists {
    /** [[http://developer.github.com/v3/gists/#list-gists]] */
    final val me: G[List[Gists]] =
      f(SelfCommand.Gists)

    /** [[http://developer.github.com/v3/gists/#list-gists]] */
    final val public: G[List[Gists]] =
      f(Command.Public)

    /** [[http://developer.github.com/v3/gists/#list-gists]] */
    final val starred: G[List[Gists]] =
      f(SelfCommand.Starred)
  }

  object search {
    /** [[http://developer.github.com/v3/search/#search-repositories]] */
    def repositories(query: String, sort: SearchRepoSort = SearchRepoSort.Default): G[SearchRepo] =
      f(Command.SearchRepositories(query, sort))

    /** [[http://developer.github.com/v3/search/#search-code]] */
    def code(query: String): G[SearchCode] =
      f(Command.SearchCode(query))

    // TODO sort, order
    /** [[http://developer.github.com/v3/search/#search-issues]] */
    def issues(query: String): G[SearchIssues] =
      f(Command.SearchIssues(query))
  }
}

