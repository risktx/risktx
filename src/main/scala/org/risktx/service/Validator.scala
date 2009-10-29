package org.risktx.service

import scala.xml._

import net.liftweb._
import net.liftweb.util.Log

import org.risktx.model.Message

/**
* Validates messages
**/
class Validator {
}

/**
* Implementation of message validator
**/
object Validator {
  /**
  * Validates messages
  * @param  message The request message we have received
  **/
  def validate(message: Message): Unit = {

    // validate the Message
    message.requestId((message.requestXml \ "PingId").text)
    message.senderParty((message.requestXml \ "Sender" \ "PartyId").text)
    message.senderPartyRole((message.requestXml \ "Sender" \ "PartyRoleCd").text)
    message.receiverParty((message.requestXml \ "Receiver" \ "PartyId").text)
    message.receiverPartyRole((message.requestXml \ "Receiver" \ "PartyRoleCd").text) 
  }
}

