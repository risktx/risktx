package org.risktx.domain.model.messaging

abstract case class TradingParty protected(
  val partyId: String,
  val partyRole: String,
  val endpointUrl: String
)

object TradingParty {
  def apply(partyId: String, partyRole: String, endpointUrl: String) = {

    require(!(partyId == null || partyId.equals("")), "partyId cannot be null or an empty String")
    require(!(partyRole == null || partyRole.equals("")), "partyRole cannot be null or an empty String")
    require(!(endpointUrl == null || endpointUrl.equals("")), "endpointUrl cannot be null or an empty String")

    new TradingParty(partyId, partyRole, endpointUrl) {}
  }
}