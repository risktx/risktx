package org.risktx.domain.model.messaging

import org.specs._
import org.specs.runner.JUnit3
import org.specs.runner.ConsoleRunner
import org.specs.matcher._
import org.specs.specification._
import org.risktx.service._
import org.risktx.repository._

class TemplatingSpecAsTest extends JUnit3(TemplatingSpec)
object TemplatingSpecRunner extends ConsoleRunner(TemplatingSpec)

object TemplatingSpec extends Specification {

  "The templating system" can {
    shareVariables

    val instruction = OutboundPingRq()
    val sender = TradingParty("urn:something:sender", "A Sender", "Service Provider", "a url")
    val receiver = TradingParty("urn:something:receiver", "A Receiver", "Service Provider", "http://localhost:8080/services/ams")

    "be used to create a valid PingRq message" in {
      val template = TemplateRepository.findRequestTemplate("AMS", "1_4_1", "Ping")
      val guid = "93dafe4a-ff70-4de7-94eb-8ca05fc28c19"
      val data = TemplateService.emptyDataMap

      data.put("senderParty", "urn:something:sender")
      data.put("senderPartyRole", "ServiceProvider")
      data.put("receiverParty", "urn:something:sender")
      data.put("receiverPartyRole", "ServiceProvider")
      data.put("timeStamp", new java.util.Date)
      data.put("requestId", guid)

      val result = TemplateService.processAsXml(data, template)

      val message = Message(
        instruction,
        result.toString,
        TradingProfile()
      )
      
      message must notBeNull

      guid must equalIgnoreCase((result \\ "PingId").text)

      val schema = ValidationSchemaRepository.findValidationSchema("AcordMsgSvc_v-1-4-0.xsd")
      ValidationService.validateSchema(message, schema)
    }

  }

}
