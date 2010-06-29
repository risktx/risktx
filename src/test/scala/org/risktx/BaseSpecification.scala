package org.risktx

import org.specs._
import bootstrap.liftweb.Boot

class BaseSpecification extends SpecificationWithJUnit {
  BaseSpecification.init
  
  //within specs preserve variables across examples 
  shareVariables()
}


/**
*  Used to call Boot.init to provide access to the domain model
**/
object BaseSpecification {
  var initialised = false
  
  def init {
    if (!initialised) {
      (new Boot).boot
      initialised = true
    }
  }
}
