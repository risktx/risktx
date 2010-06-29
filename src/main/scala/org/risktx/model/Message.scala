package org.risktx.model

import scala.xml._
import _root_.net.liftweb.common._
import _root_.net.liftweb.record._
import _root_.net.liftweb.record.field._
import _root_.net.liftweb.mongodb._
import _root_.net.liftweb.mongodb.record._
import _root_.net.liftweb.mongodb.record.field._

class Message extends MongoRecord[Message] with MongoId[Message] {
  def meta = Message

  object requestId extends StringField(this, 36)
  object responseId extends StringField(this, 36)
  object dateOf extends DateTimeField(this)
  object url extends StringField(this, 256)
  object senderParty extends StringField(this, 64)
  object senderPartyRole extends StringField(this, 32)
  object receiverParty extends StringField(this, 64)
  object receiverPartyRole extends StringField(this, 32)
  object direction extends StringField(this, 3)
  object status extends IntField(this)
  object operation extends StringField(this, 6)
  object statusDescription extends StringField(this, 4096)
  object requestContent extends StringField(this, 4096)
  object responseContent extends StringField(this, 4096)

}

object Message extends Message with MongoMetaRecord[Message] {
  def createRecord = new Message
}


