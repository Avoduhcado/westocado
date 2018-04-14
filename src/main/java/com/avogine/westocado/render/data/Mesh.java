package com.avogine.westocado.render.data;

public class Mesh {

	private VAO vao;
	
	public Mesh(VAO vao) {
		this.vao = vao;
	}
	
	public VAO getVao() {
		return vao;
	}
	
	public void setVao(VAO vao) {
		this.vao = vao;
	}
	
}
