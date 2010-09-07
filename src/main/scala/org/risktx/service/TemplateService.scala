package org.risktx.service

import freemarker.template.Configuration
import freemarker.cache.StringTemplateLoader
import freemarker.template.{Template => FMTemplate}
import java.io._
import java.util.HashMap
import java.util.Map
import org.risktx.domain.model.messaging._

object TemplateService {

  def process(data: Map[String, String], template: Template): String = {
    val writer = new StringWriter
    val t = new FMTemplate("template", new StringReader(template.content), new Configuration())

    t.process(data, writer)  
    writer.toString
  }

}