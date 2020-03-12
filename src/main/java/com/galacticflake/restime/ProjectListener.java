package com.galacticflake.restime;

public interface ProjectListener {
	
	void onPropertieChange(Project project, Property property);
	
	void onAddedEndpoints(Project project, RestEndpoint[] endpointsAdded);
	
	void onRemovedEndpoints(Project project, RestEndpoint[] endpointsRemoved);

}