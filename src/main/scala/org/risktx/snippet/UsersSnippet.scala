package org.risktx.snippet

import scala.xml._
import net.liftweb.util.Helpers._
import net.liftweb.http.SHtml._
import net.liftweb.http.S
import net.liftweb.http.js._
import net.liftweb.http.js.JsCmds._

import org.risktx.model.User

class Users {

  def list(xhtml: NodeSeq) : NodeSeq = {

    bind("Users", xhtml,
      "user" -> User.findAll.flatMap( user =>
        <tr>
          <td>{ user.firstName.value }</td>
          <td>{ user.lastName.value }</td>
          <td>{ user.email.value }</td>
          <td>{ user.locale.value }</td>
        </tr>
      )
    )
  }

}