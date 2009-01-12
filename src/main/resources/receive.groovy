import groovy.xml.MarkupBuilder
import groovy.xml.StreamingMarkupBuilder

//parse the SOAP envelope received
def postRqEnv = new XmlSlurper().parseText(payload.soapEnvelopeIn)

//create response objects
def postRsWriter = new StringWriter()
def postRs = new MarkupBuilder(postRsWriter)

//print out soap env, need a nice logging library in here!
println "inbound payload received"
println payload.soapEnvelopeIn

//generate response
println "generating response"
postRs.'soapenv:Envelope'('xmlns:soapenv': 'http://schemas.xmlsoap.org/soap/envelope/') {
  'soapenv.Body'() {
    'ac:PostRs'('xmlns:ac': 'http://www.ACORD.org/Standards/AcordMsgSvc/Inbox') {
      'ac:Sender'() {
        'ac:PartyId'(postRqEnv.Body.PostRq.Receiver.PartyId)
        'ac:PartyRoleCd'(postRqEnv.Body.PostRq.Receiver.PartyRoleCd)
      }
      'ac:Receiver'() {
        'ac:PartyId'(postRqEnv.Body.PostRq.Sender.PartyId)
        'ac:PartyRoleCd'(postRqEnv.Body.PostRq.Sender.PartyRoleCd)
      }
      'ac:Application'() {
        'ac:ApplicationCd'('Jv-Ins-Reinsurance')
        'ac:SchemaVersion'('http://www.ACORD.org/Standards/Jv-Ins-Reinsurance/2003-1')
      }
      'ac:TimeStamp'(new Date()) {
      }
      'ac:MsgItems'() {
        'ac:MsgItem'() {
          'ac:MsgId'(postRqEnv.Body.PostRq.MsgItem.MsgId)
          'ac:MsgTypeCd'(postRqEnv.Body.PostRq.MsgItem.MsgTypeCd)
          'ac:MsgStatusCd'('received')
        }
      }
    }
  }
}

//write out attachments
println "receive path is: " + System.properties['risktx.receive.directory']
receiveDirectoryPath = System.properties['risktx.receive.directory']

//create directory, uses time in milliseconds, really need to use something in the message...
def dir = new File(receiveDirectoryPath + File.separator + System.currentTimeMillis())
dir.mkdirs()

attachmentMap = payload.attachmentMap
attachmentFileMap = [:]
  
//Acord business message file path
def messageFilePath = ""

//Attachment file path - only 1 is supported until attachment handling gets finished ...
def attachmentFilePath = ""
  
def i = 0
println "saving attachment"
for (contentID in attachmentMap.allContentIDs) {

    filePath = dir.getAbsolutePath() + File.separator + contentID.replaceAll(":", ".")
    println "filepath is " + filePath
    fos = new FileOutputStream(filePath)
    dataHandler = attachmentMap.getDataHandler(contentID)
    
    println "saving file"
    dataHandler.writeTo(fos)
    //bad, bad, bad... MIME part handling is not finished
    if (i==1) {
        messageFilePath = filePath
    }
    if (i==2) {
        attachmentFilePath = filePath
    }
    i++
}

//write the payload notification message
def notificationMessage = new StreamingMarkupBuilder().bind {
  'RiskTxMessage' () {
    'AcordMessageUrl' (messageFilePath)
      'Attachments' () {
        'Attachment' (attachmentFilePath)
      }
  }
}

println "writing notification path"
def notificationFile = new File(dir.getAbsolutePath() + File.separator + 'message.xml')
notificationFile << notificationMessage.toString()

//add the response xml to the returned payload
println "write response to response writer"
payload.soapEnvelopeOut = postRsWriter.toString()

//return payload with AMS PostRs in the soapEnvelopeOut property
return payload