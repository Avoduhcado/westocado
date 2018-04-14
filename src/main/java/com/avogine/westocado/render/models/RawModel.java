package com.avogine.westocado.render.models;

public class RawModel {

	private int vaoId;
	private int vertexCount;
	
	public RawModel(int vaoId, int vertexCount) {
		this.vaoId = vaoId;
		this.vertexCount = vertexCount;
	}
	
	public int getVaoId() {
		return vaoId;
	}
	
	public int getVertexCount() {
		return vertexCount;
	}
	
	public float getBoundingRadius() {
		// TODO Calculate an actual radius (Make a real mesh class? This is kind of a ghetto place to put this)
		return 10;
	}
	
}
