package ghscala

// TODO deprecate
// http://developer.github.com/v3/repos/downloads/
case class Download(
  content_type   :Option[String],
  description    :String,
  created_at     :DateTime,
  html_url       :String,
  url            :String,
  size           :Long,
  name           :String,
  id             :Long,
  download_count :Long
)

