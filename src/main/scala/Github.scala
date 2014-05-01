package ghscala

import argonaut.DecodeJson
import httpz._

object Github {

  private[this] val baseURL = "https://api.github.com/"

  def get[A: DecodeJson](url: String, opt: Config = emptyConfig): Action[A] =
    Core.json(opt(Request(url = baseURL + url, params = Map("per_page" -> "100"))))

  def post[A: DecodeJson](url: String, opt: Config = emptyConfig): Action[A] =
    Core.json(opt(Request(url = baseURL + url, method = "POST")))

  /** [[https://developer.github.com/v3/emojis/]] */
  val emojis: Action[Map[String, String]] =
    get(s"emojis")

  /** [[http://developer.github.com/v3/repos/#list-tags]] */
  def tags(owner: String, repo: String): Action[List[Tag]] =
    get(s"repos/$owner/$repo/tags")

  /** [[http://developer.github.com/v3/repos/#list-branches]] */
  def branches(owner: String, repo: String): Action[List[Branch]] =
    get(s"repos/$owner/$repo/branches")

  // TODO create another special case class ?
  /** [[http://developer.github.com/v3/users/]] */
  val user: Action[User] =
    get("user")

  // TODO create another special case class ?
  /** [[http://developer.github.com/v3/users/]] */
  def user(user: String): Action[User] =
    get(s"users/$user")

  /** [[http://developer.github.com/v3/repos/#list-contributors]] */
  def contributors(owner: String, repo: String): Action[List[User]] =
    get(s"repos/$owner/$repo/contributors")

  /** [[http://developer.github.com/v3/users/followers/#list-followers-of-a-user]] */
  val followers: Action[List[User]] =
    get(s"user/followers")

  /** [[http://developer.github.com/v3/users/followers/#list-followers-of-a-user]] */
  def followers(user: String): Action[List[User]] =
    get(s"users/$user/followers")

  /** [[http://developer.github.com/v3/users/followers/#list-users-followed-by-another-user]] */
  val following: Action[List[User]] =
    get(s"user/following")

  /** [[http://developer.github.com/v3/users/followers/#list-users-followed-by-another-user]] */
  def following(user: String): Action[List[User]] =
    get(s"users/$user/following")

  /** [[http://developer.github.com/v3/git/blobs]] */
  def blob(user: String, repo: String, sha: String): Action[Blob] =
    get(s"repos/$user/$repo/git/blobs/$sha")

  /** [[http://developer.github.com/v3/git/trees]] */
  def trees(user: String, repo: String, sha: String): Action[Trees] =
    get(s"repos/$user/$repo/git/trees/$sha")

  // TODO parameter
  /** [[http://developer.github.com/v3/repos/#list-your-repositories]] */
  val repos: Action[List[Repo]] =
    get(s"user/repos")

  // TODO parameter
  /** [[http://developer.github.com/v3/repos/#list-user-repositories]] */
  def repos(user: String): Action[List[Repo]] =
    get(s"users/$user/repos")

  // TODO parameter
  /** [[http://developer.github.com/v3/repos/#list-organization-repositories]] */
  def orgRepos(org: String): Action[List[Repo]] =
    get(s"orgs/$org/repos")

  /** [[http://developer.github.com/v3/repos/#list-user-repositories]] */
  def repo(user: String, repo: String): Action[Repo] =
    get(s"repos/$user/$repo")

  /** [[http://developer.github.com/v3/git/commits]] */
  def commits(user: String, repo: String, sha: String): Action[CommitResponse] =
    get(s"repos/$user/$repo/commits/$sha")

  /** [[http://developer.github.com/v3/issues]] */
  def issues(user: String, repo: String, state: State = Open): Action[List[Issue]] =
    get(
      s"repos/$user/$repo/issues",
      Request.param("state", state.name)
    )

  /** [[https://developer.github.com/v3/activity/events/#list-repository-events]] */
  def repoEvents(owner: String, repo: String): Action[List[RepoEvent]] =
    get(s"repos/$owner/$repo/events")

  /** [[http://developer.github.com/v3/issues/events/]] */
  def issueEvents(user: String, repo: String, number: Long): Action[List[IssueEvent]] =
    get(s"repos/$user/$repo/issues/$number/events")

