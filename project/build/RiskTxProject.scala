import sbt._

class RiskTxProject(info: ProjectInfo) extends DefaultWebProject(info) with BasicScalaIntegrationTesting with stax.StaxPlugin with IdeaProject {

  val scalaReleases = ScalaToolsReleases

  // ------------------------------------------------------------
  // repositories
  val mavenmirror = "mavenmirror" at "http://mirrors.ibiblio.org/pub/mirrors/maven2/"
  val m2 = "m2" at "http://download.java.net/maven/2"
  val jboss = "jBoss" at "http://repository.jboss.org/maven2"
  val jbossnexus = "Jboss Nexus" at "http://repository.jboss.org/nexus/content/groups/public/"
  val akka = "Akka Repository" at "http://scalablesolutions.se/akka/repository"
  val toolsrepo = "toolsrepo" at "http://scala-tools.org/repo-releases/"
  val configgy = "Configgy" at "http://www.lag.net/repo"
  val guice = "Guice Repository" at "http://guiceyfruit.googlecode.com/svn/repo/releases/"
  val codehaus = "Codehaus" at "http://repository.codehaus.org"

//  val sunjdmk = "sunjdmk" at "http://wp5.e-taxonomy.eu/cdmlib/mavenrepo"
//  val databinder = "DataBinder" at "http://databinder.net/repo"
//  val specsRepo = "specs-repo" at "http://specs.googlecode.com/svn/maven2"
//  val google = "google" at "http://google-maven-repository.googlecode.com/svn/repository"
//  val codehaus_snapshots = "Codehaus Snapshots" at "http://snapshots.repository.codehaus.org"

  // project versions
  val liftVersion = "2.1"
  val axisVersion = "1.4.1"
  val akkaVersion = "0.10"

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

  // uncomment the following if you want to use the snapshot repo
  //val scalatoolsSnapshot = ScalaToolsSnapshots

  // If you're using JRebel for Lift development, uncomment
  // this line
  // override def scanDirectories = Nil
   
  override def libraryDependencies = Set(
    "net.liftweb" %% "lift-webkit" % liftVersion % "compile->default",
    "net.liftweb" %% "lift-mapper" % liftVersion % "compile->default",
    "net.liftweb" %% "lift-mongodb" % liftVersion % "compile->default",
    "net.liftweb" %% "lift-mongodb-record" % liftVersion % "compile->default",
    "net.liftweb" %% "lift-testkit" % liftVersion % "compile->default",
    "net.liftweb" %% "lift-widgets" % liftVersion % "compile->default",
    "net.liftweb" %% "lift-util" % liftVersion % "compile",
    "org.apache.axis2" % "axis2-kernel" % axisVersion % "compile->default",
    "org.apache.axis2" % "axis2-adb" % axisVersion % "compile->default",
    "org.apache.axis2" % "axis2-jaxws" % axisVersion % "compile->default",
    "org.apache.ws.commons.axiom" % "axiom-api" % "1.2.4" % "compile->default", 
    "org.freemarker" % "freemarker" % "2.3.16" % "compile->default",
    "se.scalablesolutions.akka" %% "akka-core"  % akkaVersion % "compile->default",
    "se.scalablesolutions.akka" %% "akka-http" % akkaVersion % "compile->default",
    "se.scalablesolutions.akka" %% "akka-persistence-mongo" % akkaVersion % "compile->default",
    "org.mortbay.jetty" % "jetty" % "6.1.22" % "test->default",
    "junit" % "junit" % "4.5" % "test->default",
    "org.scala-tools.testing" %% "specs" % "1.6.5" % "test->default",
    "com.h2database" % "h2" % "1.2.138"
  ) ++ super.libraryDependencies
}
