package com.avogine.westocado.render.utils;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import com.avogine.westocado.render.shaders.CombineShader;
import com.avogine.westocado.setup.scene.QuadRender;

public class CombineFilter {

	private QuadRender render;
	private CombineShader shader;
	
	public CombineFilter() {
		shader = new CombineShader("simpleVertex.glsl", "combineFragment.glsl", "position");
		render = new QuadRender();
	}
	
	public void render(int texture1, int texture2) {
		shader.start();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture1);
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture2);
		render.renderQuad();
		shader.stop();
	}
	
	public void cleanUp() {
		render.cleanUp();
		shader.cleanUp();
	}
	
}
