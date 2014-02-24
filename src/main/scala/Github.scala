package ghscala

import scalaz.Endo

object Github {
  import Core._

  /** [[http://developer.github.com/v3/git/blobs]] */
  def blob(user: String, repo: String, sha: String): Action[Blob] =
    get(s"repos/$user/$repo/git/blobs/$sha")

  /** [[http://developer.github.com/v3/git/trees]] */
  def trees(user: String, repo: String, sha: String): Action[Trees] =
    get(s"repos/$user/$repo/git/trees/$sha")

  /** [[http://developer.github.com/v3/repos]] */
  def repo(user: String, repo: String): Action[Repo] =
    get(s"repos/$user/$repo")

  /** [[http://developer.github.com/v3/git/commits]] */
  def commits(user: String, repo: String, sha: String): Action[CommitResponse] =
    get(s"repos/$user/$repo/commits/$sha")

  /** [[http://developer.github.com/v3/issues]] */
  def issues(user: String, repo: String, state: State = Open): Action[List[Issue]] =
    get(
      s"repos/$user/$repo/issues",
      ScalajHttp.param("state", state.name)
    )

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
    get(s"repos/$user/$repo/readme", ScalajHttp.param("ref", ref))

  /** [[http://developer.github.com/v3/repos/contents]] */
  def readme(user: String, repo: String): Action[Contents] =
    get(s"repos/$user/$repo/readme")

  /** [[http://developer.github.com/v3/repos/contents]] */
  def contents(user: String, repo: String, path: String): Action[Contents] =
    get(s"repos/$user/$repo/contents/$path")

  /** [[http://developer.github.com/v3/repos/contents]] */
  def contents(user: String, repo: String, path: String, ref: String): Action[Contents] =
    contents(user, repo, path).mapRequest(ScalajHttp.param("ref", ref))

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
  def pulls(user: String, repo: String, state: State = Open): Action[List[Pull]] =
    get(s"repos/$user/$repo/pulls")
}

