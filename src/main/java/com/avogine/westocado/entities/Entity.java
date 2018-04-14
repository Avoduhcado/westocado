package com.avogine.westocado.entities;

import org.joml.Vector3f;

import com.avogine.westocado.entities.bodies.Body;
import com.avogine.westocado.entities.utils.EntityContainer;
import com.avogine.westocado.render.Model;
import com.avogine.westocado.utils.system.AvoEvent;
import com.avogine.westocado.utils.system.AvoEventListener;

public class Entity implements AvoEventListener {
	// TODO convert Model into an Entitycomponent
	private Model model;
	
	private Body body;
	
	// TODO Convert to BodyComponent
	//private Vector3f position = new Vector3f((float) Math.random() * size - (size / 2), (float) Math.random() * size - (size / 2), (float) Math.random() * size - (size / 2));
	protected Vector3f position = new Vector3f();
	private Vector3f rotation = new Vector3f(0f, 1f, 0f);
	private float rotateDegree;
	private Vector3f scale = new Vector3f(1f, 1f, 1f);
	
	private EntityContainer container;
	
	public Entity(EntityContainer container) {
		this.container = container;
	}
	
	// TODO Grab textures from the mtl file??
	public Entity(EntityContainer container, String meshName, String textureName) {
		this(container);
		model = new Model(meshName, textureName);
	}

	@Override
	public void handleEvent(AvoEvent event) {
		
	}

	public Model getModel() {
		return model;
	}
	
	public void setModel(Model model) {
		this.model = model;
	}
	
	public Body getBody() {
		return body;
	}
	
	public void setBody(Body body) {
		this.body = body;
	}
	
	public Vector3f getPosition() {
		// TODO Get rid of the interior position, rely solely on body
		if(body != null) {
			return body.getPosition();
		}
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}

	public float getRotateDegree() {
		return rotateDegree;
	}

	public void setRotateDegree(float rotateDegree) {
		this.rotateDegree = rotateDegree;
	}

	public Vector3f getScale() {
		return scale;
	}

	public void setScale(Vector3f scale) {
		this.scale = scale;
	}
	
	public EntityContainer getContainer() {
		return container;
	}
	
}
