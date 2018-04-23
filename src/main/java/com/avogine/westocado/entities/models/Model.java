package com.avogine.westocado.entities.models;

import com.avogine.westocado.entities.components.EntityComponent;
import com.avogine.westocado.render.data.Mesh;

public abstract class Model extends EntityComponent {
	
	public Model(long entity) {
		super(entity);
	}
	
	public abstract Mesh[] getMeshes();

}
