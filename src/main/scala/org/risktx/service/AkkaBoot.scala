package org.risktx.service

import se.scalablesolutions.akka.util.Logging
import javax.ws.rs.{GET, Path, Produces}
import se.scalablesolutions.akka.actor.{ActorRegistry, SupervisorFactory, Actor, Transactor}
import se.scalablesolutions.akka.actor.Actor._
import se.scalablesolutions.akka.actor._
import se.scalablesolutions.akka.config.ScalaConfig._
import se.scalablesolutions.akka.security.{BasicAuthenticationActor,BasicCredentials, UserInfo}
import se.scalablesolutions.akka.stm.TransactionalMap
import java.lang.Integer
import javax.annotation.security.{RolesAllowed, DenyAll, PermitAll}

class Boot {
  val factory = SupervisorFactory(
    SupervisorConfig(
      RestartStrategy(OneForOne, 3, 100, List(classOf[Exception])),
      Supervise(Actor.actorOf[SecureTickActor], LifeCycle(Permanent)) ::
      Supervise(Actor.actorOf[BasicAuthenticationService], LifeCycle(Permanent)) :: Nil))

  factory.newInstance.start
}

class BasicAuthenticationService extends BasicAuthenticationActor {

  //Change this to whatever you want
  override def realm = "test"

  //Dummy method that allows you to log on with whatever username
  def verify(odc: Option[BasicCredentials]): Option[UserInfo] = odc match {
    case Some(dc) => userInfo(dc.username)
    case _ => None
  }

  //Dummy method that allows you to log on with whatever username with the password "bar"
  def userInfo(username: String): Option[UserInfo] = Some(UserInfo(username, "bar", "ninja" :: "chef" :: Nil))
}

@Path("/secureticker")
class SecureTickService {

  @GET
  @Produces(Array("text/xml"))
  @Path("/public")
  @PermitAll
  def publicTick = tick

  @GET
  @Path("/chef")
  @Produces(Array("text/xml"))
  @RolesAllowed(Array("chef"))
  def chefTick = tick

  @GET
  @Produces(Array("text/xml"))
  @DenyAll
  def paranoiaTick = tick

  def tick = {
    val myActor = ActorRegistry.actorFor[SecureTickActor].get

    val result = (myActor !! "Tick").as[Integer]

    //Return either the resulting NodeSeq or a default one
    result match {
      case (Some(counter)) => (<success>Tick: {counter}</success>)
      case _ => (<error>Error in counter</error>)
    }
  }
}

class SecureTickActor extends Transactor with Logging {
  private val KEY = "COUNTER"
  private var hasStartedTicking = false
  private lazy val storage = TransactionalMap[String, Integer]()

  def receive = {
    case "Tick" => if (hasStartedTicking) {
      val counter = storage.get(KEY).get.intValue
      storage.put(KEY, counter + 1)
      self.reply(new Integer(counter + 1))
    } else {
      storage.put(KEY, 0)
      hasStartedTicking = true
      self.reply(new Integer(0))
    }
  }
}