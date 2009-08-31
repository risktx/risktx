package org.risktx.template

import org.risktx.model.Message
import java.util.UUID._
import java.util.GregorianCalendar
import javax.xml.datatype.DatatypeFactory

class Response {

}

object Response {

  def createPingRs(message: Message): Unit = {

    message.responseId(randomUUID().toString())
  
    // format the date
    val cal = new GregorianCalendar()
    cal.setTime(message.dateOf)
    val xmlDateOf = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal).toString()

    // generate the response    
    val pingRs = 
        <ac:PingRs xmlns="http://www.ACORD.org/Standards/AcordMsgSvc/Ping" xmlns:ac="http://www.ACORD.org/Standards/AcordMsgSvc/1.4.0">
        <ac:Sender>
          <ac:PartyId>{ message.receiverParty }</ac:PartyId>
          <ac:PartyRoleCd>{ message.receiverPartyRole }</ac:PartyRoleCd>
        </ac:Sender>
        <ac:Receiver>
          <ac:PartyId>{ message.senderParty }</ac:PartyId>
          <ac:PartyRoleCd>{ message.senderPartyRole }</ac:PartyRoleCd>
        </ac:Receiver>
        <ac:Application>
          <ac:ApplicationCd>Jv-Ins-Reinsurance</ac:ApplicationCd>
          <ac:SchemaVersion>http://www.ACORD.org/Standards/Jv-Ins-Reinsurance/2003-1</ac:SchemaVersion>
        </ac:Application>
        <ac:TimeStamp>{ xmlDateOf }</ac:TimeStamp>
        <ac:PingId>{ message.responseId }</ac:PingId>
        <ac:SecurityProfileCd>Basic</ac:SecurityProfileCd>
      </ac:PingRs>
    
    // set the response
    message.responseContent(pingRs.toString())
    
  }
}
