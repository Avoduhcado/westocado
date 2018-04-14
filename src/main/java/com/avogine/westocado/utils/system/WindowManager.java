package com.avogine.westocado.utils.system;

import java.util.HashMap;

import com.avogine.westocado.io.Window;

public class WindowManager {

	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	private static final String TITLE = "Pixel Hell";
	
	private static long mainWindow = -1L;
	private static HashMap<Long, Window> windows = new HashMap<>();
	
	// Utility for testing creating multiple windows in one instance
	/*
	GLFW.glfwSetKeyCallback(WindowManager.requestMainWindow(), (window, key, scancode, action, mods) -> {
		if(key == GLFW.GLFW_KEY_F && action == GLFW.GLFW_RELEASE) {
			Thread thread = new Thread() {
				public void run() {
					long id = WindowManager.requestNewWindow();
					Window win = WindowManager.requestWindow(id);
					while(!win.shouldClose()) {
						win.render();
						win.update();
					}
					win.destroy();
				}
			};
			
			thread.start();
		}
	});*/
	
	public static long requestNewWindow(int width, int height, String title) {
		Window window = new Window(width, height, title);
		window.createWindow();
		if(windows.isEmpty()) {
			mainWindow = window.getID();
		}
		windows.put(window.getID(), window);
		
		return window.getID();
	}
	
	public static long requestNewWindow() {
		return requestNewWindow(WIDTH, HEIGHT, TITLE);
	}
	
	public static Window requestMainWindowNow() {
		return requestWindow(requestMainWindow());
	}
	
	public static long requestMainWindow() {
		return mainWindow;
	}
	
	public static Window requestWindow(long ID) {
		if(!windows.containsKey(ID)) {
			return null;
		}
		
		return windows.get(ID);
	}
	
	public static void removeWindow(long ID) {
		windows.remove(ID);
		
		// Repopulate or clear mainWindow if the current one is removed
		if(!windows.containsKey(mainWindow)) {
			if(windows.isEmpty()) {
				mainWindow = -1L;
			} else {
				mainWindow = windows.keySet().iterator().next();
			}
		}
	}
	
}
