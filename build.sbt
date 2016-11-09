name := "mSpRL"

version := "1.0"

scalaVersion := "2.12.0"

resolvers += "CogcompSoftware" at "http://cogcomp.cs.illinois.edu/m2repo/"

libraryDependencies ++= Seq("edu.illinois.cs.cogcomp" % "saul-examples_2.11" % "0.5.4" withSources)

