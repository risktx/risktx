package org.risktx.service

import net.liftweb._
import net.liftweb.util.Log

import org.risktx.model.Message
import org.risktx.template.Responder

object Receiver {

  def receive(message: Message): Unit = {
    
    // validate the Message
    Validator.validate(message)
        
    // generate the Message response
    Responder.createRs(message)
    
    // save the Message
    message.save
    
    // save the Attachments...
    
  }
}
