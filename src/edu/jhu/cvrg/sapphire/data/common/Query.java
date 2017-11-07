package edu.jhu.cvrg.sapphire.data.common;

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

public class Query {

	private String xmlNameSpace = "urn:ge:sapphire:base_1";
	private String endTime;
	private SelectNodes selectNodes;
	private SessionIdList sessionIdList;

	public String getXmlNameSpace() {
		return xmlNameSpace;
	}
	
	public void setXmlNameSpace(String xmlNameSpace) {
		this.xmlNameSpace = xmlNameSpace;
	}
	
	public String getEndTime() {
		return endTime;
	}
	
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	public SelectNodes getSelectNodes() {
		return selectNodes;
	}
	
	public void setSelectNodes(SelectNodes selectNodes) {
		this.selectNodes = selectNodes;
	}
	
	public SessionIdList getSessionIdList() {
		return sessionIdList;
	}
	
	public void setSessionIdList(SessionIdList sessionIdList) {
		this.sessionIdList = sessionIdList;
	}

}
