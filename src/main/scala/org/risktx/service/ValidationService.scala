package org.risktx.service

import org.risktx.domain.model.messaging._
import scala.xml._
import javax.xml.parsers.SAXParser
import javax.xml.parsers.SAXParserFactory
import java.io.{BufferedInputStream, ByteArrayInputStream, InputStream, Reader, StringReader}
import parsing.NoBindingFactoryAdapter
import javax.xml.transform.stream.StreamSource
import javax.xml.XMLConstants
import org.xml.sax.{EntityResolver, InputSource, XMLReader}
import org.w3c.dom.ls.{LSResourceResolver, LSInput}
import javax.xml.validation.{Validator, SchemaFactory, Schema, ValidatorHandler}
import org.risktx.repository.ValidationSchemaRepository

object ValidationService {

  def validateSchema(message: Message, validationSchema: ValidationSchema) {
    val factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema")
    factory.setResourceResolver(new SchemaResolver)
    val schema = factory.newSchema(new StreamSource(new StringReader(validationSchema.content)))
    val validator = schema.newValidator

    validator.validate(new StreamSource(new StringReader(message.payload)));
  }

}

class SchemaResolver extends LSResourceResolver {
  def resolveResource(atype: String,
                        namespaceURI: String,
                        publicId: String,
                        systemId: String,
                        baseURI: String): LSInput = {
    val schema = ValidationSchemaRepository.findValidationSchema(systemId)
    new LSInputImpl(publicId, systemId, schema.content)
  }
}

class LSInputImpl(publicId: String, systemId: String, stringData: String) extends LSInput {
  def getPublicId(): String = publicId
  def setPublicId(publicId: String) = {}
  def getBaseURI(): String = null
  def getByteStream(): InputStream = {
    new ByteArrayInputStream(stringData.getBytes)
  }
  def getCertifiedText(): Boolean = true
  def getCharacterStream(): Reader = {
    null
  }
  def getEncoding(): String = null
  def getStringData(): String = {
    stringData
  }
  def setBaseURI(baseURI: String) = {}
  def setByteStream(byteStream: InputStream) = {}
  def setCertifiedText(certifiedText: Boolean) = {}
  def setCharacterStream(characterStream: Reader) = {}
  def setEncoding(encoding: String) = {}
  def setStringData(stringData: String) = {}
  def getSystemId() = systemId
  def setSystemId(systemId: String) = {}
  def getInputStream(): BufferedInputStream = {
    null
  }
  def setInputStream(inputStream: BufferedInputStream) {}
}