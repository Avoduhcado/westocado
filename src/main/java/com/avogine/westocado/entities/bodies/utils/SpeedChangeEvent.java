package com.avogine.westocado.entities.bodies.utils;

import com.avogine.westocado.utils.system.AvoEvent;

public class SpeedChangeEvent extends AvoEvent {

	private final double value;
	
	public SpeedChangeEvent(double value) {
		this.value = value;
	}
	
	public double getValue() {
		return value;
	}
	
}
