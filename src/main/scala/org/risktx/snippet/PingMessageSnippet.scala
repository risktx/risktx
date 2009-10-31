package org.risktx.snippet

import _root_.scala.xml.{NodeSeq,Text,Node,Elem}
import _root_.net.liftweb.util.{Box,Full,Empty,Helpers,Log}
import _root_.net.liftweb.util.Helpers._
import _root_.net.liftweb.http.{S,SHtml,StatefulSnippet}

import org.risktx.model.Message
import org.risktx.service.Sender

import java.util.Date

/**
* User interface for sending an Acord Ping Message
**/
class PingMessageSnippet extends StatefulSnippet {

  /**
  * Target endpoint, default the uri to point at the local web server
  **/
  var url: String = (S.hostAndPath + "/services/ams")

  /**
  * Senders party code
  **/
  var senderParty: String = "urn:duns:000000000"

  /**
  * Senders role
  **/
  var senderPartyRole: String = "ServiceProvider"

  /**
  * Receivers party code
  **/
  var receiverParty: String = "urn:lloyds:0000"

  /**
  * Receivers role
  **/
  var receiverPartyRole: String = "Broker"

  /**
  * The message we are going to send
  **/
  var m: Message = Message.create

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
      //create a Message, set some values, currently ignore the attachments...
      m = Message.create.dateOf(new Date()).direction("out").status(1).url(url)
            .senderParty(senderParty).senderPartyRole(senderPartyRole)
            .receiverParty(receiverParty).receiverPartyRole(receiverPartyRole)

      // Send the message
      Sender.send(m)
    }

    // Bind to the Lift UI
    bind("pingMessage", xhtml,
      "url" -> SHtml.text(url, url = _),
      "senderParty" -> SHtml.text(senderParty, senderParty = _),
      "receiverParty" -> SHtml.text(receiverParty, receiverParty = _),
      "senderPartyRole" -> SHtml.text(senderPartyRole, senderPartyRole = _),
      "receiverPartyRole" -> SHtml.text(receiverPartyRole, receiverPartyRole = _),    
      "submit" -> SHtml.submit("Send Message", () => doSend()),
      "requestContent" -> m.requestContent.asHtml,
      "responseContent" -> m.responseContent.asHtml
    )
  } 
}
