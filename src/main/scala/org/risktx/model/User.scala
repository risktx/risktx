package org.risktx.model

import scala.xml.{NodeSeq, Text}

import _root_.net.liftweb.common._
import _root_.net.liftweb.util._
import _root_.net.liftweb.http._
import _root_.net.liftweb.util.Helpers._
import _root_.net.liftweb.record.field.PasswordField

import lib._

/**
 * The singleton that has methods for accessing the database
 */
object User extends User with MetaMegaProtoUser[User] {
  def createRecord = new User
  override def collectionName = "users" // define the MongoDB collection name
  
  override def screenWrap = Full(<lift:surround with="public" at="content">
      <lift:bind/>
  </lift:surround>)
  
  // configure email validations for registration - default is disabled
  override def skipEmailValidation = (Props.getBool("user.registration.email.validation", true))

  object whereAfterLogin extends SessionVar[Box[String]](Empty)
  
  override def localForm(user: User, ignorePassword: Boolean): NodeSeq = {

    val formXhtml: NodeSeq = {
      <tr><td>{user.firstName.toForm}</td></tr>
      <tr><td>{user.lastName.toForm}</td></tr>
      <tr><td>{user.email.toForm}</td></tr>
      <tr><td>{user.locale.toForm}</td></tr>
      <tr><td>{user.timezone.toForm}</td></tr>
    }

    if (!ignorePassword)
      formXhtml ++ <tr><td>{user.password.toForm}</td></tr>
    else
      formXhtml
  }
}

/**
 * A "User" class that includes first name, last name, password
 */
class User extends MegaProtoUser[User] {  
  def meta = User // what's the "meta" server
}

