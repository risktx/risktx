package org.risktx.domain.model.messaging

import java.util.Date

abstract case class Message private(
  val instruction: Instruction,
  val payload: String,
  val sender: TradingParty,
  val receiver: TradingParty,
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
}

object Message {
  def apply(instruction: Instruction, payload: String, sender: TradingParty, receiver: TradingParty) = {
    
    require(!(instruction == null), "instruction cannot be null")
    require(!(payload == null || payload.equals("")), "payload cannot be null or an empty String")
    require(!(sender == null), "sender cannot be null")
    require(!(receiver == null), "receiver cannot be null")

    new Message(instruction, payload, sender, receiver, Nil, Nil) {}
  }
}