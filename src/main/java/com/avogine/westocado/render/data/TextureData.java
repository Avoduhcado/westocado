package com.avogine.westocado.render.data;

import java.nio.ByteBuffer;

public class TextureData {

	private int width;
	private int height;
	private ByteBuffer buffer;
	
	public TextureData(ByteBuffer buffer, int width, int height) {
		this.width = width;
		this.height = height;
		this.buffer = buffer;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public ByteBuffer getBuffer() {
		return buffer;
	}
	
}
