package org.risktx.domain.model.messaging

import org.specs._

class MessageSpec extends Specification {

  "Message" can {

    val instruction = InboundPostRq()
    val messageDate = new java.util.Date()
    val messagePayload = "<xml-message>blah</xml-message>"
    val sender = TradingParty("urn:something:sender", "A Sender", "Service Provider", "a url")
    val receiver = TradingParty("urn:something:receiver", "A Receiver", "Service Provider", "a url")

    "be created from an instruction, payload, sender and receiver" in {
      val m = Message(
        InboundPostRq(),
        messagePayload,
        sender,
        receiver
      )
      m must notBeNull
      m.instruction must be_==(instruction)
      m.payload must be_==(messagePayload)
    }

    "not be created with an empty or null payload" in {
      Message(instruction, "", sender, receiver) must throwA[IllegalArgumentException]
      Message(instruction, null, sender, receiver) must throwA[IllegalArgumentException]
    }

  }

}
