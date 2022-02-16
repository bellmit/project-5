

organization := "com.kindminds.drs"
name := "drs-data-pipelines-api"
version      := "1.0.0"


libraryDependencies ++= Seq (
  "com.typesafe.akka" % "akka-actor_2.12" % "2.5.12",
  "com.typesafe.akka" % "akka-remote_2.12" % "2.5.12",
  "joda-time" % "joda-time" % "2.9.9"
)