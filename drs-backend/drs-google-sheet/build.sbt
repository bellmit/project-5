name :=  "drs-sys-core"
mainClass in Compile := Some("com.kindminds.drs.core.App")
//mainClass in Compile := Some("com.kindminds.drs.GoogleSheetApp")


scalaVersion := "2.12.4"

javaOptions in Universal ++= Seq(
  // -J params will be added as jvm parameters
  "-J-Xmx256m",
  "-J-Xms64m"
)

lazy val akkaVersion = "2.5.12"
lazy val springVersion = "5.1.5.RELEASE"

resolvers += Resolver.mavenLocal

conflictWarning := ConflictWarning.disable

javacOptions ++= Seq("-source", "1.8")
Compile / compileOrder := CompileOrder.Mixed

sources in (Compile,doc) := Seq.empty
publishArtifact in (Compile, packageDoc) := false

libraryDependencies ++= Seq (

  "com.typesafe.akka" % "akka-actor_2.12" % akkaVersion,
  "com.typesafe.akka" % "akka-remote_2.12" % akkaVersion,
  "com.typesafe.akka" % "akka-cluster_2.12" % akkaVersion,
  "com.typesafe.akka" % "akka-slf4j_2.12" % akkaVersion,
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.enragedginger" %% "akka-quartz-scheduler" % "1.6.1-akka-2.5.x",
  "org.slf4j" % "slf4j-api" % "1.7.5",
  "org.slf4j" % "slf4j-log4j12" % "1.7.5",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.9.8",
  "org.springframework" % "spring-test" %  springVersion % Test ,
  "org.springframework" % "spring-context-support" %  springVersion ,
  "org.springframework" % "spring-aop" %  springVersion ,
  "org.springframework" % "spring-tx" %  springVersion ,
  "org.springframework" % "spring-jdbc" %  springVersion ,
  "org.springframework" % "spring-orm" %  springVersion ,
  "org.springframework" % "spring-test" %  springVersion ,

  "javax.mail" % "mail" % "1.4.7",
  "org.seleniumhq.selenium" % "selenium-support" % "3.141.59",
  "org.seleniumhq.selenium" % "selenium-firefox-driver" % "3.141.59",
  "org.seleniumhq.selenium" % "selenium-api" % "3.141.59",
  //"joda-time" % "joda-time" % "2.9.9",
  "org.apache.poi" % "poi" % "3.17",
  "org.apache.poi" % "poi-ooxml" % "3.17",
  "org.scala-lang" % "scala-library" % "2.12.4",
  "technology.integration.amazonservices.mws" % "amazon-mws-order" % "1.0",
  "org.hibernate" % "hibernate-core" % "5.4.1.Final",
  "org.hibernate" % "hibernate-entitymanager" % "5.4.1.Final",
  "org.hibernate" % "hibernate-c3p0" % "5.4.1.Final",
  "org.quartz-scheduler" % "quartz" % "2.3.0",
  "com.fasterxml.uuid" % "java-uuid-generator" % "3.1.5",
  "com.google.api-client" % "google-api-client" % "1.28.0",
  "com.google.apis" % "google-api-services-sheets" % "v4-rev567-1.25.0",
  "com.google.oauth-client" % "google-oauth-client-java6" % "1.28.0",
  "com.google.oauth-client" % "google-oauth-client-jetty" % "1.28.0",

  "com.typesafe.akka" %% "akka-testkit" % "2.5.21" % Test,
  "org.scalactic" %% "scalactic" % "3.0.5",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test",
  "org.json" % "json" % "20180813",
  "org.json" % "json" % "20180813",
  "commons-lang" % "commons-lang" % "2.6",



  "com.kindminds.drs" % "drs-sys-api" % "1.0.0-RELEASE",
  "com.kindminds.drs" % "drs-sys-persist" % "1.0.0-RELEASE",
  "com.kindminds.drs" % "drs-sys-adapter" % "1.0.0-RELEASE"



)



enablePlugins(JavaAppPackaging)