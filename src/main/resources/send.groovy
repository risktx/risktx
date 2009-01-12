import groovy.xml.StreamingMarkupBuilder
import javax.activation.DataHandler
import javax.activation.URLDataSource
import java.net.URL
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

//parse the notification message and the Acord message to be sent
def notificationMessage = new XmlSlurper().parse(payload)
println "outbound payload received: " + notificationMessage.AcordMessageUrl
println "target endpoint is: " + notificationMessage.EndpointReference

println "parsing outbound message"
def acordMessage = new XmlSlurper().parse(notificationMessage.AcordMessageUrl.toString())

//create the AMD PostRq message, based on the Acord business message
println "creating post message"
def postRq = new StreamingMarkupBuilder().bind{
  mkp.declareNamespace( '': 'http://www.ACORD.org/Standards/AcordMsgSvc/Inbox' )
  'PostRq'() {
    mkp.yield acordMessage.Sender
    mkp.yield acordMessage.Receiver
    'Application'() {
      'ApplicationCd'('Jv-Ins-Reinsurance')
      'SchemaVersion'('http://www.ACORD.org/Standards/Jv-Ins-Reinsurance/2003-1')
    }
    'TimeStamp'(new Date()) {
    }
    'MsgItems'() {
      'MsgItem'() {
        //need to set this properly, it needs to be generated
        'MsgId'('f81d4fae-7dec-11d0-a765-00a0c91e6bf9')
        'MsgTypeCd'('RepositoryOperationRq')
      }
    }
    'WorkFolder'() {
      'MsgFile'() {
        //need to set this properly, it needs to equal the MIME part Content-ID for the Acord business message
        'FileId'('cid:A01EFAE7-5490-43D0-AB6B-DAEF1671CDCC')
        'FileFormatCd'('text/xml')
      }
    }
    'AttachmentPackages'() {
      'AttachmentPackage'() {
        'CommunicationChannelCd'('in_stream')
        //need to set this properly, it needs to equal the MIME part Content-ID for each document
        'FileId'('cid:E2A071D1-CB03-49B5-A58F-144036BF0B84')
        mkp.yield acordMessage.DocumentList.DocumentItem.Document.FileFormatCd
        mkp.yield acordMessage.DocumentList.DocumentItem.Document.FileSize
        'Attachments'() {
          'Attachment'() { 
            mkp.yield acordMessage.DocumentList.DocumentItem.Document
          }
        }                    
      }
    }
  }
}

//set the endpoint URL for the recipient system
println "setting endpoint as " + notificationMessage.EndpointReference
EndpointReference targetEPR = new EndpointReference(notificationMessage.EndpointReference.toString())

//create the Axis client options
Options options = new Options()
options.setTo(targetEPR)
options.setProperty(Constants.Configuration.ENABLE_SWA, Constants.VALUE_TRUE)
options.setSoapVersionURI(SOAP11Constants.SOAP_ENVELOPE_NAMESPACE_URI)
options.setTimeOutInMilliSeconds(10000)
options.setTo(targetEPR)
options.setAction("http://www.ACORD.org/Standards/AcordMsgSvc/Inbox#PostRq")

//get the Axis context for sending the message
println "getting Axis config"
ConfigurationContext configContext = ConfigurationContextFactory.createDefaultConfigurationContext()

//create the sender client
println "creating sender client"
ServiceClient sender = new ServiceClient(configContext, null)
sender.setOptions(options)
OperationClient mepClient = sender.createClient(ServiceClient.ANON_OUT_IN_OP)

MessageContext mc = new MessageContext()

//attach the Acord business message
println "attaching the business message"
URLDataSource urlDataSource1 = new URLDataSource(new URL(notificationMessage.AcordMessageUrl.toString()))
DataHandler dataHandler1 = new DataHandler(urlDataSource1)
//should actually be using these in the AMS message for it to be correct
String attachmentID1 = mc.addAttachment(dataHandler1)

//attach the actual document, this needs to iterate over the list and send all, only supports 1 document at a time currently...
println "attaching document attachement"
URLDataSource urlDataSource2 = new URLDataSource(new URL(notificationMessage.Attachments.AttachmentUrl.toString()))
DataHandler dataHandler2 = new DataHandler(urlDataSource2)

//should actually be using these in the AMS message for it to be correct
String attachmentID2 = mc.addAttachment(dataHandler2)

SOAPFactory fac = OMAbstractFactory.getSOAP11Factory()
SOAPEnvelope env = fac.getDefaultEnvelope()

OMElement postRqElement = AXIOMUtil.stringToOM(postRq.toString())
env.getBody().addChild(postRqElement)
mc.setEnvelope(env)

mepClient.addMessageContext(mc)

println "sending message"
mepClient.execute(true)
MessageContext response = mepClient.getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE)

//close off the FileInputStream or Mule gets confused when it tries to move the file
payload.close()