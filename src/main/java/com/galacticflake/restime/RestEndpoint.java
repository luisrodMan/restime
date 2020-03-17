package com.galacticflake.restime;

import java.util.HashMap;
import java.util.Map;

public class RestEndpoint {
	
	public static final String GET = "GET";
	public static final String POST = "POST";
	public static final String PUT = "PUT";
	public static final String DELETE = "DELETE";
	
	private String name;
	private String method = "GET";
	private String endpoint;
	private String contentType;
	private String content;
	private Map<String, String> headers = new HashMap<>();
	
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
		if (endpoint.endsWith("?"))
			endpoint = endpoint.substring(0, endpoint.length()-1);
		this.endpoint = endpoint;
	}
	
	public String getEndpoint() {
		return endpoint;
	}
	
	public String getQueryString() {
		int i = endpoint.indexOf('?');
		if (i != -1) {
			return endpoint.substring(i+1);
		} else {
			if (endpoint.contains("=") || endpoint.contains("&"))
				throw new RuntimeException("Not implemented xd");
			return "";
		}
	}

	public String getFullPath() {
		String query = getQueryString();
		if (query.isEmpty())
			return endpoint;
		String path = endpoint.substring(0, endpoint.length()-query.length());
		if (path.endsWith("?"))
			path = endpoint.substring(0, path.length()-1);
		if (path.endsWith("/"))
			path = endpoint.substring(0, path.length()-1);
		return path;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	public String getContentType() {
		return contentType;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String getContent() {
		return content;
	}

	public void addHeader(String name, String value) {
		headers.put(name, value);
	}
	
	public String getHeader(String name) {
		return headers.get(name);
	}

	public Map<String, String> getHeaders() {
		return new HashMap<>(headers);
	}
	
}
