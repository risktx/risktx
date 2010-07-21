package org.risktx.domain.model.messaging

import java.util.Date

abstract case class Message private(
  val requestId: String,
  val createdDate: Date,
  val payload: String
)

object Message {
  def apply(requestId: String, createdDate: Date, payload: String) = {
    
    require(!(requestId == null || requestId.equals("")), "requestId cannot be null or an empty String")
    require(createdDate != null, "createdDate cannot be null")
    require(!(payload == null || payload.equals("")), "payload cannot be null or an empty String")
    
    new Message(requestId, createdDate, payload) {}
  }
}