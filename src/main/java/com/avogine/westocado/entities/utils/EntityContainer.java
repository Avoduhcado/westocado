package com.avogine.westocado.entities.utils;

import com.avogine.westocado.entities.Entity;
import com.avogine.westocado.setup.physics.PhysicsController;

public interface EntityContainer {

	public PhysicsController getPhysics();
	
	public void addEntity(Entity entity);
	
}
