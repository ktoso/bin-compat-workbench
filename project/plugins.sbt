resolvers += Classpaths.sbtPluginReleases
resolvers += Classpaths.typesafeResolver

addSbtPlugin("com.typesafe" % "sbt-mima-plugin" % "0.1.9")
addSbtPlugin("org.scalariform" % "sbt-scalariform" % "1.6.0")
addSbtPlugin("com.lightbend.paradox" % "sbt-paradox" % "0.2.0")

