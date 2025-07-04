val scala13Version = "2.13.16"
val scala3Version = "3.3.6"
ThisBuild / scalaVersion := scala13Version
ThisBuild / crossScalaVersions := Seq(scala13Version, scala3Version)

val pekkoVersion = "1.0.2"
val foryVersion = "0.11.1"
val jacksonVersion = "2.16.2"

lazy val root = (project in file("."))
  .settings(
    name := "pekko-serialization-fory",
    organization := "com.github.pjfanning",
    libraryDependencies ++= Seq(
      "org.apache.pekko" %% "pekko-actor" % pekkoVersion,
      "org.apache.pekko" %% "pekko-actor-typed" % pekkoVersion % Optional,
      "org.apache.pekko" %% "pekko-stream" % pekkoVersion % Optional,
      "org.apache.fory" % "fory-core" % foryVersion,
      "org.apache.fory" %% "fory-scala" % foryVersion,
      "com.fasterxml.jackson.core" % "jackson-databind" % jacksonVersion,
      "com.fasterxml.jackson.core" % "jackson-core" % jacksonVersion,
      "com.fasterxml.jackson.dataformat" % "jackson-dataformat-cbor" % jacksonVersion,
      "com.fasterxml.jackson.datatype" % "jackson-datatype-jdk8" % jacksonVersion,
      "com.fasterxml.jackson.datatype" % "jackson-datatype-jsr310" % jacksonVersion,
      "com.fasterxml.jackson.module" % "jackson-module-parameter-names" % jacksonVersion,
      "com.fasterxml.jackson.module" %% "jackson-module-scala" % jacksonVersion,
      "org.lz4" % "lz4-java" % "1.8.0",
      "org.scala-lang.modules" %% "scala-java8-compat" % "1.0.2",
      "org.apache.pekko" %% "pekko-testkit" % pekkoVersion % Test,
      "org.scalatest" %% "scalatest" % "3.2.19" % Test,
      "ch.qos.logback" % "logback-classic" % "1.3.15" % Test
    ),
    javacOptions += "-parameters",
    licenses += ("Apache-2.0", new URL("https://github.com/pjfanning/pekko-serialization-fory/blob/main/LICENSE")),
    startYear := Some(2025),
    homepage := Some(url("https://github.com/pjfanning/pekko-serialization-fory")),
    developers := Developer("pjfanning", "PJ Fanning", "", url("https://github.com/pjfanning")) :: Nil,
    scmInfo := Some(
      ScmInfo(
        browseUrl = url("https://github.com/pjfanning/pekko-serialization-fory.git"),
        connection = "scm:git:git@github.com:pjfanning/pekko-serialization-fory.git"
      )
    )
  )

ThisBuild / publishTo := {
  val centralSnapshots = "https://central.sonatype.com/repository/maven-snapshots/"
  if (isSnapshot.value) Some("central-snapshots" at centralSnapshots)
  else localStaging.value
}

ThisBuild / githubWorkflowTargetTags ++= Seq("v*")
ThisBuild / githubWorkflowPublishTargetBranches :=
  Seq(
    RefPredicate.StartsWith(Ref.Tag("v")),
    RefPredicate.Equals(Ref.Branch("main"))
  )

ThisBuild / githubWorkflowPublish := Seq(
  WorkflowStep.Sbt(
    commands = List("ci-release"),
    name = Some("Publish project"),
  )
)

ThisBuild / githubWorkflowPublish := Seq(
  WorkflowStep.Sbt(
    commands = List("ci-release"),
    name = Some("Publish project"),
    env = Map(
      "PGP_PASSPHRASE" -> "${{ secrets.PGP_PASSPHRASE }}",
      "PGP_SECRET" -> "${{ secrets.PGP_SECRET }}",
      "SONATYPE_PASSWORD" -> "${{ secrets.SONATYPE_PASSWORD }}",
      "SONATYPE_USERNAME" -> "${{ secrets.SONATYPE_USERNAME }}"
    )
  )
)
