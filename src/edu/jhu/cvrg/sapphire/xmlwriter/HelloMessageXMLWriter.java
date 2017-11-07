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
import edu.jhu.cvrg.sapphire.data.common.SessionId;
import edu.jhu.cvrg.sapphire.data.request.Hello;

public class HelloMessageXMLWriter {

	private String xmlString, sessionId;
	private Hello hello;
	
	public HelloMessageXMLWriter() {

		this("000000FFFE00000000000000");
		
	}

	public HelloMessageXMLWriter(String sessionId) {

		setXmlString("");
		setSessionId(sessionId);
		setHello(new Hello());
		writeXML();
		
	}

	public String getXmlString() {
		return xmlString;
	}
	
	public void setXmlString(String xmlString) {
		this.xmlString = xmlString;
	}
	
	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Hello getHello() {
		return hello;
	}
	
	public void setHello(Hello hello) {
		
		Header header = new Header();
		SessionId sessionIdLocal = new SessionId();
		sessionIdLocal.setValue(getSessionId());
		sessionIdLocal.setType("EUI-64:NTPS-32");
		header.setSessionId(sessionIdLocal);
		header.setMsgCHN("0");
		header.setMsgSQN("1");
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		Date now = new Date();
		header.setCreationDateTime(sdfDate.format(now));
		hello.setHeader(header);
		this.hello = hello;
		
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
			
			Hello hello = getHello();
			Element helloElement = doc.createElement("hello");
			sapphireElement.appendChild(helloElement);

			Attr helloXmlns = doc.createAttribute("xmlns");
			helloXmlns.setValue(hello.getXmlNameSpace());
			helloElement.setAttributeNode(helloXmlns);

			Header header = hello.getHeader();
			Element headerElement = doc.createElement("header");
			helloElement.appendChild(headerElement);

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

			Element msgSQNElement = doc.createElement("msgSQN");
			headerElement.appendChild(msgSQNElement);

			Attr msgSQNValue = doc.createAttribute("V");
			msgSQNValue.setValue(header.getMsgSQN());
			msgSQNElement.setAttributeNode(msgSQNValue);

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
