name :=  "drs-biz-service"
mainClass in Compile := Some("com.kindminds.drs.core.App")
//mainClass in Compile := Some("com.kindminds.drs.core.GoogleSheetApp")


scalaVersion := "2.12.12"

javaOptions in Universal ++= Seq(
  // -J params will be added as jvm parameters
  "-J-Xmx256m",
  "-J-Xms64m"
)

lazy val akkaVersion = "2.6.16"
lazy val springVersion = "5.2.10.RELEASE"

resolvers += Resolver.mavenLocal

conflictWarning := ConflictWarning.disable

javacOptions ++= Seq("-source", "11")
Compile / compileOrder := CompileOrder.Mixed

sources in (Compile,doc) := Seq.empty
publishArtifact in (Compile, packageDoc) := false

libraryDependencies ++= Seq (

  "com.typesafe.akka" % "akka-actor_2.12" % akkaVersion,
  "com.typesafe.akka" % "akka-remote_2.12" % akkaVersion,
  "com.typesafe.akka" % "akka-cluster_2.12" % akkaVersion,
  "com.typesafe.akka" % "akka-slf4j_2.12" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-metrics" % akkaVersion ,
  "com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion,

  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.enragedginger" %% "akka-quartz-scheduler" % "1.8.2-akka-2.6.x",
  "org.slf4j" % "slf4j-api" % "1.7.5",
  "org.slf4j" % "slf4j-log4j12" % "1.7.5",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.9.8",


  "javax.mail" % "mail" % "1.4.7",
  "org.seleniumhq.selenium" % "selenium-support" % "3.141.59",
  "org.seleniumhq.selenium" % "selenium-firefox-driver" % "3.141.59",
  "org.seleniumhq.selenium" % "selenium-api" % "3.141.59",
  //"joda-time" % "joda-time" % "2.9.9",
  "org.apache.poi" % "poi" % "3.17",
  "org.apache.poi" % "poi-ooxml" % "3.17",
  "org.scala-lang" % "scala-library" % "2.12.4",
  "com.amazonservices.mws" % "MWSOrders_2013" % "09-01_v2021-01-06",
  "com.amazonservices.mws" % "FulfillmentInventory_2010" % "10-01_v2014-09-30",
  "com.amazonservices.mws" % "MWSFulfillmentOutboundShipment_2010" % "10-01_v2016-10-19",
  "com.amazonservices.mws" % "MWSProducts_2011" % "10-01_v2017-03-22",
  "com.amazonservices.mws" % "MaWSJavaClientLibrary" % "1.1",


  "org.quartz-scheduler" % "quartz" % "2.3.0",
  "com.fasterxml.uuid" % "java-uuid-generator" % "3.1.5",


  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
  "org.scalactic" %% "scalactic" % "3.0.5",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test",
  "org.json" % "json" % "20180813",
  "org.json" % "json" % "20180813",
  "commons-lang" % "commons-lang" % "2.6",

  "com.kindminds.drs" % "drs-message" % "1.0.0-RELEASE",
  "com.kindminds.drs" % "drs-common" % "1.0.0-RELEASE",
  "com.kindminds.drs" % "drs-biz-core" % "1.0.0-RELEASE",
  "com.kindminds.drs" % "drs-biz-core-legacy" % "1.0.0-RELEASE",
  "com.kindminds.drs" % "drs-adapter" % "1.0.0-RELEASE"


)



enablePlugins(JavaAppPackaging)