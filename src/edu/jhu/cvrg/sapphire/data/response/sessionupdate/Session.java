package edu.jhu.cvrg.sapphire.data.response.sessionupdate;

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

import edu.jhu.cvrg.sapphire.data.common.Device;
import edu.jhu.cvrg.sapphire.data.common.SessionId;

public class Session {

	private SessionId sessionId;
	private String startTime;
	private Device device;
	private DeviceStatus deviceStatus;
	private PatientInfo patientInfo;
	
	public SessionId getSessionId() {
		return sessionId;
	}
	public void setSessionId(SessionId sessionId) {
		this.sessionId = sessionId;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public Device getDevice() {
		return device;
	}
	public void setDevice(Device device) {
		this.device = device;
	}
	public DeviceStatus getDeviceStatus() {
		return deviceStatus;
	}
	public void setDeviceStatus(DeviceStatus deviceStatus) {
		this.deviceStatus = deviceStatus;
	}
	public PatientInfo getPatientInfo() {
		return patientInfo;
	}
	public void setPatientInfo(PatientInfo patientInfo) {
		this.patientInfo = patientInfo;
	}
	
}
