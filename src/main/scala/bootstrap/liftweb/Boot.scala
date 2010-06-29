package bootstrap.liftweb

import javax.mail.{Authenticator, PasswordAuthentication}

import _root_.net.liftweb.http._
import _root_.net.liftweb.http.provider.{HTTPRequest}
import _root_.net.liftweb.sitemap._
import _root_.net.liftweb.sitemap.Loc._
import _root_.net.liftweb.common._
import _root_.net.liftweb.util._
import _root_.net.liftweb.mongodb._
import _root_.net.liftweb.widgets.menu.MenuWidget

import _root_.org.risktx.model._
import _root_.org.risktx.service.rest.RestOperations

/**
 * the Lift initialisation class
 */
class Boot extends Logger {
  def boot {
    
    info("Booting RiskTx...")

    // allow requests for Axis2 to pass straight through the LiftFilter 
    LiftRules.liftRequest.append({
      case r if (r.path.partPath match {
        case "services" :: _ => true
        case _ => false
      }) => false
    })

    configMongoDB
    configMailer

    // package within which Lift looks for snippets
    LiftRules.addToPackages("org.risktx")

    // enable rest - no sessions created
    LiftRules.statelessDispatchTable.append(RestOperations)

    // build initial SiteMap
    val entries = Menu(Loc("Home", List("index"), "Home"),
      Menu(Loc("about", List("about"), "About"))
      ) ::
            Menu(Loc("messages", List("messages"), "Message Log"),
              Menu(Loc("latest", List("latest"), "Latest Messages")),
              Menu(Loc("failed", List("failed"), "Failed Messages"))
              ) ::
            Menu(Loc("test", List("test"), "Test Scripts"),
              Menu(Loc("scriptrunner", List("scriptrunner"), "Script Runner")),
              Menu(Loc("attachments", List("test-harness", "attachment", "list"), "Attachments")),
              Menu(Loc("Add Attachment", List("test-harness", "attachment", "add"), "Add Attachment", Hidden)),
              Menu(Loc("View Attachment", List("test-harness", "attachment", "view"), "View Attachment", Hidden)),
              Menu(Loc("Edit Attachment", List("test-harness", "attachment", "edit"), "Edit Attachment", Hidden)),
              Menu(Loc("Delete Attachment", List("test-harness", "attachment", "delete"), "Delete Attachment", Hidden)),
              Menu(Loc("templates", List("templates"), "Templates")),
              Menu(Loc("scripts", List("scripts"), "Scripts"))
              ) ::
            Menu(Loc("configuration", List("configuration"), "Configuration"),
              Menu(Loc("profiles", List("profiles"), "Trading Profiles")),
              Menu(Loc("parties", List("parties"), "Trading Parties")),
              Menu(Loc("schemas", List("schemas"), "Schemas")),
              Menu(Loc("users", List("users"), "Users")),
              Menu(Loc("settings", List("settings"), "General Settings")),
              ) ::
            User.sitemap

    // initialise MenuWidget (from lift-widgets)
    MenuWidget.init()

    // apply security to SiteMap, user must be logged in to access anything except user_mgt
    LiftRules.setSiteMap(new SiteMap(List({
      case Full(Req(path, _, _)) if !User.loggedIn_? && !path.startsWith(List("user_mgt")) =>
        Loc.EarlyResponse(() => {
          S.error("Please log in") //TODO - externalise
          val uri = S.uri
          S.redirectTo("/user_mgt/login", () =>
            {User.whereAfterLogin.set(Full(uri))})
        })
    }), entries: _*))

    // Map request to the primary key for Item to use our view
    LiftRules.rewrite.append {
      case RewriteRequest(
      ParsePath(List("test-harness", "attachment", action, id), _, _, _), _, _) =>
        RewriteResponse("test-harness" :: "attachment" :: action :: Nil, Map("id" -> id))
    }

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
    LiftRules.useXhtmlMimeType = false
  }

  /**
   * Force the request to be UTF-8
   */
  private def makeUtf8(req: HTTPRequest) {
    req.setCharacterEncoding("UTF-8")
  }

  /**
   * Configure MongoDB connection from properties file
   */
  def configMongoDB() {
    
    var mongoAddress = MongoAddress(MongoHost(), "risktx")

    (Props.get("mongo.host"), Props.getInt("mongo.port"), Props.get("mongo.db")) match {
      case (Full(host), Full(port), Full(db)) => {
        mongoAddress = MongoAddress(MongoHost(host, port), db)

        (Props.get("mongo.user"), Props.get("mongo.password")) match {
          case (Full(user), Full(password)) => {
            MongoDB.defineDbAuth(DefaultMongoIdentifier, mongoAddress, user, password)
          }
          case _ => {
            MongoDB.defineDb(DefaultMongoIdentifier, mongoAddress)
          }
        }
      }
      case _ => {
        info("MongoDB settings are missing... default is localhost:27017:risktx")
        MongoDB.defineDb(DefaultMongoIdentifier, mongoAddress)
      }
    }
    
  }

  /**
   * Configure email functionality from properties file
   */
  def configMailer() {

    Props.get("smtp.host") match {
      case Full(host) => {
        System.setProperty("mail.smtp.host", host)
        info("SMTP host set to " + host)

        Props.get("smtp.enabletls") match {
          case Full(tls) => {
            System.setProperty("mail.smtp.starttls.enable", "true")
            info("SMTP TLS is enabled")
          }
          case _ => //do nothing
        }

        (Props.get("smtp.user"), Props.get("smtp.password")) match {
          case (Full(user), Full(password)) => {
            System.setProperty("mail.smtp.auth", "true")
            Mailer.authenticator = Full(new Authenticator {
              override def getPasswordAuthentication = new PasswordAuthentication(user, password)
            })
            info("SMTP authentication enabled, user set to " + user)
          }
          case _ => //do nothing
        }
      }
      case _ => info("SMTP settings are missing, email notifications are disabled")
    }
  }


}
