package org.risktx.service

import scala.xml._

import net.liftweb._
import net.liftweb.util.Log

import org.risktx.model.Message

abstract class ValidationResult
case class ValidationSuccess() extends ValidationResult
case class ValidationFailure(description: String) extends ValidationResult

/**
* Implementation of message validator
**/
object Validator {
  /**
  * Validate the message and extract/set key fields from the message XML
  * @param  message The request message we have received
  **/
  def validate(message: Message): ValidationResult = {

    val requestXml = XML.loadString(message.requestContent.value)

    //extract key fields
    message.requestId((requestXml \ "PingId").text)
    message.senderParty((requestXml \ "Sender" \ "PartyId").text)
    message.senderPartyRole((requestXml \ "Sender" \ "PartyRoleCd").text)
    message.receiverParty((requestXml \ "Receiver" \ "PartyId").text)
    message.receiverPartyRole((requestXml \ "Receiver" \ "PartyRoleCd").text)
    
    /* schema validation
         - find schemas in database for message type
         - run validation for each schema
         - ...fail on first error
    */

    /* schematron validation
         - find schematrons in database for message type
         - run validation for each schema
         - ...fail on first error
    */

    /* validate package integrity
         - check the business message id ties up
         - run through attachments and check all ids tie up
         - ...fail on first error
    */

    /* validate parties and roles
         - run through party aggregates in message, and check against 
         - ...fail on first error
    */

    return ValidationSuccess()

  }
}

