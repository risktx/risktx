package bootstrap.liftweb

import _root_.java.sql.{Connection, DriverManager}

import _root_.net.liftweb.http._
import _root_.net.liftweb.http.provider.{HTTPRequest}
import _root_.net.liftweb.mapper.{DB, ConnectionManager, Schemifier, DefaultConnectionIdentifier, ConnectionIdentifier}
import _root_.net.liftweb.sitemap._
import _root_.net.liftweb.sitemap.Loc._
import _root_.net.liftweb.util._
import _root_.net.liftweb.util.Helpers._

import _root_.org.risktx.model._

/**
  * The Lift initialisation class
  */
class Boot {
  def boot {
    // allow requests for Axis2 to pass straight through the LiftFilter 
    LiftRules.liftRequest.append({case r if (r.path.partPath match {case "services" :: _ => true case _ => false}) => false}) 
    
    // setup the database connection pool
    if (!DB.jndiJdbcConnAvailable_?)
      DB.defineConnectionManager(DefaultConnectionIdentifier, DBVendor)

    // package within which Lift looks for snippets
    LiftRules.addToPackages("org.risktx")
    
    // create or update database entities in alignment with model classes
    Schemifier.schemify(true, Log.infoF _, User, TradingProfile, Message)

    // build SiteMap
    val entries = Menu(Loc("Home", List("index"), "Home")) :: Menu(Loc("PingMessage", List("pingmessage"), "Send Ping Request")) :: Nil 
    
    //:: User.sitemap
    
    LiftRules.setSiteMap(SiteMap(entries:_*))

    /*
     * Show the spinny image when an Ajax call starts
     */
    LiftRules.ajaxStart =
      Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)

    /*
     * Make the spinny image go away when it ends
     */
    LiftRules.ajaxEnd =
      Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

    LiftRules.early.append(makeUtf8)

    S.addAround(DB.buildLoanWrapper)
  }

  /**
   * Force the request to be UTF-8
   */
  private def makeUtf8(req: HTTPRequest) {
    req.setCharacterEncoding("UTF-8")
  }

}

/**
* Database connection calculation
*/
object DBVendor extends ConnectionManager {
  private var pool: List[Connection] = Nil
  private var poolSize = 0
  private val maxPoolSize = 4

  private def createOne: Box[Connection] = try {
    val driverName: String = Props.get("db.driver") openOr
    "org.h2.Driver"

    val dbUrl: String = Props.get("db.url") openOr
    "jdbc:h2:mem:DATABASE;DB_CLOSE_DELAY=-1"

    Class.forName(driverName)

    val dm = (Props.get("db.user"), Props.get("db.password")) match {
      case (Full(user), Full(pwd)) =>
	DriverManager.getConnection(dbUrl, user, pwd)

      case _ => DriverManager.getConnection(dbUrl)
    }

    Full(dm)
  } catch {
    case e: Exception => e.printStackTrace; Empty
  }

  def newConnection(name: ConnectionIdentifier): Box[Connection] =
    synchronized {
      pool match {
	case Nil if poolSize < maxPoolSize =>
	  val ret = createOne
        poolSize = poolSize + 1
        ret.foreach(c => pool = c :: pool)
        ret

	case Nil => wait(1000L); newConnection(name)
	case x :: xs => try {
          x.setAutoCommit(false)
          Full(x)
        } catch {
          case e => try {
            pool = xs
            poolSize = poolSize - 1
            x.close
            newConnection(name)
          } catch {
            case e => newConnection(name)
          }
        }
      }
    }

  def releaseConnection(conn: Connection): Unit = synchronized {
    pool = conn :: pool
    notify
  }
}


