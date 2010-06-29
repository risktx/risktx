package org.risktx.transport

import net.liftweb.mongodb._
import com.mongodb.gridfs._

import org.apache.axiom.attachments.Attachments
import org.apache.axiom.om.impl.llom.util.AXIOMUtil._
import org.apache.axiom.om.OMElement
import org.apache.axiom.soap.SOAPEnvelope
import org.apache.axis2.context.MessageContext

import org.risktx.model.Message
import org.risktx.service.Receiver

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
  * Converts request SOAP object into a RiskTX Object
  **/
  def asyncRq(operation: String):OMElement = {
    //get the context and SOAP envelope (contains the inbound AMS message)
    val context = MessageContext.getCurrentMessageContext()
    val requestContent = context.getEnvelope().getFirstElement().getFirstElement()

    //create a Message, set some values, currently ignore the attachments...
    val message = Message.createRecord
    message.dateOf(Calendar.getInstance).operation(operation).direction("in").status(1).requestContent(requestContent.toString())
    message.save

    //save all the attachments related to the Message...
    saveAttachments(context.getAttachmentMap)

    //process the Message
    Receiver.receive(message)

    //create the SOAP response from the processed Message (build an Axiom object graph)
    stringToOM(message.responseContent.value)
  }

  def saveAttachments(soapAttachments: org.apache.axiom.attachments.Attachments):Unit = {

    val contentIds = soapAttachments.getAllContentIDs

    val allIds = for(i <- (0 until contentIds.size).force) yield createAttachment(soapAttachments, contentIds(i))

    allIds
    
    //do something useful with the GridFS ids... maybe add them to the message document
  }

  def createAttachment(soapAttachments: org.apache.axiom.attachments.Attachments, contentId: String) = {

    Log.info("processing attachment with id: " + contentId)

    val dataHandler = soapAttachments.getDataHandler(contentId)
    val inputStream = dataHandler.getInputStream

    MongoDB.use(DefaultMongoIdentifier) ( db => {
      val fs = new GridFS(db)
      val inputFile = fs.createFile(inputStream, dataHandler.getContentType)
      inputFile.setContentType(dataHandler.getContentType)
      inputFile.save

      inputFile.getId
    })


  }

}
