package org.risktx.domain.model.messaging

abstract case class TradingProfile protected(
  outboundSoapAction: String,
  outboundTimeoutMillis: Int
)

object TradingProfile {
  def apply() = {
    new TradingProfile("http://www.ACORD.org/Standards/AcordMsgSvc/Ping#PingRq", 1000) {}
  }
}