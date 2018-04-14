package com.avogine.westocado.render;

import java.io.FileNotFoundException;
import java.util.List;

import com.avogine.westocado.render.data.Mesh;
import com.avogine.westocado.render.data.Texture;
import com.avogine.westocado.utils.loader.ModelLoader;
import com.avogine.westocado.utils.loader.TextureLoader;

public class Model {

	private List<Mesh> meshList;
	//private Mesh mesh;
	private Texture texture;
	private Texture texture2;
	
	public Model(String modelName, String textureName) {
		try {
			meshList = ModelLoader.loadAIModel(modelName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		texture = TextureLoader.loadTexture(textureName);
	}
	
	/*public Model(String meshName, String textureName) {
		mesh = MeshLoader.loadMesh(meshName);
		texture = TextureLoader.loadTexture(textureName);
		//texture2 = TextureLoader.loadTexture("hatchDefault15");
	}*/
	
	public Mesh getMesh() {
		//return mesh;
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
	
}
