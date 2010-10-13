package org.risktx

import domain.model.messaging._
import service._
import repository._

import org.specs._
import org.specs.runner.JUnit3
import org.specs.runner.ConsoleRunner
import org.specs.matcher._
import org.specs.specification._

class MessageDeliverySpecAsTest extends JUnit3(MessageDeliverySpec)
object MessageDeliverySpecRunner extends ConsoleRunner(MessageDeliverySpec)

object MessageDeliverySpec extends Specification {

  "The message delivery functionality" should {
    shareVariables

    var message: Message = null
    var profile: TradingProfile = null
    val instruction = OutboundPingRq()
    val messageDate = new java.util.Date()
    val messagePayload = <request />.toString

    "create Trading Profiles" in {
      profile = TradingProfile()
      profile must notBeNull
    }  

    "create Messages" in {
      message = Message(
        instruction,
        messagePayload,
        profile
      )
      message must notBeNull
      message.instruction must be_==(instruction)
      message.payload must be_==(messagePayload)
      message.profile.receiver.endpointUrl must notBeNull
    }

    "send Messages" in {
      DeliveryService.handleMessage(message)
      message.delivery.responsePayload must notBeNull
      message.delivery.responsePayload must notEqualIgnoreCase("")
    }
    
    "find a Message using it's ID" in {
    // ...when persistence is implemented
    }

  }

}
