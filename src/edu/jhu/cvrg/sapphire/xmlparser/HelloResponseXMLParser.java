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
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import edu.jhu.cvrg.sapphire.data.common.Device;
import edu.jhu.cvrg.sapphire.data.common.Header;
import edu.jhu.cvrg.sapphire.data.common.SessionId;
import edu.jhu.cvrg.sapphire.data.common.SoftwareVersion;
import edu.jhu.cvrg.sapphire.data.response.HelloResponse;
import edu.jhu.cvrg.sapphire.util.NamedNodeMapIterable;
import edu.jhu.cvrg.sapphire.util.NodeListIterator;

public class HelloResponseXMLParser {

	private String xmlString;
	private HelloResponse helloResponse;

	public HelloResponseXMLParser() {

		this("");

	}

	public HelloResponseXMLParser(String messageXML) {

		setXmlString(messageXML);

	}

	public String getXmlString() {
		return xmlString;
	}

	public void setXmlString(String xmlString) {
		processXML(xmlString);
		this.xmlString = xmlString;
	}

	public HelloResponse getHelloResponse() {
		return helloResponse;
	}

	public void setHelloResponse(HelloResponse helloResponse) {
		this.helloResponse = helloResponse;
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
					NodeList helloResponseNodes = sapphireNode.getChildNodes();
					HelloResponse helloResponse = new HelloResponse();
					for (Node helloResponseNode : NodeListIterator.asList(helloResponseNodes)) {
						if (helloResponseNode instanceof Element) {
							NamedNodeMap helloResponseNodeAttributes = helloResponseNode.getAttributes();
							for (Node attribute : NamedNodeMapIterable.of(helloResponseNodeAttributes)) {
								switch (attribute.getNodeName()) {
								case "status": 
									helloResponse.setStatus(attribute.getNodeValue());
									break;
								case "errorString": 
									helloResponse.setErrorString(attribute.getNodeValue());
									break;
								}
							}
							NodeList helloResponseSubNodes = helloResponseNode.getChildNodes();
							for (Node helloResponseSubNode : NodeListIterator.asList(helloResponseSubNodes)) {
								if (helloResponseSubNode instanceof Element) {
									switch (helloResponseSubNode.getNodeName()) {
									case "header": 
										Header header = new Header();
										NodeList headerSubNodes = helloResponseSubNode.getChildNodes();
										for (Node headerSubNode : NodeListIterator.asList(headerSubNodes)) {
											if (headerSubNode instanceof Element) {
												switch (headerSubNode.getNodeName()) {
												case "sessionID": 
													SessionId sessionId = new SessionId();
													sessionId.setValue(headerSubNode.getAttributes().getNamedItem("V").getNodeValue());
													header.setSessionId(sessionId);
													break;
												case "msgCHN": 
													header.setMsgCHN(headerSubNode.getAttributes().getNamedItem("V").getNodeValue());
													break;
												case "replytoMsgSQN": 
													header.setReplytoMsgSQN(headerSubNode.getAttributes().getNamedItem("V").getNodeValue());
													break;
												case "creationDateTime": 
													header.setCreationDateTime(headerSubNode.getAttributes().getNamedItem("V").getNodeValue());
													break;											
												}
											}
										}
										helloResponse.setHeader(header);
										break;
									case "device":
										Device device = new Device();
										NodeList deviceSubNodes = helloResponseSubNode.getChildNodes();
										for (Node deviceSubNode : NodeListIterator.asList(deviceSubNodes)) {
											if (deviceSubNode instanceof Element) {
												switch (deviceSubNode.getNodeName()) {
												case "globalID": 
													device.setGlobalId(deviceSubNode.getAttributes().getNamedItem("V").getNodeValue());
													break;
												case "vendorID": 
													device.setVendorId(deviceSubNode.getAttributes().getNamedItem("V").getNodeValue());
													break;
												case "modelID": 
													device.setModelId(deviceSubNode.getAttributes().getNamedItem("V").getNodeValue());
													break;
												case "softwareVersion": 
													SoftwareVersion softwareVersion = new SoftwareVersion();
													softwareVersion.setName(deviceSubNode.getAttributes().getNamedItem("name").getNodeValue());
													softwareVersion.setValue(deviceSubNode.getAttributes().getNamedItem("V").getNodeValue());
													device.setSoftwareVersion(softwareVersion);;
													break;
												}
											}
										}
										helloResponse.setDevice(device);
										break;
									}
								}
							}
						}
					}
					setHelloResponse(helloResponse);
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
