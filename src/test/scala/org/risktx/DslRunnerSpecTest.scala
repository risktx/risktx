package org.risktx

import org.specs.runner._
import org.risktx.service.dsl.DslRunner

class DslRunnerSpecTest extends BaseSpecification {
  "The DSL runner" can {

    "successfully validate a Ping message" in {
      DslRunner.validate("""
		    ping to "urn:lloyds:0000"
		  """)
    }

  }
}

