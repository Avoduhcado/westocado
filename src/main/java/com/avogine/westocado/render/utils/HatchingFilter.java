package com.avogine.westocado.render.utils;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import com.avogine.westocado.render.shaders.HatchDepthShader;
import com.avogine.westocado.setup.scene.ObjectRender;
import com.avogine.westocado.setup.scene.QuadRender;

public class HatchingFilter {

	private QuadRender render;
	private HatchDepthShader shader;
	
	public HatchingFilter() {
		shader = new HatchDepthShader("simpleVertex.glsl", "hatchDepthFragment.glsl", "position");
		shader.start();
		shader.near.loadFloat(ObjectRender.NEAR_PLANE);
		shader.far.loadFloat(ObjectRender.FAR_PLANE);
		shader.stop();
		
		render = new QuadRender();
	}
	
	public void render(int texture, int hatch) {
		shader.start();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, hatch);
		render.renderQuad();
		shader.stop();
	}
	
	public void cleanUp() {
		render.cleanUp();
		shader.cleanUp();
	}
	
}
