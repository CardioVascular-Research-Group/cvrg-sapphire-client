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

public class Header {

	private String xmlNameSpace = "urn:ge:sapphire:base_1";
	private SessionId sessionId;
	private String msgCHN, msgSQN, replytoMsgSQN, requestId, creationDateTime;
	private ResponseId responseId;

	public Header() {
		
		setSessionId(new SessionId());
		setMsgCHN("");
		setMsgSQN("");
		setReplytoMsgSQN("");
		setResponseId(new ResponseId());
		setCreationDateTime("");
		
	}
	
	public String getXmlNameSpace() {
		return xmlNameSpace;
	}

	public void setXmlNameSpace(String xmlNameSpace) {
		this.xmlNameSpace = xmlNameSpace;
	}

	public SessionId getSessionId() {
		return sessionId;
	}

	public void setSessionId(SessionId sessionId) {
		this.sessionId = sessionId;
	}

	public String getMsgCHN() {
		return msgCHN;
	}

	public void setMsgCHN(String msgCHN) {
		this.msgCHN = msgCHN;
	}

	public String getMsgSQN() {
		return msgSQN;
	}

	public void setMsgSQN(String msgSQN) {
		this.msgSQN = msgSQN;
	}

	public String getReplytoMsgSQN() {
		return replytoMsgSQN;
	}

	public void setReplytoMsgSQN(String replytoMsgSQN) {
		this.replytoMsgSQN = replytoMsgSQN;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public ResponseId getResponseId() {
		return responseId;
	}

	public void setResponseId(ResponseId responseId) {
		this.responseId = responseId;
	}

	public String getCreationDateTime() {
		return creationDateTime;
	}

	public void setCreationDateTime(String creationDateTime) {
		this.creationDateTime = creationDateTime;
	}
	
}
