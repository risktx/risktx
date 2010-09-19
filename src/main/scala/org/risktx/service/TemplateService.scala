package org.risktx.service

import freemarker.template.Configuration
import freemarker.template.{Template => FMTemplate}
import java.io._
import java.util.Map
import java.util.HashMap
import scala.xml._

import org.risktx.domain.model.messaging._
object TemplateService {

  def emptyDataMap(): Map[String, Object] = {
    new HashMap
  }

  def process(data: Map[String, Object], template: Template): String = {
    val writer = new StringWriter
    val t = new FMTemplate("template", new StringReader(template.content), new Configuration())

    t.process(data, writer)
    writer.toString
  }

  def processAsXml(data: Map[String, Object], template: Template): NodeSeq = {
    XML.loadString(process(data, template))
  }

}