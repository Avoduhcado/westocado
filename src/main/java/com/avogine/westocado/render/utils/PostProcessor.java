package com.avogine.westocado.render.utils;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.avogine.westocado.io.Window;
import com.avogine.westocado.render.data.Mesh;
import com.avogine.westocado.render.data.VAO;
import com.avogine.westocado.utils.loader.TextureLoader;

public class PostProcessor {
	
	private static final float[] POSITIONS = { -1, 1, -1, -1, 1, 1, 1, -1 };	
	private static Mesh quad;
	
	private static OutlineFilter outlineFilter;
	private static CombineFilter combineFilter;
	private static HatchingFilter hatchingFilter;
	
	private static ScreenFilter screenFilter;
	
	private static int hatchTexture;
	
	public static void init(Window window) {
		VAO vao = VAO.create();
		vao.bind(0);
		vao.createAttribute(0, POSITIONS, 2);
		vao.unbind(0);
		quad = new Mesh(vao);
		
		outlineFilter = new OutlineFilter(1280, 720, window);
		combineFilter = new CombineFilter();
		hatchingFilter = new HatchingFilter();
		hatchTexture = TextureLoader.loadTexture("default35").getId();
		
		screenFilter = new ScreenFilter();
	}
	
	public static void doPostProcessing(int colorTexture, int depthTexture) {
		start();
		//outlineFilter.render(depthTexture);
		//combineFilter.render(colorTexture, outlineFilter.getOutputTexture());
		//hatchingFilter.render(depthTexture, hatchTexture);
		screenFilter.render(colorTexture);
		end();
	}
	
	public static void cleanUp() {
		outlineFilter.cleanUp();
		combineFilter.cleanUp();
		hatchingFilter.cleanUp();
		screenFilter.cleanUp();
	}
	
	private static void start() {
		GL30.glBindVertexArray(quad.getVao().id);
		GL20.glEnableVertexAttribArray(0);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}
	
	private static void end() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}

}
