package org.risktx.service.dsl

import java.util.Calendar
import scala.util.parsing.combinator.syntactical.StandardTokenParsers
import net.liftweb.common._
import org.risktx.model.Message
import org.risktx.template.AcordTemplate
import org.risktx.service.Sender

sealed abstract class Statement
case class Ping(party: Party) extends Statement
case class Party(partyId: String) extends Statement

object DslRunner extends StandardTokenParsers {
  lexical.reserved += ("ping", "to")

  /**
  * Parses the script, returns an Empty Box for success or Failure for parser error.
  **/
  def validate(input: String): Box[String] = {
    val tokens = new lexical.Scanner(input)
    val result = phrase(program)(tokens)
    result match {
      case Success(tree, _) => Full("Script successfully validated")
      // Failure needs to be qualified with the Lift package
      case e: NoSuccess => net.liftweb.common.Failure(e.toString)
    }
  }

  /**
  * Parses the script, generates appropriate message and attachments then sends the message.
  **/
  def run(input: String, url: String): Box[Message] = {
    val tokens = new lexical.Scanner(input)
    val result = phrase(program)(tokens)
    result match {
      case Success(tree, _) => {
        try {
          val bundle = new Interpreter(tree, url).run()
          Full(bundle)
        } catch {
          case e: Exception =>
            net.liftweb.common.Failure(e.toString)
        }
      }
      // Failure needs to be qualified with the Lift package
      case e: NoSuccess => net.liftweb.common.Failure(e.toString)
    }
  }

  def program = messageOperation+

  def messageOperation: Parser[Statement] = ("ping" ~ "to" ~ party ^^ {case _ ~ p => Ping(p)})

  def party = (stringLit ^^ {case s => Party(s)})
}

class Interpreter(tree: List[Statement], url: String) {
  def run(): Message = {
    val message = Message.createRecord
    walkTree(tree, message)
    message.url(url)
    Sender.send(message)
    message
  }

  private def walkTree(tree: List[Statement], message: Message) {
    tree match {
      case Ping(party) :: rest => {
        message.dateOf(Calendar.getInstance).direction("out").senderParty("urn:risktx:test").senderPartyRole("ServiceProvider").receiverParty(party.partyId).receiverPartyRole("ServiceProvider")
        AcordTemplate.createRq(message)
        walkTree(rest, message)
      }

      case _ => ()
    }
  }
}
