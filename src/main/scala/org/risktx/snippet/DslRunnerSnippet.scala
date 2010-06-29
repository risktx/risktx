package org.risktx.snippet

import _root_.scala.xml.{NodeSeq,Elem}
import _root_.net.liftweb.common.{Box,Full,Empty}
import _root_.net.liftweb.util.Helpers._
import _root_.net.liftweb.http.{S,SHtml,StatefulSnippet}
import net.liftweb.common._

import org.risktx.model.Message
import org.risktx.service.Sender
import org.risktx.template.AcordTemplate
import org.risktx.service.dsl.DslRunner
import net.liftweb.json.JsonDSL._
import net.liftweb.json.JsonAST.JObject

import java.util.Date

/**
* User interface for sending an Acord Ping Message
**/
class DslRunnerSnippet extends StatefulSnippet with Logger {

  /**
  * Target endpoint, default the uri to point at the local web server
  **/
  var url: String = (S.hostAndPath + "/services/ams")

  /**
  * DSL script
  **/
  var script = ""

  /**
  * Overall result of validating/running script - doesn't use Lift's notice mechanism as we need formatted parser output
  **/
  var notice = <div />

  /**
   * Formatted output from running script
   */
  var output = <div />

  /**
   *  The message we are going to send
   **/
  var m: Message = Message.createRecord

  /**
  * Dispatcher, matches on send to sent the message
  **/
  val dispatch: DispatchIt = {
    case "send" => send _
  }
  
  /**
  * Sends the message
  **/
  def send(xhtml: NodeSeq): NodeSeq = {  

    def doValidate () = {
      notice = DslRunner.validate(script) match {
        case Full(message) => {
          <div class="success"><strong>{ message }</strong></div>
        }
        case Failure(message, _, _) => {
          <div class="error"><strong>Validation failed: </strong><pre>{ message }</pre></div>
        }
      }
    }

    /**
    * Use the values entered by the user and send the message
    **/
    def doRun () = {
      val result = DslRunner.run(script, url)
      notice = result match {
        case Full(message) => {
          <div class="success"><strong>Script successfully run</strong></div>
        }
        case Failure(message, _, _) => {
          <div class="error"><strong>Run failed: </strong><pre>{ message }</pre></div>
        }
      }
      output = result match {
        case Full(message) => {
          <div><br /><hr /><h3>Request message sent</h3><pre>{ message.requestContent }</pre><br /><hr /><h3>Synchronous response received</h3><pre>{ message.responseContent }</pre></div>
        }
        case _ => <div />
      }
    }

    // Generate templated xhtml
    bind("dslRunner", xhtml,
      "url" -> SHtml.text(url, url = _, "class" -> "text"),
      "script" -> SHtml.textarea(script, script = _, "class" -> "text"),
      "notice" -> notice,
      "output" -> output,
      "validate" -> SHtml.submit("Validate", () => doValidate()),
      "run" -> SHtml.submit("Run", () => doRun())
    )
  } 
}
