package com.avogine.westocado.render.shaders;

import com.avogine.westocado.render.shaders.utils.ShaderProgram;
import com.avogine.westocado.render.shaders.utils.UniformFloat;
import com.avogine.westocado.render.shaders.utils.UniformSampler;

public class HatchDepthShader extends ShaderProgram {

	private UniformSampler depthTexture = new UniformSampler("depthTexture");
	private UniformSampler hatchTexture = new UniformSampler("hatchTexture");
	public UniformFloat near = new UniformFloat("near");
	public UniformFloat far = new UniformFloat("far");
	
	public HatchDepthShader(String vertexFile, String fragmentFile, String... inVariables) {
		super(vertexFile, fragmentFile, inVariables);
		storeAllUniformLocations(depthTexture, hatchTexture, near, far);
		connectTextureUnits();
	}
	
	private void connectTextureUnits() {
		super.start();
		depthTexture.loadTexUnit(0);
		hatchTexture.loadTexUnit(1);
		super.stop();
	}

}
