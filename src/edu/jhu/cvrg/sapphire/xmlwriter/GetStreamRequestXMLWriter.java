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
import java.util.ArrayList;
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
import edu.jhu.cvrg.sapphire.data.common.Query;
import edu.jhu.cvrg.sapphire.data.common.SelectNodes;
import edu.jhu.cvrg.sapphire.data.common.SessionId;
import edu.jhu.cvrg.sapphire.data.common.SessionIdList;
import edu.jhu.cvrg.sapphire.data.request.GetStreamRequest;

public class GetStreamRequestXMLWriter {

	private String xmlString, sessionId, bedSessionId;
	private GetStreamRequest getStreamRequest;
	
	public GetStreamRequestXMLWriter() {

		this("000000FFFE00000000000000");
		
	}

	public GetStreamRequestXMLWriter(String sessionId) {

		this(sessionId, "000000FFFE00000000000000");
		
	}
	
	public GetStreamRequestXMLWriter(String sessionId, String bedSessionId) {
		
		this(sessionId, bedSessionId, "wav");

	}	
	
	public GetStreamRequestXMLWriter(String sessionId, String bedSessionId, String paramsRequest) {
		
		this(sessionId, bedSessionId, paramsRequest, "500");

	}	
	
	public GetStreamRequestXMLWriter(String sesssionId, String bedSessionId, String paramsRequest, String requestId) {

		setXmlString("");
		setSessionId(sesssionId);
		setBedSessionId(bedSessionId);
		setGetStreamRequest(new GetStreamRequest(), paramsRequest, requestId);
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

	public String getBedSessionId() {
		return bedSessionId;
	}

	public void setBedSessionId(String bedSessionId) {
		this.bedSessionId = bedSessionId;
	}

	public GetStreamRequest getGetStreamRequest() {
		return getStreamRequest;
	}
	
	public void setGetStreamRequest(GetStreamRequest getStreamRequest, String paramsRequest, String requestId) {
		
		Header header = new Header();
		SessionId sessionIdLocal = new SessionId();
		sessionIdLocal.setValue(getSessionId());
		sessionIdLocal.setType("EUI-64:NTPS-32");
		header.setSessionId(sessionIdLocal);
		header.setMsgCHN("0");
		header.setMsgSQN("4");
		header.setRequestId(requestId);
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		Date now = new Date();
		header.setCreationDateTime(sdfDate.format(now));
		getStreamRequest.setHeader(header);
		Query query = new Query();
		query.setEndTime("*");
		SessionIdList sessionIdList = new SessionIdList();
		ArrayList<SessionId> sessionIds = new ArrayList<SessionId>();
		SessionId bedSession = new SessionId();
		bedSession.setValue(getBedSessionId());
		bedSession.setType("IP-32:PORT-32:NTPS-32");
		sessionIds.add(bedSession);
		sessionIdList.setSessionIds(sessionIds);
		query.setSessionIdList(sessionIdList);
		SelectNodes selectNodes = new SelectNodes();
		selectNodes.setSelect("//params/*/"+ paramsRequest + "/*");
		query.setSelectNodes(selectNodes);
		getStreamRequest.setQuery(query);
		this.getStreamRequest = getStreamRequest;
		
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
			
			GetStreamRequest getStreamRequest = getGetStreamRequest();
			Element getStreamRequestElement = doc.createElement("getStreamRequest");
			sapphireElement.appendChild(getStreamRequestElement);

			Attr getStreamRequestXmlns = doc.createAttribute("xmlns");
			getStreamRequestXmlns.setValue(getStreamRequest.getXmlNameSpace());
			getStreamRequestElement.setAttributeNode(getStreamRequestXmlns);

			Header header = getStreamRequest.getHeader();
			Element headerElement = doc.createElement("header");
			getStreamRequestElement.appendChild(headerElement);

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

			Element requestIdElement = doc.createElement("requestID");
			headerElement.appendChild(requestIdElement);

			Attr requestIdValue = doc.createAttribute("V");
			requestIdValue.setValue(header.getRequestId());
			requestIdElement.setAttributeNode(requestIdValue);
			
			Element creationDateTimeElement = doc.createElement("creationDateTime");
			headerElement.appendChild(creationDateTimeElement);

			Attr creationDateTimeValue = doc.createAttribute("V");
			creationDateTimeValue.setValue(header.getCreationDateTime());
			creationDateTimeElement.setAttributeNode(creationDateTimeValue);
			
			Query query = getStreamRequest.getQuery();
			Element queryElement = doc.createElement("query");
			getStreamRequestElement.appendChild(queryElement);

			Attr queryXmlns = doc.createAttribute("xmlns");
			queryXmlns.setValue(query.getXmlNameSpace());
			queryElement.setAttributeNode(queryXmlns);
			
			Element endTimeElement = doc.createElement("endTime");
			queryElement.appendChild(endTimeElement);
			
			Attr endTimeValue = doc.createAttribute("V");
			endTimeValue.setValue(query.getEndTime());
			endTimeElement.setAttributeNode(endTimeValue);
			
			Element sessionIdListElement = doc.createElement("sessionIDList");
			queryElement.appendChild(sessionIdListElement);
			
			for (SessionId sessionId : query.getSessionIdList().getSessionIds()) {
				
				Element sessionIdsElement = doc.createElement("sessionID");
				
				Attr sessionIdsValue = doc.createAttribute("V");
				sessionIdsValue.setValue(sessionId.getValue());
				sessionIdsElement.setAttributeNode(sessionIdsValue);
				
				Attr sessionIdsType = doc.createAttribute("type");
				sessionIdsType.setValue(sessionId.getType());
				sessionIdsElement.setAttributeNode(sessionIdsType);
				
				sessionIdListElement.appendChild(sessionIdsElement);
				
			}

			Element selectNodesElement = doc.createElement("selectNodes");
			queryElement.appendChild(selectNodesElement);
			
			Attr selectNodesValue = doc.createAttribute("select");
			selectNodesValue.setValue(query.getSelectNodes().getSelect());
			selectNodesElement.setAttributeNode(selectNodesValue);
			
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
