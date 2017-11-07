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
import java.util.HashMap;

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
import edu.jhu.cvrg.sapphire.data.common.ResponseId;
import edu.jhu.cvrg.sapphire.data.common.SessionId;
import edu.jhu.cvrg.sapphire.data.common.StartDateTime;
import edu.jhu.cvrg.sapphire.data.response.BinDescriptor;
import edu.jhu.cvrg.sapphire.data.response.bindescriptor.Block;
import edu.jhu.cvrg.sapphire.data.response.bindescriptor.BlockLength;
import edu.jhu.cvrg.sapphire.data.response.bindescriptor.BlockSQN;
import edu.jhu.cvrg.sapphire.data.response.bindescriptor.FormatId;
import edu.jhu.cvrg.sapphire.data.response.bindescriptor.GroupBT;
import edu.jhu.cvrg.sapphire.data.response.bindescriptor.Parameter;
import edu.jhu.cvrg.sapphire.data.response.bindescriptor.ParameterSet;
import edu.jhu.cvrg.sapphire.data.response.bindescriptor.Parameters;
import edu.jhu.cvrg.sapphire.data.response.bindescriptor.SessionId_BT;
import edu.jhu.cvrg.sapphire.data.response.bindescriptor.SubParameterInfo;
import edu.jhu.cvrg.sapphire.util.NamedNodeMapIterable;
import edu.jhu.cvrg.sapphire.util.NodeListIterator;

public class BinDescriptorXMLParser {

	private String xmlString;
	private BinDescriptor binDescriptor;
	private boolean highSpeed;
	private enum ParameterSetName {bis,cco,co,ecg,ecgResp,eeg,entropy,gasDeliv,gasMon,icg,infusor,ip,nbp,nico,resp,saO2,spO2,svO2,tcO2,temp,urimeter;

		public static boolean contains(String s) {
			for(ParameterSetName parameterSetName: values())
				if (parameterSetName.name().equals(s)) 
					return true;
			return false;
		} 
	};

	private enum ParameterName {cfg,num,wav;

		public static boolean contains(String s) {
			for(ParameterName parameterName: values())
				if (parameterName.name().equals(s)) 
					return true;
			return false;
		} 
	};

	private enum SubParameterInfoName {defibSyncMarker,ecgWaveformMXG,ipWaveform,paceMarker,pleth,qrsMarker,heartRate,v_p_cRate,stDeviationMXG,pawMeasurements,systolic,mean,diastolic,pulseRate,satO2,nbpMeasurements,cuffPressure,respRate,temp,respGas,ambientPressure;

		public static boolean contains(String s) {
			for(SubParameterInfoName subParameterInfoName: values())
				if (subParameterInfoName.name().equals(s)) 
					return true;
			return false;
		} 
	};

	public BinDescriptorXMLParser() {

		this("");

	}

	public BinDescriptorXMLParser(String messageXML) {

		this(messageXML, true);

	}

	public BinDescriptorXMLParser(String messageXML, boolean highSpeed) {

		setHighSpeed(highSpeed);
		setXmlString(messageXML);

	}

	public String getXmlString() {
		return xmlString;
	}

	public void setXmlString(String xmlString) {
		processXML(xmlString);
		this.xmlString = xmlString;
	}

	public BinDescriptor getBinDescriptor() {
		return binDescriptor;
	}

