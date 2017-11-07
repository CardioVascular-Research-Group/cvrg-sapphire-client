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

import edu.jhu.cvrg.sapphire.data.common.Device;
import edu.jhu.cvrg.sapphire.data.common.Header;
import edu.jhu.cvrg.sapphire.data.common.ResponseId;
import edu.jhu.cvrg.sapphire.data.common.SessionId;
import edu.jhu.cvrg.sapphire.data.response.SessionUpdate;
import edu.jhu.cvrg.sapphire.data.response.sessionupdate.AssignedLocation;
import edu.jhu.cvrg.sapphire.data.response.sessionupdate.DeviceStatus;
import edu.jhu.cvrg.sapphire.data.response.sessionupdate.Identifier;
import edu.jhu.cvrg.sapphire.data.response.sessionupdate.Name;
import edu.jhu.cvrg.sapphire.data.response.sessionupdate.PatientData;
import edu.jhu.cvrg.sapphire.data.response.sessionupdate.PatientInfo;
import edu.jhu.cvrg.sapphire.data.response.sessionupdate.Service;
import edu.jhu.cvrg.sapphire.data.response.sessionupdate.Services;
import edu.jhu.cvrg.sapphire.data.response.sessionupdate.Session;
import edu.jhu.cvrg.sapphire.data.response.sessionupdate.SessionList;
import edu.jhu.cvrg.sapphire.data.response.sessionupdate.State;
import edu.jhu.cvrg.sapphire.data.response.sessionupdate.UnknownId;
import edu.jhu.cvrg.sapphire.data.response.sessionupdate.Visit;
import edu.jhu.cvrg.sapphire.util.NamedNodeMapIterable;
import edu.jhu.cvrg.sapphire.util.NodeListIterator;

public class SessionUpdateXMLParser {

	private String xmlString;
	private SessionUpdate sessionUpdate;

	public SessionUpdateXMLParser() {

		this("");

	}

	public SessionUpdateXMLParser(String messageXML) {

		setXmlString(messageXML);

	}

	public String getXmlString() {
		return xmlString;
	}

	public void setXmlString(String xmlString) {
		processXML(xmlString);
		this.xmlString = xmlString;
	}

	public SessionUpdate getSessionUpdate() {
		return sessionUpdate;
	}

