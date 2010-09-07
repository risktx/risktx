package org.risktx.domain.model.messaging

abstract case class Template protected(
  val content: String
)

object Template {
  def apply(content: String) = {
    require(!(content == null || content.equals("")), "content cannot be null or an empty String")
    new Template(content) {}
  }
}