package org.risktx.service

import org.risktx.template.Responder
import org.risktx.model.Message

/**
 * Implementation of Acord Message Receiver.
**/
object Receiver {

  /**
  * Receives message by validating, generating a response, then persisting
  * the message and attachments.
  * @param message The request message received
  * @param attachments A List of Attachments for the message
  **/
  def receive(message: Message) {
    // validate message and set response
    message.responseContent(processRequest(message))
    // persist message
    message.save
  }

  /**
  *
  * @param  message The request message we have received
  **/
  private def processRequest(message: Message): String = {
    // validate the Message and generate an appropriate response
    Validator.validate(message) match {
      case ValidationSuccess() =>
        Responder.createSuccessResponse(message)
      case ValidationFailure(description) =>
        Responder.createFailureResponse(message, description)
    }
  }
}
