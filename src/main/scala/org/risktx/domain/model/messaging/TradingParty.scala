package org.risktx.domain.model.messaging

abstract case class TradingParty protected(
  val partyId: String,
  val partyName: String,
  val partyRole: String,
  val endpointUrl: String
)

object TradingParty {
  def apply(partyId: String, partyName: String, partyRole: String, endpointUrl: String) = {

    require(!(partyId == null || partyId.isEmpty), "partyId cannot be null or an empty String")
    require(!(partyName == null || partyName.isEmpty), "partyRole cannot be null or an empty String")
    require(!(partyRole == null || partyRole.isEmpty), "partyRole cannot be null or an empty String")
    require(!(endpointUrl == null || endpointUrl.isEmpty), "endpointUrl cannot be null or an empty String")

    new TradingParty(partyId, partyName, partyRole, endpointUrl) {}
  }
}