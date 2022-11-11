ThisBuild / scalaVersion     := "2.13.10"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .enablePlugins(DockerPlugin)
  .enablePlugins(AshScriptPlugin)
  .settings(
    name := "scala-zio2-scopt-workshop",
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % "2.0.3",
      "com.github.scopt" %% "scopt" % "4.1.0",
      "eu.timepit" %% "refined"                 % "0.10.1",
      "dev.zio" %% "zio-test" % "2.0.3" % Test
    ),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework"),
    Docker / packageName := "scopt-playground",
    dockerBaseImage := "openjdk:11-jre-slim-buster",
    dockerExposedPorts ++= Seq(8080),
    dockerUpdateLatest := true,
  )
