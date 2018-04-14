package com.avogine.westocado.render.shaders;

import com.avogine.westocado.render.shaders.utils.ShaderProgram;
import com.avogine.westocado.render.shaders.utils.UniformMatrix;

public class StaticShader extends ShaderProgram {
		
	private static final String VERTEX_FILE = SHADER_PATH + "vertexShader.glsl";
	private static final String FRAGMENT_FILE = SHADER_PATH + "fragmentShader.glsl";
	
	private UniformMatrix transformationMatrix = new UniformMatrix("transformationMatrix");
	private UniformMatrix projectionViewMatrix = new UniformMatrix("projectionViewMatrix");
	
	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE, "position", "textureCoords", "normal");
		super.storeAllUniformLocations(transformationMatrix, projectionViewMatrix);
		connectTextureUnits();
	}

	/**
	 * Indicates which texture unit the diffuse texture should be sampled from.
	 */
	private void connectTextureUnits() {
		super.start();
		//diffuseMap.loadTexUnit(DIFFUSE_TEX_UNIT);
		super.stop();
	}

	/*@Override
	protected void bindAttributes() {
		super.bindFragOutput(0, "outColor");
		super.bindFragOutput(1, "outBrightColor");
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}*/

	/*@Override
	protected void getAllUniformLocations() {
		locationTransformationMatrix = super.getUniformLocation("transformationMatrix");
		locationProjectionMatrix = super.getUniformLocation("projectionMatrix");
		locationViewMatrix = super.getUniformLocation("viewMatrix");
		locationShineDamper = super.getUniformLocation("shineDamper");
		locationReflectivity = super.getUniformLocation("reflectivity");
		locationUseFakeLighting = super.getUniformLocation("useFakeLighting");
		locationSkyColor = super.getUniformLocation("skyColor");
		locationNumberOfRows = super.getUniformLocation("numberOfRows");
		locationOffset = super.getUniformLocation("offset");
		locationPlane = super.getUniformLocation("plane");
		locationToShadowMapSpace = super.getUniformLocation("toShadowMapSpace");
		locationShadowMap = super.getUniformLocation("shadowMap");
		locationSpecularMap = super.getUniformLocation("specularMap");
		locationUsesSpecularMap = super.getUniformLocation("usesSpecularMap");
		locationTextureSampler = super.getUniformLocation("textureSampler");
		
		locationLightPosition = new int[MAX_LIGHTS];
		locationLightColor = new int[MAX_LIGHTS];
		locationAttenuation = new int[MAX_LIGHTS];
		for(int i = 0; i < MAX_LIGHTS; i++) {
			locationLightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
			locationLightColor[i] = super.getUniformLocation("lightColor[" + i + "]");
			locationAttenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
		}
	}*/
	
	/*public void connectTextureUnits() {
		super.loadInt(locationShadowMap, 5);
		super.loadInt(locationTextureSampler, 0);
		super.loadInt(locationSpecularMap, 1);
	}
	
	public void loadUseSpecularMap(boolean useMap) {
		super.loadBoolean(locationUsesSpecularMap, useMap);
	}
	
	public void loadToShadowMapSpace(Matrix4f matrix) {
		super.loadMatrix(locationToShadowMapSpace, matrix);
	}
	
	public void loadClipPlane(Vector4f plane) {
		super.loadVector(locationPlane, plane);
	}
	
	public void loadNumberOfRows(int numberOfRows) {
		super.loadFloat(locationNumberOfRows, numberOfRows);
	}
	
	public void loadOffset(float x, float y) {
		super.load2DVector(locationOffset, new Vector2f(x, y));
	}
	
	public void loadSkyColor(float r, float g, float b) {
		super.loadVector(locationSkyColor, new Vector3f(r, g, b));
	}
	
	public void loadFakeLighting(boolean useFake) {
		super.loadBoolean(locationUseFakeLighting, useFake);
	}
	
	public void loadShineVariables(float damper, float reflectivity) {
		super.loadFloat(locationShineDamper, damper);
		super.loadFloat(locationReflectivity, reflectivity);
	}
	
	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(locationTransformationMatrix, matrix);
	}*/
	
	// TODO Using the wrong Light
	/*public void loadLights(List<Light> lights) {
		for(int i = 0; i < MAX_LIGHTS; i++) {
			if(i < lights.size()) {
				super.loadVector(locationLightPosition[i], lights.get(i).getPosition());
				super.loadVector(locationLightColor[i], lights.get(i).getColor());
				super.loadVector(locationAttenuation[i], lights.get(i).getAttenuation());
			} else {
				super.loadVector(locationLightPosition[i], new Vector3f());
				super.loadVector(locationLightColor[i], new Vector3f());
				super.loadVector(locationAttenuation[i], new Vector3f(1, 0, 0));
			}
		}
	}*/

	/*public void loadViewMatrix(Camera camera) {
		Matrix4f viewMatrix = MathUtils.createViewMatrix(camera);
		super.loadMatrix(locationViewMatrix, viewMatrix);
	}
	
	public void loadProjectionMatrix(Matrix4f matrix) {
		super.loadMatrix(locationProjectionMatrix, matrix);
	}*/

}
