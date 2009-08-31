package org.risktx.model

import net.liftweb.mapper._
import scala.xml._

class Message extends LongKeyedMapper[Message] with IdPK {
  def getSingleton = Message
  
  def requestXml():Elem = {
    if (cachedRequestXml == null) {
      cachedRequestXml = XML.loadString(requestContent)
    }
    cachedRequestXml 
  }
  
  def responseXml():Elem =  {  
    if (cachedResponseXml == null) {
      cachedResponseXml = XML.loadString(responseContent)
    }
    cachedResponseXml   
  }   

  object tradingProfile extends MappedLongForeignKey(this, TradingProfile) {
    override def dbIndexed_? = true
  }

  object requestId extends MappedString(this, 36)

  object responseId extends MappedString(this, 36)
  
  object dateOf extends MappedDateTime(this)
  
  object url extends MappedString(this, 256)
  
  object senderParty extends MappedString(this, 64)
  
  object senderPartyRole extends MappedString(this, 32)
  
  object receiverParty extends MappedString(this, 64)
  
  object receiverPartyRole extends MappedString(this, 32)
  
  object direction extends MappedString(this, 3)
  
  object status extends MappedInt(this)
  
  object statusDescription extends MappedText(this)
  
  object requestContent extends MappedText(this)
  private var cachedRequestXml : Elem = null
  
  object responseContent extends MappedText(this)
  private var cachedResponseXml : Elem = null
}

object Message extends Message with LongKeyedMetaMapper[Message] {
  def findByTradingProfile (tradingProfile : TradingProfile) : List[Message] = 
    Message.findAll(By(Message.tradingProfile, tradingProfile.id.is))
}
