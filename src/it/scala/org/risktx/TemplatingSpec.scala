package org.risktx.domain.model.messaging

import java.io._
import java.util.HashMap
import java.util.Map
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
    "be used with a simple example" in {
      val template = TemplateRepository.findRequestTemplate("AMS", "1_4_1")
      
      val data: Map[String, String] = new HashMap
		  data.put("value1", "test")

      val result = TemplateService.process(data, template)
      result must equalIgnoreCase("Here's a test")
    }

  }

}
