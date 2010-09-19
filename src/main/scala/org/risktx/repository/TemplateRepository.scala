package org.risktx.repository

import org.risktx.domain.model.messaging._

object TemplateRepository {
  
  def findRequestTemplate(standard: String, version: String, messageType: String): Template = {
    val template = "/request/" + standard + "_" + version + "_" + messageType + ".tmpl"
    loadTemplate(template)
  }

  def findResponseTemplate(standard: String, version: String, messageType: String): Template = {
    val template = "/response/" + standard + "_" + version + "_" + messageType + ".tmpl"
    loadTemplate(template)
  }

  private def loadTemplate(template: String): Template = {
    val stream = this.getClass.getResourceAsStream(template)
    val content = io.Source.fromInputStream(stream).getLines.mkString

    Template(content)
  }

}