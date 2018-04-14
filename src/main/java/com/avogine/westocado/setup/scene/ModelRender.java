package com.avogine.westocado.setup.scene;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.avogine.westocado.entities.Camera;
import com.avogine.westocado.entities.Entity;
import com.avogine.westocado.render.shaders.StaticShader;

public class ModelRender extends Render {

	private StaticShader shader;
	
	public ModelRender(StaticShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
	}
	
	public void render(Entity entity, Camera camera, Vector3f lightDirection) {
		/*prepare(camera, lightDirection);
		entity.getTexture().bindToUnit(0);
		entity.getModel().bind(0, 1, 2, 3, 4);
		//shader.jointTransforms.loadMatrixArray(entity.getJointTransforms());
		GL11.glDrawElements(GL11.GL_TRIANGLES, entity.getModel().getIndexCount(), GL11.GL_UNSIGNED_INT, 0);
		entity.getModel().unbind(0, 1, 2, 3, 4);
		finish();*/
	}
	
	private void prepare(Camera camera, Vector3f lightDirection) {
		
	}
	
	private void finish() {
		
	}
	
	@Override
	public void cleanUp() {
		// TODO Auto-generated method stub
		
	}

}
