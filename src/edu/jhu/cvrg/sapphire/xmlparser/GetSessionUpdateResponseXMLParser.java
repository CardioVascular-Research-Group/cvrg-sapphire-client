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
import edu.jhu.cvrg.sapphire.data.response.GetSessionUpdateResponse;
import edu.jhu.cvrg.sapphire.util.NamedNodeMapIterable;
import edu.jhu.cvrg.sapphire.util.NodeListIterator;

public class GetSessionUpdateResponseXMLParser {

	private String xmlString;
	private GetSessionUpdateResponse getSessionUpdateResponse;

	public GetSessionUpdateResponseXMLParser() {

		this("");

	}

	public GetSessionUpdateResponseXMLParser(String messageXML) {

		setXmlString(messageXML);

	}

	public String getXmlString() {
		return xmlString;
	}

	public void setXmlString(String xmlString) {
		processXML(xmlString);
		this.xmlString = xmlString;
	}

	public GetSessionUpdateResponse getGetSessionUpdateResponse() {
		return getSessionUpdateResponse;
	}

	public void setGetSessionUpdateResponse(GetSessionUpdateResponse getSessionUpdateResponse) {
		this.getSessionUpdateResponse = getSessionUpdateResponse;
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
					NodeList getSessionUpdateResponseNodes = sapphireNode.getChildNodes();
					GetSessionUpdateResponse getSessionUpdateResponse = new GetSessionUpdateResponse();
					for (Node getSessionUpdateResponseNode : NodeListIterator.asList(getSessionUpdateResponseNodes)) {
						if (getSessionUpdateResponseNode instanceof Element) {
							NamedNodeMap getSessionUpdateResponseNodeAttributes = getSessionUpdateResponseNode.getAttributes();
							for (Node attribute : NamedNodeMapIterable.of(getSessionUpdateResponseNodeAttributes)) {
								switch (attribute.getNodeName()) {
								case "status": 
									getSessionUpdateResponse.setStatus(attribute.getNodeValue());
									break;
								case "errorString": 
									getSessionUpdateResponse.setErrorString(attribute.getNodeValue());
									break;
								}
							}
							NodeList getSessionUpdateResponseSubNodes = getSessionUpdateResponseNode.getChildNodes();
							for (Node getSessionUpdateResponseSubNode : NodeListIterator.asList(getSessionUpdateResponseSubNodes)) {
								if (getSessionUpdateResponseSubNode instanceof Element) {
									switch (getSessionUpdateResponseSubNode.getNodeName()) {
									case "header": 
										Header header = new Header();
										NodeList headerSubNodes = getSessionUpdateResponseSubNode.getChildNodes();
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
										getSessionUpdateResponse.setHeader(header);
										break;
									case "query":
										Query query = new Query();
										NodeList querySubNodes = getSessionUpdateResponseSubNode.getChildNodes();
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
										getSessionUpdateResponse.setQuery(query);
										break;
									}
								}
							}
						}
					}
					setGetSessionUpdateResponse(getSessionUpdateResponse);
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
