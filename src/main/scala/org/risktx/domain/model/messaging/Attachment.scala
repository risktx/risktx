package org.risktx.domain.model.messaging

abstract case class Attachment protected(
  val fileId: String,
  val contentId: String,
  val contentType: String
)

object Attachment {
  def apply(fileId: String, contentId: String, contentType: String) = {

    require(!(fileId == null || fileId.equals("")), "fileId cannot be null or an empty String")
    require(!(contentId == null || contentId.equals("")), "contentId cannot be null or an empty String")
    require(!(contentType == null || contentType.equals("")), "contentType cannot be null or an empty String")

    new Attachment(fileId, contentId, contentType) {}
  }
}