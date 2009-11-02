/**
 * 
 */
package com.codeminders.yfrog.android.model;

import java.io.*;
import java.util.ArrayList;

/**
 * @author idemydenko
 *
 */
public class TwitterQueryResult implements Serializable {
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
	
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeUTF(query);
		out.writeObject(results);
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		query = in.readUTF();
		results = (ArrayList<TwitterSearchResult>) in.readObject();
	}

}
