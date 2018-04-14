package com.avogine.westocado.render.shaders;

import com.avogine.westocado.render.shaders.utils.ShaderProgram;
import com.avogine.westocado.render.shaders.utils.UniformSampler;

public class CombineShader extends ShaderProgram {

	//private static final String VERTEX_FILE = SHADER_PATH + "simpleVertex.glsl";
	//private static final String FRAGMENT_FILE = SHADER_PATH + "combineFragment.glsl";
	
	private UniformSampler texture1 = new UniformSampler("texture1");
	private UniformSampler texture2 = new UniformSampler("texture2");
	
	public CombineShader(String vertexFile, String fragmentFile, String...inVariables) {
		super(vertexFile, fragmentFile, inVariables);
		storeAllUniformLocations(texture1, texture2);
		connectTextureUnits();
	}
	
	private void connectTextureUnits() {
		super.start();
		texture1.loadTexUnit(0);
		texture1.loadTexUnit(1);
		super.stop();
	}

}
