package org.hhg.rpi.telegram.tus.model;

import java.util.SortedSet;
import java.util.TreeSet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TUSBusEstimationResponse {
	@JsonProperty(value="summary")
	private TUSResponseSummary summary;
	
	@JsonProperty(value="resources")
	private SortedSet<TUSEstimationItem> results = new TreeSet<TUSEstimationItem>();
	
	
	public TUSResponseSummary getSummary() {
		return summary;
	}
	public void setSummary(TUSResponseSummary summary) {
		this.summary = summary;
	}
	public SortedSet<TUSEstimationItem> getResults() {
		return results;
	}
	public void setResults(SortedSet<TUSEstimationItem> results) {
		this.results = results;
	}
}
