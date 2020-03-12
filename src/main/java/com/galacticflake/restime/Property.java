package com.galacticflake.restime;

public class Property {
	
	private final String name;
	private Object oldValue;
	
	public Property(final String propertyName, Object oldValue) {
		name = propertyName;
		this.oldValue = oldValue;
	}
	
	public String getName() {
		return name;
	}
	
	public Object getOldValue() {
		return oldValue;
	}

}