	public void setSessionUpdate(SessionUpdate sessionUpdate) {
		this.sessionUpdate = sessionUpdate;
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
					NodeList sessionUpdateNodes = sapphireNode.getChildNodes();
					SessionUpdate sessionUpdate = new SessionUpdate();
					for (Node sessionUpdateNode : NodeListIterator.asList(sessionUpdateNodes)) {
						if (sessionUpdateNode instanceof Element) {
							NodeList sessionUpdateSubNodes = sessionUpdateNode.getChildNodes();
							for (Node sessionUpdateSubNode : NodeListIterator.asList(sessionUpdateSubNodes)) {
								if (sessionUpdateSubNode instanceof Element) {
									switch (sessionUpdateSubNode.getNodeName()) {
									case "header": 
										Header header = new Header();
										NodeList headerSubNodes = sessionUpdateSubNode.getChildNodes();
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
										sessionUpdate.setHeader(header);
										break;
									case "sessionList": 
										SessionList sessionList = new SessionList();
										NodeList sessions = sessionUpdateSubNode.getChildNodes();
										for (Node session : NodeListIterator.asList(sessions)) {
											Session ses = new Session();
											if (session instanceof Element) {
												NodeList sessionSubNodes = session.getChildNodes();
												for (Node sessionSubNode : NodeListIterator.asList(sessionSubNodes)) {
													if (sessionSubNode instanceof Element) {
														switch (sessionSubNode.getNodeName()) {
														case "sessionID": 
															SessionId sessionId = new SessionId();
															sessionId.setValue(sessionSubNode.getAttributes().getNamedItem("V").getNodeValue());
															sessionId.setType(sessionSubNode.getAttributes().getNamedItem("type").getNodeValue());
															ses.setSessionId(sessionId);
															break;
														case "startTime":
															ses.setStartTime(sessionSubNode.getAttributes().getNamedItem("V").getNodeValue());
															break;														
														case "device":
															NodeList deviceSubNodes = sessionSubNode.getChildNodes();
															Device device = new Device();
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
																	case "manufacturerName": 
																		device.setManufacturerName(deviceSubNode.getAttributes().getNamedItem("V").getNodeValue());
																		break;
																	}
																}
															}
															ses.setDevice(device);
															break;														
														case "deviceStatus":
															NodeList deviceStatusSubNodes = sessionSubNode.getChildNodes();
															DeviceStatus deviceStatus = new DeviceStatus();
															AssignedLocation assignedLocation = new AssignedLocation();
															Services services = new Services();
															State state = new State();
															for (Node deviceStatusSubNode : NodeListIterator.asList(deviceStatusSubNodes)) {
																if (deviceStatusSubNode instanceof Element) {
																	if (deviceStatusSubNode.getNodeName().equalsIgnoreCase("assignedLocation")) {
																		NamedNodeMap patientInfoSubNodeAttributes = deviceStatusSubNode.getAttributes();
																		for (Node attribute : NamedNodeMapIterable.of(patientInfoSubNodeAttributes)) {
																			if (attribute.getNodeName().equalsIgnoreCase("xsi:type")) assignedLocation.setXsiType(attribute.getNodeValue());
																		}
																	}
																	NodeList deviceStatusSubSubNodes = deviceStatusSubNode.getChildNodes();
																	for (Node deviceStatusSubSubNode : NodeListIterator.asList(deviceStatusSubSubNodes)) {
																		if (deviceStatusSubSubNode instanceof Element) {
																			switch (deviceStatusSubNode.getNodeName()) {
																			case "assignedLocation": 
																				switch (deviceStatusSubSubNode.getNodeName()) {
																				case "unitName":
																					assignedLocation.setUnitName(deviceStatusSubSubNode.getAttributes().getNamedItem("V").getNodeValue());
																					break;
																				case "bedName": 
																					assignedLocation.setBedName(deviceStatusSubSubNode.getAttributes().getNamedItem("V").getNodeValue());
																					break;
																				}
																				break;
																			case "services": 
																				Service service = new Service();
																				service.setValue(deviceStatusSubSubNode.getAttributes().getNamedItem("V").getNodeValue());
																				ArrayList<Service> temp = services.getServices();
																				temp.add(service);
																				services.setServices(temp);
																				break;
																			case "state": 
																				switch (deviceStatusSubSubNode.getNodeName()) {
																				case "network":
																					state.setNetwork(deviceStatusSubSubNode.getAttributes().getNamedItem("V").getNodeValue());
																					break;
																				case "patientStatus": 
																					state.setPatientStatus(deviceStatusSubSubNode.getAttributes().getNamedItem("V").getNodeValue());
																					break;
																				}
																				break;
																			}
																		}
																	}
																}
															}
															deviceStatus.setAssignedLocation(assignedLocation);
															deviceStatus.setServices(services);
															deviceStatus.setState(state);
															ses.setDeviceStatus(deviceStatus);
															break;														
														case "patientInfo":
															NodeList patientInfoSubNodes = sessionSubNode.getChildNodes();
															PatientInfo patientInfo = new PatientInfo();
															Name name = new Name();
															Identifier identifier = new Identifier();
															UnknownId unknownId = new UnknownId();
															Visit visit = new Visit();
															for (Node patientInfoSubNode : NodeListIterator.asList(patientInfoSubNodes)) {
																if (patientInfoSubNode instanceof Element) {
																	NamedNodeMap patientInfoSubNodeAttributes = patientInfoSubNode.getAttributes();
																	for (Node attribute : NamedNodeMapIterable.of(patientInfoSubNodeAttributes)) {
																		if (attribute.getNodeName().equalsIgnoreCase("xsi:type")) identifier.setXsiType(attribute.getNodeValue());
																		if (attribute.getNodeName().equalsIgnoreCase("use")) name.setUse(attribute.getNodeValue());
																		if (attribute.getNodeName().equalsIgnoreCase("V")) unknownId.setValue(attribute.getNodeValue());
																	}
																	AssignedLocation patInfoAssignedLocation = new AssignedLocation();
																	PatientData patientData = new PatientData();
																	NodeList patientInfoSubSubNodes = patientInfoSubNode.getChildNodes();
																	for (Node patientInfoSubSubNode : NodeListIterator.asList(patientInfoSubSubNodes)) {
																		if (patientInfoSubSubNode instanceof Element) {
																			switch (patientInfoSubNode.getNodeName()) {
																			case "name":
																				switch (patientInfoSubSubNode.getNodeName()) {
																				case "given":
																					name.setGiven(patientInfoSubSubNode.getAttributes().getNamedItem("V").getNodeValue());
																					break;
																				case "family": 
																					name.setFamily(patientInfoSubSubNode.getAttributes().getNamedItem("V").getNodeValue());
																					break;
																				}																					
																				break;
																			case "identifer":
																				switch (patientInfoSubSubNode.getNodeName()) {
																				case "authority":
																					identifier.setAuthority(patientInfoSubSubNode.getAttributes().getNamedItem("V").getNodeValue());
																					break;
																				case "primary": 
																					identifier.setPrimary(patientInfoSubSubNode.getAttributes().getNamedItem("V").getNodeValue());
																					break;
																				case "id": 
																					identifier.setId(patientInfoSubSubNode.getAttributes().getNamedItem("V").getNodeValue());
																					break;
																				}																					
																				break;
																			}
																			if (patientInfoSubSubNode.hasAttributes()) {
																				if (patientInfoSubSubNode.getNodeName().equalsIgnoreCase("assignedLocation")) {
																					NamedNodeMap patientInfoSubSubNodeAttributes = patientInfoSubSubNode.getAttributes();
																					for (Node attribute : NamedNodeMapIterable.of(patientInfoSubSubNodeAttributes)) {
																						if (attribute.getNodeName().equalsIgnoreCase("xsi:type")) patInfoAssignedLocation.setXsiType(attribute.getNodeValue());
																					}
																				} 
																			}
																			NodeList patientInfoSubSubSubNodes = patientInfoSubSubNode.getChildNodes();
																			for (Node patientInfoSubSubSubNode : NodeListIterator.asList(patientInfoSubSubSubNodes)) {
																				if (patientInfoSubSubSubNode instanceof Element) {
																					switch (patientInfoSubSubNode.getNodeName()) {
																					case "assignedLocation": 
																						switch (patientInfoSubSubSubNode.getNodeName()) {
																						case "unitName":
																							patInfoAssignedLocation.setUnitName(patientInfoSubSubSubNode.getAttributes().getNamedItem("V").getNodeValue());
																							break;
																						case "bedName": 
																							patInfoAssignedLocation.setBedName(patientInfoSubSubSubNode.getAttributes().getNamedItem("V").getNodeValue());
																							break;
																						}
																						break;
																					case "patientData":
																						break;
																					}
																				}
																			}
																		}
																	}
																	visit.setAssignedLocation(patInfoAssignedLocation);
																	visit.setPatientData(patientData);
																}
															}
															patientInfo.setName(name);
															patientInfo.setUnknownId(unknownId);
															patientInfo.setIdentifier(identifier);
															patientInfo.setVisit(visit);
															ses.setPatientInfo(patientInfo);
															break;														
														}
													}									
												}
												ArrayList<Session> temp = sessionList.getSessions();
												temp.add(ses);
												sessionList.setSessions(temp);
											}
										}
										sessionUpdate.setSessionList(sessionList);
										setSessionUpdate(sessionUpdate);
										break;
									}
								}
							}
						}
					}
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
