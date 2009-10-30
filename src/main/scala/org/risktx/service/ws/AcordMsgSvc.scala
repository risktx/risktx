package org.risktx.service.ws

import org.apache.axiom.attachments.Attachments
import org.apache.axiom.om.impl.llom.util.AXIOMUtil._
import org.apache.axiom.om.OMElement
import org.apache.axiom.soap.SOAPEnvelope
import org.apache.axis2.context.MessageContext

import org.risktx.model.Attachment
import org.risktx.model.Message
import org.risktx.service.Receiver

import net.liftweb.util.Log

import javax.activation.DataHandler
import java.util.Date
import java.io.ByteArrayOutputStream

class AcordMsgSvc {

  def PingRq():OMElement = {
    asyncRq("PingRq")
  }
  
  def PostRq():OMElement = {
    asyncRq("PostRq")
  }  
  
  def asyncRq(operation: String):OMElement = {
    //get the context and SOAP envelope (contains the inbound AMS message)
    val context = MessageContext.getCurrentMessageContext()
    val requestContent = context.getEnvelope().getFirstElement().getFirstElement()

    //create a Message, set some values, currently ignore the attachments...
    val message = Message.create.dateOf(new Date()).operation(operation).direction("in").status(1).requestContent(requestContent.toString()).saveMe
    
    //save all the attachments related to the Message...
    saveAttachments(context.getAttachmentMap, message)
    
    //process the Message
    Receiver.receive(message)
    
    //create the SOAP response from the processed Message (build an Axiom object graph)
    stringToOM(message.responseContent)
  }
  
  def saveAttachments(attachments: Attachments, message: Message): Unit = {
    val contentIds = attachments.getAllContentIDs
    for(i <- 0 until contentIds.size) {
      val id = contentIds(i)
      Log.info("processing attachment with id: " + id)
      
      val dataHandler = attachments.getDataHandler(id)
      val data = new ByteArrayOutputStream
      dataHandler.writeTo(data)      
      
      val attachment = Attachment.create.attachmentId(id).contentType(dataHandler.getContentType).content(data.toByteArray).message(message).saveMe
      
      Log.info("saved attachment with id: " + attachment.id + " & contentType: " + attachment.content.size)
    }
    
  }
}
