package org.risktx

import org.specs.runner._
import net.liftweb.common._
import org.risktx.model.User

class UserSpecTest extends BaseSpecification {

	"User" can {
		val created = User.createRecord

		"be created" in {
			created.firstName("Foo").lastName("Bar")
			created.save must_== created
		}

		"be updated" in {
			created.firstName("Bar").lastName("Foo")
			created.save must_== created
		}
	}
}
