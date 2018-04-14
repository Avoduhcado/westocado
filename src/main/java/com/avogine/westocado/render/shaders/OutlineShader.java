package com.avogine.westocado.render.shaders;

import com.avogine.westocado.render.shaders.utils.ShaderProgram;
import com.avogine.westocado.render.shaders.utils.UniformSampler;

public class OutlineShader extends ShaderProgram {

	//private static final String VERTEX_FILE = SHADER_PATH + "simpleVertex.glsl";
	//private static final String FRAGMENT_FILE = SHADER_PATH + "outlineFragment.glsl";
	
	private UniformSampler depthTexture = new UniformSampler("depthTexture");
	
	public OutlineShader(String vertexFile, String fragmentFile, String...inVariables) {
		super(vertexFile, fragmentFile, inVariables);
		storeAllUniformLocations(depthTexture);
		connectTextureUnits();
	}
	
	private void connectTextureUnits() {
		super.start();
		depthTexture.loadTexUnit(0);
		super.stop();
	}

}
