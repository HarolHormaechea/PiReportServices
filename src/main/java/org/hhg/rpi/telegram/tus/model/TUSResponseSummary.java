package org.hhg.rpi.telegram.tus.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TUSResponseSummary {
	@JsonProperty(value = "items")
	private Integer itemNumber;
	@JsonProperty(value = "items_per_page")
	private Integer itemsPerPage;
	@JsonProperty(value = "pages")
	private Integer totalPages;
	@JsonProperty(value = "current_page")
	private Integer currentPage;

	public Integer getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(Integer itemNumber) {
		this.itemNumber = itemNumber;
	}

	public Integer getItemsPerPage() {
		return itemsPerPage;
	}

	public void setItemsPerPage(Integer itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}

	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

}
