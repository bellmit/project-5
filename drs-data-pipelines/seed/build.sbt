organization := "com.kindminds.drs"
name :=  "drs-data-pipelines-seed"
version      := "1.0.0"

mainClass in Compile := Some("com.kindminds.drs.data.pipelines.seed.Main")

lazy val akkaVersion = "2.6.16"
lazy val springVersion = "5.1.7.RELEASE"



libraryDependencies ++= Seq (
  "com.typesafe.akka" %% "akka-actor-typed"           % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-typed"         % akkaVersion,
  "com.typesafe.akka" %% "akka-serialization-jackson" % akkaVersion,
  "ch.qos.logback"    %  "logback-classic"             % "1.2.3",
  "com.typesafe.akka" %% "akka-multi-node-testkit"    % akkaVersion % Test,
  "org.scalatest"     %% "scalatest"                  % "3.0.8"     % Test,
  "com.typesafe.akka" %% "akka-actor-testkit-typed"   % akkaVersion % Test

)


enablePlugins(JavaAppPackaging)