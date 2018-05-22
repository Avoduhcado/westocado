package com.avogine.westocado.render.data;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.stb.STBImage.stbi_failure_reason;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

public class Texture {
	private final int id;
	private int width;
	private int height;

	public Texture() {
		id = glGenTextures();
	}
	
	public int getId() {
		return id;
	}

	public void bind() {
		glBindTexture(GL_TEXTURE_2D, id);
	}

	public void setParameter(int name, int value) {
		glTexParameteri(GL_TEXTURE_2D, name, value);
	}

	public void uploadData(int width, int height, ByteBuffer data) {
		uploadData(GL_RGBA8, width, height, GL_RGBA, data);
	}

	public void uploadData(int internalFormat, int width, int height, int format, ByteBuffer data) {
		glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, width, height, 0, format, GL_UNSIGNED_BYTE, data);
	}

	public void delete() {
		glDeleteTextures(id);
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		if (width > 0) {
			this.width = width;
		}
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		if (height > 0) {
			this.height = height;
		}
	}

	public void bindToUnit(int unit) {
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + unit);
		bind();
	}

	public static Texture createTexture(int width, int height, ByteBuffer data) {
		Texture texture = new Texture();
		texture.setWidth(width);
		texture.setHeight(height);

		texture.bind();

		texture.setParameter(GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		texture.setParameter(GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		texture.setParameter(GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		texture.setParameter(GL_TEXTURE_MAG_FILTER, GL_NEAREST);

		texture.uploadData(GL_RGBA, width, height, GL_RGBA, data);

		return texture;
	}

	public static Texture loadTexture(String path) {
		String fullPath = "graphics/" + path;
		System.out.println("loading texture: " + fullPath);
		
		try (MemoryStack stack = MemoryStack.stackPush()) {
			ByteBuffer fileBuf;
			try (InputStream in = ClassLoader.getSystemResourceAsStream(fullPath)) {
				byte[] bytes = in.readAllBytes();
				fileBuf = MemoryUtil.memAlloc(bytes.length).put(bytes).flip();
			} catch (Exception ex) {
				throw new RuntimeException("Unable to load texture: " + fullPath, ex);
			}
			
			try {
				IntBuffer w = stack.mallocInt(1);
				IntBuffer h = stack.mallocInt(1);
				IntBuffer comp = stack.mallocInt(1);
	
				ByteBuffer image = STBImage.stbi_load_from_memory(fileBuf, w, h, comp, 4);
				
				if (image == null) {
					throw new RuntimeException("Failed to load texture: " + fullPath + " - " + stbi_failure_reason());
				}
	
				return createTexture(w.get(), h.get(), image);
			} finally {
				MemoryUtil.memFree(fileBuf);
			}
		}
	}

}
