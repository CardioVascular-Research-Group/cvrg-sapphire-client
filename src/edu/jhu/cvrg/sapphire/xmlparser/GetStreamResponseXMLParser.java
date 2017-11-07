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
import edu.jhu.cvrg.sapphire.data.common.Query;
import edu.jhu.cvrg.sapphire.data.common.ResponseId;
import edu.jhu.cvrg.sapphire.data.common.SelectNodes;
import edu.jhu.cvrg.sapphire.data.common.SessionId;
import edu.jhu.cvrg.sapphire.data.common.SessionIdList;
import edu.jhu.cvrg.sapphire.data.response.GetStreamResponse;
import edu.jhu.cvrg.sapphire.util.NamedNodeMapIterable;
import edu.jhu.cvrg.sapphire.util.NodeListIterator;

public class GetStreamResponseXMLParser {

	private String xmlString;
	private GetStreamResponse getStreamResponse;

	public GetStreamResponseXMLParser() {

		this("");

	}

	public GetStreamResponseXMLParser(String messageXML) {

		setXmlString(messageXML);

	}

	public String getXmlString() {
		return xmlString;
	}

	public void setXmlString(String xmlString) {
		processXML(xmlString);
		this.xmlString = xmlString;
	}

	public GetStreamResponse getGetStreamResponse() {
		return getStreamResponse;
	}

	public void setGetStreamResponse(GetStreamResponse getStreamResponse) {
		this.getStreamResponse = getStreamResponse;
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
					NodeList getStreamResponseNodes = sapphireNode.getChildNodes();
					GetStreamResponse getStreamResponse = new GetStreamResponse();
					for (Node getStreamResponseNode : NodeListIterator.asList(getStreamResponseNodes)) {
						if (getStreamResponseNode instanceof Element) {
							NamedNodeMap getStreamResponseNodeAttributes = getStreamResponseNode.getAttributes();
							for (Node attribute : NamedNodeMapIterable.of(getStreamResponseNodeAttributes)) {
								switch (attribute.getNodeName()) {
								case "status": 
									getStreamResponse.setStatus(attribute.getNodeValue());
									break;
								case "errorString": 
									getStreamResponse.setErrorString(attribute.getNodeValue());
									break;
								}
							}
							NodeList getStreamResponseSubNodes = getStreamResponseNode.getChildNodes();
							for (Node getStreamResponseSubNode : NodeListIterator.asList(getStreamResponseSubNodes)) {
								if (getStreamResponseSubNode instanceof Element) {
									switch (getStreamResponseSubNode.getNodeName()) {
									case "header": 
										Header header = new Header();
										NodeList headerSubNodes = getStreamResponseSubNode.getChildNodes();
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
										getStreamResponse.setHeader(header);
										break;
									case "newMsgCHN":
										getStreamResponse.setNewMsgCHN(getStreamResponseSubNode.getAttributes().getNamedItem("V").getNodeValue());
										break;
									case "query":
										Query query = new Query();
										NodeList querySubNodes = getStreamResponseSubNode.getChildNodes();
										for (Node querySubNode : NodeListIterator.asList(querySubNodes)) {
											if (querySubNode instanceof Element) {
												switch (querySubNode.getNodeName()) {
												case "endTime": 
													query.setEndTime(querySubNode.getAttributes().getNamedItem("V").getNodeValue());
													break;
												case "sessionIDList": 
													SessionIdList sessionIdList = new SessionIdList();
													ArrayList<SessionId> sessionIds = new ArrayList<SessionId>();
													NodeList sessionIdListSubNodes = querySubNode.getChildNodes();
													for (Node sessionIdListSubNode : NodeListIterator.asList(sessionIdListSubNodes)) {
														if (sessionIdListSubNode instanceof Element) {
															SessionId sessionId = new SessionId();
															sessionId.setValue(sessionIdListSubNode.getAttributes().getNamedItem("V").getNodeValue());
															sessionId.setStatus(sessionIdListSubNode.getAttributes().getNamedItem("status").getNodeValue());
															sessionIds.add(sessionId);
														}
													}
													sessionIdList.setSessionIds(sessionIds);
													query.setSessionIdList(sessionIdList);
													break;
												case "selectNodes": 
													SelectNodes selectNodes = new SelectNodes();
													selectNodes.setSelect(querySubNode.getAttributes().getNamedItem("select").getNodeValue());
													selectNodes.setStatus(querySubNode.getAttributes().getNamedItem("status").getNodeValue());
													query.setSelectNodes(selectNodes);
													break;
												}
											}
										}
										getStreamResponse.setQuery(query);
										break;
									}
								}
							}
						}
					}
					setGetStreamResponse(getStreamResponse);
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
