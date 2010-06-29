import sbt._

class Plugins(info: ProjectInfo) extends PluginDefinition(info) {
  val stax = "eu.getintheloop" % "sbt-stax-plugin" % "0.1.1"
  val staxReleases = "stax-release-repo" at "http://mvn.stax.net/content/repositories/public"
}