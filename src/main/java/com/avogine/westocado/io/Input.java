package com.avogine.westocado.io;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import com.avogine.westocado.io.utils.InputListener;
import com.avogine.westocado.io.utils.KeyInputEvent;
import com.avogine.westocado.io.utils.KeyInputListener;
import com.avogine.westocado.io.utils.MouseButtonInputEvent;
import com.avogine.westocado.io.utils.MouseButtonInputListener;
import com.avogine.westocado.io.utils.MouseMotionInputEvent;
import com.avogine.westocado.io.utils.MouseMotionInputListener;
import com.avogine.westocado.io.utils.MouseScrollInputEvent;
import com.avogine.westocado.io.utils.MouseScrollInputListener;
import com.avogine.westocado.utils.system.Screenshot;

public class Input {

	private long window;
	
	private boolean[] keys;
	private boolean[] mouseButtonDelays;
	
	private double mouseX;
	private double mouseY;
	private double mouseDX;
	private double mouseDY;
	private double mouseWheelDX;
	private double mouseWheelDY;
	
	private List<InputListener> inputListeners = new ArrayList<>();
	
	// TODO Clean up all this mouse shit, handle it all in actual listeners
	
	public Input(long window) {
		this.window = window;
		this.keys = new boolean[GLFW.GLFW_KEY_LAST];
		Arrays.fill(keys, false);
		
		this.mouseButtonDelays = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];
		Arrays.fill(mouseButtonDelays, false);

		double[] mX = new double[1];
		double[] mY = new double[1];
		GLFW.glfwGetCursorPos(window, mX, mY);
		mouseX = mX[0];
		mouseY = mY[0];
		
		GLFW.glfwSetKeyCallback(window, (w, key, scancode, action, mods) -> {
			// TODO Put this shit in an actual listener somewhere
			switch(key) {
			case GLFW.GLFW_KEY_ESCAPE:
				if(action == GLFW.GLFW_RELEASE && GLFW.glfwGetInputMode(window, GLFW.GLFW_CURSOR) != GLFW.GLFW_CURSOR_NORMAL) {
					GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
				}
				break;
			case GLFW.GLFW_KEY_F1:
				if(action == GLFW.GLFW_RELEASE) {
					if(GL11.glGetInteger(GL11.GL_POLYGON_MODE) == GL11.GL_LINE) {
						GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
					} else {
						GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
					}
				}
				break;
			case GLFW.GLFW_KEY_F12:
				if(action == GLFW.GLFW_RELEASE) {
					// TODO Get the actual screen size
					System.out.println(Screenshot.saveScreenshot(new File(System.getProperty("user.dir")), 1280, 720));
				}
				break;
			default:
				keys[key] = action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT ? true : false;
			}
			
			fireKeyInput(new KeyInputEvent(getKeyEventType(action), key));
		});
		
		GLFW.glfwSetCursorPosCallback(window, (w, x, y) -> {
			fireMouseMotionEvent(new MouseMotionInputEvent(
					GLFW.glfwGetMouseButton(window, 0) == GLFW.GLFW_PRESS ? MouseMotionInputEvent.MOUSE_DRAGGED : MouseMotionInputEvent.MOUSE_MOVED,
					x, y, mouseX - x, mouseY - y));
			
			mouseDX = x - mouseX;
			mouseDY = y - mouseY;
			mouseX = x;
			mouseY = y;
		});
		
		GLFW.glfwSetScrollCallback(window, (w, x, y) -> {
			fireMouseScrollEvent(new MouseScrollInputEvent(MouseScrollInputEvent.MOUSE_SCROLL, x, y));
			
			mouseWheelDX = x;
			mouseWheelDY = y;
		});
		
