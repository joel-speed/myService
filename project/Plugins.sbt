import sbt._

resolvers += "speedledger repo" at "https://artifactory.speedledger.net/repo/"
addSbtPlugin("io.spray" % "sbt-revolver" % "0.9.1")
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.0.0")
addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.9.5")

lazy val root = (project in file(".")).dependsOn(speedledgerPlugin)

lazy val speedledgerPlugin = RootProject(uri("ssh://git@github.com/speedledger/sbt-speedledger.git#aa4a2abc270793b8a036700f09184e513b6922a2"))
