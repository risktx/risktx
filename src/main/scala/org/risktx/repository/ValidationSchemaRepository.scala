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

  private def loadSchema(schemaFile: String): ValidationSchema = {
    val content = io.Source.fromFile(schemaFile).mkString

    ValidationSchema(content)
  }

}