package org.risktx.service

import java.util.UUID._
import org.risktx.domain.model.messaging._

object DeliveryService {

  private def createDelivery(message: Message):Delivery = {
    Delivery(randomUUID().toString, new java.util.Date, "", 1)
  }

  def handleMessage(message: Message) {
    //add a delivery
    message.deliveries = message.deliveries ::: List(createDelivery(message))

    //send to right actor for processing
    // validate
    // extract key fields
    // process labels
    // save via repository
    message match {
      case Message(InboundPostRq(), _, _, _, _, _) => // message ! inbound-actor
      case Message(InboundPingRq(), _, _, _, _, _) => // message ! inbound-actor
      case Message(OutboundPostRq(), _, _, _, _, _) => // message ! outbound-actor
      case Message(OutboundPingRq(), _, _, _, _, _) => // message ! outbound-actor
    }
    
    message.delivery.responsePayload = "<response-goes-here />"
  }

}