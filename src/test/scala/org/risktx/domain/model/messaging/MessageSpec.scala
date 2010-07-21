package org.risktx.domain.model.messaging

import org.specs._

class MessageSpec extends Specification {

  "Message" can {

    val messageRequestId = "ABCDE123etc."
    val messageDate = new java.util.Date()
    val messagePayload = "<xml-message>blah</xml-message>"

    "be created from a requestId, createdDate and payload" in {
      val m = Message(messageRequestId, messageDate, messagePayload)
      m must notBeNull
      m.requestId must be_==(messageRequestId)
      m.createdDate must be_==(messageDate)
      m.payload must be_==(messagePayload)
    }

    "not be created with an empty or null requestId" in {
      Message("", messageDate, messagePayload) must throwA[IllegalArgumentException]
      Message(null, messageDate, messagePayload) must throwA[IllegalArgumentException]
    }

    "not be created with a null createdDate" in {
      Message(messageRequestId, null, messagePayload) must throwA[IllegalArgumentException]
    }

    "not be created with an empty or null payload" in {
      Message(messageRequestId, messageDate, "") must throwA[IllegalArgumentException]
      Message(messageRequestId, messageDate, null) must throwA[IllegalArgumentException]
    }

  }

}
