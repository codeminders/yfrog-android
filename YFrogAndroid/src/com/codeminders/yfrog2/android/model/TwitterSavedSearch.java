/**
 * 
 */
package com.codeminders.yfrog2.android.model;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @author idemydenko
 *
 */
public class TwitterSavedSearch implements Serializable {
	private int id;
	private String name;
	private String query;
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
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
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		
		if (!(o instanceof TwitterSavedSearch)) {
			return false;
		}
		
		TwitterSavedSearch castObj = (TwitterSavedSearch) o;
		
		if (id != castObj.id) {
			return false;
		}
		
		if (!query.equals(castObj.query)) {
			return false;
		}
		return true;
	}
	
	public int hashCode() {
		final int prime = 37;
		int result = 13;

		result = result * prime + id;
		result = result * prime + query.hashCode();
		
		return result;
	}
	
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeInt(id);
		out.writeUTF(name);
		out.writeUTF(query);
	}
	
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		id = in.readInt();
		name = in.readUTF();
		query = in.readUTF();
	}

}
