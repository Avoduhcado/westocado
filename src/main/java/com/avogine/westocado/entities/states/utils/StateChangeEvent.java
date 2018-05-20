package com.avogine.westocado.entities.states.utils;

import com.avogine.westocado.utils.system.AvoEvent;

public class StateChangeEvent extends AvoEvent {

	private final EntityState newState;
	
	public StateChangeEvent(EntityState newState) {
		this.newState = newState;
	}
	
	public EntityState getNewState() {
		return newState;
	}
	
}
