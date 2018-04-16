package com.avogine.westocado.entities.bodies;

import org.joml.Vector3f;

import com.avogine.westocado.render.data.Mesh;
import com.avogine.westocado.utils.system.AvoEvent;

public class PlainBody extends Body {

	public PlainBody(long entity) {
		super(entity);
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

}
