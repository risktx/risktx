package org.risktx.model

import net.liftweb.mapper._
import scala.xml._

/**
* Message entity which encapsulates a RiskTx message exchange
**/
class Message extends LongKeyedMapper[Message] with IdPK {
  def getSingleton = Message

  /**
  * Returns the XML Request and utilises caching
  **/
  def requestXml():Elem = {
    if (cachedRequestXml == null) {
      cachedRequestXml = XML.loadString(requestContent)
    }
    cachedRequestXml 
  }

  /**
  * Returns the XML Response and utilises caching
  **/
  def responseXml():Elem =  {  
    if (cachedResponseXml == null) {
      cachedResponseXml = XML.loadString(responseContent)
    }
    cachedResponseXml   
  }   

   // TODO: Is this the sender trading profile ?
  /**
  * A message has an associated Trading Profile
  **/
  object tradingProfile extends MappedLongForeignKey(this, TradingProfile) {
    override def dbIndexed_? = true
  }

  /**
  * Request Message ID in UUID format
  **/
  object requestId extends MappedString(this, 36)

  /**
  * Response Message ID in UUID format
  **/
  object responseId extends MappedString(this, 36)

  /**
  * Message Date
  **/
  object dateOf extends MappedDateTime(this)

  /**
  * Target url/end point
  **/
  object url extends MappedString(this, 256)

  /**
  * Message senders name
  **/
  object senderParty extends MappedString(this, 64)

  /**
  * Message Senders Role
  **/
  object senderPartyRole extends MappedString(this, 32)

  /**
  * Name of the message Receipient
  **/
  object receiverParty extends MappedString(this, 64)

  /**
  * Role of the Receiver
  **/
  object receiverPartyRole extends MappedString(this, 32)

  /**
  * Is this an Inbound or Outbound Message
  **/
  object direction extends MappedString(this, 3)

  /**
  * Status of the message
  **/
  object status extends MappedInt(this)

  object operation extends MappedString(this, 6)  
  
  /**
  * Description of the message status
  **/
  object statusDescription extends MappedText(this)

  /**
  * Implementation of the Request Object
  **/
  object requestContent extends MappedText(this)
    private var cachedRequestXml : Elem = null

  /**
  * Implementation of the Response Object
  **/
  object responseContent extends MappedText(this)
    private var cachedResponseXml : Elem = null
}

/**
* Message companion object with Helper methods
**/
object Message extends Message with LongKeyedMetaMapper[Message] with CRUDify[Long, Message] {
  /**
  * Helper method to find messages for a particular Trading Party
  *
  * @param tradingProfile TradingProfile of the Trading Party that you
  *                       wish to see messages for
  **/
  def findByTradingProfile (tradingProfile : TradingProfile) : List[Message] = 
    Message.findAll(By(Message.tradingProfile, tradingProfile.id.is))
}
