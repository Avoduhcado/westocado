package com.avogine.westocado.render.utils;

import java.util.List;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.avogine.westocado.entities.bodies.Body;
import com.avogine.westocado.render.models.RawModel;

public class FrustumCullingFilter {

	private static final int NUM_PLANES = 6;

	private final Matrix4f prjViewMatrix;

	private final Vector4f[] frustumPlanes;

	public FrustumCullingFilter() {
		prjViewMatrix = new Matrix4f();
		frustumPlanes = new Vector4f[NUM_PLANES];
		for(int i = 0; i < NUM_PLANES; i++) {
			frustumPlanes[i] = new Vector4f();
		}
	}
	
	public void filter(Map<? extends RawModel, List<Body>> mapMesh) {
		for (Map.Entry<? extends RawModel, List<Body>> entry : mapMesh.entrySet()) {
			List<Body> gameItems = entry.getValue();
			filter(gameItems, entry.getKey().getBoundingRadius());
		}
	}

	public void updateFrustum(Matrix4f projMatrix, Matrix4f viewMatrix) {
		// Calculate projection view matrix
		prjViewMatrix.set(projMatrix);
		prjViewMatrix.mul(viewMatrix);
		// Get frustum planes
		for(int i = 0; i < NUM_PLANES; i++) {
			prjViewMatrix.frustumPlane(i, frustumPlanes[i]);
		}
	}

	public boolean insideFrustum(float x0, float y0, float z0, float boundingRadius) {
		boolean result = true;
		for(int i = 0; i < NUM_PLANES; i++) {
			Vector4f plane = frustumPlanes[i];
			if(plane.x * x0 + plane.y * y0 + plane.z * z0 + plane.w <= -boundingRadius) {
				result = false;
				break;
			}
		}
		return result;
	}
	
	public void filter(List<Body> gameItems, float meshBoundingRadius) {
	    float boundingRadius;
	    Vector3f pos;
	    for (Body gameItem : gameItems) {
	        boundingRadius = gameItem.getScale().x() * meshBoundingRadius;
	        pos = gameItem.getPosition();
	        gameItem.setInsideFrustum(insideFrustum(pos.x, pos.y, pos.z, boundingRadius));
	    }
	}
	
}
