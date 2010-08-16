package org.risktx.domain.model.messaging

abstract case class Instruction protected(
  val operation: String,
  val direction: String
)

case class InboundPingRq() extends Instruction("PingRq", "inbound")
case class InboundPostRq() extends Instruction("PostRq", "inbound")

object Instruction {
  def apply(operation: String, direction: String) = {

    require(!(operation == null || operation.equals("")), "operation cannot be null or an empty String")

    new Instruction(operation, direction) {}
  }
}