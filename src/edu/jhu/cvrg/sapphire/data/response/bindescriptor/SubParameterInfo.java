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

import java.util.ArrayList;
import java.util.Arrays;

public class SubParameterInfo {

	String subParameterInfoType, units, scaleFactor, binaryType, invalidLowerEnd, invalidUpperEnd, arraySizeBinaryType, leadName;
	ArrayList<String> invalidAttributes, invalidMapping;
	
	public SubParameterInfo() {
		
		setSubParameterInfoType("unknown");
		setUnits("unknown");
		setScaleFactor("1.0");
		setBinaryType("unknown");
		setInvalidLowerEnd("unknown");
		setInvalidUpperEnd("unknown");
		setArraySizeBinaryType("1");
		setLeadName("unknown");
		setInvalidAttributes(new ArrayList<String>());
		setInvalidMapping(new ArrayList<String>());
		
	}
	
	public String getSubParameterInfoType() {
		return subParameterInfoType;
	}
	public void setSubParameterInfoType(String subParameterInfoType) {
		this.subParameterInfoType = subParameterInfoType;
	}
	public String getUnits() {
		return units;
	}
	public void setUnits(String units) {
		this.units = units;
	}
	public String getScaleFactor() {
		return scaleFactor;
	}
	public void setScaleFactor(String scaleFactor) {
		this.scaleFactor = scaleFactor;
	}
	public String getBinaryType() {
		return binaryType;
	}
	public void setBinaryType(String binaryType) {
		this.binaryType = binaryType;
	}
	public String getInvalidLowerEnd() {
		return invalidLowerEnd;
	}
	public void setInvalidLowerEnd(String invalidLowerEnd) {
		this.invalidLowerEnd = invalidLowerEnd;
	}
	public String getInvalidUpperEnd() {
		return invalidUpperEnd;
	}
	public void setInvalidUpperEnd(String invalidUpperEnd) {
		this.invalidUpperEnd = invalidUpperEnd;
	}
	public String getArraySizeBinaryType() {
		return arraySizeBinaryType;
	}
	public void setArraySizeBinaryType(String arraySizeBinaryType) {
		this.arraySizeBinaryType = arraySizeBinaryType;
	}
	public String getLeadName() {
		return leadName;
	}
	public void setLeadName(String leadName) {
		this.leadName = leadName;
	}
	public ArrayList<String> getInvalidAttributes() {
		return invalidAttributes;
	}
	public void setInvalidAttributes(ArrayList<String> invalidAttributes) {
		this.invalidAttributes = invalidAttributes;
	}
	public void setInvalidAttributes(String invalidAttributes) {
		String[] splitter = invalidAttributes.split(" ");
		this.invalidAttributes = new ArrayList<String>(Arrays.asList(splitter));
	}
	public ArrayList<String> getInvalidMapping() {
		return invalidMapping;
	}
	public void setInvalidMapping(ArrayList<String> invalidMapping) {
		this.invalidMapping = invalidMapping;
	}
	public void setInvalidMapping(String invalidMapping) {
		String[] splitter = invalidMapping.split(" ");
		this.invalidMapping = new ArrayList<String>(Arrays.asList(splitter));
	}
	
}
