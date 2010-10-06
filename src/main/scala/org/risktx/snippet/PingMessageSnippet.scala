package org.risktx.snippet

import scala.xml.{NodeSeq}
import net.liftweb._
import http.{S,SHtml,StatefulSnippet}
import util._
//import S._
//import SHtml._
import scala.xml._
import Helpers._
import net.liftweb.common.{Box,Full,Empty}
import org.risktx.domain.model.messaging.{Message, Instruction, TradingParty}
import net.liftweb.common.{Logger}

//import org.risktx.model.Message
//import org.risktx.service.Sender

import java.util.Date

/**
* User interface for sending an Acord Ping Message
**/
class PingMessageSnippet extends StatefulSnippet with Logger {

  /**
  * Target endpoint, default the uri to point at the local web server
  **/
  var senderUrl: String = (S.hostAndPath + "/services/ams")

  var receiverUrl: String = (S.hostAndPath + "/services/ams")

  var senderPartyName: String = "SenderOrg"

  /**
   *  Senders party code
   **/
  var senderPartyId: String = "urn:duns:000000000"

  /**
  * Senders role
  **/
  var senderPartyRole: String = "ServiceProvider"
  
  var receiverPartyName: String = "ReceiverOrg"

  /**
  * Receivers party code
  **/
  var receiverPartyId: String = "urn:lloyds:0000"

  /**
  * Receivers role
  **/
  var receiverPartyRole: String = "Broker"

  /**
  * The message we are going to send
  **/
//  var m: Message = Message.create

  /**
  * Dispatcher, matches on send to sent the message
  **/
  val dispatch: DispatchIt = {
    case "send" => send _
  }
  
  /**
  * Sends the message
  **/
  def send(xhtml: NodeSeq): NodeSeq = {  
    /**
    * Use the values entered by the user and send the message
    **/
    def doSend () = {
      info("in do send")

//      val senderTp = new TradingParty()

      //val msg = new Message()
      //create a Message, set some values, currently ignore the attachments...
//      m = Message.create.dateOf(new Date()).direction("out").status(1).url(url)
//            .senderParty(senderParty).senderPartyRole(senderPartyRole)
//            .receiverParty(receiverParty).receiverPartyRole(receiverPartyRole)

      // Send the message
//      Sender.send(m)
    }

    // Bind to the Lift UI
    bind("pingMessage", xhtml,
      "receiverUrl" -> SHtml.text(receiverUrl, receiverUrl = _, "class" -> "text"),
      "senderPartyId" -> SHtml.text(senderPartyId, senderPartyId = _, "class" -> "text"),
      "receiverPartyId" -> SHtml.text(receiverPartyId, receiverPartyId = _, "class" -> "text"),
      "senderPartyRole" -> SHtml.text(senderPartyRole, senderPartyRole = _, "class" -> "text"),
      "receiverPartyRole" -> SHtml.text(receiverPartyRole, receiverPartyRole = _, "class" -> "text"),    
      "submit" -> SHtml.submit("Send Message", () => doSend())  //,
//      "requestContent" -> m.requestContent.asHtml,
//      "responseContent" -> m.responseContent.asHtml
    )
  } 
}
