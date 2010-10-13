package org.risktx.domain.model.messaging

import java.util.Date

abstract case class Message private(
  val instruction: Instruction,
  val payload: String,
  val profile: TradingProfile,
  var attachments: List[Attachment],
  var deliveries: List[Delivery]) {

  def delivery():Delivery = {
    deliveries.last
  }

  def retries():List[Delivery] = {
    deliveries.tail
  }

  def addRetry(retry: Delivery) = {
    deliveries = deliveries ::: List(retry)
  }

  def addAttachment(attachment: Attachment) = {
    attachments = attachments ::: List(attachment)
  }
}

object Message {
  def apply(instruction: Instruction, payload: String, profile: TradingProfile) = {
    
    require(!(instruction == null), "instruction cannot be null")
    require(!(payload == null || payload.equals("")), "payload cannot be null or an empty String")
    require(!(profile == null), "trading profile cannot be null")

    new Message(instruction, payload, profile, Nil, Nil) {}
  }
}