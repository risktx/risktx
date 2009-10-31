package org.risktx.model

import _root_.net.liftweb.mapper._
import _root_.net.liftweb.util._

/**
 * The singleton that has methods for accessing the database
 */
object User extends User with MetaMegaProtoUser[User] {
  override def dbTableName = "users" // define the DB table name
  override def screenWrap = Full(<lift:surround with="default" at="content">
			       <lift:bind /></lift:surround>)
  // define the order fields will appear in forms and output
  override def fieldOrder = List(id, firstName, lastName, email,
                                      locale, timezone, password)

  // comment this line out to require email validations
  override def skipEmailValidation = true
  
  //override def signupFields = firstName :: lastName :: email :: locale :: timezone :: password :: Nil
}

/**
 * An O-R mapped "User" class that includes first name, last name, password and we add a "Personal Essay" to it
 */
class User extends MegaProtoUser[User] {
  def getSingleton = User // what's the "meta" server
}
