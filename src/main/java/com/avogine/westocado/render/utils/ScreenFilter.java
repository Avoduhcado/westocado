package com.avogine.westocado.render.utils;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import com.avogine.westocado.render.shaders.SimpleShader;
import com.avogine.westocado.setup.scene.QuadRender;

public class ScreenFilter {

	private QuadRender render;
	private SimpleShader shader;
	
	public ScreenFilter() {
		shader = new SimpleShader("simpleVertex.glsl", "simpleFragment.glsl", "position");
		render = new QuadRender();
	}
	
	public void render(int texture) {
		shader.start();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		render.renderQuad();
		shader.stop();
	}
	
	public void cleanUp() {
		render.cleanUp();
		shader.cleanUp();
	}
	
}
