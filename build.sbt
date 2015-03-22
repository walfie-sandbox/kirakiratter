name := """kirakiratter"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.4"

libraryDependencies ++= Seq(
  cache,
  ws,
  "org.reactivemongo" %% "play2-reactivemongo" % "0.10.5.0.akka23",
  "org.reactivemongo" % "reactivemongo-extensions-json_2.11" % "0.10.5.0.0.akka23"
)

// Only show failing specs
testOptions in Test += Tests.Argument(TestFrameworks.Specs2, "-xonly")

javaOptions in Test += "-Dconfig.file=conf/application.test.conf"

