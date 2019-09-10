val Http4sVersion = "0.20.1"
val Specs2Version = "4.1.0"
val CatsVersion = "1.6.0"
val Fs2Version = "1.0.4"
val CirceVersion = "0.11.1"
val DoobieVersion = "0.6.0"

scalacOptions in ThisBuild ++= Seq(
  "-feature",
  "-deprecation",
  "-Xlint",
  "-Ywarn-dead-code",
  "-Ywarn-value-discard",
  "-Ywarn-unused",
  "-language:higherKinds",
  "-Ypartial-unification",
  "-Ywarn-unused-import",
  "-Yrangepos"
)

lazy val root = (project in file("."))
  .settings(
    organization := "com.speedledger",
    name := "myservice",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.12.8",
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s" %% "http4s-circe" % Http4sVersion,
      "org.http4s" %% "http4s-dsl" % Http4sVersion,
      "org.http4s" %% "http4s-blaze-client" % Http4sVersion,
      "io.circe" %% "circe-literal" % CirceVersion,
      "io.circe" %% "circe-generic" % CirceVersion,
      "io.circe" %% "circe-parser" % CirceVersion,
      "co.fs2" %% "fs2-core" % Fs2Version,
      "io.estatico" %% "newtype" % "0.4.2",
      "com.speedledger" %% "launch-brightly" % "0.8",
      "com.github.pureconfig" %% "pureconfig" % "0.10.0",
      "org.typelevel" %% "cats-core" % CatsVersion,
      "org.scalatest" %% "scalatest" % "3.0.5" % "test",
      "org.scalacheck" %% "scalacheck" % "1.14.0" % "test",
      "org.tpolecat" %% "doobie-core"      % DoobieVersion,
      "org.tpolecat" %% "doobie-postgres"  % DoobieVersion,
      "org.tpolecat" %% "doobie-scalatest" % DoobieVersion % "test",
      "org.flywaydb" % "flyway-core" % "5.0.7"
    ),
    addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.6"),
    addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.2.4"),
    addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full),
    addCompilerPlugin(scalafixSemanticdb),
    logbackConfiguration := generateLogbackConfiguration(
      applicationName = "myservice",
      filePattern = "%date{ISO8601} %-5level [%logger] %msg",
      loggers = Seq(
        LoggerConfiguration("http4s", "WARN"),
        LoggerConfiguration("org.http4s.blaze", "WARN"),
        LoggerConfiguration("org.http4s.blazecore", "WARN"),
        LoggerConfiguration("org.http4s.server", "WARN"),
        LoggerConfiguration("org.http4s.client", "WARN"),
        LoggerConfiguration("ch.qos.logback", "OFF")
      )
    )
  ).enablePlugins(SpeedLedgerPlugin, JavaAppPackaging)
