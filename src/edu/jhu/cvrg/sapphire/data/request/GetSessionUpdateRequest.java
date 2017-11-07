package edu.jhu.cvrg.sapphire.data.request;

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

import edu.jhu.cvrg.sapphire.data.common.Header;
import edu.jhu.cvrg.sapphire.data.common.Output;
import edu.jhu.cvrg.sapphire.data.common.Query;

public class GetSessionUpdateRequest {

	private String xmlNameSpace = "urn:ge:sapphire:sessionUpdateMsg_1";
	private Header header;
	private Output output;
	private Query query;
	
	public String getXmlNameSpace() {
		return xmlNameSpace;
	}
	
	public void setXmlNameSpace(String xmlNameSpace) {
		this.xmlNameSpace = xmlNameSpace;
	}
	
	public Header getHeader() {
		return header;
	}
	
	public void setHeader(Header header) {
		this.header = header;
	}
	
	public Output getOutput() {
		return output;
	}
	
	public void setOutput(Output output) {
		this.output = output;
	}
	
	public Query getQuery() {
		return query;
	}
	
	public void setQuery(Query query) {
		this.query = query;
	}
	
}
