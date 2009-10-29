package org.risktx.service.ws

import org.apache.axiom.om.impl.llom.util.AXIOMUtil._
import org.apache.axiom.om.OMElement
import org.apache.axiom.soap.SOAPEnvelope
import org.apache.axis2.context.MessageContext

//import net.liftweb._

import org.risktx.model.Message
import org.risktx.service.Receiver

import java.util.Date

/**
* Encapsulates an Acord messgage
**/
class AcordMsgSvc {

  /**
  * Handle Ping requests
  **/
  def PingRq():OMElement = {
    AsyncRq()
  }

  /**
  * Handle Post requests
  **/
  def PostRq():OMElement = {
    AsyncRq()
  }  

  /**
  * Converts request SOAP object into a RiskTX Object
  **/
  def AsyncRq():OMElement = {
      
    //get the context and SOAP envelope (contains the inbound AMS message)
    val context = MessageContext.getCurrentMessageContext()
    val requestContent = context.getEnvelope().getFirstElement().getFirstElement()

    //create a Message, set some values, currently ignore the attachments...
    val m = Message.create.dateOf(new Date()).direction("in").status(1).requestContent(requestContent.toString())

    //process the Message
    Receiver.receive(m)
    
    //create the SOAP response from the processed Message (build an Axiom object graph)
    stringToOM(m.responseContent)
  }
}
