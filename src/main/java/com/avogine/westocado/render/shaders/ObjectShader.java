package com.avogine.westocado.render.shaders;

import com.avogine.westocado.render.shaders.utils.ShaderProgram;
import com.avogine.westocado.render.shaders.utils.UniformMatrix;
import com.avogine.westocado.render.shaders.utils.UniformSampler;
import com.avogine.westocado.render.shaders.utils.UniformVec3;

public class ObjectShader extends ShaderProgram {

	private static final int DIFFUSE_TEX_UNIT = 0;
	
	public UniformMatrix model = new UniformMatrix("model");
	public UniformMatrix projection = new UniformMatrix("projection");
	public UniformMatrix view = new UniformMatrix("view");
	public UniformSampler texture = new UniformSampler("tex");
	public UniformSampler texture2 = new UniformSampler("tex2");
	public UniformVec3 lightPosition = new UniformVec3("lightPosition");
	public UniformVec3 lightColor = new UniformVec3("lightColor");
	
	public ObjectShader(String vertexFile, String fragmentFile, String...inVariables) {
		super(vertexFile, fragmentFile, inVariables);
		storeAllUniformLocations(projection, view, model, texture, texture2, lightPosition, lightColor);
		connectTextureUnits();
	}

	private void connectTextureUnits() {
		super.start();
		texture.loadTexUnit(DIFFUSE_TEX_UNIT);
		texture2.loadTexUnit(1);
		super.stop();
	}
	
}
