package com.avogine.westocado.entities.models;

import java.io.FileNotFoundException;
import java.util.List;

import com.avogine.westocado.render.data.Mesh;
import com.avogine.westocado.render.data.Texture;
import com.avogine.westocado.utils.loader.ModelLoader;
import com.avogine.westocado.utils.loader.TextureLoader;
import com.avogine.westocado.utils.system.AvoEvent;

public class PlainModel extends Model {

	private List<Mesh> meshList;
	private Texture texture;
	private Texture texture2;
	
	public PlainModel(long entity, String modelName, String textureName) {
		super(entity);
		try {
			meshList = ModelLoader.loadAIModel(modelName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		texture = TextureLoader.loadTexture(textureName);
	}
	
	public Mesh getMesh() {
		return meshList.get(0);
	}
	
	public List<Mesh> getMeshes() {
		return meshList;
	}
	
	public void setMesh(Mesh mesh) {
		//this.mesh = mesh;
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	
	public Texture getTexture2() {
		return texture2;
	}

	@Override
	public void fireEvent(AvoEvent e) {
		// TODO Auto-generated method stub

	}

}
