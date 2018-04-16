package com.avogine.westocado.entities.models;

import java.util.List;

import com.avogine.westocado.entities.components.EntityComponent;
import com.avogine.westocado.render.data.Mesh;
import com.avogine.westocado.render.data.Texture;

public abstract class Model extends EntityComponent {
	
	public Model(long entity) {
		super(entity);
	}
	
	public abstract List<Mesh> getMeshes();
	public abstract Texture getTexture();

}
