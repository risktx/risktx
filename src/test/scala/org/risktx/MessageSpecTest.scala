package org.risktx

import java.util.Date
import org.specs.runner._
import net.liftweb.util._
import org.risktx.model.Message

class MessageSpecTest extends BaseSpecification {

	"Message" can {
		val created = Message.create

		"be created" in {
		  created.dateOf(new Date()).operation("PostRq")
			created.save() must beTrue
		}
	  
		"be updated" in {
			created.direction("out")
			created.save() must beTrue
		}

		"be fetched" in {
		  Message.find(created.id) match {
		    case Full(fetched) => 
		      (fetched == created) must beTrue
		    case _ => // for match completeness, avoids compiler warnings
		      true must beFalse
		  }
		}
	}
}

