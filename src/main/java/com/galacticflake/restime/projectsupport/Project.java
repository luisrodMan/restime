package com.galacticflake.restime.projectsupport;

import java.util.LinkedList;
import java.util.List;

import com.galacticflake.restime.RestEndpoint;

public class Project {
	
	private String name;
	private String path;
	private List<RestEndpoint> endpoints = new LinkedList<>();
	private List<ProjectListener> listeners = new LinkedList<>();
	
	public Project(String name) {
		setName(name);
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public List<RestEndpoint> getEndpoints() {
		return new LinkedList<>(endpoints);
	}
	
	public void addEndpoint(RestEndpoint endpoint) {
		endpoints.add(endpoint);
		for (int i = listeners.size()-1; i > -1; i--) {
			listeners.get(i).onAddedEndpoints(this, new RestEndpoint[] {endpoint});
		}
	}
	
	public void removeEndpoint(RestEndpoint endpoint) {
		endpoints.remove(endpoint);
		for (int i = listeners.size()-1; i > -1; i--) {
			listeners.get(i).onRemovedEndpoints(this, new RestEndpoint[] {endpoint});
		}
	}

	public void addProjectListener(ProjectListener projectListener) {
		listeners.add(projectListener);
	}
	
	public void removeProjectListener(ProjectListener projectListener) {
		listeners.remove(projectListener);
	}

	public int getEndpointsCount() {
		return endpoints.size();
	}

	public String getVersion() {
		return "1.0";
	}

}