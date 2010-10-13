package org.risktx.domain.model.messaging

abstract case class TradingProfile protected(
  val sender: TradingParty,
  val receiver: TradingParty,
  val outboundSoapAction: String,
  val outboundTimeoutMillis: Int
)

object TradingProfile {
  def apply() = {
    new TradingProfile(
      TradingParty("urn:something:sender", "A Sender", "Service Provider", "http://localhost:8080/services/ams"),
      TradingParty("urn:something:receiver", "A Receiver", "Service Provider", "http://localhost:8080/services/ams"),
      "http://www.ACORD.org/Standards/AcordMsgSvc/Ping#PingRq",
      10000) {}
  }
}