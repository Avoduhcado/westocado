package com.avogine.westocado.render.utils;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import com.avogine.westocado.io.Window;
import com.avogine.westocado.render.shaders.OutlineShader;
import com.avogine.westocado.setup.scene.QuadRender;

// TODO Make a super class for Filters
public class OutlineFilter {

	private OutlineShader shader;
	private QuadRender render;
	
	public OutlineFilter(int targetFboWidth, int targetFboHeight, Window window) {
		shader = new OutlineShader("simpleVertex.glsl", "outlineFragment.glsl", "position");
		render = new QuadRender(targetFboWidth, targetFboHeight, window);
	}
	
	public void render(int texture) {
		shader.start();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		render.renderQuad();
		shader.stop();
	}
	
	public int getOutputTexture() {
		return render.getOutputTexture();
	}
	
	public void cleanUp(){
		render.cleanUp();
		shader.cleanUp();
	}
}
