package com.avogine.westocado.entities.components;

import org.joml.Vector3f;

import com.avogine.westocado.utils.system.AvoEvent;

public class LightEmitter extends EntityComponent {

	private Vector3f color;
	
	public LightEmitter(long entity, Vector3f color) {
		super(entity);
		this.color = color;
	}

	public Vector3f getColor() {
		return color;
	}
	
	public void setColor(Vector3f color) {
		this.color = color;
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
		// TODO Auto-generated method stub
		return 0;
	}

	public Vector3f getConeDirection() {
		// TODO Auto-generated method stub
		return new Vector3f(0, 0, 0);
	}

}
