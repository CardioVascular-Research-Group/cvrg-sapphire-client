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

import edu.jhu.cvrg.sapphire.data.common.StartDateTime;

public class Block {

	private int rate;
	private String units, repeat;
	private FormatId formatId;
	private SessionId_BT sessionId_BT;
	private BlockSQN blockSQN;
	private BlockLength blockLength;
	private StartDateTime startDateTime;
	private GroupBT groupBT;

	public Block() {
		
		setRate(0);
		setUnits("unknown");
		setRepeat("0");
		setFormatId(new FormatId());
		setSessionId_BT(new SessionId_BT());
		setBlockSQN(new BlockSQN());
		setBlockLength(new BlockLength());
		setStartDateTime(new StartDateTime());
		setGroupBT(new GroupBT());
		
	}
	
	public int getRate() {
		return rate;
	}
	public void setRate(int rate) {
		this.rate = rate;
	}
	public String getUnits() {
		return units;
	}
	public void setUnits(String units) {
		this.units = units;
	}
	public String getRepeat() {
		return repeat;
	}
	public void setRepeat(String repeat) {
		this.repeat = repeat;
	}
	public FormatId getFormatId() {
		return formatId;
	}
	public void setFormatId(FormatId formatId) {
		this.formatId = formatId;
	}
	public SessionId_BT getSessionId_BT() {
		return sessionId_BT;
	}

	public void setSessionId_BT(SessionId_BT sessionId_BT) {
		this.sessionId_BT = sessionId_BT;
	}

	public BlockSQN getBlockSQN() {
		return blockSQN;
	}
	public void setBlockSQN(BlockSQN blockSQN) {
		this.blockSQN = blockSQN;
	}
	public BlockLength getBlockLength() {
		return blockLength;
	}

	public void setBlockLength(BlockLength blockLength) {
		this.blockLength = blockLength;
	}

	public StartDateTime getStartDateTime() {
		return startDateTime;
	}
	public void setStartDateTime(StartDateTime startDateTime) {
		this.startDateTime = startDateTime;
	}
	public GroupBT getGroupBT() {
		return groupBT;
	}
	public void setGroupBT(GroupBT groupBT) {
		this.groupBT = groupBT;
	}
	
}
