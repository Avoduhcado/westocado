package com.avogine.westocado.entities.bodies.utils;

import org.joml.Vector3f;

import com.avogine.westocado.utils.system.AvoEvent;

public class MovementEvent extends AvoEvent {

	public static final int RELATIVE = 0;
	public static final int FIXED = 1;
	
	private final Vector3f velocity;
	private final int moveType;
	
	public MovementEvent(Vector3f velocity, int moveType) {
		this.velocity = velocity;
		this.moveType = moveType;
	}
	
	public Vector3f getVelocity() {
		return velocity;
	}
	
	public int getMoveType() {
		return moveType;
	}
	
}
