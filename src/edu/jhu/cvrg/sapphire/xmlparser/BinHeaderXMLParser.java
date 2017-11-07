package edu.jhu.cvrg.sapphire.xmlparser;

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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import edu.jhu.cvrg.sapphire.data.common.Header;
import edu.jhu.cvrg.sapphire.data.common.ResponseId;
import edu.jhu.cvrg.sapphire.data.common.SessionId;
import edu.jhu.cvrg.sapphire.data.response.BinHeader;
import edu.jhu.cvrg.sapphire.data.response.binheader.BinaryFormat;
import edu.jhu.cvrg.sapphire.util.NodeListIterator;

public class BinHeaderXMLParser {

	private String xmlString;
	private BinHeader binHeader;

	public BinHeaderXMLParser() {

		this("");

	}

	public BinHeaderXMLParser(String messageXML) {

		setXmlString(messageXML);

	}

	public String getXmlString() {
		return xmlString;
	}

	public void setXmlString(String xmlString) {
		processXML(xmlString);
		this.xmlString = xmlString;
	}

	public BinHeader getBinHeader() {
		return binHeader;
	}

	public void setBinHeader(BinHeader binHeader) {
		this.binHeader = binHeader;
	}

	private void processXML(String xmlString) {

		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new InputSource(new ByteArrayInputStream(xmlString.getBytes("utf-8"))));

			// normalize text representation
			doc.getDocumentElement().normalize();

			NodeList sapphireNodes = doc.getChildNodes();
			for (Node sapphireNode : NodeListIterator.asList(sapphireNodes)) {
				if (sapphireNode instanceof Element) {
					NodeList binHeaderNodes = sapphireNode.getChildNodes();
					BinHeader binHeader = new BinHeader();
					for (Node binHeaderNode : NodeListIterator.asList(binHeaderNodes)) {
						if (binHeaderNode instanceof Element) {
							NodeList binHeaderSubNodes = binHeaderNode.getChildNodes();
							for (Node binHeaderSubNode : NodeListIterator.asList(binHeaderSubNodes)) {
								if (binHeaderSubNode instanceof Element) {
									switch (binHeaderSubNode.getNodeName()) {
									case "header": 
										Header header = new Header();
										NodeList headerSubNodes = binHeaderSubNode.getChildNodes();
										for (Node headerSubNode : NodeListIterator.asList(headerSubNodes)) {
											if (headerSubNode instanceof Element) {
												switch (headerSubNode.getNodeName()) {
												case "sessionID": 
													SessionId sessionId = new SessionId();
													sessionId.setValue(headerSubNode.getAttributes().getNamedItem("V").getNodeValue());
													sessionId.setType(headerSubNode.getAttributes().getNamedItem("type").getNodeValue());
													header.setSessionId(sessionId);
													break;
												case "msgCHN": 
													header.setMsgCHN(headerSubNode.getAttributes().getNamedItem("V").getNodeValue());
													break;
												case "msgSQN": 
													header.setMsgSQN(headerSubNode.getAttributes().getNamedItem("V").getNodeValue());
													break;
												case "creationDateTime": 
													header.setCreationDateTime(headerSubNode.getAttributes().getNamedItem("V").getNodeValue());
													break;											
												case "responseID": 
													ResponseId responseId = new ResponseId();
													responseId.setValue(headerSubNode.getAttributes().getNamedItem("V").getNodeValue());
													responseId.setMore(headerSubNode.getAttributes().getNamedItem("more").getNodeValue());
													header.setResponseId(responseId);
													break;											
												}
											}
										}
										binHeader.setHeader(header);
										break;
									case "startDateTime":
										binHeader.setStartDateTime(binHeaderSubNode.getAttributes().getNamedItem("V").getNodeValue());
										break;
									case "binaryFormat":
										BinaryFormat binaryFormat = new BinaryFormat();
										NodeList binaryFormatSubNodes = binHeaderSubNode.getChildNodes();
										for (Node binaryFormatSubNode : NodeListIterator.asList(binaryFormatSubNodes)) {
											if (binaryFormatSubNode instanceof Element) {
												switch (binaryFormatSubNode.getNodeName()) {
												case "byteOrder": 
													binaryFormat.setByteOrder(binaryFormatSubNode.getAttributes().getNamedItem("V").getNodeValue());
													break;
												}
											}
										}
										binHeader.setBinaryFormat(binaryFormat);
										break;
									}
								}
							}
						}
					}
					setBinHeader(binHeader);
				}
			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
