package com.avogine.westocado.setup;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import com.avogine.westocado.entities.bodies.Body;
import com.avogine.westocado.entities.bodies.CameraBody;
import com.avogine.westocado.entities.components.EntityComponent;
import com.avogine.westocado.entities.subcomponents.Tweenable;
import com.avogine.westocado.io.Window;
import com.avogine.westocado.io.utils.MouseButtonInputEvent;
import com.avogine.westocado.io.utils.MouseButtonInputListener;
import com.avogine.westocado.io.utils.MouseMotionInputEvent;
import com.avogine.westocado.io.utils.MouseMotionInputListener;
import com.avogine.westocado.io.utils.TimeEvent;
import com.avogine.westocado.utils.math.MathUtils;
import com.avogine.westocado.utils.math.TweenUtils.Tween;
import com.avogine.westocado.utils.system.AvoEvent;
import com.avogine.westocado.utils.system.WindowManager;

/**
 * Currently just a third person camera implementation
 */
public class Camera extends EntityComponent implements MouseMotionInputListener, MouseButtonInputListener {

	private Matrix4f viewMatrix;

	/** Rotation around the X axis */
	private float pitch;
	/** Rotation around the Y axis */
	private float yaw;
	
	private final float defaultYaw = 180f;
	
	private Vector3f position;
	private float followRadius;
	
	private Body focus;
	
	private List<Tweenable> tweens = new ArrayList<>();
	
	public Camera(long entity, Window window) {
		super(entity);
		window.getInput().addInputListener(this);
		viewMatrix = new Matrix4f();
		position = new Vector3f(0f, 12f, 10f);
		pitch = 22.5f;
		yaw = defaultYaw;
		followRadius = 25;
		// THERE MUST ALWAYS BE A LICHCAMERA
		setFocus(new CameraBody(entity));
	}
	
	public void setFocus(Body focus) {
		this.focus = focus;
	}
	
	public Matrix4f getViewMatrix() {
		return viewMatrix;
	}
	
	public Vector3f getPosition() {
		return position;
	}

	public void resetYaw() {
		yaw = defaultYaw;
	}
	
	public void updateViewMatrix() {
		viewMatrix.identity();
		viewMatrix.arcball(followRadius, new Vector3f(focus.getPosition().x, focus.getPosition().y + 7.5f, focus.getPosition().z),
				(float) Math.toRadians(pitch), (float) Math.toRadians(yaw - focus.getYaw()));
	}

	@Override
	public void fireEvent(AvoEvent e) {
	}

	@Override
	public void mouseMoved(MouseMotionInputEvent e) {
		if(GLFW.glfwGetInputMode(WindowManager.requestMainWindow(), GLFW.GLFW_CURSOR) == GLFW.GLFW_CURSOR_DISABLED) {
			pitch = MathUtils.clamp((float) (pitch - (e.getDy() / 10)), -65, 80);
			pitch %= 360;
		}
	}

	@Override
	public void mouseDragged(MouseMotionInputEvent e) {
		pitch = MathUtils.clamp((float) (pitch - (e.getDy() / 10)), -65, 80);
		pitch %= 360;
		yaw += e.getDx() / 10;
		yaw %= 360;
	}

	@Override
	public void mouseButtonPressed(MouseButtonInputEvent e) {
	}

	@Override
	public void mouseButtonReleased(MouseButtonInputEvent e) {
		tweens.add(new Tweenable(this, yaw, defaultYaw - yaw, 0.85f, Tween.OUT) {
			@Override
			public void timePassed(TimeEvent e) {
				yaw = tween((float) e.getDelta());
				yaw %= 360;
			}
			
			@Override
			public void finish() {
				super.finish();
				resetYaw();
				tweens.remove(this);
			}
		});
	}

	@Override
	public void mouseButtonHeld(MouseButtonInputEvent e) {
	}

}
