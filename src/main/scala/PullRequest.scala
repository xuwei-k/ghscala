package ghscala

final case class PullRequest (
  _links              :PullLinks,
  additions           :Long,
  assignee            :User,
  base                :Pull.Ref,
  body                :String,
  changed_files       :Long,
  closed_at           :Option[DateTime],
  comments            :Long,
  comments_url        :String,
  commits             :Long,
  commits_url         :String,
  created_at          :String,
  deletions           :Long,
  diff_url            :String,
  head                :Pull.Ref,
  html_url            :String,
  id                  :Long,
  issue_url           :String,
  locked              :Boolean,
  merge_commit_sha    :String,
  mergeable           :Boolean,
  mergeable_state     :String,
  merged              :Boolean,
  merged_at           :Option[DateTime],
  merged_by           :User,
  milestone           :Option[Milestone],
  number              :Long,
  patch_url           :String,
  review_comment_url  :String,
  review_comments     :Long,
  review_comments_url :String,
  state               :String,
  statuses_url        :String,
  title               :String,
  updated_at          :DateTime,
  url                 :String,
  user                :User
) extends JsonToString[PullRequest]

object PullRequest{
  implicit val pullRequestCodecJson: CodecJson[PullRequest] = ??? // TODO
}
