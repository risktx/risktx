package org.risktx.snippet

import _root_.scala.xml.{NodeSeq,Text,Node,Elem}
import _root_.net.liftweb.util.{Box,Full,Empty,Helpers,Log}
import _root_.net.liftweb.util.Helpers._
import _root_.net.liftweb.http.{S,SHtml,StatefulSnippet}

import org.risktx.model.Message
import org.risktx.service.Sender

import java.util.Date

class PingMessageSnippet extends StatefulSnippet {

  //default the uri to point at the local web server
  var url: String = (S.hostAndPath + "/services/ams")
  var senderParty: String = "urn:duns:000000000"
  var senderPartyRole: String = "ServiceProvider"
  var receiverParty: String = "urn:lloyds:0000"
  var receiverPartyRole: String = "Broker"
  
  var m: Message = Message.create
  
  val dispatch: DispatchIt = {
    case "send" => send _
  }
  
  //def gif (m != null) m.responseContent else ""
  
  def send(xhtml: NodeSeq): NodeSeq = {  
    def doSend () = {
      //create a Message, set some values, currently ignore the attachments...
      m = Message.create.dateOf(new Date()).direction("out").status(1).url(url).senderParty(senderParty).senderPartyRole(senderPartyRole).receiverParty(receiverParty).receiverPartyRole(receiverPartyRole)
      
      Sender.send(m)
    }

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
