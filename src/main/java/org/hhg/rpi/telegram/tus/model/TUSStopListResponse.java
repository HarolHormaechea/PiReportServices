package org.hhg.rpi.telegram.tus.model;

import java.util.SortedSet;
import java.util.TreeSet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TUSStopListResponse {
	@JsonProperty(value="summary")
	private TUSResponseSummary summary;
	
	@JsonProperty(value="resources")
	private SortedSet<TUSStopItem> results = new TreeSet<TUSStopItem>();

	public TUSResponseSummary getSummary() {
		return summary;
	}

	public void setSummary(TUSResponseSummary summary) {
		this.summary = summary;
	}

	public SortedSet<TUSStopItem> getResults() {
		return results;
	}

	public void setResults(SortedSet<TUSStopItem> results) {
		this.results = results;
	}
	
	
	
}
