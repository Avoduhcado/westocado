package com.avogine.westocado.entities.components;

import org.joml.Vector3f;

import com.avogine.westocado.utils.system.AvoEvent;

public class LightEmitter extends EntityComponent {

	public static final int DIRECTIONAL = 0;
	public static final int POINT = 1;
	
	private Vector3f color;
	private float coneAngle;
	private Vector3f coneDirection;
	private int lightType;
	
	public LightEmitter(long entity, Vector3f color, int lightType) {
		super(entity);
		this.color = color;
		this.coneAngle = 0f;
		this.coneDirection = new Vector3f();
		this.lightType = lightType;
	}
	
	public LightEmitter(long entity, Vector3f color, float coneAngle, Vector3f coneDirection) {
		super(entity);
		this.color = color;
		this.coneAngle = coneAngle;
		this.coneDirection = coneDirection;
	}

	public Vector3f getColor() {
		return color;
	}
	
	public void setColor(Vector3f color) {
		this.color = color;
	}
	
	public int getLightType() {
		return lightType;
	}

	@Override
	public void fireEvent(AvoEvent e) {
		// TODO Auto-generated method stub
		
	}

	public float getAttenuation() {
		// TODO Auto-generated method stub
		return 0.06f;
	}

	public float getAmbientCoefficient() {
		// TODO Auto-generated method stub
		return 1.0f;
	}

	public float getConeAngle() {
		return coneAngle;
	}

	public Vector3f getConeDirection() {
		return coneDirection;
	}

}