		GLFW.glfwSetMouseButtonCallback(window, (w, button, action, mods) -> {
			if(button == 0 && action == GLFW.GLFW_PRESS && GLFW.glfwGetInputMode(window, GLFW.GLFW_CURSOR) != GLFW.GLFW_CURSOR_DISABLED) {
				GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
			}
			
			if(action == GLFW.GLFW_PRESS) {
				mouseButtonDelays[button] = true;
			}
			
			fireMouseButtonEvent(new MouseButtonInputEvent(getMouseButtonEventType(action), button));
		});
	}
	
	public void fireKeyInput(KeyInputEvent event) {
		inputListeners.stream()
		.filter(l -> l instanceof KeyInputListener)
		.map(l -> (KeyInputListener) l)
		.forEach(l -> {
			switch(event.getEventType()) {
			case KeyInputEvent.KEY_PRESS:
				l.keyPressed(event);
				break;
			case KeyInputEvent.KEY_RELEASE:
				l.keyReleased(event);
				break;
			case KeyInputEvent.KEY_HELD:
				l.keyHeld(event);
				break;
			}
			// TODO Concept of consumed events? Break here when consumed
		});
	}
	
	public void fireMouseButtonEvent(MouseButtonInputEvent event) {
		inputListeners.stream()
		.filter(l -> l instanceof MouseButtonInputListener)
		.map(l -> (MouseButtonInputListener) l)
		.forEach(l -> {
			switch(event.getEventType()) {
			case MouseButtonInputEvent.MOUSE_PRESS:
				l.mouseButtonPressed(event);
				break;
			case MouseButtonInputEvent.MOUSE_RELEASE:
				l.mouseButtonReleased(event);
				break;
			case MouseButtonInputEvent.MOUSE_HELD:
				l.mouseButtonHeld(event);
				break;
			}
		});
	}
	
	public void fireMouseScrollEvent(MouseScrollInputEvent event) {
		inputListeners.stream()
		.filter(l -> l instanceof MouseScrollInputListener)
		.map(l -> (MouseScrollInputListener) l)
		.forEach(l -> l.mouseScrolled(event));
	}
	
	public void fireMouseMotionEvent(MouseMotionInputEvent event) {
		inputListeners.stream()
		.filter(l -> l instanceof MouseMotionInputListener)
		.map(l -> (MouseMotionInputListener) l)
		.forEach(l -> {
			switch(event.getEventType()) {
			case MouseMotionInputEvent.MOUSE_MOVED:
				l.mouseMoved(event);
				break;
			case MouseMotionInputEvent.MOUSE_DRAGGED:
				l.mouseDragged(event);
				break;
			}
		});
	}
	
	public boolean isKeyDown(int key) {
		return GLFW.glfwGetKey(window, key) == GLFW.GLFW_PRESS;
	}

	public boolean isKeyPressed(int key) {
		return isKeyDown(key) && !keys[key];
	}

	public boolean isKeyReleased(int key) {
		return !isKeyDown(key) && keys[key];
	}
	
	public boolean isMouseButtonDown(int button) {
		return GLFW.glfwGetMouseButton(window, button) == GLFW.GLFW_PRESS;
	}
	
	public double getMouseX() {
		return mouseX;
	}
	
	public double getMouseY() {
		return mouseY;
	}
	
	public double getMouseDX() {
		return mouseDX;
	}
	
	public double getMouseDY() {
		return mouseDY;
	}
	
	public double getMouseWheelDX() {
		return mouseWheelDX;
	}
	
	public double getMouseWheelDY() {
		return mouseWheelDY;
	}
	
	public void update() {
		mouseDX = 0;
		mouseDY = 0;
		mouseWheelDX = 0;
		mouseWheelDY = 0;
		
		for(int i = GLFW.GLFW_MOUSE_BUTTON_1; i < GLFW.GLFW_MOUSE_BUTTON_LAST; i++) {
			if(isMouseButtonDown(i) && !mouseButtonDelays[i]) {
				fireMouseButtonEvent(new MouseButtonInputEvent(MouseButtonInputEvent.MOUSE_HELD, i));
			} else if(isMouseButtonDown(i)) {
				mouseButtonDelays[i] = false;
			}
		}
		
		// Key_Repeat has god awful lag, so we're gonna roll our own keyDown events
		for(int i = GLFW.GLFW_KEY_SPACE; i < keys.length; i++) {
			if(isKeyDown(i)) {
				fireKeyInput(new KeyInputEvent(KeyInputEvent.KEY_HELD, i));
			}
		}
	}
	
	public void addInputListener(InputListener l) {
		inputListeners.add(l);
	}
	
	public void removeInputListener(InputListener l) {
		inputListeners.remove(l);
	}
	
	public static int getKeyEventType(int action) {
		switch(action) {
		case GLFW.GLFW_PRESS:
			return KeyInputEvent.KEY_PRESS;
		case GLFW.GLFW_RELEASE:
			return KeyInputEvent.KEY_RELEASE;
		case GLFW.GLFW_REPEAT:
			return KeyInputEvent.KEY_HELD;
		}
		return -1;
	}
	
	public static int getMouseButtonEventType(int action) {
		switch(action) {
		case GLFW.GLFW_PRESS:
			return MouseButtonInputEvent.MOUSE_PRESS;
		case GLFW.GLFW_RELEASE:
			return MouseButtonInputEvent.MOUSE_RELEASE;
		// XXX This doesn't actually work because GLFW will not fire events itself for mouse button repeats, so its handled in the input.update method
		case GLFW.GLFW_REPEAT:
			return MouseButtonInputEvent.MOUSE_HELD;
		}
		return -1;
	}
	
}
