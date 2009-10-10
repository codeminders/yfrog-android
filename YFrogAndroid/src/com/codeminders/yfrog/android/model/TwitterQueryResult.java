/**
 * 
 */
package com.codeminders.yfrog.android.model;

import java.util.ArrayList;

/**
 * @author idemydenko
 *
 */
public class TwitterQueryResult {
	private String query;
	private ArrayList<TwitterSearchResult> results;

	/**
	 * @return the query
	 */
	public String getQuery() {
		return query;
	}
	/**
	 * @param query the query to set
	 */
	public void setQuery(String query) {
		this.query = query;
	}
	/**
	 * @return the results
	 */
	public ArrayList<TwitterSearchResult> getResults() {
		return results;
	}
	/**
	 * @param results the results to set
	 */
	public void setResults(ArrayList<TwitterSearchResult> results) {
		this.results = results;
	}
}
