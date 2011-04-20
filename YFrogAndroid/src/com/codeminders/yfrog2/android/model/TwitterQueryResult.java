/**
 * 
 */
package com.codeminders.yfrog2.android.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author idemydenko
 *
 */
public class TwitterQueryResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2930569204919257151L;
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
	
	@SuppressWarnings("unchecked")
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		query = in.readUTF();
		results = (ArrayList<TwitterSearchResult>) in.readObject();
	}

}
