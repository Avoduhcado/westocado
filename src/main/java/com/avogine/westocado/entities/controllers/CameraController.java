package com.avogine.westocado.entities.controllers;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import com.avogine.westocado.entities.Entities;
import com.avogine.westocado.entities.bodies.utils.MovementEvent;
import com.avogine.westocado.entities.bodies.utils.RotationEvent;
import com.avogine.westocado.entities.bodies.utils.SpeedChangeEvent;
import com.avogine.westocado.io.Window;
import com.avogine.westocado.io.utils.KeyInputEvent;
import com.avogine.westocado.io.utils.KeyInputListener;
import com.avogine.westocado.io.utils.MouseButtonInputEvent;
import com.avogine.westocado.io.utils.MouseButtonInputListener;
import com.avogine.westocado.io.utils.MouseMotionInputEvent;
import com.avogine.westocado.io.utils.MouseMotionInputListener;
import com.avogine.westocado.io.utils.MouseScrollInputEvent;
import com.avogine.westocado.io.utils.MouseScrollInputListener;
import com.avogine.westocado.io.utils.TimeEvent;
import com.avogine.westocado.io.utils.TimeListener;
import com.avogine.westocado.system.TimeWizard;
import com.avogine.westocado.utils.math.MathUtils;
import com.avogine.westocado.utils.system.AvoEvent;
import com.avogine.westocado.utils.system.WindowManager;

public class CameraController extends Controller implements KeyInputListener, MouseButtonInputListener, MouseMotionInputListener, MouseScrollInputListener, TimeListener {
	
	private boolean inverted;
	
	private float fireCooldown = 0;
	
	public CameraController(long entity, Window window) {
		super(entity);
		TimeWizard.addListener(this);
		window.getInput().addInputListener(this);
		
		// TODO Make a proper toggle property
		inverted = true;
	}

	@Override
	public void control() {
		
	}

	@Override
	public void fireEvent(AvoEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyInputEvent e) {
		
	}

	@Override
	public void keyReleased(KeyInputEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyHeld(KeyInputEvent e) {
		switch(e.getKey()) {
		case GLFW.GLFW_KEY_W:
			Entities.bodyComponentMap.get(entity).fireEvent(new MovementEvent(new Vector3f(0, 0, -1), MovementEvent.RELATIVE));
			break;
		case GLFW.GLFW_KEY_S:
			Entities.bodyComponentMap.get(entity).fireEvent(new MovementEvent(new Vector3f(0, 0, 1), MovementEvent.RELATIVE));
			break;
		case GLFW.GLFW_KEY_A:
			Entities.bodyComponentMap.get(entity).fireEvent(new MovementEvent(new Vector3f(-1, 0, 0), MovementEvent.RELATIVE));
			break;
		case GLFW.GLFW_KEY_D:
			Entities.bodyComponentMap.get(entity).fireEvent(new MovementEvent(new Vector3f(1, 0, 0), MovementEvent.RELATIVE));
			break;
		case GLFW.GLFW_KEY_SPACE:
			Entities.bodyComponentMap.get(entity).fireEvent(new MovementEvent(new Vector3f(0, 1, 0), MovementEvent.FIXED));
			break;
		case GLFW.GLFW_KEY_LEFT_SHIFT:
			Entities.bodyComponentMap.get(entity).fireEvent(new MovementEvent(new Vector3f(0, -1, 0), MovementEvent.FIXED));
			break;
		}
	}

	@Override
	public void mouseButtonPressed(MouseButtonInputEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseButtonReleased(MouseButtonInputEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseButtonHeld(MouseButtonInputEvent e) {
		// Move this to an FPS controller???
		if(e.getMouseButton() == 0 && fireCooldown <= 0) {
			fireCooldown = 0.2f;
			
			//Entity bullet = new Entity(getContainer());
			/*long bullet = Entities.reserveNewEntity();
			Body body = getContainer().getPhysics().createBody(bullet, new JBulletBodyParams(new SphereShape(1), BTUtils.vector3f(getPosition().x, getPosition().y, getPosition().z), new Quat4f(0, 0, 0, 1)));
			//bullet.setBody(body);
			Vector3f shotVector = new Vector3f(0, 0, -1f);
			shotVector.mul(getCameraView());
			shotVector.normalize();
			shotVector.mul(25);
			body.applyForce(shotVector);*/
			
			//getContainer().addEntity(bullet);
		}
	}

	@Override
	public void mouseMoved(MouseMotionInputEvent e) {
		if(GLFW.glfwGetInputMode(WindowManager.requestMainWindow(), GLFW.GLFW_CURSOR) == GLFW.GLFW_CURSOR_DISABLED) {
			Vector3f rotation = new Vector3f(MathUtils.clamp((float) (e.getDy() / 10), -60, 80), (float) (e.getDx() / 10), 0f /*roll*/);
			if(inverted) {
				rotation.negate();
			}
			Entities.bodyComponentMap.get(entity).fireEvent(new RotationEvent(rotation));
		}
	}

	@Override
	public void mouseDragged(MouseMotionInputEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseScrolled(MouseScrollInputEvent e) {
		Entities.bodyComponentMap.get(entity).fireEvent(new SpeedChangeEvent(e.getDy()));
	}

	@Override
	public void timePassed(TimeEvent e) {
		if(fireCooldown > 0) {
			fireCooldown -= e.getDelta();
		}
	}

}
