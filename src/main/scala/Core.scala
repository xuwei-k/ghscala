package com.github.xuwei_k.ghscala

trait Core extends Common{

  val _followers = (page:Int) => (user:String) => listRequest[User]("users",user,"followers")()(page)

  val _repos = (page:Int) => (user:String) => listRequest[Repo]("users",user,"repos")()(page)

  val _refs = (page:Int) => {
    listRequest[Ref]("repos",_:String,_:String,"git/refs")()(page)
  }.tupled

  val _downloads = (page:Int) => {
    listRequest[Download]("repos",_:String,_:String,"downloads")()(page)
  }.tupled

  val _forks = (page:Int) => {
    listRequest[Repo]("repos",_:String,_:String,"forks")()(page)
  }.tupled

  val _watchers = (page:Int) => {
    usersAndOrgs("repos",_:String,_:String,"watchers")()(page)
  }.tupled

  val _collaborators = (page:Int) => {
    listRequest[User]("repos",_:String,_:String,"collaborators")()(page)
  }.tupled

  val _orgs = (page:Int) => {
    listRequest[Org]("users",_:String,"orgs")()(page)
  }

}
