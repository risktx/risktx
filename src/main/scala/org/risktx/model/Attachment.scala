package org.risktx.model

import net.liftweb.mapper._

class Attachment extends LongKeyedMapper[Attachment] with IdPK {
  def getSingleton = Attachment

  object message extends MappedLongForeignKey(this, Message) {
    override def dbIndexed_? = true
  }

  object attachmentId extends MappedString(this, 256)

  object contentType extends MappedString(this, 36)
  
  object content extends MappedBinary(this)
}

object Attachment extends Attachment with LongKeyedMetaMapper[Attachment] {
}
