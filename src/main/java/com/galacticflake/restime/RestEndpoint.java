package com.galacticflake.restime;

public class RestEndpoint {
	
	public static final String GET = "GET";
	public static final String POST = "POST";
	public static final String PUT = "PUT";
	public static final String DELETE = "DELETE";
	
	private String name;
	private String method = "GET";
	private String endpoint;
	
	public RestEndpoint(String name) {
		this(name, "GET", null);
	}
	
	public RestEndpoint(String name, String endpoint) {
		this(name, "GET", endpoint);
	}

	public RestEndpoint(String name, String method, String endpoint) {
		setName(name);
		setEndpoint(endpoint);
		setMethod(method);
	}

	public void setMethod(String method) {
		if (method == null)
			throw new RuntimeException("Invalid argument method null");
		this.method = method;
	}
	
	public String getMethod() {
		return method;
	}

	public void setName(String name) {
		if (name == null)
			throw new RuntimeException("Invalid argument name null");
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setEndpoint(String endpoint) {
		if (endpoint == null)
			throw new RuntimeException("Invalid argument endpoint null");
		this.endpoint = endpoint;
	}
	
	public String getEndpoint() {
		return endpoint;
	}
	
}
