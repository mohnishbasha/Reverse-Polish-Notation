name := """RPN"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava,PlayEbean)

scalaVersion := "2.11.8"

EclipseKeys.preTasks := Seq(compile in Compile)

EclipseKeys.projectFlavor := EclipseProjectFlavor.Java    

EclipseKeys.createSrc := EclipseCreateSrc.ValueSet(EclipseCreateSrc.ManagedClasses, EclipseCreateSrc.ManagedResources) 

libraryDependencies += javaJdbc
libraryDependencies += javaWs
libraryDependencies += "io.swagger" %% "swagger-play2" % "1.5.3"
libraryDependencies += "org.webjars" % "swagger-ui" % "2.1.2"
