package com.avogine.westocado.entities.bodies;

import org.joml.Vector3f;

import com.avogine.westocado.render.data.Mesh;
import com.avogine.westocado.utils.system.AvoEvent;

public class FollowBody extends Body {

	private long followEntity;
	
	public FollowBody(long entity, long followEntity) {
		super(entity);
		this.setFollowEntity(followEntity);
	}

	@Override
	public Mesh getDebugMesh() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void applyForce(Vector3f force) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fireEvent(AvoEvent e) {
		// TODO Auto-generated method stub
		
	}

	public long getFollowEntity() {
		return followEntity;
	}

	public void setFollowEntity(long followEntity) {
		this.followEntity = followEntity;
	}

}
