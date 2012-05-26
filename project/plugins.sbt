resolvers += Resolver.url("sbt-plugin-releases",
  new URL("http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases/"))(Resolver.ivyStylePatterns)


addSbtPlugin("com.eed3si9n" % "sbt-scalashim" % "0.2.2")

addSbtPlugin("com.typesafe" % "sbt-mima-plugin" % "0.1.3")

addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.6.0")