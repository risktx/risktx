package org.risktx.repository

import org.risktx.domain.model.messaging._
import net.liftweb.common._
import net.liftweb.util.Props

object ValidationSchemaRepository {

  def findValidationSchema(schema: String): ValidationSchema = {
    (Props.get("schema.directory")) match {
      case (Full(directory)) => {
        loadSchema(directory + schema)
      }
      case _ => {
        ValidationSchema("")
      }
    }
  }

  private def loadSchema(schema: String): ValidationSchema = {
    val stream = this.getClass.getResourceAsStream(schema)
    val content = io.Source.fromInputStream(stream).getLines.mkString

    ValidationSchema(content)
  }

}