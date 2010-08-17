package org.risktx.service

import org.apache.axiom.attachments.Attachments
import org.apache.axiom.om.impl.llom.util.AXIOMUtil._
import org.apache.axiom.om.OMElement
import org.apache.axiom.soap.SOAPEnvelope
import org.apache.axis2.context.MessageContext

import org.risktx.domain.model.messaging._

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
    handleMessage(InboundPingRq())
  }

  /**
  * Handle Post requests
  **/
  def PostRq():OMElement = {
    handleMessage(InboundPostRq())
  }  

  /**
  * Receives the Axis2 message and returns the response
  **/
  def handleMessage(instruction: Instruction):OMElement = {
    //get the context and SOAP envelope (contains the inbound AMS message)
    val context = MessageContext.getCurrentMessageContext()
    val requestContent = context.getEnvelope().getFirstElement().getFirstElement()

    val message = Message("", new java.util.Date(), requestContent.toString())
    val delivery = DeliveryService.createDelivery(message)

    stringToOM(delivery.responsePayload)
  }

}
