package com.avogine.westocado.render.data;

import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import com.avogine.westocado.Theater;

public class Texture {

	private int id;
	private int width;
	private int height;
	private final int type;
	
	public Texture(String ref) {
		BufferedImage bi;
		try(InputStream in = ClassLoader.getSystemResourceAsStream("graphics/" + ref)) {
			// XXX: If you break this and disable textures effectively everything goes black and looks kinda neat
			bi = ImageIO.read(in);
			width = bi.getWidth();
			height = bi.getHeight();
			
			int[] pixelsRaw = bi.getRGB(0, 0, width, height, null, 0, width);
			ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 4);
			
			for(int i = 0; i < height; i++) {
				for(int j = 0; j < width; j++) {
					int pixel = pixelsRaw[i * width + j];
					pixels.put((byte) ((pixel >> 16) & 0xFF));	// RED
					pixels.put((byte) ((pixel >> 8) & 0xFF));	// GREEN
					pixels.put((byte) ((pixel >> 0) & 0xFF));	// BLUE
					pixels.put((byte) ((pixel >> 24) & 0xFF));	// ALPHA
				}
			}
			
			pixels.flip();
			
			id = glGenTextures();
			
			glBindTexture(GL_TEXTURE_2D, id);
			
			//GL11.glTexParameterf(GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST_MIPMAP_NEAREST);
			//GL11.glTexParameterf(GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST_MIPMAP_NEAREST);
			GL11.glTexParameterf(GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameterf(GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameterf(GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
			GL11.glTexParameterf(GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
			
			//GL11.glTexParameteri(GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL, 3);
			
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
			if(Theater.bullshit) {
				for(int level = 4; level > 0; level--) {
					try(InputStream in2 = ClassLoader.getSystemResourceAsStream("graphics/" + ref.substring(0, ref.length() - 5) + level + ".png")) {
						// XXX: If you break this and disable textures effectively everything goes black and looks kinda neat
						bi = ImageIO.read(in2);
						width = bi.getWidth();
						height = bi.getHeight();
						
						int[] pixelsRaw2 = bi.getRGB(0, 0, width, height, null, 0, width);
						ByteBuffer pixels2 = BufferUtils.createByteBuffer(width * height * 4);
						
						for(int i = 0; i < height; i++) {
							for(int j = 0; j < width; j++) {
								int pixel = pixelsRaw2[i * width + j];
								pixels2.put((byte) ((pixel >> 16) & 0xFF));	// RED
								pixels2.put((byte) ((pixel >> 8) & 0xFF));	// GREEN
								pixels2.put((byte) ((pixel >> 0) & 0xFF));	// BLUE
								pixels2.put((byte) ((pixel >> 24) & 0xFF));	// ALPHA
							}
						}
						
						pixels2.flip();
						
						glTexImage2D(GL_TEXTURE_2D, 5 - level, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels2); // Copy data to the second mipmap
					}
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			glBindTexture(GL_TEXTURE_2D, 0);
		}
		
		type = GL11.GL_TEXTURE_2D;
	}
	
	public void bindToUnit(int unit) {
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + unit);
		GL11.glBindTexture(type, id);
	}

	public void delete() {
		GL11.glDeleteTextures(id);
	}
	
	public int getId() {
		return id;
	}
}
