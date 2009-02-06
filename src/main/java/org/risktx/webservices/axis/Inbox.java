package org.risktx.webservices.axis;

import javax.xml.stream.XMLStreamException;
import org.apache.axiom.om.impl.llom.util.AXIOMUtil;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axis2.context.MessageContext;

public class Inbox {

  public OMElement PostRq() throws XMLStreamException, Exception {
    //retrieve the context and SOAP envelope (contains the AMS message)
    MessageContext context = MessageContext.getCurrentMessageContext();
    SOAPEnvelope envelope = context.getEnvelope();

    //create the Message TO and set the attachment map and envelope
    Message message = new Message();
    message.setAttachmentMap(context.getAttachmentMap());
    message.setSoapEnvelopeIn(envelope.toString());

    /* //connect to Mule and pass in the message using the VM transport
    MuleClient client = new MuleClient();
    client.send("vm://risktx.receive.vm", message, null); */
    
    //create the SOAP response from the string returned by Mule (using the Axiom Object Model)
    return AXIOMUtil.stringToOM(message.getSoapEnvelopeOut());
  }
}
