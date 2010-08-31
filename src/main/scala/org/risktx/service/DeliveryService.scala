package org.risktx.service

import java.util.UUID._
import org.risktx.domain.model.messaging._

object DeliveryService {

  private def createDelivery(message: Message):Delivery = {
    Delivery(randomUUID().toString, new java.util.Date, "", 1)
  }

  def handleMessage(message: Message, profile: TradingProfile) {
    //add a delivery
    message.deliveries = message.deliveries ::: List(createDelivery(message))
    
    message match {
      case Message(InboundPostRq(), _, _, _, _, _) => // message ! inbound-actor
      case Message(InboundPingRq(), _, _, _, _, _) => message.delivery.responsePayload = <response />.toString // message ! inbound-actor
      case Message(OutboundPostRq(), _, _, _, _, _) => // message ! outbound-actor
      case Message(OutboundPingRq(), _, _, _, _, _) => AcordMessagingService.sendMessage(message, profile)
    }
  }

}