package org.risktx.repository

import org.risktx.domain.model.messaging._

object TemplateRepository {

  def findTemplate(standard: String, version: String): Template = {
    Template("Here's a ${value1}")
  }

}