  /** [[http://developer.github.com/v3/issues/events/]] */
  def issueEvents(user: String, repo: String): Action[List[IssueEvent2]] =
    get(s"repos/$user/$repo/issues/events")

  /** [[http://developer.github.com/v3/repos/comments]] */
  def comments(user: String, repo: String): Action[List[Comment]] =
    get(s"repos/$user/$repo/comments")

  /** [[http://developer.github.com/v3/repos/contents]] */
  def readme(user: String, repo: String, ref: String): Action[Contents] =
    get(s"repos/$user/$repo/readme", Request.param("ref", ref))

  /** [[http://developer.github.com/v3/repos/contents]] */
  def readme(user: String, repo: String): Action[Contents] =
    get(s"repos/$user/$repo/readme")

  /** [[http://developer.github.com/v3/repos/contents]] */
  def contents(user: String, repo: String, path: String): Action[Contents] =
    get(s"repos/$user/$repo/contents/$path")

  /** [[http://developer.github.com/v3/repos/contents]] */
  def contents(user: String, repo: String, path: String, ref: String): Action[Contents] =
    contents(user, repo, path).mapRequest(Request.param("ref", ref))

  /** [[http://developer.github.com/v3/orgs]] */
  def org(orgName: String): Action[Organization] =
    get(s"orgs/$orgName")

  /** [[http://developer.github.com/v3/orgs]] */
  def orgs(user: String): Action[List[Org]] =
    get(s"users/$user/orgs")

  /** [[http://developer.github.com/v3/orgs]] */
  def orgs: Action[List[Org]] =
    get(s"user/orgs")

  /** [[http://developer.github.com/v3/pulls]] */
  def pulls(user: String, repo: String): Action[List[Pull]] =
    get(s"repos/$user/$repo/pulls")

  /** [[http://developer.github.com/v3/pulls]] */
  def pulls(user: String, repo: String, state: State = Open, baseBranch: String = null): Action[List[Pull]] = {
    val r = pulls(user, repo).mapRequest(Request.params("state" -> state.name))
    Option(baseBranch).fold(r)(branch => r.mapRequest(Request.params("base" -> branch)))
  }

  /** [[http://developer.github.com/v3/gists/#list-gists]] */
  def gists(user: String): Action[List[Gists]] =
    get(s"users/$user/gists")

  /** [[http://developer.github.com/v3/gists/#get-a-single-gist]] */
  def gist(id: String): Action[Gist] =
    get(s"gists/$id")

  def markdown(text: String): ActionE[Throwable, String] = {
    import argonaut.Json
    Core.string(Request(
      url = baseURL + "markdown",
      method = "POST",
      body = Some(
        Json.obj("text" -> Json.jString(text)).toString.getBytes
      )
    ))
  }

  /** [[https://developer.github.com/v3/activity/watching/#list-watchers]] */
  def subscribers(owner: String, repo: String): Action[List[User]] =
    get(s"repos/$owner/$repo/subscribers")

  object gitignore {
    val templates: Action[List[String]] =
      get("gitignore/templates")

    def apply(language: String): Action[Gitignore] =
      get(s"gitignore/templates/$language")
  }

  object gists {
    /** [[http://developer.github.com/v3/gists/#list-gists]] */
    val me: Action[List[Gists]] =
      get("gists")

    /** [[http://developer.github.com/v3/gists/#list-gists]] */
    val public: Action[List[Gists]] =
      get("gists/public")

    /** [[http://developer.github.com/v3/gists/#list-gists]] */
    val starred: Action[List[Gists]] =
      get("gists/starred")
  }

  object search {
    /** [[http://developer.github.com/v3/search/#search-repositories]] */
    def repositories(query: String, sort: SearchRepoSort = SearchRepoSort.Default): Action[SearchRepo] = {
      val r = get[SearchRepo]("search/repositories", Request.param("q", query))
      sort.name.fold(r)(n => r.mapRequest(Request.param("sort", n)))
    }

    /** [[http://developer.github.com/v3/search/#search-code]] */
    def code(query: String): Action[SearchCode] =
      get("search/code", Request.param("q", query))

    // TODO sort, order
    /** [[http://developer.github.com/v3/search/#search-issues]] */
    def issues(query: String): Action[SearchIssues] =
      get("search/issues", Request.param("q", query))
  }
}

