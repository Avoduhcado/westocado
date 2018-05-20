package com.avogine.westocado.entities.states.utils;

public enum EntityState {

	IDLE("IDLE"), 
	WALK("WALK");
	
	String animationName;
	
	EntityState(String animationName) {
		this.animationName = animationName;
	}
	
}
