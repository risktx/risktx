package org.risktx.service

import org.risktx.domain.model.messaging._

object DeliveryService {

  def createDelivery(message: Message):Delivery = {
    Delivery("", new java.util.Date, "", 1)
  }

}