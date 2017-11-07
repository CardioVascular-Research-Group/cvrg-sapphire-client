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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import edu.jhu.cvrg.sapphire.data.common.Header;
import edu.jhu.cvrg.sapphire.data.common.ResponseId;
import edu.jhu.cvrg.sapphire.data.common.SessionId;
import edu.jhu.cvrg.sapphire.data.response.BinEnd;
import edu.jhu.cvrg.sapphire.data.response.bindescriptor.Block;
import edu.jhu.cvrg.sapphire.data.response.bindescriptor.BlockSQN;
import edu.jhu.cvrg.sapphire.data.response.bindescriptor.FormatId;
import edu.jhu.cvrg.sapphire.util.NamedNodeMapIterable;
import edu.jhu.cvrg.sapphire.util.NodeListIterator;

public class BinEndXMLParser {

	private String xmlString;
	private BinEnd binEnd;

	public BinEndXMLParser() {

		this("");

	}

	public BinEndXMLParser(String messageXML) {

		setXmlString(messageXML);

	}

	public String getXmlString() {
		return xmlString;
	}

	public void setXmlString(String xmlString) {
		processXML(xmlString);
		this.xmlString = xmlString;
	}

	public BinEnd getBinEnd() {
		return binEnd;
	}

	public void setBinEnd(BinEnd binEnd) {
		this.binEnd = binEnd;
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
					NodeList binEndNodes = sapphireNode.getChildNodes();
					BinEnd binEnd = new BinEnd();
					for (Node binEndNode : NodeListIterator.asList(binEndNodes)) {
						if (binEndNode instanceof Element) {
							NamedNodeMap binEndNodeAttributes = binEndNode.getAttributes();
							for (Node attribute : NamedNodeMapIterable.of(binEndNodeAttributes)) {
								switch (attribute.getNodeName()) {
								case "message": 
									binEnd.setMessage(attribute.getNodeValue());
									break;
								}
							}
							NodeList binEndSubNodes = binEndNode.getChildNodes();
							for (Node binEndSubNode : NodeListIterator.asList(binEndSubNodes)) {
								if (binEndSubNode instanceof Element) {
									switch (binEndSubNode.getNodeName()) {
									case "header": 
										Header header = binEnd.getHeader();
										NodeList headerSubNodes = binEndSubNode.getChildNodes();
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
										binEnd.setHeader(header);
										break;
									case "block":
										Block block = binEnd.getBlock();
										NodeList blockSubNodes = binEndSubNode.getChildNodes();
										for (Node blockSubNode : NodeListIterator.asList(blockSubNodes)) {
											if (blockSubNode instanceof Element) {
												switch (blockSubNode.getNodeName()) {
												case "formatID": 
													FormatId formatId = block.getFormatId();
													NamedNodeMap formatIdNodeAttributes = blockSubNode.getAttributes();
													for (Node attribute : NamedNodeMapIterable.of(formatIdNodeAttributes)) {
														switch (attribute.getNodeName()) {
														case "V": 
															formatId.setValue(new Integer(attribute.getNodeValue()).intValue());
															break;
														}
													}
													block.setFormatId(formatId);
													break;
												case "blockSQN":
													BlockSQN blockSQN = block.getBlockSQN();
													NamedNodeMap blockSQNNodeAttributes = blockSubNode.getAttributes();
													for (Node attribute : NamedNodeMapIterable.of(blockSQNNodeAttributes)) {
														switch (attribute.getNodeName()) {
														case "V": 
															blockSQN.setInitialBinaryValue(new Integer(attribute.getNodeValue()).intValue());
															break;
														}
													}
													block.setBlockSQN(blockSQN);
													break;
												}
											}
										}
										binEnd.setBlock(block);
										break;
									}
								}
							}
						}
					}
					setBinEnd(binEnd);
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
