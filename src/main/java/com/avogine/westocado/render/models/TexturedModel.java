package com.avogine.westocado.render.models;

import com.avogine.westocado.render.data.ModelTexture;

public class TexturedModel {

	private RawModel rawModel;
	private ModelTexture texture;
	
	public TexturedModel(RawModel model, ModelTexture texture) {
		this.rawModel = model;
		this.texture = texture;
	}
	
	public RawModel getRawModel() {
		return rawModel;
	}
	
	public ModelTexture getTexture() {
		return texture;
	}
	
}
