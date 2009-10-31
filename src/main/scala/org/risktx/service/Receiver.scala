package org.risktx.service

import net.liftweb._
import net.liftweb.util.Log

import org.risktx.model.Message
import org.risktx.template.Responder

/**
* Implementation of Acord Message Receiver
**/
object Receiver {

  /**
  * Handles the receiving of a message
  * @param  message The request message we have received
  **/
  def receive(message: Message): Unit = {
    
    // validate the Message
    Validator.validate(message)
        
    // generate the Message response
    Responder.createRs(message)
    
    // save the Message
    message.save
  }
}
