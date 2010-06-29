package org.risktx

import java.util.Calendar
import org.specs.runner._
import net.liftweb.common._
import org.risktx.model._

class MessageSpecTest extends BaseSpecification {
	"Message" can {
		val created = Message.createRecord
      "be created" in {
        created.dateOf(Calendar.getInstance).operation("PostRq")
        created.save must_== created
      }

      "be updated" in {
        created.direction("out")
        created.save must_== created
      }
    }
  }


