import sbt.addCompilerPlugin

val Http4sVersion = "0.23.11"
val CirceVersion = "0.14.1"
val MunitVersion = "0.7.29"
val LogbackVersion = "1.2.10"
val MunitCatsEffectVersion = "1.0.7"

ThisBuild / organization := "com.purecrawler"
ThisBuild / name := "pureccrawler"
ThisBuild / version := "0.0.1-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.8"

lazy val PureCrawler =
  project
    .in(file("."))
    .settings(
      assembly / mainClass := Some("com.purecrawler.app.Main")
    )
    .dependsOn(domain, repository, services, application)
    .aggregate(domain, repository, services, application)

lazy val domain =
  project
    .in(file("domain"))
    .settings(commonSettings)

lazy val repository =
  project
    .in(file("repository"))
    .settings(commonSettings)

lazy val services =
  project
    .in(file("services"))
    .dependsOn(domain)
    .settings(commonSettings)

lazy val application =
  project
    .in(file("application"))
    .dependsOn(domain, services, repository)
    .settings(
    )
    .settings(commonSettings)

lazy val commonSettings = Seq(
    libraryDependencies ++= Seq(
      "org.http4s"      %% "http4s-ember-server" % Http4sVersion,
      "org.http4s"      %% "http4s-ember-client" % Http4sVersion,
      "org.http4s"      %% "http4s-circe"        % Http4sVersion,
      "org.http4s"      %% "http4s-dsl"          % Http4sVersion,
      "io.circe"        %% "circe-generic"       % CirceVersion,
      "org.scalameta"   %% "munit"               % MunitVersion           % Test,
      "org.typelevel"   %% "munit-cats-effect-3" % MunitCatsEffectVersion % Test,
      "ch.qos.logback"  %  "logback-classic"     % LogbackVersion         % Runtime,
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.13.2" cross CrossVersion.full),
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1"),
    addCompilerPlugin("org.typelevel" % "kind-projector" % "0.13.2" cross CrossVersion.full),
    testFrameworks += new TestFramework("munit.Framework"),
    Compile / console / scalacOptions --= Seq(
      "-Wunused:_",
      "-Xfatal-warnings"
    ),
    Compile / scalaSource := baseDirectory.value / "src/main/scala",
    Test / scalaSource := baseDirectory.value / "src/test/scala",
    Test / console / scalacOptions :=
      (Compile / console / scalacOptions).value
  )
