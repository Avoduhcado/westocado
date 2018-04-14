package com.avogine.westocado.render.shaders;

import com.avogine.westocado.render.shaders.utils.ShaderProgram;
import com.avogine.westocado.render.shaders.utils.UniformSampler;

public class SimpleShader extends ShaderProgram {

	private UniformSampler colorTexture = new UniformSampler("colorTexture");
	
	public SimpleShader(String vertexFile, String fragmentFile, String...inVariables) {
		super(vertexFile, fragmentFile, inVariables);
		storeAllUniformLocations(colorTexture);
		connectTextureUnits();
	}
	
	private void connectTextureUnits() {
		super.start();
		colorTexture.loadTexUnit(0);
		super.stop();
	}

}
