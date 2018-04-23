package com.avogine.westocado.render.shaders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.avogine.westocado.render.shaders.utils.ShaderProgram;
import com.avogine.westocado.render.shaders.utils.Uniform;
import com.avogine.westocado.render.shaders.utils.UniformFloat;
import com.avogine.westocado.render.shaders.utils.UniformMatrix;
import com.avogine.westocado.render.shaders.utils.UniformSampler;
import com.avogine.westocado.render.shaders.utils.UniformVec3;
import com.avogine.westocado.render.shaders.utils.UniformVec4;

public class ObjectShader extends ShaderProgram {

	public static final int MAX_LIGHTS = 10;
	private static final int DIFFUSE_TEX_UNIT = 0;
	
	public UniformMatrix model = new UniformMatrix("model");
	public UniformMatrix projection = new UniformMatrix("projection");
	public UniformMatrix view = new UniformMatrix("view");
	public UniformSampler texture = new UniformSampler("tex");
	public UniformVec3 cameraPosition = new UniformVec3("cameraPosition");
	public UniformFloat materialShininess = new UniformFloat("materialShininess");
	public UniformVec3 materialSpecularColor = new UniformVec3("materialSpecularColor");
	public UniformFloat numLights = new UniformFloat("numLights");
	
	public UniformVec4[] lightPositions = new UniformVec4[MAX_LIGHTS];
	public UniformVec3[] lightIntensities = new UniformVec3[MAX_LIGHTS];
	public UniformFloat[] lightAttenuations = new UniformFloat[MAX_LIGHTS];
	public UniformFloat[] lightAmbientCoefficients = new UniformFloat[MAX_LIGHTS];
	public UniformFloat[] lightConeAngles = new UniformFloat[MAX_LIGHTS];
	public UniformVec3[] lightConeDirections = new UniformVec3[MAX_LIGHTS];
	
	public ObjectShader(String vertexFile, String fragmentFile, String...inVariables) {
		super(vertexFile, fragmentFile, inVariables);
		for(int i = 0; i < MAX_LIGHTS; i++) {
			lightPositions[i] = new UniformVec4("lights[" + i + "].position");
			lightIntensities[i] = new UniformVec3("lights[" + i + "].intensities");
			lightAttenuations[i] = new UniformFloat("lights[" + i + "].attenuation");
			lightAmbientCoefficients[i] = new UniformFloat("lights[" + i + "].ambientCoefficient");
			lightConeAngles[i] = new UniformFloat("lights[" + i + "].coneAngle");
			lightConeDirections[i] = new UniformVec3("lights[" + i + "].coneDirection");
		}
		// WEW
		List<Uniform> uniformList = new ArrayList<>();
		uniformList.addAll(Arrays.asList(projection, view, model, texture, cameraPosition, materialShininess, materialSpecularColor, numLights));
		uniformList.addAll(Arrays.asList(lightPositions));
		uniformList.addAll(Arrays.asList(lightIntensities));
		uniformList.addAll(Arrays.asList(lightAttenuations));
		uniformList.addAll(Arrays.asList(lightAmbientCoefficients));
		uniformList.addAll(Arrays.asList(lightConeAngles));
		uniformList.addAll(Arrays.asList(lightConeDirections));
		storeAllUniformLocations(uniformList.toArray(new Uniform[0]));
		connectTextureUnits();
	}

	private void connectTextureUnits() {
		super.start();
		texture.loadTexUnit(DIFFUSE_TEX_UNIT);
		super.stop();
	}
	
}
