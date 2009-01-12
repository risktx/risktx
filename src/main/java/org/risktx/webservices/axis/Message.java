package org.risktx.webservices.axis;

import javax.activation.DataHandler;
import org.apache.axiom.attachments.Attachments;
import org.apache.axiom.om.util.UUIDGenerator;

//Used to pass message into Mule from Axis
public class Message {

    private Attachments attachments;
    private String businessMessage;
    private String soapEnvelopeIn;
    private String soapEnvelopeOut;

    public void setAttachmentMap(Attachments attachments) {
        this.attachments = attachments;
    }

    public Attachments getAttachmentMap() {
        if (attachments == null) {
            attachments = new Attachments();
        }
        return attachments;
    }

    public void addAttachment(String contentID, DataHandler dataHandler) {
        if (attachments == null) {
            attachments = new Attachments();
        }
        attachments.addDataHandler(contentID, dataHandler);
    }

    public String addAttachment(DataHandler dataHandler) {
        String contentID = UUIDGenerator.getUUID();
        addAttachment(contentID, dataHandler);
        return contentID;
    }

    public DataHandler getAttachment(String contentID) {
        if (attachments == null) {
            attachments = new Attachments();
        }
        return attachments.getDataHandler(contentID);
    }

    public void removeAttachment(String contentID) {
        if (attachments != null) {
            attachments.removeDataHandler(contentID);
        }
    }

    public void setBusinessMessage(String message) {
        this.businessMessage = message;
    }
    
    public String getBusinessMessage() {
        return this.businessMessage;
    }

    public void setSoapEnvelopeIn(String envelope) {
        this.soapEnvelopeIn = envelope;
    }

    public String getSoapEnvelopeIn() {
        return this.soapEnvelopeIn;
    }

    public void setSoapEnvelopeOut(String envelope) {
        this.soapEnvelopeOut = envelope;
    }

    public String getSoapEnvelopeOut() {
        return this.soapEnvelopeOut;
    }
}
