package com.avogine.westocado.io;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import com.avogine.westocado.setup.Stage;
import com.avogine.westocado.utils.loader.Loader;
import com.avogine.westocado.utils.system.WindowManager;

import de.matthiasmann.twl.utils.PNGDecoder;

public class Window {

	private int refreshRate = 60;
	private int unfocusedRefreshRate = 30;
	
	private long ID;
	
	private int width;
	private int height;
	private String title;
	
	private boolean hasFocus = true;
	
	private Input input;
	private Stage stage;
	private Loader loader;
			
	public Window(int width, int height, String title) {
		this.width = width;
		this.height = height;
		this.title = title;
	}
	
	public void createWindow() {
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
		// XXX: global setting
		// TODO Extract for option to use basic AA not FBO AA
		GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, 8);
		GLFW.glfwWindowHint(GLFW.GLFW_DEPTH_BITS, 24);
		GLFW.glfwWindowHint(GLFW.GLFW_STENCIL_BITS, 8);
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		
		ID = GLFW.glfwCreateWindow(width, height, title, 0, 0);
		if(ID == 0) {
			throw new IllegalStateException("Failed to create window!");
		}
		
		try {
			PNGDecoder decoder = new PNGDecoder(ClassLoader.getSystemResourceAsStream("graphics/Icon.png"));
			
			int iconWidth = decoder.getWidth();
			int iconHeight = decoder.getHeight();
			ByteBuffer buffer = BufferUtils.createByteBuffer(iconWidth * iconHeight * 4);
			decoder.decode(buffer, iconWidth * 4, PNGDecoder.Format.RGBA);
			buffer.flip();
			GLFWImage image = GLFWImage.malloc();
			image.set(iconWidth, iconHeight, buffer);
			GLFWImage.Buffer images = GLFWImage.malloc(1);
			images.put(0, image);
	
			GLFW.glfwSetWindowIcon(ID, images);
	
			images.free();
			image.free();
		} catch (IOException e) {
			System.err.println("Failed to load icon image.");
			e.printStackTrace();
		}

		/*GLFW.glfwSetMouseButtonCallback(ID, (window, button, action, mods) -> {
			if(button == 0 && action == GLFW.GLFW_PRESS && GLFW.glfwGetInputMode(ID, GLFW.GLFW_CURSOR) != GLFW.GLFW_CURSOR_DISABLED) {
				GLFW.glfwSetInputMode(ID, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
			}
		});*/
		
		GLFW.glfwSetFramebufferSizeCallback(ID, new GLFWFramebufferSizeCallback() {
			@Override
			public void invoke(long window, int width, int height) {
				GL11.glViewport(0, 0, width, height);
				Window.this.width = width;
				Window.this.height = height;
			}
		});
		
		GLFW.glfwSetWindowFocusCallback(ID, (window, focused) -> {
			hasFocus = focused;
		});
		
		GLFW.glfwMakeContextCurrent(ID);
		GL.createCapabilities();
		
		GLFWVidMode videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
		setRefreshRate(videoMode.refreshRate());
		GLFW.glfwSetWindowPos(ID, (videoMode.width() - width) / 2, (videoMode.height() - height) / 2);
		
		// Enable backface culling
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		
		// Enable v-sync
		//GLFW.glfwSwapInterval(1);
		// Enable multisampling
		GL11.glEnable(GL13.GL_MULTISAMPLE);
		
		GL11.glClearColor((float) Math.random(), (float) Math.random(), (float) Math.random(), 1);
		
		GLFW.glfwShowWindow(ID);
		GLFW.glfwSetInputMode(ID, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
		
		loader = new Loader();
		
		input = new Input(ID);
		
		stage = new Stage(this);
	}
	
	public void render() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		stage.render();
		
		GLFW.glfwSwapBuffers(ID);
	}
	
	public void update() {
		input.update();
		
		stage.simulateWorld();
		
		GLFW.glfwPollEvents();
	}
	
	public boolean shouldClose() {
		return GLFW.glfwWindowShouldClose(ID);
	}

	public void destroy() {
		stage.cleanUp();
		GLFW.glfwDestroyWindow(ID);
		
		WindowManager.removeWindow(ID);
	}
	
	public void setTitle(String title) {
		GLFW.glfwSetWindowTitle(ID, title);
	}
	
	public Input getInput() {
		return input;
	}
	
	public Loader getLoader() {
		return loader;
	}

	public int getRefreshRate() {
		if(hasFocus) {
			return refreshRate;
		} else {
			return unfocusedRefreshRate;
		}
	}

	public void setRefreshRate(int refreshRate) {
		this.refreshRate = refreshRate;
	}

	public long getID() {
		return ID;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
}
