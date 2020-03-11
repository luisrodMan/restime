package com.galacticflake.restime;

public interface Editor extends View {

	boolean isDirty();
	
	void save();
	
	void saveAs();
	
}