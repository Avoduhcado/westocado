package com.avogine.westocado.entities.components;

import com.avogine.westocado.entities.Entities;
import com.avogine.westocado.utils.system.AvoEvent;

public abstract class EntityComponent {
	
	protected long entity;

	public EntityComponent(long entity) {
		this.entity = entity;
		Entities.registerComponent(entity, this);
	}
	
	public long getEntity() {
		return entity;
	}

	public void setEntity(long entity) {
		this.entity = entity;
	}
	
	public abstract void fireEvent(AvoEvent e);
	
}
