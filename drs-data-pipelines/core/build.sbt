
organization := "com.kindminds.drs"
name :=  "drs-data-pipelines-core"
version      := "1.0.0"

//mainClass in Compile := Some("com.kindminds.drs.data.pipelines.core.Main")

//mainClass in Compile := Some("com.kindminds.drs.data.pipelines.core.app.AllOrdersApp")

//mainClass in Compile := Some("com.kindminds.drs.data.pipelines.core.app.DrsDPApp")

mainClass in Compile := Some("com.kindminds.drs.data.pipelines.core.app.DrsDPAwsApp")

//lazy val akkaVersion = "2.5.12"
lazy val akkaVersion = "2.6.16"
lazy val springVersion = "5.1.7.RELEASE"

resolvers += Resolver.mavenLocal

libraryDependencies ++= Seq (

  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-remote" % akkaVersion ,
  "com.typesafe.akka" %% "akka-cluster" % akkaVersion ,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion ,

  "com.typesafe.akka" %% "akka-cluster-metrics" % akkaVersion ,
  "com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion,
  //"com.typesafe.akka" %% "akka-cluster-typed" % akkaVersion,


  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-core" % "10.1.10",
  "com.typesafe.akka" %% "akka-http" % "10.1.10",


  "com.lightbend.akka" % "akka-stream-alpakka-csv_2.12" % "3.0.0",
  "io.spray" %% "spray-json" % "1.3.5",
  //"com.lightbend.akka" % "akka-stream-alpakka-mongodb_2.12" % "1.1.2",
  "com.lightbend.akka" %% "akka-stream-alpakka-mongodb" % "3.0.0",
  //todo arthru upgrade elasticsearch
  "com.lightbend.akka" %% "akka-stream-alpakka-elasticsearch" % "2.0.2",
  "com.lightbend.akka" %% "akka-stream-alpakka-slick" % "3.0.0",
  "com.lightbend.akka" %% "akka-stream-alpakka-text" % "3.0.0",

  //"com.lightbend.akka" %% "akka-stream-alpakka-slick" % "2.0.1",
  "com.zaxxer" % "HikariCP" % "3.4.5",

  /*
  "com.amazonservices.mws" % "MWSOrders_2013" % "09-01_v2018-10-31",
  "com.amazonservices.mws" % "FulfillmentInventory_2010" % "10-01_v2014-09-30",
  "com.amazonservices.mws" % "MWSFulfillmentOutboundShipment_2010" % "10-01_v2016-10-19",
  "com.amazonservices.mws" % "MWSProducts_2011" % "10-01_v2017-03-22",
    //
  */

  "com.amazonservices.mws" % "MaWSJavaClientLibrary" % "1.1",

//  "org.mongodb" % "mongodb-driver-reactivestreams" % "4.2.2",
//  "org.mongodb" % "mongodb-driver-core" % "4.2.2",
//
//  "org.mongodb" % "mongo-java-driver" % "3.12.8",
//  "org.mongodb" % "mongodb-driver-sync" % "4.2.2",
//  "org.mongodb.scala" % "mongo-scala-driver_2.12" % "4.2.2",


"com.typesafe.akka" % "akka-testkit_2.12" % akkaVersion,
  "com.typesafe.akka" % "akka-stream-testkit_2.12" % akkaVersion,

  "com.enragedginger" %% "akka-quartz-scheduler" % "1.9.1-akka-2.6.x",


  /*
  "org.springframework" % "spring-test" %  springVersion  ,
  "org.springframework" % "spring-context-support" %  springVersion ,
  "org.springframework" % "spring-aop" %  springVersion ,
  "org.springframework" % "spring-tx" %  springVersion ,
  */


  //"org.springframework.data" % "spring-data-mongodb-parent" % "2.1.8.RELEASE",

  "ch.qos.logback" % "logback-classic" % "1.1.3",
  "org.scalatest" % "scalatest_2.12" % "3.0.5",

  "org.json4s" % "json4s-native_2.12" % "3.6.3",

  "joda-time" % "joda-time" % "2.9.9",

  "com.fasterxml.uuid" % "java-uuid-generator" % "3.1.5",

  "org.jooq" % "jooq" % "3.12.4",
  "org.jooq" %% "jooq-scala" % "3.12.4",

  // "org.postgresql" % "postgresql" % "42.2.12",

  //drs might conflict this

  //  "com.kindminds.drs" % "drs-biz-core-common" % "1.0.0-RELEASE",
  //("com.kindminds.drs" % "drs-biz-core-crud" % "1.0.0-RELEASE")
  // .exclude("org.apache.httpcomponents", "httpclient"),
  // ("com.kindminds.drs" % "drs-biz-core-cqrs" % "1.0.0-RELEASE")
  // .exclude("org.apache.httpcomponents", "httpclient")


  "com.kindminds.drs" % "drs-message" % "1.0.0-RELEASE",
  "com.kindminds.drs" % "drs-common" % "1.0.0-RELEASE",
  "com.kindminds.drs" % "drs-biz-core" % "1.0.0-RELEASE",
  "com.kindminds.drs" % "drs-biz-core-legacy" % "1.0.0-RELEASE",
  "com.kindminds.drs" % "drs-adapter" % "1.0.0-RELEASE"


)

dependencyOverrides ++= Seq("org.apache.httpcomponents" % "httpclient" % "4.5.3",
  "org.elasticsearch.client" % "elasticsearch-rest-high-level-client" % "6.6.2",
  "org.elasticsearch.client" % "elasticsearch-rest-client" % "6.6.2"
)

val sparkVersion = "2.4.0"
libraryDependencies ++= Seq(
  "org.apache.spark" % "spark-core_2.12" % sparkVersion,
  "org.apache.spark" % "spark-sql_2.12" % sparkVersion,
  "org.apache.spark" % "spark-yarn_2.12" % sparkVersion,
  //  "org.apache.spark" %% "spark-mllib" % sparkVersion % sparkDependencyScope,
  //  "org.apache.spark" %% "spark-streaming" % sparkVersion % sparkDependencyScope
  "com.google.guava" % "guava" % "28.1-jre",
  // "org.apache.hbase" % "hbase-protocol" % "2.2.2",
  "org.apache.hadoop" % "hadoop-client" % "2.8.0",
  "org.apache.hbase" % "hbase-common" % "1.3.1",
  "org.apache.hbase" % "hbase-server" % "1.3.1"
    exclude("org.mortbay.jetty", "jsp-api-2.1"),
  "org.apache.hbase" % "hbase-client" % "1.3.1"

)

excludeDependencies += "org.mongodb" % "mongo-java-driver"


enablePlugins(JavaAppPackaging)