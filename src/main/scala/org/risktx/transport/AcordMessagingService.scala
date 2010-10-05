package org.risktx.transport

import org.apache.axiom.attachments.Attachments
import org.apache.axiom.om.impl.llom.util.AXIOMUtil._
import org.apache.axiom.om.OMElement
import org.apache.axiom.soap.SOAPEnvelope
import org.apache.axis2.context.MessageContext

import org.risktx.domain.model.messaging.{Message,Instruction, TradingParty,Attachment,Delivery}
//import org.risktx.service.Receiver

import net.liftweb.util.Log

import javax.activation.DataHandler
import java.util.Date
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
  * Converts request SOAP object into a RiskTX Object
  **/
  def asyncRq(operation: String):OMElement = {
      
    //get the context and SOAP envelope (contains the inbound AMS message)
    val context = MessageContext.getCurrentMessageContext()
    val requestContent = context.getEnvelope().getFirstElement().getFirstElement()

    //create a Message, set some values, currently ignore the attachments...
//    val message = Message.create.dateOf(new Date()).operation(operation).direction("in").status(1).requestContent(requestContent.toString())
    
    //save all the attachments related to the Message...
//    val attachments = getAttachments(context.getAttachmentMap, message)

    // bundle up the message and attached files
//    val bundle = MessageBundle(message, attachments)

    //process the Message
    //Receiver.receive(bundle)
    
    //create the SOAP response from the processed Message (build an Axiom object graph)
//    stringToOM(bundle.message.responseContent)
      stringToOM("test")
  }

/*  
  def getAttachments(soapAttachments: org.apache.axiom.attachments.Attachments,
                      message: Message): List[org.risktx.model.Attachment] = {

    val contentIds = soapAttachments.getAllContentIDs
    val attachments: List[org.risktx.model.Attachment] = Nil

    val all = for(i <- (0 until contentIds.size).force) yield createAttachment(message, soapAttachments, contentIds(i))

    all.toList
  }

  def createAttachment(message: org.risktx.model.Message,
                       soapAttachments: org.apache.axiom.attachments.Attachments,
                       contentId: String) = {

    info("processing attachment with id: " + contentId)

    val dataHandler = soapAttachments.getDataHandler(contentId)
    val data = new ByteArrayOutputStream
    dataHandler.writeTo(data)

    // TODO: review attachments approach, Lift Mapper doesn't support streaming so this is all in-memory
    Attachment.create.attachmentId(contentId).contentType(dataHandler.getContentType).content(data.toByteArray).message(data.toByteArray)
  }
*/
}
