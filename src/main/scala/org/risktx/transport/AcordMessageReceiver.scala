package org.risktx.transport


import org.apache.axiom.om.OMElement
import org.apache.axis2.AxisFault
import org.apache.axis2.context.MessageContext
import org.apache.axis2.rpc.receivers.RPCMessageReceiver

import net.liftweb.util.Log

/**
* Message handler
**/
class AcordMessageReceiver extends RPCMessageReceiver {
    //TODO: check these comments
    /**
    * Invoke the business logic defined in the Axis configuration and
    * then tidy up the outbound message
    *
    * @param inMessage  Inbound Message
    * @param outMessage Outbound Message
    **/
    override def invokeBusinessLogic(inMessage:MessageContext, outMessage:MessageContext): Unit = {
        super.invokeBusinessLogic(inMessage, outMessage)

        //Get the first element of the response, which is the automatically generated PostRqResponse element
        val elementWrapped = outMessage.getEnvelope().getBody().getFirstElement()

        //Detach it from the OMBody object
        elementWrapped.detach()

        //Get the start of our response
        val elementActual = elementWrapped.getFirstElement().getFirstElement()

        //Add the start of our response to the OMBody object
        outMessage.getEnvelope().getBody().addChild(elementActual)
    }
}