package com.galacticflake.restime.projectsupport;

import com.galacticflake.restime.Property;
import com.galacticflake.restime.RestEndpoint;

public interface ProjectListener {
	
	void onPropertieChange(Project project, Property property);
	
	void onAddedEndpoints(Project project, RestEndpoint[] endpointsAdded);
	
	void onRemovedEndpoints(Project project, RestEndpoint[] endpointsRemoved);

}