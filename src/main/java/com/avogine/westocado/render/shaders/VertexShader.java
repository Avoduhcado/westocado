package com.avogine.westocado.render.shaders;

import com.avogine.westocado.render.shaders.utils.ShaderProgram;
import com.avogine.westocado.render.shaders.utils.UniformMatrix;
import com.avogine.westocado.render.shaders.utils.UniformVec3;

public class VertexShader extends ShaderProgram {

	public UniformMatrix model = new UniformMatrix("model");
	public UniformMatrix projection = new UniformMatrix("projection");
	public UniformMatrix view = new UniformMatrix("view");
	public UniformVec3 color = new UniformVec3("color");
	
	public VertexShader(String vertexFile, String fragmentFile, String...inVariables) {
		super(vertexFile, fragmentFile, inVariables);
		storeAllUniformLocations(projection, view, model, color);
	}
	
}
