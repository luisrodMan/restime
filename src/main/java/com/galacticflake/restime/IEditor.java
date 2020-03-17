package com.galacticflake.restime;

public interface IEditor extends IView {

	boolean isDirty();
	
	void save();
	
	void saveAs();
	
}