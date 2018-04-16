package com.avogine.westocado.setup.physics;

import com.avogine.westocado.entities.bodies.Body;
import com.avogine.westocado.entities.bodies.utils.BodyParams;

public abstract class PhysicsController<B extends Body, T extends BodyParams> {

	public abstract B createBody(long entity, T params);
	
	public abstract void physicsStep();
	public abstract void cleanUp();
	
}
