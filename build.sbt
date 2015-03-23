name := """kirakiratter"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.4"

libraryDependencies ++= Seq(
  cache,
  "org.reactivemongo" %% "reactivemongo" % "0.10.5.0.akka23",
  "org.twitter4j" % "twitter4j-core" % "4.0.2"
)

// Only show failing specs
testOptions in Test += Tests.Argument(TestFrameworks.Specs2, "-xonly")

javaOptions in Test ++= Seq(
  // Load alternate application conf file in tests
  "-Dconfig.file=conf/application.test.conf",
  // Disable StatusLogger message
  "-Dorg.apache.logging.log4j.simplelog.StatusLogger.level=OFF"
)

