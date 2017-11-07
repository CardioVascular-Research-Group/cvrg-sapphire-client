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
import java.util.ArrayList;

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

import edu.jhu.cvrg.sapphire.data.common.Header;
import edu.jhu.cvrg.sapphire.data.common.RequestId;
import edu.jhu.cvrg.sapphire.data.common.RequestIdList;
import edu.jhu.cvrg.sapphire.data.common.ResponseId;
import edu.jhu.cvrg.sapphire.data.common.SessionId;
import edu.jhu.cvrg.sapphire.data.response.EndStreamResponse;
import edu.jhu.cvrg.sapphire.util.NamedNodeMapIterable;
import edu.jhu.cvrg.sapphire.util.NodeListIterator;

public class EndStreamResponseXMLParser {

	private String xmlString;
	private EndStreamResponse endStreamResponse;

	public EndStreamResponseXMLParser() {

		this("");

	}

	public EndStreamResponseXMLParser(String messageXML) {

		setXmlString(messageXML);

	}

	public String getXmlString() {
		return xmlString;
	}

	public void setXmlString(String xmlString) {
		processXML(xmlString);
		this.xmlString = xmlString;
	}

	public EndStreamResponse getEndStreamResponse() {
		return endStreamResponse;
	}

	public void setEndStreamResponse(EndStreamResponse endStreamResponse) {
		this.endStreamResponse = endStreamResponse;
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
					NodeList endStreamResponseNodes = sapphireNode.getChildNodes();
					EndStreamResponse endStreamResponse = new EndStreamResponse();
					for (Node endStreamResponseNode : NodeListIterator.asList(endStreamResponseNodes)) {
						if (endStreamResponseNode instanceof Element) {
							NamedNodeMap endStreamResponseNodeAttributes = endStreamResponseNode.getAttributes();
							for (Node attribute : NamedNodeMapIterable.of(endStreamResponseNodeAttributes)) {
								switch (attribute.getNodeName()) {
								case "status": 
									endStreamResponse.setStatus(attribute.getNodeValue());
									break;
								case "errorString": 
									endStreamResponse.setErrorString(attribute.getNodeValue());
									break;
								}
							}
							NodeList endStreamResponseSubNodes = endStreamResponseNode.getChildNodes();
							for (Node endStreamResponseSubNode : NodeListIterator.asList(endStreamResponseSubNodes)) {
								if (endStreamResponseSubNode instanceof Element) {
									switch (endStreamResponseSubNode.getNodeName()) {
									case "header": 
										Header header = new Header();
										NodeList headerSubNodes = endStreamResponseSubNode.getChildNodes();
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
												case "responseID": 
													ResponseId responseId = new ResponseId();
													responseId.setValue(headerSubNode.getAttributes().getNamedItem("V").getNodeValue());
													responseId.setMore(headerSubNode.getAttributes().getNamedItem("more").getNodeValue());
													header.setResponseId(responseId);
													break;											
												}
											}
										}
										endStreamResponse.setHeader(header);
										break;
									case "requestIDList": 
										RequestIdList requestIdList = new RequestIdList();
										ArrayList<RequestId> requestIds = new ArrayList<RequestId>();
										NodeList requestIdListSubNodes = endStreamResponseSubNode.getChildNodes();
										for (Node requestIdListSubNode : NodeListIterator.asList(requestIdListSubNodes)) {
											if (requestIdListSubNode instanceof Element) {
												RequestId requestId = new RequestId();
												requestId.setValue(requestIdListSubNode.getAttributes().getNamedItem("V").getNodeValue());
												requestId.setStatus(requestIdListSubNode.getAttributes().getNamedItem("status").getNodeValue());
												requestIds.add(requestId);
											}
										}
										requestIdList.setRequestIds(requestIds);
										endStreamResponse.setRequestIdList(requestIdList);
										break;
									}
								}
							}
						}
					}
					setEndStreamResponse(endStreamResponse);
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
