package org.risktx.model

import net.liftweb.mapper._

/**
* Attachment entity which encapsulates a message part
**/
class Attachment extends LongKeyedMapper[Attachment] with IdPK {
  def getSingleton = Attachment
  
  /**
  * The message which the attachment belongs to
  **/
  object message extends MappedLongForeignKey(this, Message) {
    override def dbIndexed_? = true
  }

  /**
  * The unique id of the attachment. Attachment ids should be unique within the scope of the message
  **/  
  object attachmentId extends MappedString(this, 256)

  /**
  * The MIME type of the attachment, e.g. text/xml
  **/  
  object contentType extends MappedString(this, 36)
  
  /**
  * The actual content of the attachment
  **/  
  object content extends MappedBinary(this)
}

/**
* Attachment companion object
**/
object Attachment extends Attachment with LongKeyedMetaMapper[Attachment] {
}
