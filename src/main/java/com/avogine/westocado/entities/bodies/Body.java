package com.avogine.westocado.entities.bodies;

import org.joml.Vector3f;

import com.avogine.westocado.entities.components.EntityComponent;
import com.avogine.westocado.render.data.Mesh;
import com.avogine.westocado.utils.system.AvoEvent;

public abstract class Body extends EntityComponent {

	public static final Body DEFAULT = new Body(-1L) {
		@Override
		public Mesh getDebugMesh() {
			return null;
		}

		@Override
		public void applyForce(Vector3f force) {}

		@Override
		public void fireEvent(AvoEvent e) {}
	};
	
	protected Vector3f position = new Vector3f();
	protected Vector3f scale = new Vector3f(1);
	protected Vector3f rotation = new Vector3f();
	
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
	
	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}
	
	public Vector3f getRotation() {
		return rotation;
	}
	
	public void setPitch(float pitch) {
		this.rotation.x = pitch;
	}
	
	public float getPitch() {
		return this.rotation.x;
	}

	public void setYaw(float yaw) {
		this.rotation.y = yaw;
	}
	
	public float getYaw() {
		return this.rotation.y;
	}

	public void setRoll(float roll) {
		this.rotation.z = roll;
	}
	
	public float getRoll() {
		return this.rotation.z;
	}
	
	public boolean isInsideFrustum() {
		return insideFrustum;
	}
	
	public void setInsideFrustum(boolean insideFrustum) {
		this.insideFrustum = insideFrustum;
	}

}
