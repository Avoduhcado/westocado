package com.avogine.westocado.utils.loader;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;

import com.avogine.westocado.render.data.Texture;

public class TextureLoader {

	private static Map<String, Texture> textureMap = new HashMap<>();
	
	public static Texture loadTexture(String textureFileName) {
		Texture texture = textureMap.get(textureFileName);
		if(texture != null) {
			return texture;
		}
		
		// TODO Extract ".png" to some kinda constant I guess
		texture = new Texture(textureFileName + ".png");
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
		
		textureMap.put(textureFileName, texture);
		
		return texture;
	}
	
}
