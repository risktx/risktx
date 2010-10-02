package org.risktx.domain.model.messaging

import java.util.Date

abstract case class Delivery private(
  val requestId: String,
  val createdDate: Date,
  var responsePayload: String,
  val deliveryStatus: Int  //TODO: enum or case class?
)

object Delivery {
  def apply(requestId: String, createdDate: Date, responsePayload: String, deliveryStatus: Int) = {

    require(!(requestId == null || requestId.equals("")), "requestId cannot be null or an empty String")
    require(createdDate != null, "createdDate cannot be null")

    new Delivery(requestId, createdDate, responsePayload, deliveryStatus) {}
  }
}