package org.hhg.rpi.telegram.tus.model;

import java.util.SortedSet;
import java.util.TreeSet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TUSResponse {
	@JsonProperty(value="summary")
	private TUSResponseSummary summary;
	
	@JsonProperty(value="resources")
	private SortedSet<TUSEstimationResponse> results = new TreeSet<TUSEstimationResponse>();
	
	
	public TUSResponseSummary getSummary() {
		return summary;
	}
	public void setSummary(TUSResponseSummary summary) {
		this.summary = summary;
	}
	public SortedSet<TUSEstimationResponse> getResults() {
		return results;
	}
	public void setResults(SortedSet<TUSEstimationResponse> results) {
		this.results = results;
	}
	
	

}
