
ThisBuild / scalaVersion := "2.12.20"

val dl4jVersion = "1.0.0-M2.1"
val sparkVersion = "3.5.1"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-mllib" % sparkVersion,
  "io.github.ollama4j" % "ollama4j" % "1.0.79",
  //  "ch.qos.logback" % "logback-classic" % "1.5.6",
  //  "org.slf4j" % "slf4j-api" % "2.0.12",
  //  "org.slf4j" % "slf4j-log4j12" % "2.0.13",
  "com.typesafe.akka" %% "akka-http" % "10.5.3",
  "com.typesafe.akka" %% "akka-stream" % "2.8.6",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.5.3" % Test, // Akka HTTP TestKit
  "org.scalatest" %% "scalatest" % "3.2.19" % Test, // Testing library
  "org.mockito" % "mockito-core" % "5.11.0" % Test,// Mocking library
  "com.amazonaws" % "aws-lambda-java-core" % "1.2.2",
  "com.amazonaws" % "aws-lambda-java-events" % "3.12.0",
)

libraryDependencies += "com.typesafe" % "config" % "1.4.3"

libraryDependencies += "com.typesafe" % "config" % "1.4.3"
