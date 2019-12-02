name := "functional-programming"

version := "0.1"

scalaVersion := "2.13.1"

val scalaTestVersion = "3.0.8"

val prodLibs = Seq()

val testLibs = Seq(
  "org.scalactic" %% "scalactic" % scalaTestVersion % Test,
  "org.scalatest" %% "scalatest" % scalaTestVersion % Test
)

//@see https://www.scala-sbt.org/1.x/docs/Testing.html
lazy val root = (project in file("."))
  .configs(IntegrationTest)
  .settings(
    Defaults.itSettings,
    libraryDependencies ++= prodLibs ++ testLibs
  )