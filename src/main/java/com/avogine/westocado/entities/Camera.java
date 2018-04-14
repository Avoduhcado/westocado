package com.avogine.westocado.entities;

import javax.vecmath.Quat4f;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import com.avogine.westocado.Theater;
import com.avogine.westocado.entities.bodies.Body;
import com.avogine.westocado.entities.bodies.utils.JBulletBodyParams;
import com.avogine.westocado.entities.utils.EntityContainer;
import com.avogine.westocado.io.Window;
import com.avogine.westocado.io.utils.MouseButtonInputEvent;
import com.avogine.westocado.io.utils.MouseButtonInputListener;
import com.avogine.westocado.utils.math.BTUtils;
import com.avogine.westocado.utils.math.MathUtils;
import com.avogine.westocado.utils.system.WindowManager;
import com.bulletphysics.collision.shapes.SphereShape;

public class Camera extends Entity implements MouseButtonInputListener {

	private Matrix4f viewMatrix;

	private Vector3f velocity;
	/** Rotation around the X axis */
	private float pitch;
	/** Rotation around the Y axis */
	private float yaw;
	
	
	private float cameraSpeed = 15f;
	
	private float fireCooldown = 0;
	
	public Camera(EntityContainer container) {
		super(container);
		viewMatrix = new Matrix4f();
		position = new Vector3f(0f, 12f, 10f);
		velocity = new Vector3f();
		pitch = 22.5f;
	}
	
	public void move() {
		Window window = WindowManager.requestWindow(WindowManager.requestMainWindow());
		velocity.set(0f);
		
		if(window.getInput().isKeyDown(GLFW.GLFW_KEY_W)) {
			velocity.add(0f, 0f, -1f);
		}
		
		if(window.getInput().isKeyDown(GLFW.GLFW_KEY_S)) {
			velocity.add(0f, 0f, 1f);
		}
		
		if(window.getInput().isKeyDown(GLFW.GLFW_KEY_A)) {
			velocity.add(-1f, 0f, 0f);
		}
		
		if(window.getInput().isKeyDown(GLFW.GLFW_KEY_D)) {
			velocity.add(1f, 0f, 0f);
		}
		
		velocity.normalize();
		if(velocity.length() > 0) {
			velocity.mul(getInvertedYaw());
			velocity.mul(Theater.getDeltaChange(cameraSpeed));
			position.add(velocity);
		}
		
		if(window.getInput().isKeyDown(GLFW.GLFW_KEY_SPACE)) {
			Vector3f movement = new Vector3f(0f, 1f, 0f);
			movement.mul(Theater.getDeltaChange(cameraSpeed));
			position.add(movement);
		}
		
		if(window.getInput().isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
			Vector3f movement = new Vector3f(0, -1, 0);
			movement.mul(Theater.getDeltaChange(cameraSpeed));
			position.add(movement);
		}
		
		if(GLFW.glfwGetInputMode(window.getID(), GLFW.GLFW_CURSOR) == GLFW.GLFW_CURSOR_DISABLED) {
			yaw += window.getInput().getMouseDX() / 10;
			yaw %= 360;
			pitch = MathUtils.clamp((float) (pitch + window.getInput().getMouseDY() / 10), -60, 80);
			pitch %= 360;
		}
		
		if(window.getInput().getMouseWheelDY() != 0) {
			cameraSpeed += window.getInput().getMouseWheelDY();
			System.out.println(cameraSpeed);
		}
		
		if(fireCooldown > 0) {
			fireCooldown -= Theater.getDelta();
		}
		
		updateViewMatrix();
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
	public void mouseButtonPressed(MouseButtonInputEvent e) {
	}

	@Override
	public void mouseButtonReleased(MouseButtonInputEvent e) {
	}

	@Override
	public void mouseButtonHeld(MouseButtonInputEvent e) {
		if(e.getMouseButton() == 0 && fireCooldown <= 0) {
			fireCooldown = 0.2f;
			
			Entity bullet = new Entity(getContainer());
			Body body = getContainer().getPhysics().createBody(bullet, new JBulletBodyParams(new SphereShape(1), BTUtils.vector3f(getPosition().x, getPosition().y, getPosition().z), new Quat4f(0, 0, 0, 1)));
			bullet.setBody(body);
			Vector3f shotVector = new Vector3f(0, 0, -1f);
			shotVector.mul(getCameraView());
			shotVector.normalize();
			shotVector.mul(25);
			body.applyForce(shotVector);
			
			getContainer().addEntity(bullet);
		}
	}
	
}
