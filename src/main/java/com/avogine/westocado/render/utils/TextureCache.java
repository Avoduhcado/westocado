package com.avogine.westocado.render.utils;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;

import com.avogine.westocado.render.data.Texture;

/**
 * Singleton instance object that handles loading and storing {@link Texture} objects.
 */
public class TextureCache {

	private Map<String, Texture> textureMap = new HashMap<>();
	
	private static TextureCache instance;
	
	public static TextureCache getInstance() {
		if(instance == null) {
			instance = new TextureCache();
		}
		
		return instance;
	}
	
	/**
	 * Gets a {@link Texture} stored in the cache if it exists otherwise returns null.
	 * @param textureName The full resource path name of the texture file in the cache
	 * @return A {@link Texture} object already loaded into the cache or null if it doesn't exist
	 */
	public Texture getTextureOrNull(String texturePath) {
		if(!textureMap.containsKey(texturePath)) {
			return null;
		}
		
		return textureMap.get(texturePath);
	}
	
	/**
	 * Returns a {@link Texture} from a specified path, if it isn't already loaded into the map it will load the texture and then return a new {@link Texture}.
	 * @param texturePath The full resource path name of the texture file in the cache or on disk
	 * @return
	 */
	public Texture getTexture(String texturePath) {
		Texture texture = textureMap.get(texturePath);
		if(texture == null) {
			texture = loadTexture(texturePath);
			textureMap.put(texturePath, texture);
		}
		
		return texture;
	}
	
	/**
	 * Remove a mapping for a {@link Texture} in the cache.
	 * @param texturePath The {@link Texture} to be freed
	 * @return A {@link Texture} object that was previously stored in the cache or null if there was no mapping
	 */
	public Texture freeTexture(String texturePath) {
		if(!textureMap.containsKey(texturePath)) {
			System.out.println("Texture: " + texturePath + " was not in the cache.");
			return null;
		}
		
		return textureMap.remove(texturePath);
	}
	
	public static Texture loadTexture(String textureFileName) {
		// TODO Extract ".png" to some kinda constant I guess
		Texture texture = new Texture(textureFileName);
		GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -0.4f);
		if(GL.getCapabilities().GL_EXT_texture_filter_anisotropic) {
			// XXX: Extract some global Anisotropic filtering value
			float amount = Math.min(4f, GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT));
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, amount);
		} else {
			System.out.println("Anisotropic filtering not supported.");
		}
				
		return texture;
	}
	
}
