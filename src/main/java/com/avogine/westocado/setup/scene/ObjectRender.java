package com.avogine.westocado.setup.scene;

import java.util.List;
import java.util.stream.Collectors;

import org.joml.Matrix4f;

import com.avogine.westocado.entities.Entities;
import com.avogine.westocado.entities.bodies.Body;
import com.avogine.westocado.entities.bodies.CameraBody;
import com.avogine.westocado.entities.components.LightEmitter;
import com.avogine.westocado.entities.models.Model;
import com.avogine.westocado.entities.models.PlainModel;
import com.avogine.westocado.render.animation.AnimatedFrame;
import com.avogine.westocado.render.data.Mesh;
import com.avogine.westocado.render.shaders.ObjectShader;

public class ObjectRender {

	// TODO: Put these somewhere better, geez
	public static final float FOV = 70;
	public static final float NEAR_PLANE = 0.1f;
	public static final float FAR_PLANE = Float.MAX_VALUE;
	
	private CameraBody camera;
	
	private ObjectShader shader;
	private Matrix4f projectionMatrix;

	public ObjectRender(CameraBody camera) {
		this.camera = camera;
		
		//shader = new ObjectShader("diffuseVertShader.glsl", "diffuseFragShader.glsl", "position", "textureCoords", "normals", "weights", "jointIndices");
		shader = new ObjectShader("testerVert.glsl", "diffuseFragShader.glsl", "position", "textureCoords", "normals", "weights", "jointIndices");
		createProjectionMatrix();
		shader.start();
		shader.projection.loadMatrix(projectionMatrix);
		shader.stop();
	}
	
	public void renderScene() {
		for(Model model : Entities.modelComponentMap.values()) {
			render(model);
		}
	}
	
	public void render(Model model) {
		prepareInstance(model);
		
		// TODO Batch it baby, grab all the same meshes and render them at a time
		for(Mesh mesh : model.getMeshes()) {
			mesh.render();
		}
		/*for(Mesh mesh : model.getMeshes()) {
			model.getTexture().bindToUnit(0);
			mesh.getVao().bind(0, 1, 2, 3);

			GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getVao().getIndexCount(), GL11.GL_UNSIGNED_INT, 0);
			mesh.getVao().unbind(0, 1, 2, 3);
		}*/

		finish();
	}

	private void prepareInstance(Model model) {
		shader.start();
		Matrix4f transform = new Matrix4f();
		Body modelBody = Entities.bodyComponentMap.get(model.getEntity());
		if(modelBody != null) {
			// TODO Organize all this stuff to get proper model view transformations
			transform.translate(modelBody.getPosition());
			transform.scale(modelBody.getScale());
		}
		
		if(model instanceof PlainModel) {
			PlainModel pModel = (PlainModel) model;
			if(pModel.getAnimation("") != null) {
				// Lol should tweened animations be a toggleable option? CAN'T BE CHEAP TO COMPUTE
				AnimatedFrame frame = pModel.getAnimation("").getCurrentFrameTweened();
				shader.jointsMatrix.loadMatrixArray(frame.getJointMatrices());
				pModel.getAnimation("").animate();
			}
		}
		
		shader.materialShininess.loadFloat(10f);
		shader.materialSpecularColor.loadVec3(1, 1, 1);
		
		// Grab ALL the lights we got (but only up to our max)
		List<LightEmitter> lights = Entities.lightComponentMap.values().stream()
				.limit(ObjectShader.MAX_LIGHTS)
				.collect(Collectors.toList());
		// Pass the baby UP
		shader.numLights.loadFloat(lights.size());
		for(int i = 0; i < ObjectShader.MAX_LIGHTS; i++) {
			// As long as we have lights, set those values to GO
			if(i < lights.size()) {
				LightEmitter light = Entities.lightComponentMap.get(lights.get(i).getEntity());
				
				Body body = Entities.bodyComponentMap.get(light.getEntity());
				// I HOPE we have a body for each light but u nvr no dood
				if(body != null) {
					shader.lightPositions[i].loadVec4(body.getPosition().x, body.getPosition().y, body.getPosition().z, 0);
				} else {
					shader.lightPositions[i].loadVec4(0, 0, 0, 0);
				}
				
				shader.lightIntensities[i].loadVec3(light.getColor());
				shader.lightAttenuations[i].loadFloat(light.getAttenuation());
				shader.lightAmbientCoefficients[i].loadFloat(light.getAmbientCoefficient());
				shader.lightConeAngles[i].loadFloat(light.getConeAngle());
				shader.lightConeDirections[i].loadVec3(light.getConeDirection());
			} else {
				// Load up some bots if we ran out
				shader.lightPositions[i].loadVec4(0, 0, 0, 0);
				shader.lightIntensities[i].loadVec3(0, 0, 0);
				shader.lightAttenuations[i].loadFloat(0);
				shader.lightAmbientCoefficients[i].loadFloat(0);
				shader.lightConeAngles[i].loadFloat(0);
				shader.lightConeDirections[i].loadVec3(0, 0, 0);
			}
		}
		
		shader.model.loadMatrix(transform);
		if(camera != null) {
			shader.view.loadMatrix(camera.getViewMatrix());
			shader.cameraPosition.loadVec3(camera.getPosition());
		} else {
			shader.view.loadMatrix(new Matrix4f());
			shader.cameraPosition.loadVec3(0, 0, 0);
		}
	}

	public void finish() {
		shader.stop();
	}
	
	public void cleanUp() {
		shader.cleanUp();
	}

	public void setCameraEntity(CameraBody camera) {
		if(camera == null || !(camera instanceof CameraBody)) {
			System.err.println("Invalid camera entity, make sure to implement the CameraBody before setting it to renderer.");
			return;
		}
		this.camera = camera;
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
