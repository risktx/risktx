package org.risktx.repository

import javax.activation.DataHandler
import net.liftweb.mongodb._
import com.mongodb.gridfs._
import org.risktx.domain.model.messaging._

object FileRepository {

  def createAttachment(handler: DataHandler, contentId: String): Attachment = {
    val inputStream = handler.getInputStream

    MongoDB.use(DefaultMongoIdentifier) ( db => {
      val fs = new GridFS(db)
      val inputFile = fs.createFile(inputStream, handler.getContentType)
      inputFile.save
      Attachment(inputFile.getId.toString, contentId, handler.getContentType)
    })

  }

}