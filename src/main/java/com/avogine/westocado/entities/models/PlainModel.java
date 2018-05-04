package com.avogine.westocado.entities.models;

import java.util.Map;

import com.avogine.westocado.render.data.Animation;
import com.avogine.westocado.render.data.Mesh;
import com.avogine.westocado.utils.loader.MeshLoader;
import com.avogine.westocado.utils.system.AvoEvent;
import com.avogine.westocado.utils.system.Pair;

public class PlainModel extends Model {

	//private List<Mesh> meshList;
	private Mesh[] meshList;
	private Map<String, Animation> animationMap;
	
	public PlainModel(long entity, String modelName) {
		super(entity);
		try {
			Pair<Mesh[], Map<String, Animation>> meshAnimationPair = MeshLoader.load(modelName);
			meshList = meshAnimationPair.getFirst();
			animationMap = meshAnimationPair.getSecond();
			
			for(Animation a : animationMap.values()) {
				System.out.println(a.getFrames().size());
			}
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
	
	public Map<String, Animation> getAnimations() {
		return animationMap;
	}
	
	public Animation getAnimation(String animationName) {
		return animationMap.get(animationName);
	}
	
	public void setMesh(Mesh mesh) {
		//this.mesh = mesh;
	}
	
	@Override
	public void fireEvent(AvoEvent e) {
		// TODO Auto-generated method stub

	}

}
