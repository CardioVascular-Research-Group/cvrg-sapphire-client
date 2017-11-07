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

public class ParameterSet {

	private String parameterSetType, parameterSetLabel, parameterSetSite, parameterSetSource, parameterSetCount;
	private ArrayList<Parameter> parameters;
	
	public ParameterSet() {
		
		setParameterSetType("unknown");
		setParameterSetLabel("unknown");
		setParameterSetSite("unknown");
		setParameterSetSource("unknown");
		setParameterSetCount("unknown");
		setParameters(new ArrayList<Parameter>());
		
	}
	
	public String getParameterSetType() {
		return parameterSetType;
	}
	public void setParameterSetType(String parameterSetType) {
		this.parameterSetType = parameterSetType;
	}
	public String getParameterSetLabel() {
		return parameterSetLabel;
	}
	public void setParameterSetLabel(String parameterSetLabel) {
		this.parameterSetLabel = parameterSetLabel;
	}
	public String getParameterSetSite() {
		return parameterSetSite;
	}
	public void setParameterSetSite(String parameterSetSite) {
		this.parameterSetSite = parameterSetSite;
	}
	public String getParameterSetSource() {
		return parameterSetSource;
	}
	public void setParameterSetSource(String parameterSetSource) {
		this.parameterSetSource = parameterSetSource;
	}
	public String getParameterSetCount() {
		return parameterSetCount;
	}
	public void setParameterSetCount(String parameterSetCount) {
		this.parameterSetCount = parameterSetCount;
	}
	public ArrayList<Parameter> getParameters() {
		return parameters;
	}
	public void setParameters(ArrayList<Parameter> parameters) {
		this.parameters = parameters;
	}

}
