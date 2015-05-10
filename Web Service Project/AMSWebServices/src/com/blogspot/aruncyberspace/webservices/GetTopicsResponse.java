/**
 * 
 */
package com.blogspot.aruncyberspace.webservices;

/**
 * @author Arun
 *
 */

import java.util.HashMap;

import org.simpleframework.xml.*;

@Root
public class GetTopicsResponse {

	@ElementMap
	private HashMap<String, String> topics;

	public HashMap<String, String> getTopics() {
		return topics;
	}

	public void setTopics(HashMap<String, String> topics) {
		this.topics = topics;
	}

}
