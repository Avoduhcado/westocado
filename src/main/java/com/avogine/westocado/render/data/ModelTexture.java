package com.avogine.westocado.render.data;

public class ModelTexture {

	private int textureId;
	private int normalMap;
	private int specularMap;
	
	private float shineDamper = 1;
	private float reflectivity = 0;
	
	private boolean hasTransparency = false;
	private boolean useFakeLighting = false;
	private boolean hasSpecularMap = false;
	
	private int numberOfRows = 1;
	
	public ModelTexture(int id) {
		this.textureId = id;
	}
	
	public int getTextureId() {
		return textureId;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}

	public boolean isHasTransparency() {
		return hasTransparency;
	}

	public void setHasTransparency(boolean hasTransparency) {
		this.hasTransparency = hasTransparency;
	}

	public boolean isUseFakeLighting() {
		return useFakeLighting;
	}

	public void setUseFakeLighting(boolean useFakeLighting) {
		this.useFakeLighting = useFakeLighting;
	}
	
	public boolean hasSpecularMap() {
		return hasSpecularMap;
	}
	
	public int getSpecularMap() {
		return specularMap;
	}
	
	public void setExtraInfoMap(int specularMap) {
		this.specularMap = specularMap;
		this.hasSpecularMap = true;
	}
	
	public int getNumberOfRows() {
		return numberOfRows;
	}
	
	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}
	
	public int getNormalMap() {
		return normalMap;
	}
	
	public void setNormalMap(int normalMap) {
		this.normalMap = normalMap;
	}
	
}
