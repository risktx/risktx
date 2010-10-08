package org.risktx.service

//import se.scalablesolutions.akka.actor.SupervisorFactory
import se.scalablesolutions.akka.config.ScalaConfig._
import se.scalablesolutions.akka.actor.Actor._
import se.scalablesolutions.akka.actor.{TypedActor, Supervisor}
import se.scalablesolutions.akka.security.{DigestAuthenticationActor, UserInfo, BasicAuthenticationActor, BasicCredentials}

class AkkaBoot {
//  val factory = SupervisorFactory(
//    SupervisorConfig(
//      RestartStrategy(OneForOne, 3, 100, List(classOf[Exception])),
//      Supervise(
//        new BasicAuthenticationService,
//        LifeCycle(Permanent)) ::
//      Supervise(
//        new SecureTickActor,
//        LifeCycle(Permanent)):: Nil))
//
//  val supervisor = factory.newInstance
//  supervisor.start      // Removed as it seems to break start-up in an app server

  val supervisor = Supervisor(SupervisorConfig(
                RestartStrategy(OneForOne, 3, 100, List(classOf[Exception])),
                Supervise(actorOf(new SecureTickActor), LifeCycle(Permanent)) :: Nil))
}

/*
* In akka.conf you can set the FQN of any AuthenticationActor of your wish, under the property name: akka.rest.authenticator
*/
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