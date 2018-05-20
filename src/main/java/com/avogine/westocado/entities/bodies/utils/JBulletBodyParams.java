package com.avogine.westocado.entities.bodies.utils;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import com.bulletphysics.collision.shapes.CollisionShape;

public class JBulletBodyParams extends BodyParams {

	private CollisionShape shape;
	private Vector3f position;
	private Quat4f rotation;
	private float mass = 2.5f;
	private Vector3f inertia = new Vector3f();
	
	public JBulletBodyParams(CollisionShape shape, Vector3f position, Quat4f rotation) {
		this.shape = shape;
		this.position = position;
		this.rotation = rotation;
	}
	
	public CollisionShape getShape() {
		return shape;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public Quat4f getRotation() {
		return rotation;
	}
	
	public float getMass() {
		return mass;
	}
	
	public Vector3f getInertia() {
		return inertia;
	}
	
}
