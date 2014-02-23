package ghscala

import scalaz.Endo

object API {
  import Github._

  /** [[http://developer.github.com/v3/git/trees]] */
  def trees(user: String, repo: String, sha: String): Result[Trees] =
    get(s"repos/$user/$repo/git/trees/$sha")

  /** [[http://developer.github.com/v3/repos]] */
  def repo(user: String, repo: String): Result[Repo] =
    get(s"repos/$user/$repo")

  /** [[http://developer.github.com/v3/git/commits]] */
  def commits(user: String, repo: String, sha: String): Result[CommitResponse] =
    get(s"repos/$user/$repo/commits/$sha")

  /** [[http://developer.github.com/v3/issues]] */
  def issues(user: String, repo: String, state: State = Open): Result[List[Issue]] =
    get(
      s"repos/$user/$repo/issues",
      ScalajHttp.param("state", state.toString)
    )

  /** [[http://developer.github.com/v3/issues/events/]] */
  def issueEvents(user: String, repo: String, number: Long): Result[List[IssueEvent]] =
    get(s"repos/$user/$repo/issues/$number/events")

  /** [[http://developer.github.com/v3/issues/events/]] */
  def issueEvents(user: String, repo: String): Result[List[IssueEvent2]] =
    get(s"repos/$user/$repo/issues/events")

  /** [[http://developer.github.com/v3/repos/comments]] */
  def comments(user: String, repo: String): Result[List[Comment]] =
    get(s"repos/$user/$repo/comments")

  /** [[http://developer.github.com/v3/repos/contents]] */
  def readme(user: String, repo: String, ref: String): Result[Contents] =
    get(s"repos/$user/$repo/readme", ScalajHttp.param("ref", ref))

  /** [[http://developer.github.com/v3/repos/contents]] */
  def readme(user: String, repo: String): Result[Contents] =
    get(s"repos/$user/$repo/readme")

  def org(orgName: String): Result[Organization] =
    get(s"orgs/$orgName")

  def orgs(user: String): Result[List[Org]] =
    get(s"users/$user/orgs")

  def orgs: Result[List[Org]] =
    get(s"user/orgs")

  /** [[http://developer.github.com/v3/pulls]] */
  def pulls(user: String, repo: String, state: State = Open): Result[List[Pull]] =
    get(s"repos/$user/$repo/pulls")
}

