package org.risktx.service

import net.liftweb._

import org.risktx.model.Message
import org.risktx.template.Response

/**
* Acord Message Receiver
**/
class Receiver {
}

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
    Response.createPingRs(message)
    
    // save the Message
    message.save
  }
}
