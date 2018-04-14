package com.avogine.westocado.render.shaders.utils;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

public class UniformMatrix extends Uniform{
	
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

	public UniformMatrix(String name) {
		super(name);
	}
	
	public void loadMatrix(Matrix4f matrix){
		GL20.glUniformMatrix4fv(super.getLocation(), false, matrix.get(matrixBuffer));
	}
	
	

}
