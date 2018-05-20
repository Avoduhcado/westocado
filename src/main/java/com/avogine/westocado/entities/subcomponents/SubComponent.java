package com.avogine.westocado.entities.subcomponents;

import com.avogine.westocado.entities.components.EntityComponent;

public class SubComponent {

	private EntityComponent parent;
	
	public SubComponent(EntityComponent parent) {
		this.parent = parent;
	}
	
	public EntityComponent getParent() {
		return parent;
	}
	
}
