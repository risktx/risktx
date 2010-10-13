package org.risktx.domain.model.messaging

import org.specs._

class MessageSpec extends Specification {

  "Message" can {

    val instruction = InboundPostRq()
    val messageDate = new java.util.Date()
    val messagePayload = "<xml-message>blah</xml-message>"

    "be created from an instruction, payload, sender and receiver" in {
      val m = Message(
        InboundPostRq(),
        messagePayload,
        TradingProfile()
      )
      m must notBeNull
      m.instruction must be_==(instruction)
      m.payload must be_==(messagePayload)
    }

    "not be created with an empty or null payload" in {
      Message(instruction, "", TradingProfile()) must throwA[IllegalArgumentException]
      Message(instruction, null, TradingProfile()) must throwA[IllegalArgumentException]
    }

  }

}
