import sbt.Keys.resolvers

organization in ThisBuild := "com.example"
version in ThisBuild := "1.0-SNAPSHOT"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.12.4"

lazy val `hello` = (project in file("."))
  .aggregate(`hello-api`, `hello-impl`)

lazy val `hello-api` = (project in file("hello-api"))
  .settings(common: _*)
  .settings(
    libraryDependencies ++= Seq(
      lagomJavadslApi,
      lombok
    )
  )

lazy val `hello-impl` = (project in file("hello-impl"))
  .enablePlugins(LagomJava, Cinnamon)
  .settings(common: _*)
  .settings(
    libraryDependencies ++= Seq(
      lagomLogback,
      lagomJavadslCluster,
      lombok,
      "com.lightbend.akka" %% "akka-split-brain-resolver" % "1.1.3"
    ),
    credentials += Credentials(Path.userHome / ".lightbend" / "commercial.credentials"),
    resolvers += "com-mvn" at "https://repo.lightbend.com/commercial-releases/",
    resolvers += Resolver.url("com-ivy",
      url("https://repo.lightbend.com/commercial-releases/"))(Resolver.ivyStylePatterns),
    cinnamon in run := true,
    cinnamonLogLevel := "INFO"

  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`hello-api`)

val lombok = "org.projectlombok" % "lombok" % "1.16.18"

def common = Seq(
  javacOptions in compile += "-parameters"
)

lagomCassandraEnabled in ThisBuild := false
lagomKafkaEnabled in ThisBuild := false
