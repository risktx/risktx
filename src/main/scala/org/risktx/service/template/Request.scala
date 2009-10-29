package org.risktx.template

import org.risktx.model.Message
import java.util.UUID._
import java.util.GregorianCalendar
import javax.xml.datatype.DatatypeFactory

/**
* Acord Ping Request message
**/
class Request {
}

/**
* Acord Ping Request message
**/
object Request {

  // TODO: Check the scaladoc entry here!
  /**
  * Create an Acord Ping request
  * @param message ??Blank Message
  **/
  def createPingRq(message: Message): Unit = {
    // Set the message ID
    message.requestId(randomUUID().toString())
  
    // Get the date of the request and format the date
    val cal = new GregorianCalendar()
    cal.setTime(message.dateOf)

    // Create timestamp for the message
    val xmlDateOf = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal).toString()    
    
    // generate the request
    val pingRq = 
          <ac:PingRq xmlns="http://www.ACORD.org/Standards/AcordMsgSvc/Ping" xmlns:ac="http://www.ACORD.org/Standards/AcordMsgSvc/1.4.0">
            <ac:Sender>
              <ac:PartyId>{ message.senderParty }</ac:PartyId>
              <ac:PartyRoleCd>{ message.senderPartyRole }</ac:PartyRoleCd>
            </ac:Sender>
            <ac:Receiver>
              <ac:PartyId>{ message.receiverParty }</ac:PartyId>
              <ac:PartyRoleCd>{ message.receiverPartyRole }</ac:PartyRoleCd>
            </ac:Receiver>
            <ac:Application>
              <ac:ApplicationCd>Jv-Ins-Reinsurance</ac:ApplicationCd>
              <ac:SchemaVersion>http://www.ACORD.org/Standards/Jv-Ins-Reinsurance/2003-1</ac:SchemaVersion>
            </ac:Application>
            <ac:TimeStamp>{ xmlDateOf }</ac:TimeStamp>
            <ac:PingId>{ message.requestId }</ac:PingId>
            <ac:SecurityProfileCd>Basic</ac:SecurityProfileCd>
          </ac:PingRq>
          
    // return the request
    message.requestContent(pingRq.toString())
  }
}
