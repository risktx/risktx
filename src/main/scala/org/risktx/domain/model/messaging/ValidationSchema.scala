package org.risktx.domain.model.messaging

abstract case class ValidationSchema protected(
  val content: String
)

object ValidationSchema {
  def apply(content: String) = {
    require(!(content == null || content.isEmpty), "content cannot be null or an empty String")
    new ValidationSchema(content) {}
  }
}