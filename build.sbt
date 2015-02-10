import AssemblyKeys._

assemblySettings

name := "FlightInfo"

version := "1.0"


//libraryDependencies += "org.apache.spark" %% "spark-core" % "1.2.0" % "provided"

libraryDependencies += "org.apache.spark" %% "spark-sql" % "1.2.0" % "provided"

libraryDependencies += "org.apache.spark" % "spark-hive_2.10" % "1.2.0" % "provided"

scalaVersion := "2.10.3"


resolvers += "Akka Repository" at "http://repo.akka.io/releases/"

resolvers += Resolver.mavenLocal

exportJars := true

mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
{
  case PathList("javax", "servlet", xs @ _*) => MergeStrategy.last
  case PathList("org", "apache", xs @ _*) => MergeStrategy.last
  case PathList("com", "esotericsoftware", xs @ _*) => MergeStrategy.last
  case "about.html" => MergeStrategy.rename
  case x => old(x)
}
}

