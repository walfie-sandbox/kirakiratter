name := """kirakiratter"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.4"

libraryDependencies ++= Seq(
  cache,
  ws
)

// Only show failing specs
testOptions in Test += Tests.Argument(TestFrameworks.Specs2, "-xonly")

