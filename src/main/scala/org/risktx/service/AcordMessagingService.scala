package org.risktx.service

import org.apache.axiom.attachments.Attachments
import org.apache.axiom.om.impl.llom.util.AXIOMUtil._

import org.risktx.domain.model.messaging._
import org.risktx.repository.FileRepository

import net.liftweb._
import net.liftweb.common._

import org.apache.axiom.om.impl.builder.StAXOMBuilder
import org.apache.axiom.om.impl.llom.util.AXIOMUtil
import org.apache.axiom.om.OMAbstractFactory
import org.apache.axiom.om.OMElement
import org.apache.axiom.soap.SOAP11Constants
import org.apache.axiom.soap.SOAPEnvelope
import org.apache.axiom.soap.SOAPFactory
import org.apache.axis2.addressing.EndpointReference
import org.apache.axis2.client.OperationClient
import org.apache.axis2.client.Options
import org.apache.axis2.client.ServiceClient
import org.apache.axis2.Constants
import org.apache.axis2.context.ConfigurationContext
import org.apache.axis2.context.ConfigurationContextFactory
import org.apache.axis2.context.MessageContext
import org.apache.axis2.wsdl.WSDLConstants

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
    AcordMessagingService.receiveMessage(InboundPingRq())
  }

  /**
  * Handle Post requests
  **/
  def PostRq():OMElement = {
    AcordMessagingService.receiveMessage(InboundPostRq())
  }
}


object AcordMessagingService {

  private def saveAttachments(soapAttachments: org.apache.axiom.attachments.Attachments):Seq[Attachment] = {
    val contentIds = soapAttachments.getAllContentIDs
    val attachments = for(i <- (0 until contentIds.size)) //   .force    force needs replacing
      yield FileRepository.createAttachment(soapAttachments.getDataHandler(contentIds(i)), contentIds(i))
    attachments
  }

  /**
  * Receives the Axis2 message and returns the response
  **/
  def receiveMessage(instruction: Instruction):OMElement = {
    //get the context and SOAP envelope (contains the inbound AMS message)
    val context = MessageContext.getCurrentMessageContext()
    val requestContent = context.getEnvelope().getFirstElement().getFirstElement()

    val message = Message(
      instruction,
      requestContent.toString(),
      TradingParty("urn:something:sender", "A Sender", "Service Provider", "a url"),
      TradingParty("urn:something:receiver", "A Receiver", "Service Provider", "a url")
    )

    val profile = TradingProfile()

//    val attachments = Nil  // Temporary fix

    val attachments = saveAttachments(context.getAttachmentMap)
    attachments.foreach { attachment =>
      message.addAttachment(attachment)
    }

    DeliveryService.handleMessage(message, profile)

    stringToOM(message.delivery.responsePayload)
  }

  def sendMessage(message: Message, profile: TradingProfile) = {
    val targetEPR = new EndpointReference(message.receiver.endpointUrl)

    // create the Axis client options
    val options  = new Options()
    options.setProperty(Constants.Configuration.ENABLE_SWA, Constants.VALUE_TRUE)
    options.setSoapVersionURI(SOAP11Constants.SOAP_ENVELOPE_NAMESPACE_URI)
    options.setTimeOutInMilliSeconds(profile.outboundTimeoutMillis)
    options.setTo(targetEPR)
    options.setAction(profile.outboundSoapAction)

    // get the Axis context for sending the message
    val configContext = ConfigurationContextFactory.createDefaultConfigurationContext

    val sender = new ServiceClient(configContext, null)
    sender.setOptions(options)

    val mepClient = sender.createClient(ServiceClient.ANON_OUT_IN_OP)
    val mc = new MessageContext()

    val fac = OMAbstractFactory.getSOAP11Factory
    val env = fac.getDefaultEnvelope

    val postRqElement = AXIOMUtil.stringToOM(message.payload)
    env.getBody.addChild(postRqElement)
    mc.setEnvelope(env)

    mepClient.addMessageContext(mc)

    mepClient.execute(true)
    val response = mepClient.getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE)

    // Set response content
    message.delivery.responsePayload = response.getEnvelope.getFirstElement.getFirstElement.toString
  }
}