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

public class DeviceStatus {

	private AssignedLocation assignedLocation;
	private Services services;
	private State state;
	
	public AssignedLocation getAssignedLocation() {
		return assignedLocation;
	}
	public void setAssignedLocation(AssignedLocation assignedLocation) {
		this.assignedLocation = assignedLocation;
	}
	public Services getServices() {
		return services;
	}
	public void setServices(Services services) {
		this.services = services;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	
	
}
