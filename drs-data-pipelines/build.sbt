name := "drs-data-pipelines"
organization in ThisBuild := "com.kindminds.drs"
scalaVersion in ThisBuild := "2.12.10"
version      := "1.0.0-RELEASE"

// PROJECTS
lazy val global = project
  .in(file("."))
  .aggregate(
    api,
    core,
    seed
  )

lazy val api = project
  .settings(
    name := "api"
  )


lazy val core = project.
 dependsOn(api)
  .settings(
    name := "core"
  )

lazy val seed = project.
 dependsOn(api)
  .settings(
    name := "seed"
  )


    
