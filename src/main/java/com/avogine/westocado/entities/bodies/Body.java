package com.avogine.westocado.entities.bodies;

import org.joml.Vector3f;

import com.avogine.westocado.entities.components.EntityComponent;
import com.avogine.westocado.render.data.Mesh;

public abstract class Body extends EntityComponent {
		
	protected Vector3f position = new Vector3f();
	protected Vector3f scale = new Vector3f(1);
	
	protected boolean insideFrustum;
	
	public Body(long entity) {
		super(entity);
	}
	
	public abstract Mesh getDebugMesh();
	
	public abstract void applyForce(Vector3f force);
	
	public Vector3f getSize() {
		return new Vector3f(1f);
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public void setScale(Vector3f scale) {
		this.scale = scale;
	}
	
	public Vector3f getScale() {
		return scale;
	}
	
	public boolean isInsideFrustum() {
		return insideFrustum;
	}
	
	public void setInsideFrustum(boolean insideFrustum) {
		this.insideFrustum = insideFrustum;
	}

}
