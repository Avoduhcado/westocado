package com.avogine.westocado.entities.components;

import com.avogine.westocado.entities.Entity;

public abstract class EntityComponent {

	protected Entity entity;
	
	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public EntityComponent(Entity entity) {
		this.entity = entity;
	}
	
}
