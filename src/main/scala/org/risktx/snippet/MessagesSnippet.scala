package org.risktx.snippet

import scala.xml._
import net.liftweb.util.Helpers._
import net.liftweb.http.SHtml._
import net.liftweb.http.S
import net.liftweb.http.js._
import net.liftweb.http.js.JsCmds._

import org.risktx.model.Message

class Messages {

  def list(xhtml: NodeSeq) : NodeSeq = {

    bind("Messages", xhtml,
      "message" -> Message.findAll.flatMap( message =>
        <tr>
          <td>{ String.format("%1$tF %1$tT", message.dateOf.value) }</td>
          <td>{ message.requestId.value }</td>
          <td>{ message.senderParty.value }</td>
          <td>{ message.senderPartyRole.value }</td>
        </tr>
      )
    )
  }

}