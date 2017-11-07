package edu.jhu.cvrg.sapphire.data.response.bindescriptor;

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

public class FormatId {

	String binaryType;
	int initialBinaryValue;
	int value;

	public FormatId() {

		setBinaryType("unknown");
		setInitialBinaryValue(0);
		setValue(0);
		
	}

	public String getBinaryType() {
		return binaryType;
	}

	public void setBinaryType(String binaryType) {
		this.binaryType = binaryType;
	}

	public int getInitialBinaryValue() {
		return initialBinaryValue;
	}

	public void setInitialBinaryValue(int initialBinaryValue) {
		this.initialBinaryValue = initialBinaryValue;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
}
