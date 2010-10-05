package org.risktx.snippet

import scala.xml.{NodeSeq}
import net.liftweb.common.{Full}

import org.risktx.domain.model.administration.User

/**
* Snippet class which allows logged in user to be displayed
**/
class LoggedIn {
  
  /**
  * Builds and returns xhtml fragment with user details, if user is logged in
  **/
  def render (xhtml : NodeSeq) : NodeSeq = User.currentUser match {
    case Full(user) => <span>Logged in as {user.niceName}</span>
    case _ => NodeSeq.Empty
  }

}
