package com.avogine.westocado.setup.scene;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import com.avogine.westocado.entities.Camera;
import com.avogine.westocado.entities.Entity;
import com.avogine.westocado.entities.Light;
import com.avogine.westocado.render.data.Mesh;
import com.avogine.westocado.render.shaders.ObjectShader;

public class ObjectRender {

	// TODO: Put these somewhere better, geez
	public static final float FOV = 70;
	public static final float NEAR_PLANE = 0.1f;
	public static final float FAR_PLANE = 10000f;

	private ObjectShader shader;
	private Matrix4f projectionMatrix;

	public ObjectRender() {
		shader = new ObjectShader("diffuseVertShader.glsl", "diffuseFragShader.glsl", "position", "color", "textureCoords", "normals");
		createProjectionMatrix();
		shader.start();
		shader.projection.loadMatrix(projectionMatrix);
		shader.stop();
	}

	public void render(Entity entity, Camera camera, Light light) {
		prepareInstance(entity, camera, light);
		
		for(Mesh mesh : entity.getModel().getMeshes()) {
			entity.getModel().getTexture().bindToUnit(0);
			mesh.getVao().bind(0, 1, 2, 3);

			GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getVao().getIndexCount(), GL11.GL_UNSIGNED_INT, 0);
			mesh.getVao().unbind(0, 1, 2, 3);
		}
		
		/*// TODO Batch it baby, grab all the same meshes and render them at a time
		entity.getModel().getTexture().bindToUnit(0);
		//entity.getModel().getTexture2().bindToUnit(1);
		entity.getModel().getMesh().getVao().bind(0, 1, 2, 3);

		GL11.glDrawElements(GL11.GL_TRIANGLES, entity.getModel().getMesh().getVao().getIndexCount(), GL11.GL_UNSIGNED_INT, 0);
		entity.getModel().getMesh().getVao().unbind(0, 1, 2, 3);
		if(entity.getBody() != null) {
						entity.getBody().getDebugMesh().getVao().bind(0);
						GL11.glDrawElements(GL11.GL_LINE_LOOP, entity.getBody().getDebugMesh().getVao().getIndexCount(), GL11.GL_UNSIGNED_INT, 0);
						entity.getBody().getDebugMesh().getVao().unbind(0);
					}
		*/
		finish();
	}

	private void prepareInstance(Entity entity, Camera camera, Light light) {
		shader.start();
		Matrix4f transform = new Matrix4f();
		// TODO Organize all this stuff to get proper model view transformations
		transform.translate(entity.getPosition());
		/*if(entity.getRotateDegree() != -1) {
			entity.setRotateDegree(entity.getRotateDegree() + Theater.getDeltaChange(1f));
			transform.rotate(entity.getRotateDegree(), entity.getRotation());
		}*/
		transform.scale(entity.getScale());
		//transform.translate(new Vector3f(0, -5f, 0));
		shader.lightPosition.loadVec3(light.getPosition());
		shader.lightColor.loadVec3(light.getColor());
		shader.model.loadMatrix(transform);
		shader.view.loadMatrix(camera.getViewMatrix());
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
