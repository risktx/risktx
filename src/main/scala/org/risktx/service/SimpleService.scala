package org.risktx.service

import se.scalablesolutions.akka.actor.{Transactor, Actor}
import se.scalablesolutions.akka.config.ScalaConfig._
//import se.scalablesolutions.akka.stm.TransactionalState
import se.scalablesolutions.akka.stm.TransactionalMap

import java.lang.Integer
import javax.ws.rs.{GET, Path, Produces}
//import java.nio.ByteBuffer

/**
 * Try service out by invoking (multiple times):
 * <pre>
 * curl http://localhost:9998/liftcount
 * </pre>
 * Or browse to the URL from a web browser.
 */
@Path("/liftcount")
class SimpleService extends Transactor {
  case object Tick
  private val KEY = "COUNTER"
  private var hasStartedTicking = false
//  private lazy val storage = TransactionalState.newMap[String, Integer]
  private lazy val storage = new TransactionalMap [String, Integer]

  @GET
  @Produces(Array("text/html"))
  def count = (this !! Tick).getOrElse(<h1>Error in counter</h1>)

  def receive = {
    case Tick => if (hasStartedTicking) {
      val counter = storage.get(KEY).get.asInstanceOf[Integer].intValue
      storage.put(KEY, new Integer(counter + 1))
      reply(<h1>Tick: {counter + 1}</h1>)
    } else {
      storage.put(KEY, new Integer(0))
      hasStartedTicking = true
      reply(<h1>Tick: 0</h1>)
    }
  }
}