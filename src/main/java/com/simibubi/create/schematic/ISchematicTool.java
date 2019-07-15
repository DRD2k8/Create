package com.simibubi.create.schematic;

public interface ISchematicTool {

	public void init();
	public void updateSelection();
	
	public boolean handleRightClick();
	public boolean handleMouseWheel(double delta);
	
	public void renderTool();
	public void renderOverlay();
	
	
}
