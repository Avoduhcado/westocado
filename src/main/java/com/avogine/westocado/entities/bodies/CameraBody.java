package com.avogine.westocado.entities.bodies;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.avogine.westocado.Theater;
import com.avogine.westocado.entities.bodies.utils.BodyListener;
import com.avogine.westocado.entities.bodies.utils.MovementEvent;
import com.avogine.westocado.entities.bodies.utils.RotationEvent;
import com.avogine.westocado.entities.bodies.utils.SpeedChangeEvent;
import com.avogine.westocado.render.data.Mesh;
import com.avogine.westocado.utils.system.AvoEvent;

public class CameraBody extends Body implements BodyListener {

	private Matrix4f viewMatrix;

	private Vector3f velocity;
	/** Rotation around the X axis */
	private float pitch;
	/** Rotation around the Y axis */
	private float yaw;
	
	private float speed;
	
	public CameraBody(long entity) {
		super(entity);
		viewMatrix = new Matrix4f();
		position = new Vector3f(0f, 12f, 10f);
		velocity = new Vector3f();
		pitch = 22.5f;
		speed = 15;
	}

	public Matrix4f getViewMatrix() {
		return viewMatrix;
	}

	private Matrix3f getInvertedYaw() {
		return new Matrix3f().rotateY((float) Math.toRadians(yaw)).invert();
	}
	
	private Matrix3f getCameraView() {
		return new Matrix3f().rotateX((float) Math.toRadians(pitch)).rotateY((float) Math.toRadians(yaw)).invert();
	}
	
	private void updateViewMatrix() {
		viewMatrix.identity();
		viewMatrix.arcball(0, position, (float) Math.toRadians(pitch), (float) Math.toRadians(yaw));
	}

	@Override
	public Mesh getDebugMesh() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void applyForce(Vector3f force) {
		position.add(force);
		
		updateViewMatrix();
	}

	@Override
	public void fireEvent(AvoEvent e) {
		if(e instanceof MovementEvent) {
			move((MovementEvent) e);
		} else if(e instanceof RotationEvent) {
			rotate((RotationEvent) e);
		} else if(e instanceof SpeedChangeEvent) {
			speedChange((SpeedChangeEvent) e);
		}
	}
	
	@Override
	public void move(MovementEvent e) {
		velocity.set(e.getVelocity());
		
		switch(e.getMoveType()) {
		case MovementEvent.FIXED:
			break;
		case MovementEvent.RELATIVE:
			velocity.mul(getInvertedYaw());
			break;
		}
		
		velocity.mul(Theater.getDeltaChange(speed));
		applyForce(velocity);
	}

	@Override
	public void rotate(RotationEvent e) {
		pitch += e.getRotation().x;
		pitch %= 360;
		yaw += e.getRotation().y;
		yaw %= 360;
		// ROLL
		
		updateViewMatrix();
	}

	@Override
	public void speedChange(SpeedChangeEvent e) {
		speed += e.getValue();
		System.out.println(speed);
	}

}
