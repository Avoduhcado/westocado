package com.avogine.westocado.entities.models;

import com.avogine.westocado.render.data.Mesh;
import com.avogine.westocado.utils.loader.StaticMeshesLoader;
import com.avogine.westocado.utils.system.AvoEvent;

public class PlainModel extends Model {

	//private List<Mesh> meshList;
	private Mesh[] meshList;
	
	public PlainModel(long entity, String modelName) {
		super(entity);
		try {
			meshList = StaticMeshesLoader.load(modelName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Mesh getMesh() {
		return meshList[0];
	}
	
	public Mesh[] getMeshes() {
		return meshList;
	}
	
	public void setMesh(Mesh mesh) {
		//this.mesh = mesh;
	}
	
	@Override
	public void fireEvent(AvoEvent e) {
		// TODO Auto-generated method stub

	}

}
