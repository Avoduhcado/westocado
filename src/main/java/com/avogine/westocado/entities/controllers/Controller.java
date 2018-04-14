package com.avogine.westocado.entities.controllers;

import com.avogine.westocado.entities.Entity;
import com.avogine.westocado.entities.components.EntityComponent;

public abstract class Controller extends EntityComponent {

	public Controller(Entity entity) {
		super(entity);
	}
	
	public abstract void control();

}
