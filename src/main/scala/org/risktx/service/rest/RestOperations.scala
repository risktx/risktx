package org.risktx.service.rest

import _root_.net.liftweb._
import http._
import http.rest._
import common._
import util._
import _root_.org.risktx.model._

object RestOperations extends RestHelper {

  serve {
    case "api" :: "messages" :: _ XmlGet _ => 
      <messages>
        {
          Message.findAll.flatMap(message => {
            <message>
              <requestId>{ message.requestId.value }</requestId>
              <responseId>{ message.responseId.value }</responseId>
              <date>{ String.format("%1$tF %1$tT", message.dateOf.value) }</date>
              <senderParty>{ message.senderParty.value }</senderParty>
              <senderPartyRole>{ message.senderPartyRole.value }</senderPartyRole>
              <receiverParty>{ message.receiverParty.value }</receiverParty>
              <receiverPartyRole>{ message.receiverPartyRole.value }</receiverPartyRole>
              <requestContent>{ message.requestContent.value }</requestContent>
              <responseContent>{ message.responseContent.value }</responseContent>
            </message>
          })
        }
      </messages>
  }

}