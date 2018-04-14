package com.avogine.westocado.io.utils;

public interface MouseButtonInputListener extends InputListener {

	public void mouseButtonPressed(MouseButtonInputEvent e);
	
	public void mouseButtonReleased(MouseButtonInputEvent e);
	
	public void mouseButtonHeld(MouseButtonInputEvent e);
	
}
