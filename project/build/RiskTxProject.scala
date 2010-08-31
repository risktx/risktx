import sbt._

class RiskTxProject(info: ProjectInfo) extends DefaultWebProject(info) with BasicScalaIntegrationTesting with stax.StaxPlugin with IdeaProject {

  val scalaReleases = ScalaToolsReleases

  val liftVersion = "2.0"
  val axisVersion = "1.4.1"

  lazy val staxApp = property[String]
  lazy val staxUser = property[String]

  override def staxApplicationId = staxApp.value
  override def staxUsername = staxUser.value

  override def ivyXML =
    <dependency org="log4j" name="log4j" rev="1.2.15">
      <exclude org="com.sun.jdmk"/>
      <exclude org="com.sun.jmx"/>
      <exclude org="javax.jms"/>
    </dependency>

  override def libraryDependencies = Set(
    "net.liftweb" % "lift-webkit" % liftVersion % "compile->default",
    "net.liftweb" % "lift-mongodb-record" % liftVersion % "compile->default",
    "net.liftweb" % "lift-testkit" % liftVersion % "compile->default",
    "net.liftweb" % "lift-widgets" % liftVersion % "compile->default",  
    "org.apache.axis2" % "axis2-kernel" % axisVersion % "compile->default",
    "org.apache.axis2" % "axis2-adb" % axisVersion % "compile->default",
    "org.apache.axis2" % "axis2-jaxws" % axisVersion % "compile->default",
    "org.mortbay.jetty" % "jetty" % "6.1.22" % "test->default",
    "junit" % "junit" % "4.5" % "test->default",
    "org.scala-tools.testing" % "specs" % "1.6.2.1" % "test->default"
  ) ++ super.libraryDependencies
}
