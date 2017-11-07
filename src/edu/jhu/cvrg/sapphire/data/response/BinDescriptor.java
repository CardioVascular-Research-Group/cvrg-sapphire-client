package edu.jhu.cvrg.sapphire.data.response;

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

import java.util.HashMap;

import edu.jhu.cvrg.sapphire.data.common.Header;
import edu.jhu.cvrg.sapphire.data.response.bindescriptor.Block;

public class BinDescriptor {

	private Header header;
	private Block block;
	private HashMap<String,Double> parameterIntervals;
	private int numOfVariables;
	
	public BinDescriptor() {
		
		setHeader(new Header());
		setBlock(new Block());
		setParameterIntervals(new HashMap<String, Double>());
		setNumOfVariables(0);
		
	}
	
	public Header getHeader() {
		return header;
	}
	public void setHeader(Header header) {
		this.header = header;
	}
	public Block getBlock() {
		return block;
	}
	public void setBlock(Block block) {
		this.block = block;
	}

	public HashMap<String, Double> getParameterIntervals() {
		return parameterIntervals;
	}

	public void setParameterIntervals(HashMap<String, Double> parameterIntervals) {
		this.parameterIntervals = parameterIntervals;
	}

	public int getNumOfVariables() {
		return numOfVariables;
	}

	public void setNumOfVariables(int numOfVariables) {
		this.numOfVariables = numOfVariables;
	}

}
