package org.risktx.service

import net.liftweb._

import org.risktx.model.Message
import org.risktx.template.Response

class Receiver {

}

object Receiver {

  def receive(message: Message): Unit = {

    // validate the Message
    Validator.validate(message)
        
    // generate the Message response
    Response.createPingRs(message)
    
    // save the Message
    message.save
    
  }
}
