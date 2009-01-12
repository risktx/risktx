package org.risktx.webservices.axis;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.rpc.receivers.RPCMessageReceiver;

public class RisktxRPCMessageReceiver extends RPCMessageReceiver {

    public void invokeBusinessLogic(MessageContext inMessage, MessageContext outMessage)
            throws AxisFault {
        super.invokeBusinessLogic(inMessage, outMessage);

        //Get the first element of the response, which is the automatically generated PostRqResponse element
        OMElement elementWrapped = outMessage.getEnvelope().getBody().getFirstElement();
        //Detach it from the OMBody object
        elementWrapped.detach();
        //Get the start of our response
        OMElement elementActual = elementWrapped.getFirstElement().getFirstElement();
        //Add the start of our response to the OMBody object
        outMessage.getEnvelope().getBody().addChild(elementActual);
    }
}
