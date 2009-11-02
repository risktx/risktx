package org.risktx.model

import _root_.net.liftweb.mapper._
import _root_.net.liftweb.util._

/**
* User entity based on Lift's MegaProtoUser
**/
class User extends MegaProtoUser[User] {
  def getSingleton = User
}

/**
* User companion object with Helper methods
**/
object User extends User with MetaMegaProtoUser[User] {
  // DB table name
  override def dbTableName = "users" 
  
  override def screenWrap = Full(<lift:surround with="default" at="content">
			       <lift:bind /></lift:surround>)
  
  // define the order fields will appear in forms and output
  override def fieldOrder = List(id, firstName, lastName, email,
                                      locale, timezone, password)

  // skip email validations for signup
  override def skipEmailValidation = true
}

