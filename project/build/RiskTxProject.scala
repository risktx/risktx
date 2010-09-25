import sbt._

class RiskTxProject(info: ProjectInfo) extends DefaultWebProject(info) with BasicScalaIntegrationTesting with stax.StaxPlugin with IdeaProject {

  val scalaReleases = ScalaToolsReleases

  // ------------------------------------------------------------
  // repositories
  val akka = "Akka Repository" at "http://scalablesolutions.se/akka/repository"
  val guice = "Guice Repository" at "http://guiceyfruit.googlecode.com/svn/repo/releases/"
  val dispatch = "Dispatch Repository" at "http://databinder.net/repo/"
  val lag = "www.lag.net Repository" at "http://www.lag.net/repo/"
  //val sunjdmk = "sunjdmk" at "http://wp5.e-taxonomy.eu/cdmlib/mavenrepo"
  //val databinder = "DataBinder" at "http://databinder.net/repo"
  val configgy = "Configgy" at "http://www.lag.net/repo"
  val codehaus = "Codehaus" at "http://repository.codehaus.org"
  //val codehaus_snapshots = "Codehaus Snapshots" at "http://snapshots.repository.codehaus.org"
  val jboss = "jBoss" at "http://repository.jboss.org/maven2"
  //val google = "google" at "http://google-maven-repository.googlecode.com/svn/repository"
  val m2 = "m2" at "http://download.java.net/maven/2"

  // ------------------------------------------------------------
  // project versions
  val liftVersion = "2.0"
  val axisVersion = "1.4.1"
  val akkaVersion = "0.7.1"

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
    "net.liftweb" % "lift-util" % liftVersion % "compile",
    "org.apache.axis2" % "axis2-kernel" % axisVersion % "compile->default",
    "org.apache.axis2" % "axis2-adb" % axisVersion % "compile->default",
    "org.apache.axis2" % "axis2-jaxws" % axisVersion % "compile->default",
    "org.freemarker" % "freemarker" % "2.3.16" % "compile->default",
    "se.scalablesolutions.akka" % "akka-core_2.7.7" % akkaVersion % "compile->default",
    "se.scalablesolutions.akka" % "akka-kernel_2.7.7" % akkaVersion % "compile->default",
    "se.scalablesolutions.akka" % "akka-persistence-mongo_2.7.7" % akkaVersion % "compile->default",
    "se.scalablesolutions.akka" % "akka-rest_2.7.7" % akkaVersion % "compile->default",
    "se.scalablesolutions.akka" % "akka-security_2.7.7" % akkaVersion % "compile->default",
    "se.scalablesolutions.akka" % "akka-sample-security_2.7.7" % akkaVersion % "compile->default",
    "org.mortbay.jetty" % "jetty" % "6.1.22" % "test->default",
    "junit" % "junit" % "4.5" % "test->default",
    "org.scala-tools.testing" % "specs" % "1.6.2.1" % "test->default"
  ) ++ super.libraryDependencies
}
