package org.risktx.transport

import org.apache.axiom.attachments.Attachments
import org.apache.axiom.om.impl.llom.util.AXIOMUtil._
import org.apache.axiom.om.OMElement
import org.apache.axiom.soap.SOAPEnvelope
import org.apache.axis2.context.MessageContext

import net.liftweb.util.Log

import javax.activation.DataHandler
import java.io.File
import java.util.Date
import java.util.Calendar
import java.io.ByteArrayOutputStream

/**
 * The ACORD Message Service class provides all AMS operation endpoints. It's responsible for
 * processing all attachments in the message
**/
class AcordMessagingService {

  /**
  * Handle Ping requests
  **/
  def PingRq():OMElement = {
    asyncRq("PingRq")
  }

  /**
  * Handle Post requests
  **/
  def PostRq():OMElement = {
    asyncRq("PostRq")
  }  

  /**
  * Converts request SOAP object into a RiskTx Object
  **/
  def asyncRq(operation: String):OMElement = {
    //get the context and SOAP envelope (contains the inbound AMS message)
    val context = MessageContext.getCurrentMessageContext()
    val requestContent = context.getEnvelope().getFirstElement().getFirstElement()

    //TODO: call an application level service to process the message and get a response
    stringToOM("")
  }

}
