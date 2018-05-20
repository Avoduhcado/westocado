package com.avogine.westocado;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

import com.avogine.westocado.io.Window;
import com.avogine.westocado.io.utils.TimeEvent;
import com.avogine.westocado.system.AvoEventQueue;
import com.avogine.westocado.system.TimeWizard;
import com.avogine.westocado.utils.system.WindowManager;

public class Theater {

	public static final boolean wireFrame = false;

	private Window window;
	
	private static double currentTime;
	private static double lastTime;
	private static double frameTime;
	private static double delta;
	
	private static int fps;
	
	private double refreshRate = 60;
	private double frameLag = 0.0;
	private long milliSleep;
	private int nanoSleep;
	
	public Theater() {
		GLFWErrorCallback.createPrint(System.err).set();
		
		if(!GLFW.glfwInit()) {
			throw new IllegalStateException("Failed to initialize GLFW!");
		}
		
		long windowID = WindowManager.requestNewWindow();
		window = WindowManager.requestWindow(windowID);
		//window.loadScene();
	}
	
	private void play() {
		lastTime = getTime();
		System.out.println("Time: " + lastTime);
		
		while(!window.shouldClose()) {
			doFps();

			window.update();
			window.render();

			doSync();
			doLater();
		}
		
		destroy();
	}
	
	private void doFps() {
		currentTime = getTime();
		delta = currentTime - lastTime;
		TimeWizard.fireEvent(new TimeEvent(delta));
		lastTime = currentTime;
		frameTime += delta;
		if(frameTime >= 1.0) {
			window.setTitle("Pixel Hell fps:" + fps);
			fps = 0;
			frameTime = 0;
			refreshRate = window.getRefreshRate();
		} else {
			fps++;
		}
	}
	
	private void doSync() {
		// Get the frame difference from what we're currently displaying and what we should be displaying based on target refresh rate and progress through the current second
		frameLag = fps - (refreshRate * frameTime);
		// Don't sleep if we're already behind
		if(frameLag < 0) {
			return;
		}
		try {
			// Simple calculation to get flat milliseconds to sleep
			milliSleep = (long) (1000.0 / (refreshRate - frameLag));
			// Get the decimal value from the total sleep time up to the 6th place
			nanoSleep = (int) (((1000.0 / (refreshRate - frameLag)) * 1000000) - (milliSleep * 1000000));
			Thread.sleep(milliSleep, nanoSleep);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Process events after all game loop interaction has ceased for the frame.
	 */
	private void doLater() {
		AvoEventQueue.processEvents();
	}
	
	private void destroy() {
		window.destroy();
		// End it all if we've destroyed the main window
		if(WindowManager.requestMainWindow() == -1L) {
			GLFW.glfwTerminate();
		}
	}
	
	public static float getDeltaChange(float value) {
		return (float) (delta * value);
	}
	
	public static double getDelta() {
		return delta;
	}
	
	private static double getTime() {
		return GLFW.glfwGetTime();
	}
	
	public static void main(String[] args) {
		Theater theater = new Theater();
		theater.play();
	}
	
}