	public void setBinDescriptor(BinDescriptor binDescriptor) {
		this.binDescriptor = binDescriptor;
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
					NodeList binDescriptorNodes = sapphireNode.getChildNodes();
					BinDescriptor binDescriptor = new BinDescriptor();
					for (Node binDescriptorNode : NodeListIterator.asList(binDescriptorNodes)) {
						if (binDescriptorNode instanceof Element) {
							NodeList binDescriptorSubNodes = binDescriptorNode.getChildNodes();
							for (Node binDescriptorSubNode : NodeListIterator.asList(binDescriptorSubNodes)) {
								if (binDescriptorSubNode instanceof Element) {
									switch (binDescriptorSubNode.getNodeName()) {
									case "header": 
										Header header = new Header();
										NodeList headerSubNodes = binDescriptorSubNode.getChildNodes();
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
										binDescriptor.setHeader(header);
										break;
									case "block":
										Block block = binDescriptor.getBlock();
										NamedNodeMap binDescriptorSubNodeAttributes = binDescriptorSubNode.getAttributes();
										for (Node attribute : NamedNodeMapIterable.of(binDescriptorSubNodeAttributes)) {
											switch (attribute.getNodeName()) {
											case "rate": 
												block.setRate(new Integer(attribute.getNodeValue()).intValue());
												break;
											case "U": 
												block.setUnits(attribute.getNodeValue());
												break;
											case "repeat": 
												block.setRepeat(attribute.getNodeValue());
												break;
											}
										}
										NodeList blockSubNodes = binDescriptorSubNode.getChildNodes();
										for (Node blockSubNode : NodeListIterator.asList(blockSubNodes)) {
											if (blockSubNode instanceof Element) {
												switch (blockSubNode.getNodeName()) {
												case "formatID": 
													FormatId formatId = block.getFormatId();
													NamedNodeMap formatIdNodeAttributes = blockSubNode.getAttributes();
													for (Node attribute : NamedNodeMapIterable.of(formatIdNodeAttributes)) {
														switch (attribute.getNodeName()) {
														case "BT": 
															formatId.setBinaryType(attribute.getNodeValue());
															break;
														case "iBV": 
															formatId.setInitialBinaryValue(new Integer(attribute.getNodeValue()).intValue());
															break;
														}
													}
													block.setFormatId(formatId);
													break;
												case "sessionID":
													SessionId_BT sessionId_BT = block.getSessionId_BT();
													NamedNodeMap sessionIdNodeAttributes = blockSubNode.getAttributes();
													for (Node attribute : NamedNodeMapIterable.of(sessionIdNodeAttributes)) {
														switch (attribute.getNodeName()) {
														case "BT": 
															sessionId_BT.setBinaryType(attribute.getNodeValue());
															break;
														case "asizeBT": 
															sessionId_BT.setArraySizeBinaryType(attribute.getNodeValue());
															break;
														case "type": 
															sessionId_BT.setSessionType(attribute.getNodeValue());
															break;
														}
													}
													block.setSessionId_BT(sessionId_BT);
													break;
												case "blockSQN":
													BlockSQN blockSQN = block.getBlockSQN();
													NamedNodeMap blockSQNNodeAttributes = blockSubNode.getAttributes();
													for (Node attribute : NamedNodeMapIterable.of(blockSQNNodeAttributes)) {
														switch (attribute.getNodeName()) {
														case "BT": 
															blockSQN.setBinaryType(attribute.getNodeValue());
															break;
														case "iBV": 
															blockSQN.setInitialBinaryValue(new Integer(attribute.getNodeValue()).intValue());
															break;
														}
													}
													block.setBlockSQN(blockSQN);
													break;
												case "blockLength":
													BlockLength blockLength = block.getBlockLength();
													NamedNodeMap blockLengthNodeAttributes = blockSubNode.getAttributes();
													for (Node attribute : NamedNodeMapIterable.of(blockLengthNodeAttributes)) {
														switch (attribute.getNodeName()) {
														case "BT": 
															blockLength.setBinaryType(attribute.getNodeValue());
															break;
														}
													}
													block.setBlockLength(blockLength);
													break;
												case "startDateTime":
													StartDateTime startDateTime = block.getStartDateTime();
													NamedNodeMap startDateTimeNodeAttributes = blockSubNode.getAttributes();
													for (Node attribute : NamedNodeMapIterable.of(startDateTimeNodeAttributes)) {
														switch (attribute.getNodeName()) {
														case "BT": 
															startDateTime.setBinaryType(attribute.getNodeValue());
															break;
														case "INV": 
															startDateTime.setInvalidAttribute(new Integer(attribute.getNodeValue()).intValue());
															break;
														}
													}
													block.setStartDateTime(startDateTime);
													break;
												case "groupBT":
													GroupBT groupBT = block.getGroupBT();
													NamedNodeMap groupBTNodeAttributes = blockSubNode.getAttributes();
													for (Node attribute : NamedNodeMapIterable.of(groupBTNodeAttributes)) {
														switch (attribute.getNodeName()) {
														case "repeat": 
															groupBT.setRepeat(new Integer(attribute.getNodeValue()).intValue());
															break;
														}
													}
													Parameters localParameters = groupBT.getParameters();
													ArrayList<ParameterSet> localParameterSets = localParameters.getParameterSets();
													NodeList groupBTNodes = blockSubNode.getChildNodes();
													for (Node groupBTNode : NodeListIterator.asList(groupBTNodes)) {
														if (groupBTNode instanceof Element) {
															NodeList parameterSets = groupBTNode.getChildNodes();
															for (Node parameterSet : NodeListIterator.asList(parameterSets)) {
																if (ParameterSetName.contains(parameterSet.getNodeName())) {
																	ParameterSet localParameterSet = new ParameterSet();
																	localParameterSet.setParameterSetType(parameterSet.getNodeName());
																	NamedNodeMap parameterSetAttributes = parameterSet.getAttributes();
																	for (Node attribute : NamedNodeMapIterable.of(parameterSetAttributes)) {
																		switch (attribute.getNodeName()) {
																		case "index": 
																			localParameterSet.setParameterSetCount(attribute.getNodeValue());
																			break;
																		case "label":
																			localParameterSet.setParameterSetLabel(attribute.getNodeValue());
																			break;
																		case "site":
																			localParameterSet.setParameterSetSite(attribute.getNodeValue());										
																		case "source": 
																			localParameterSet.setParameterSetSource(attribute.getNodeValue());
																			break;
																		}
																	}
																	NodeList parameters = parameterSet.getChildNodes();
																	ArrayList<Parameter> localParameterArray = localParameterSet.getParameters();
																	for (Node parameter : NodeListIterator.asList(parameters)) {
																		if (ParameterName.contains(parameter.getNodeName())) {
																			Parameter localParameter = new Parameter();
																			localParameter.setParameterType(parameter.getNodeName());
																			NodeList subParameters = parameter.getChildNodes();
																			ArrayList<SubParameterInfo> localSubParameterArray = localParameter.getSubParameterInfo();
																			for (Node subParameter : NodeListIterator.asList(subParameters)) {
																				if (SubParameterInfoName.contains(subParameter.getNodeName())) {
																					SubParameterInfo localSubParameterInfo = new SubParameterInfo();
																					localSubParameterInfo.setSubParameterInfoType(subParameter.getNodeName());
																					if (!(localParameterSet.getParameterSetLabel().equalsIgnoreCase("unknown")))
																						localSubParameterInfo.setSubParameterInfoType(subParameter.getNodeName() + localParameterSet.getParameterSetLabel());
																					if ((subParameter.getNodeName().endsWith("MXG")) || (subParameter.getNodeName().endsWith("Gas"))) {
																						NodeList subSubParameters = subParameter.getChildNodes();
																						for (Node subSubParameter : NodeListIterator.asList(subSubParameters)) {
																							if (!subSubParameter.getNodeName().equalsIgnoreCase("#text")) {
																								localSubParameterInfo = new SubParameterInfo();
																								NamedNodeMap subParameterInfoAttributes = subSubParameter.getAttributes();
																								for (Node attribute : NamedNodeMapIterable.of(subParameterInfoAttributes)) {
																									switch (attribute.getNodeName()) {
																									case "BT":
																										localSubParameterInfo.setBinaryType(attribute.getNodeValue());
																										break;
																									case "INV":
																										localSubParameterInfo.setInvalidAttributes(attribute.getNodeValue());
																										break;
																									case "INV_LE":
																										localSubParameterInfo.setInvalidLowerEnd(attribute.getNodeValue());
																										break;
																									case "S": 
																										localSubParameterInfo.setScaleFactor(attribute.getNodeValue());																
																										break;
																									case "U":
																										localSubParameterInfo.setUnits(attribute.getNodeValue());
																										break;
																									case "asizeBT":
																										localSubParameterInfo.setArraySizeBinaryType(attribute.getNodeValue());
																										break;
																									case "lead":
																										localSubParameterInfo.setLeadName(attribute.getNodeValue());
																										localSubParameterInfo.setSubParameterInfoType(subSubParameter.getNodeName() + attribute.getNodeValue());
																										String localSubParameterName = localSubParameterInfo.getSubParameterInfoType();
																										localSubParameterInfo.setSubParameterInfoType("carescape." + localParameterSet.getParameterSetType());
																										if (!(localParameterSet.getParameterSetLabel().equalsIgnoreCase("unknown"))) {
																											localSubParameterInfo.setSubParameterInfoType(localSubParameterInfo.getSubParameterInfoType() + "." + localParameterSet.getParameterSetSite());
																										} else {
																											localSubParameterInfo.setSubParameterInfoType(localSubParameterInfo.getSubParameterInfoType() + ".index" + localParameterSet.getParameterSetCount());														
																										}
																										localSubParameterInfo.setSubParameterInfoType(localSubParameterInfo.getSubParameterInfoType() + "." + localSubParameterName + "." + localSubParameterInfo.getUnits().replaceAll("%", "percent").replaceAll("\\/min", "").replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\{", "").replaceAll("\\}", ""));
																										if (block.getRate() > 1) {
																											localSubParameterInfo.setSubParameterInfoType(localSubParameterInfo.getSubParameterInfoType() + ".perMs");
																										} else {
																											localSubParameterInfo.setSubParameterInfoType(localSubParameterInfo.getSubParameterInfoType() + ".perSec");															
																										}

																										HashMap<String, Double> temp = binDescriptor.getParameterIntervals();
																										double interval = 1000.0 / (new Double(block.getRate()).doubleValue() * new Double(localSubParameterInfo.getArraySizeBinaryType()).doubleValue());
																										temp.put(localSubParameterInfo.getSubParameterInfoType(), new Double(interval));														
																										binDescriptor.setParameterIntervals(temp);
																										break;
																									case "mapINV":
																										localSubParameterInfo.setInvalidMapping(attribute.getNodeValue());
																										break;
																									case "respGas":
																										localSubParameterInfo.setSubParameterInfoType(subSubParameter.getNodeName() + attribute.getNodeValue());
																										localSubParameterName = localSubParameterInfo.getSubParameterInfoType();
																										localSubParameterInfo.setSubParameterInfoType("carescape." + localParameterSet.getParameterSetType());
																										if (!(localParameterSet.getParameterSetLabel().equalsIgnoreCase("unknown"))) {
																											localSubParameterInfo.setSubParameterInfoType(localSubParameterInfo.getSubParameterInfoType() + "." + localParameterSet.getParameterSetSite());
																										} else {
																											localSubParameterInfo.setSubParameterInfoType(localSubParameterInfo.getSubParameterInfoType() + ".index" + localParameterSet.getParameterSetCount());														
																										}
																										localSubParameterInfo.setSubParameterInfoType(localSubParameterInfo.getSubParameterInfoType() + "." + localSubParameterName + "." + localSubParameterInfo.getUnits().replaceAll("%", "percent").replaceAll("\\/min", "").replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\{", "").replaceAll("\\}", ""));
																										if (block.getRate() > 1) {
																											localSubParameterInfo.setSubParameterInfoType(localSubParameterInfo.getSubParameterInfoType() + ".perMs");
																										} else {
																											localSubParameterInfo.setSubParameterInfoType(localSubParameterInfo.getSubParameterInfoType() + ".perSec");															
																										}
																										temp = binDescriptor.getParameterIntervals();
																										interval = 1000.0 / (new Double(block.getRate()).doubleValue() * new Double(localSubParameterInfo.getArraySizeBinaryType()).doubleValue());
																										temp.put(localSubParameterInfo.getSubParameterInfoType(), new Double(interval));														
																										binDescriptor.setParameterIntervals(temp);
																										break;
																									}
																								}
																								localSubParameterArray.add(localSubParameterInfo);
																							}
																						}
																					} else if (subParameter.getNodeName().endsWith("Measurements")) {
																						String measurementBinaryType = subParameter.getAttributes().getNamedItem("asizeBT").getNodeValue();
																						String measurementInfoType = "carescape." + localParameterSet.getParameterSetType() + "." + localParameterSet.getParameterSetSite() + "." + subParameter.getNodeName() + "." + localSubParameterInfo.getBinaryType() + ".perSec:";
																						localSubParameterInfo.setBinaryType(measurementBinaryType);
																						localSubParameterInfo.setSubParameterInfoType(measurementInfoType);
																						localSubParameterInfo.setArraySizeBinaryType("1");
																						HashMap<String, Double> temp = binDescriptor.getParameterIntervals();
																						double interval = 1000.0 / (new Double(block.getRate()).doubleValue() * new Double(localSubParameterInfo.getArraySizeBinaryType()).doubleValue());
																						temp.put(localSubParameterInfo.getSubParameterInfoType(), new Double(interval));														
																						binDescriptor.setParameterIntervals(temp);
																						localSubParameterArray.add(localSubParameterInfo);
																						NodeList subSubParameters = subParameter.getChildNodes();
																						for (Node subSubParameter : NodeListIterator.asList(subSubParameters)) {
																							if (subSubParameter.getNodeName().equalsIgnoreCase("episodic")) {
																								NodeList subSubSubParameters = subSubParameter.getChildNodes();
																								for (Node subSubSubParameter : NodeListIterator.asList(subSubSubParameters)) {
																									if (!subSubSubParameter.getNodeName().equalsIgnoreCase("#text")) {
																										localSubParameterInfo = new SubParameterInfo();
																										if (!(localParameterSet.getParameterSetLabel().equalsIgnoreCase("unknown"))) {
																											localSubParameterInfo.setSubParameterInfoType(subSubSubParameter.getNodeName() + localParameterSet.getParameterSetLabel());
																										} else {
																											localSubParameterInfo.setSubParameterInfoType(subSubSubParameter.getNodeName());
																										}
																										NamedNodeMap subParameterInfoAttributes = subSubSubParameter.getAttributes();
																										for (Node attribute : NamedNodeMapIterable.of(subParameterInfoAttributes)) {
																											switch (attribute.getNodeName()) {
																											case "BT":
																												localSubParameterInfo.setBinaryType(attribute.getNodeValue());
																												break;
																											case "INV":
																												localSubParameterInfo.setInvalidAttributes(attribute.getNodeValue());
																												if (localSubParameterInfo.getSubParameterInfoType().startsWith("date")) {
																													String localSubParameterName = localSubParameterInfo.getSubParameterInfoType();
																													localSubParameterInfo.setSubParameterInfoType("carescape." + localParameterSet.getParameterSetType());
																													if (!(localParameterSet.getParameterSetLabel().equalsIgnoreCase("unknown"))) {
																														localSubParameterInfo.setSubParameterInfoType(localSubParameterInfo.getSubParameterInfoType() + "." + localParameterSet.getParameterSetSite());
																													} else {
																														localSubParameterInfo.setSubParameterInfoType(localSubParameterInfo.getSubParameterInfoType() + ".index" + localParameterSet.getParameterSetCount());														
																													}
																													localSubParameterInfo.setSubParameterInfoType(localSubParameterInfo.getSubParameterInfoType() + "." + localSubParameterName + "." + localSubParameterInfo.getUnits().replaceAll("%", "percent").replaceAll("\\/min", "").replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\{", "").replaceAll("\\}", ""));
																													if (block.getRate() > 1) {
																														localSubParameterInfo.setSubParameterInfoType(localSubParameterInfo.getSubParameterInfoType() + ".perMs");
																													} else {
																														localSubParameterInfo.setSubParameterInfoType(localSubParameterInfo.getSubParameterInfoType() + ".perSec");															
																													}
																													temp = binDescriptor.getParameterIntervals();
																													interval = 1000.0 / (new Double(block.getRate()).doubleValue() * new Double(localSubParameterInfo.getArraySizeBinaryType()).doubleValue());
																													temp.put(localSubParameterInfo.getSubParameterInfoType(), new Double(interval));														
																													binDescriptor.setParameterIntervals(temp);
																												}
																												break;
																											case "S": 
																												localSubParameterInfo.setScaleFactor(attribute.getNodeValue());																
																												break;
																											case "U":
																												localSubParameterInfo.setUnits(attribute.getNodeValue());
																												String localSubParameterName = localSubParameterInfo.getSubParameterInfoType();
																												localSubParameterInfo.setSubParameterInfoType("carescape." + localParameterSet.getParameterSetType());
																												if (!(localParameterSet.getParameterSetLabel().equalsIgnoreCase("unknown"))) {
																													localSubParameterInfo.setSubParameterInfoType(localSubParameterInfo.getSubParameterInfoType() + "." + localParameterSet.getParameterSetSite());
																												} else {
																													localSubParameterInfo.setSubParameterInfoType(localSubParameterInfo.getSubParameterInfoType() + ".index" + localParameterSet.getParameterSetCount());														
																												}
																												localSubParameterInfo.setSubParameterInfoType(localSubParameterInfo.getSubParameterInfoType() + "." + localSubParameterName + "." + localSubParameterInfo.getUnits().replaceAll("%", "percent").replaceAll("\\/min", "").replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\{", "").replaceAll("\\}", ""));
																												if (block.getRate() > 1) {
																													localSubParameterInfo.setSubParameterInfoType(localSubParameterInfo.getSubParameterInfoType() + ".perMs");
																												} else {
																													localSubParameterInfo.setSubParameterInfoType(localSubParameterInfo.getSubParameterInfoType() + ".perSec");															
																												}
																												temp = binDescriptor.getParameterIntervals();
																												interval = 1000.0 / (new Double(block.getRate()).doubleValue() * new Double(localSubParameterInfo.getArraySizeBinaryType()).doubleValue());
																												temp.put(localSubParameterInfo.getSubParameterInfoType(), new Double(interval));														
																												binDescriptor.setParameterIntervals(temp);
																												break;
																											}
																										}
																										localSubParameterInfo.setSubParameterInfoType(localSubParameterInfo.getSubParameterInfoType() + ":" + subParameter.getNodeName());
																										localSubParameterArray.add(localSubParameterInfo);
																									}
																								}
																							}
																						}																						
																					} else {
																						NamedNodeMap subParameterInfoAttributes = subParameter.getAttributes();
																						for (Node attribute : NamedNodeMapIterable.of(subParameterInfoAttributes)) {
																							switch (attribute.getNodeName()) {
																							case "BT":
																								localSubParameterInfo.setBinaryType(attribute.getNodeValue());
																								break;
																							case "INV":
																								localSubParameterInfo.setInvalidAttributes(attribute.getNodeValue());
																								break;
																							case "INV_LE":
																								localSubParameterInfo.setInvalidLowerEnd(attribute.getNodeValue());
																								break;
																							case "S": 
																								localSubParameterInfo.setScaleFactor(attribute.getNodeValue());																
																								break;
																							case "U":
																								localSubParameterInfo.setUnits(attribute.getNodeValue());
																								break;
																							case "asizeBT":
																								localSubParameterInfo.setArraySizeBinaryType(attribute.getNodeValue());
																								String localSubParameterName = localSubParameterInfo.getSubParameterInfoType();
																								localSubParameterInfo.setSubParameterInfoType("carescape." + localParameterSet.getParameterSetType());
																								if (!(localParameterSet.getParameterSetLabel().equalsIgnoreCase("unknown"))) {
																									localSubParameterInfo.setSubParameterInfoType(localSubParameterInfo.getSubParameterInfoType() + "." + localParameterSet.getParameterSetSite());
																								} else {
																									localSubParameterInfo.setSubParameterInfoType(localSubParameterInfo.getSubParameterInfoType() + ".index" + localParameterSet.getParameterSetCount());														
																								}
																								localSubParameterInfo.setSubParameterInfoType(localSubParameterInfo.getSubParameterInfoType() + "." + localSubParameterName + "." + localSubParameterInfo.getUnits().replaceAll("%", "percent").replaceAll("\\/min", "").replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\{", "").replaceAll("\\}", ""));
																								if (block.getRate() > 1) {
																									localSubParameterInfo.setSubParameterInfoType(localSubParameterInfo.getSubParameterInfoType() + ".perMs");
																								} else {
																									localSubParameterInfo.setSubParameterInfoType(localSubParameterInfo.getSubParameterInfoType() + ".perSec");															
																								}
																								HashMap<String, Double> temp = binDescriptor.getParameterIntervals();
																								double interval = 1000.0 / (new Double(block.getRate()).doubleValue() * new Double(localSubParameterInfo.getArraySizeBinaryType()).doubleValue());
																								temp.put(localSubParameterInfo.getSubParameterInfoType(), new Double(interval));
																								binDescriptor.setParameterIntervals(temp);
																								break;
																							case "mapINV":
																								localSubParameterInfo.setInvalidMapping(attribute.getNodeValue());
																								if(!isHighSpeed() && !localParameterSet.getParameterSetType().equalsIgnoreCase("ecgResp")) {																									
																									localSubParameterName = localSubParameterInfo.getSubParameterInfoType();
																									localSubParameterInfo.setSubParameterInfoType("carescape." + localParameterSet.getParameterSetType());
																									if (!(localParameterSet.getParameterSetLabel().equalsIgnoreCase("unknown"))) {
																										localSubParameterInfo.setSubParameterInfoType(localSubParameterInfo.getSubParameterInfoType() + "." + localParameterSet.getParameterSetSite());
																									} else {
																										localSubParameterInfo.setSubParameterInfoType(localSubParameterInfo.getSubParameterInfoType() + ".index" + localParameterSet.getParameterSetCount());														
																									}
																									localSubParameterInfo.setSubParameterInfoType(localSubParameterInfo.getSubParameterInfoType() + "." + localSubParameterName + "." + localSubParameterInfo.getUnits().replaceAll("%", "percent").replaceAll("\\/min", "").replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\{", "").replaceAll("\\}", ""));
																									if (block.getRate() > 1) {
																										localSubParameterInfo.setSubParameterInfoType(localSubParameterInfo.getSubParameterInfoType() + ".perMs");
																									} else {
																										localSubParameterInfo.setSubParameterInfoType(localSubParameterInfo.getSubParameterInfoType() + ".perSec");															
																									}
																									temp = binDescriptor.getParameterIntervals();
																									interval = 1000.0 / (new Double(block.getRate()).doubleValue() * new Double(localSubParameterInfo.getArraySizeBinaryType()).doubleValue());
																									temp.put(localSubParameterInfo.getSubParameterInfoType(), new Double(interval));
																									binDescriptor.setParameterIntervals(temp);
																								}
																								break;
																							case "respSite":
																								localSubParameterInfo.setSubParameterInfoType(localSubParameterInfo.getSubParameterInfoType() + attribute.getNodeValue());
																								localSubParameterName = localSubParameterInfo.getSubParameterInfoType();
																								localSubParameterInfo.setSubParameterInfoType("carescape." + localParameterSet.getParameterSetType());
																								if (!(localParameterSet.getParameterSetLabel().equalsIgnoreCase("unknown"))) {
																									localSubParameterInfo.setSubParameterInfoType(localSubParameterInfo.getSubParameterInfoType() + "." + localParameterSet.getParameterSetSite());
																								} else {
																									localSubParameterInfo.setSubParameterInfoType(localSubParameterInfo.getSubParameterInfoType() + ".index" + localParameterSet.getParameterSetCount());														
																								}
																								localSubParameterInfo.setSubParameterInfoType(localSubParameterInfo.getSubParameterInfoType() + "." + localSubParameterName + "." + localSubParameterInfo.getUnits().replaceAll("%", "percent").replaceAll("\\/min", "").replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\{", "").replaceAll("\\}", ""));
																								if (block.getRate() > 1) {
																									localSubParameterInfo.setSubParameterInfoType(localSubParameterInfo.getSubParameterInfoType() + ".perMs");
																								} else {
																									localSubParameterInfo.setSubParameterInfoType(localSubParameterInfo.getSubParameterInfoType() + ".perSec");															
																								}
																								temp = binDescriptor.getParameterIntervals();
																								interval = 1000.0 / (new Double(block.getRate()).doubleValue() * new Double(localSubParameterInfo.getArraySizeBinaryType()).doubleValue());
																								temp.put(localSubParameterInfo.getSubParameterInfoType(), new Double(interval));
																								binDescriptor.setParameterIntervals(temp);																								
																								break;
																							}
																						}
																						localSubParameterArray.add(localSubParameterInfo);
																					}
																				}
																				localParameter.setSubParameterInfo(localSubParameterArray);
																			}
																			localParameterArray.add(localParameter);
																		}
																	}
																	localParameterSet.setParameters(localParameterArray);
																	localParameterSets.add(localParameterSet);
																}
															}
															localParameters.setParameterSets(localParameterSets);
															break;
														}
														groupBT.setParameters(localParameters);
													}
													block.setGroupBT(groupBT);
												}
											}
										}
										binDescriptor.setBlock(block);
										break;
									}
								}
							}
						}
						binDescriptor.setNumOfVariables(binDescriptor.getParameterIntervals().keySet().size() + 5);
						setBinDescriptor(binDescriptor);
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
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public boolean isHighSpeed() {
		return highSpeed;
	}

	public void setHighSpeed(boolean highSpeed) {
		this.highSpeed = highSpeed;
	}
}
