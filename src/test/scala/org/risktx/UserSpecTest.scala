package org.risktx

import org.specs.runner._
import net.liftweb.util._
import org.risktx.model.User

class UserSpecTest extends BaseSpecification {

	"User" can {
		val created = User.create

		"be created" in {
			created.firstName("Foo").lastName("Bar")
			created.save() must beTrue
		}
	  
		"be updated" in {
			created.firstName("Bar").lastName("Foo")
			created.save() must beTrue
		}

		"be fetched" in {
		  User.find(created.id) match {
		    case Full(fetched) => 
		      (fetched == created) must beTrue
		    case _ => // for match completeness, avoids compiler warnings
		      true must beFalse
		  }
		}
	}
}

