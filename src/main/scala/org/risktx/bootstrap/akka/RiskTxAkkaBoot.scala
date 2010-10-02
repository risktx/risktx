package org.risktx.bootstrap.akka

import se.scalablesolutions.akka.actor.{SupervisorFactory, Actor}
import se.scalablesolutions.akka.config.ScalaConfig.{SupervisorConfig, RestartStrategy, OneForOne,
                                                    Supervise, LifeCycle, Permanent}
import se.scalablesolutions.akka.util.{Logging}
import org.risktx.service.{SimpleService}

/**
 * Akka bootstrap class for Risktx, called from the akka listener via akka.conf
 */
class RiskTxAkkaBoot extends Logging {
  def boot {
    org.apache.log4j.BasicConfigurator.configure
    
    log.info("Configuring Akka for Risktx")

    val factory = SupervisorFactory(
      SupervisorConfig(
        RestartStrategy(OneForOne, 3, 100, List(classOf[Exception])),
        Supervise(new SimpleService,LifeCycle(Permanent)) ::Nil))

    log.info("Starting Akka Factory")
    factory.newInstance.start
    log.info("Akka Factory Started")
  }
}