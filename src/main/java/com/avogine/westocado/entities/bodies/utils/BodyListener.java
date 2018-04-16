package com.avogine.westocado.entities.bodies.utils;

public interface BodyListener {

	public void move(MovementEvent e);
	
	public void rotate(RotationEvent e);
	
	public void speedChange(SpeedChangeEvent e);
	
}
