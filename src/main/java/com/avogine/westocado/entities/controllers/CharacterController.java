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
import com.avogine.westocado.io.utils.MouseMotionInputEvent;
import com.avogine.westocado.io.utils.MouseMotionInputListener;
import com.avogine.westocado.io.utils.MouseScrollInputEvent;
import com.avogine.westocado.io.utils.MouseScrollInputListener;
import com.avogine.westocado.utils.system.AvoEvent;
import com.avogine.westocado.utils.system.WindowManager;

public class CharacterController extends Controller implements KeyInputListener, MouseMotionInputListener, MouseScrollInputListener {

	private boolean inverted = true;
	
	public CharacterController(long entity, Window window) {
		super(entity);
		window.getInput().addInputListener(this);
	}

	@Override
	public void keyPressed(KeyInputEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyInputEvent e) {
		switch(e.getKey()) {
		case GLFW.GLFW_KEY_W:
			Entities.bodyComponentMap.fireEventAt(entity, new MovementEvent(new Vector3f(0, 0, -1), MovementEvent.RELATIVE));
			break;
		case GLFW.GLFW_KEY_S:
			Entities.bodyComponentMap.fireEventAt(entity, new MovementEvent(new Vector3f(0, 0, 1), MovementEvent.RELATIVE));
			break;
		case GLFW.GLFW_KEY_A:
			Entities.bodyComponentMap.fireEventAt(entity, new MovementEvent(new Vector3f(-1, 0, 0), MovementEvent.RELATIVE));
			break;
		case GLFW.GLFW_KEY_D:
			Entities.bodyComponentMap.fireEventAt(entity, new MovementEvent(new Vector3f(1, 0, 0), MovementEvent.RELATIVE));
			break;
		}
	}

	@Override
	public void keyHeld(KeyInputEvent e) {
		switch(e.getKey()) {
		case GLFW.GLFW_KEY_W:
			Entities.bodyComponentMap.fireEventAt(entity, new MovementEvent(new Vector3f(0, 0, 1), MovementEvent.RELATIVE));
			break;
		case GLFW.GLFW_KEY_S:
			Entities.bodyComponentMap.fireEventAt(entity, new MovementEvent(new Vector3f(0, 0, -1), MovementEvent.RELATIVE));
			break;
		case GLFW.GLFW_KEY_A:
			Entities.bodyComponentMap.fireEventAt(entity, new MovementEvent(new Vector3f(1, 0, 0), MovementEvent.RELATIVE));
			break;
		case GLFW.GLFW_KEY_D:
			Entities.bodyComponentMap.fireEventAt(entity, new MovementEvent(new Vector3f(-1, 0, 0), MovementEvent.RELATIVE));
			break;
		}
	}

	@Override
	public void control() {
		// TODO Auto-generated method stub

	}

	@Override
	public void fireEvent(AvoEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseScrolled(MouseScrollInputEvent e) {
		Entities.bodyComponentMap.fireEventAt(entity, new SpeedChangeEvent(e.getDy()));
	}

	@Override
	public void mouseMoved(MouseMotionInputEvent e) {
		if(GLFW.glfwGetInputMode(WindowManager.requestMainWindow(), GLFW.GLFW_CURSOR) == GLFW.GLFW_CURSOR_DISABLED) {
			Vector3f rotation = new Vector3f(0f, (float) (e.getDx() / 10), 0f);
			if(inverted) {
				rotation.negate();
			}
			Entities.bodyComponentMap.fireEventAt(entity, new RotationEvent(rotation));
		}
	}

	@Override
	public void mouseDragged(MouseMotionInputEvent e) {
		// TODO Auto-generated method stub
		
	}

}
