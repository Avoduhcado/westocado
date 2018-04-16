package com.avogine.westocado.entities.bodies.utils;

import org.joml.Vector3f;

import com.avogine.westocado.utils.system.AvoEvent;

public class RotationEvent extends AvoEvent {

	private final Vector3f rotation;
	
	public RotationEvent(Vector3f rotation) {
		this.rotation = rotation;
	}
	
	public Vector3f getRotation() {
		return rotation;
	}
	
}
