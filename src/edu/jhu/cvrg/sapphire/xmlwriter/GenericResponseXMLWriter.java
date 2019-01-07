package edu.jhu.cvrg.sapphire.xmlwriter;

/*
Copyright 2017 Johns Hopkins University Institute for Computational Medicine

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

/**
 * @author Stephen Granite
 * 
 */

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.jhu.cvrg.sapphire.data.common.Header;
import edu.jhu.cvrg.sapphire.data.common.ResponseId;
import edu.jhu.cvrg.sapphire.data.common.SessionId;
import edu.jhu.cvrg.sapphire.data.request.GenericResponse;

public class GenericResponseXMLWriter {

	private String xmlString;
	private Header header;
	private GenericResponse genericResponse;
	
	public GenericResponseXMLWriter() {

		this(new Header());
		
	}

	public GenericResponseXMLWriter(Header header) {

		setXmlString("");
		setHeader(header);
		setGenericResponse(new GenericResponse());
		writeXML();
		
	}

	public String getXmlString() {
		return xmlString;
	}
	
	public void setXmlString(String xmlString) {
		this.xmlString = xmlString;
	}
	
	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public GenericResponse getGenericResponse() {
		return genericResponse;
	}
	
	public void setGenericResponse(GenericResponse genericResponse) {
		
		Header localHeader = new Header();
		SessionId sessionIdLocal = new SessionId();
		sessionIdLocal.setValue(getHeader().getSessionId().getValue());
		sessionIdLocal.setType("EUI-64:NTPS-32");
		localHeader.setSessionId(sessionIdLocal);
		localHeader.setMsgCHN(getHeader().getMsgCHN());
		localHeader.setReplytoMsgSQN(getHeader().getMsgSQN());
		localHeader.setResponseId(getHeader().getResponseId());
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		Date now = new Date();
		localHeader.setCreationDateTime(sdfDate.format(now));
		genericResponse.setHeader(localHeader);
		genericResponse.setStatus("ack");
		this.genericResponse = genericResponse;
		
	}

	private void writeXML() {

		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();

			Element sapphireElement = doc.createElement("sapphire");
			doc.appendChild(sapphireElement);

			Attr xmlns = doc.createAttribute("xmlns");
			xmlns.setValue("urn:ge:sapphire:sapphire_1");
			sapphireElement.setAttributeNode(xmlns);
			Attr xmlnsXsi = doc.createAttribute("xmlns:xsi");
			xmlnsXsi.setValue("http://www.w3.org/2001/XMLSchema-instance");
			sapphireElement.setAttributeNode(xmlnsXsi);
			Attr version = doc.createAttribute("version");
			version.setValue("2.0.1");
			sapphireElement.setAttributeNode(version);
			
			GenericResponse genericResponse = getGenericResponse();
			Element genericResponseElement = doc.createElement("genericResponse");
			sapphireElement.appendChild(genericResponseElement);

			Attr genericResponseXmlns = doc.createAttribute("xmlns");
			genericResponseXmlns.setValue(genericResponse.getXmlNameSpace());
			genericResponseElement.setAttributeNode(genericResponseXmlns);

			Attr genericResponseStatus = doc.createAttribute("status");
			genericResponseStatus.setValue(genericResponse.getStatus());
			genericResponseElement.setAttributeNode(genericResponseStatus);

			Header header = genericResponse.getHeader();
			Element headerElement = doc.createElement("header");
			genericResponseElement.appendChild(headerElement);

			Attr headerXmlns = doc.createAttribute("xmlns");
			headerXmlns.setValue(header.getXmlNameSpace());
			headerElement.setAttributeNode(headerXmlns);
			
			Element sessionIdElement = doc.createElement("sessionID");
			headerElement.appendChild(sessionIdElement);

			Attr sessionIdValue = doc.createAttribute("V");
			sessionIdValue.setValue(new String(header.getSessionId().getValue()));
			sessionIdElement.setAttributeNode(sessionIdValue);

			Attr sessionIdType = doc.createAttribute("type");
			sessionIdType.setValue(header.getSessionId().getType());
			sessionIdElement.setAttributeNode(sessionIdType);

			Element msgCHNElement = doc.createElement("msgCHN");
			headerElement.appendChild(msgCHNElement);

			Attr msgCHNValue = doc.createAttribute("V");
			msgCHNValue.setValue(header.getMsgCHN());
			msgCHNElement.setAttributeNode(msgCHNValue);

			Element replytoMsgSQNElement = doc.createElement("replytoMsgSQN");
			headerElement.appendChild(replytoMsgSQNElement);

			Attr replytoMsgSQNValue = doc.createAttribute("V");
			replytoMsgSQNValue.setValue(header.getReplytoMsgSQN());
			replytoMsgSQNElement.setAttributeNode(replytoMsgSQNValue);

			ResponseId responseId = header.getResponseId();
			Element responseIdElement = doc.createElement("responseID");
			headerElement.appendChild(responseIdElement);

			Attr responseIdValue = doc.createAttribute("V");
			responseIdValue.setValue(responseId.getValue());
			responseIdElement.setAttributeNode(responseIdValue);

			Attr responseIdMore = doc.createAttribute("more");
			responseIdMore.setValue(responseId.getMore());
			responseIdElement.setAttributeNode(responseIdMore);
			
			Element creationDateTimeElement = doc.createElement("creationDateTime");
			headerElement.appendChild(creationDateTimeElement);

			Attr creationDateTimeValue = doc.createAttribute("V");
			creationDateTimeValue.setValue(header.getCreationDateTime());
			creationDateTimeElement.setAttributeNode(creationDateTimeValue);
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty("indent", "yes");
			DOMSource source = new DOMSource(doc);
			StringWriter output = new StringWriter();
			StreamResult result = new StreamResult(output);

			transformer.transform(source, result);
			output.flush();
			setXmlString(output.toString());
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
		    e.printStackTrace(); 
		} catch (TransformerException e) {
		    e.printStackTrace(); 
		} 
	}
	
}
