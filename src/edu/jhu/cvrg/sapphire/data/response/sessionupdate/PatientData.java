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

public class PatientData {

	private String patientAge, initialHeight, initialWeight, initialBSA, initialBMI;

	public PatientData() {
		
		setPatientAge("");
		setInitialHeight("");
		setInitialWeight("");
		setInitialBSA("");
		setInitialBMI("");
		
	}
	
	public String getPatientAge() {
		return patientAge;
	}

	public void setPatientAge(String patientAge) {
		this.patientAge = patientAge;
	}

	public String getInitialHeight() {
		return initialHeight;
	}

	public void setInitialHeight(String initialHeight) {
		this.initialHeight = initialHeight;
	}

	public String getInitialWeight() {
		return initialWeight;
	}

	public void setInitialWeight(String initialWeight) {
		this.initialWeight = initialWeight;
	}

	public String getInitialBSA() {
		return initialBSA;
	}

	public void setInitialBSA(String initialBSA) {
		this.initialBSA = initialBSA;
	}

	public String getInitialBMI() {
		return initialBMI;
	}

	public void setInitialBMI(String initialBMI) {
		this.initialBMI = initialBMI;
	}
	
}
