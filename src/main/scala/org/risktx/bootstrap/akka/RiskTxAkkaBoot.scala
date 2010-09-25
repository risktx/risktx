package org.risktx.bootstrap.akka

import _root_.net.liftweb.common._
import se.scalablesolutions.akka.actor.{SupervisorFactory, Actor}
import se.scalablesolutions.akka.config.ScalaConfig._
import se.scalablesolutions.akka.util.Logging
import org.risktx.service.{SimpleService}

/**
 * Akka bootstrap class for Risktx, called from the akka listener via akka.conf
 */
class RiskTxAkkaBoot extends Logger {
  def boot {
    org.apache.log4j.BasicConfigurator.configure
    
    info("Configuring Akka for Risktx")

    val factory = SupervisorFactory(
      SupervisorConfig(
        RestartStrategy(OneForOne, 3, 100, List(classOf[Exception])),
        Supervise(new SimpleService,LifeCycle(Permanent)) ::Nil))

    info("Starting Akka Factory")
    factory.newInstance.start
    info("Akka Factory Started")
  }
}