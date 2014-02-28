package ghscala

sealed abstract class SearchRepoSort(
  private[ghscala] val name: Option[String]
)

object SearchRepoSort {
  object Default extends SearchRepoSort(None)
  object Forks extends SearchRepoSort(Some("forks"))
  object Stars extends SearchRepoSort(Some("stars"))
  object Updated extends SearchRepoSort(Some("updated"))
}

