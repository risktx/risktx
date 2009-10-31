package org.risktx.service

import net.liftweb._
import net.liftweb.util.Log

import org.risktx.model.Message
import org.risktx.template.Requester

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

/**
* Implementation of the Acord Message Sender
**/
object Sender {
  // TODO: Check these comments
  /**
  * Handles the sending of a message
  *
  * @param  message The request we have received or a blank message
  **/
  def send(m: Message): Unit = {

    // generate the request
    Requester.createRq(m)
    
    // Client.getEndpoint
    // set the endpoint URL for the recipient system
    Log.info("setting endpoint as " + m.url)
    val targetEPR = new EndpointReference(m.url)

    // Client.getAxisOptions
    // create the Axis client options
    val options  = new Options()
    options.setProperty(Constants.Configuration.ENABLE_SWA, Constants.VALUE_TRUE)
    options.setSoapVersionURI(SOAP11Constants.SOAP_ENVELOPE_NAMESPACE_URI)
    options.setTimeOutInMilliSeconds(10000)
    options.setTo(targetEPR)
    options.setAction("http://www.ACORD.org/Standards/AcordMsgSvc/Ping#PingRq")

    // Log the target endpoint
    Log.info("sending to: " + m.url)
    
    // Client.getAxisConfigurationContext    
    // get the Axis context for sending the message
    val configContext = ConfigurationContextFactory.createDefaultConfigurationContext()
        
    // Client.getAxisClient(m: Message)    
    //create the sender client
    Log.info("creating sender client")
    val sender = new ServiceClient(configContext, null)
    sender.setOptions(options)

    // Client.getMessageContext(m: Message)
    val mepClient = sender.createClient(ServiceClient.ANON_OUT_IN_OP)
    val mc = new MessageContext()

    val fac = OMAbstractFactory.getSOAP11Factory()
       
    // Client.getMessageEnvelope(m: Message)
    val env = fac.getDefaultEnvelope()

    val postRqElement = AXIOMUtil.stringToOM(m.requestContent)
    env.getBody().addChild(postRqElement)
    mc.setEnvelope(env)

    mepClient.addMessageContext(mc)

    Log.info("sending message")
    mepClient.execute(true)
    val response = mepClient.getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE)
    
    // Set response content
    m.responseContent(response.getEnvelope().getFirstElement().getFirstElement().toString())
  }
}
