package org.txemasv.mycharacters.apiserver.model;

public class MyResource {
	
	private String resource;
	private String verb;
	private String path;
	
	public MyResource(String resource, String verb, String path) {
		this.resource = resource;
		this.verb = verb;
		this.path = path;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getVerb() {
		return verb;
	}

	public void setVerb(String verb) {
		this.verb = verb;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	

}
