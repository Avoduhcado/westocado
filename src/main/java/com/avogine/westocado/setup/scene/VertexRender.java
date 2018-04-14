package com.avogine.westocado.setup.scene;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import com.avogine.westocado.entities.Camera;
import com.avogine.westocado.entities.Entity;
import com.avogine.westocado.render.shaders.VertexShader;

public class VertexRender {

	public static final float FOV = 70;
	public static final float NEAR_PLANE = 0.1f;
	public static final float FAR_PLANE = 1000f;

	private VertexShader shader;
	private Matrix4f projectionMatrix;

	public VertexRender() {
		shader = new VertexShader("vertexShader.glsl", "fragmentShader.glsl", "position");
		createProjectionMatrix();
		shader.start();
		shader.projection.loadMatrix(projectionMatrix);
		shader.stop();
	}

	public void render(Entity entity, Camera camera) {
		prepareInstance(entity, camera);
		if(entity.getBody() != null && entity.getBody().getDebugMesh() != null) {
			entity.getBody().getDebugMesh().getVao().bind(0);
			//GL11.glDrawElements(GL11.GL_LINE_LOOP, entity.getBody().getDebugMesh().getVao().getIndexCount(), GL11.GL_UNSIGNED_INT, 0);
			GL11.glDrawElements(GL11.GL_TRIANGLES, entity.getBody().getDebugMesh().getVao().getIndexCount(), GL11.GL_UNSIGNED_INT, 0);
			entity.getBody().getDebugMesh().getVao().unbind(0);
		}
		
		finish();
	}

	private void prepareInstance(Entity entity, Camera camera) {
		shader.start();
		Matrix4f transform = new Matrix4f();
		transform.translate(entity.getBody().getPosition());
		transform.scale(entity.getBody().getSize());
		shader.model.loadMatrix(transform);
		shader.view.loadMatrix(camera.getViewMatrix());
		shader.color.loadVec3(1, 0, 0);
	}

	public void finish() {
		shader.stop();
	}
	
	public void cleanUp() {
		shader.cleanUp();
	}

	private void createProjectionMatrix() {
		projectionMatrix = new Matrix4f();
		float aspectRatio = (float) 1280 / (float) 720;
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))));
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;
		
		projectionMatrix.m00(x_scale);
		projectionMatrix.m11(y_scale);
		projectionMatrix.m22(-((FAR_PLANE + NEAR_PLANE) / frustum_length));
		projectionMatrix.m23(-1);
		projectionMatrix.m32(-((2 * NEAR_PLANE * FAR_PLANE) / frustum_length));
		projectionMatrix.m33(0);
	}
	
}
