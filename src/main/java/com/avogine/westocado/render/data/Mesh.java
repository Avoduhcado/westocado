package com.avogine.westocado.render.data;

import java.util.Arrays;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class Mesh {

	public static final int MAX_WEIGHTS = 3;
	public static final int MAX_COLORS = 3;

	private VAO vao;
	private Material material;
	private float boundingRadius;

	public Mesh(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
		this(positions, textureCoords, normals, createEmptyFloatArray(MAX_COLORS * positions.length / 3, 0), indices, createEmptyFloatArray(MAX_WEIGHTS * positions.length / 3, 0), createEmptyIntArray(MAX_WEIGHTS * positions.length / 3, 0));
	}

	public Mesh(float[] positions, float[] textureCoords, float[] normals, float[] color, int[] indices, float[] weights, int[] jointIndices) {
		calculateBoundingRadius(positions);

		vao = VAO.create();
		vao.bind(0, 1, 2, 3, 4);

		vao.createAttribute(0, positions, 3);
		vao.createAttribute(1, textureCoords, 2);
		vao.createAttribute(2, normals, 3);
		//vao.createAttribute(3, color, 3);
		vao.createAttribute(3, weights, 3);
		vao.createIntAttribute(4, jointIndices, 3);
		vao.createIndexBuffer(indices);

		vao.unbind(0, 1, 2, 3, 4);
	}

	private void calculateBoundingRadius(float positions[]) {
		int length = positions.length;
		boundingRadius = 0;
		for(int i = 0; i < length; i++) {
			float pos = positions[i];
			boundingRadius = Math.max(Math.abs(pos), boundingRadius);
		}
	}

	public VAO getVao() {
		return vao;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public float getBoundingRadius() {
		return boundingRadius;
	}

	public void setBoundingRadius(float boundingRadius) {
		this.boundingRadius = boundingRadius;
	}

	protected void initRender() {
		// Bind main texture
		Texture texture = material != null ? material.getTexture() : null;
		if (texture != null) {
			texture.bindToUnit(0);
		} else {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		}
		// Bind normal map
		Texture normalMap = material != null ? material.getNormalMap() : null;
		if (normalMap != null) {
			normalMap.bindToUnit(1);
		} else {
			GL13.glActiveTexture(GL13.GL_TEXTURE1);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		}

		// Bind VAO and attributes
		getVao().bind(0, 1, 2, 3, 4);
	}

	protected void endRender() {
		// Restore state
		getVao().unbind(0, 1, 2, 3, 4);
	}

	public void render() {
		initRender();

		GL11.glDrawElements(GL11.GL_TRIANGLES, getVao().getIndexCount(), GL11.GL_UNSIGNED_INT, 0);

		endRender();
	}

	/*public void renderList(List<Model> models, Consumer<Model> consumer) {
		initRender();

		for(Model model : models) {
			if(model.isInsideFrustum()) {
				// Set up data required by gameItem
				consumer.accept(model);
				// Render this game item
				GL11.glDrawElements(GL11.GL_TRIANGLES, getVao().getIndexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
		}

		endRender();
	}*/

	public void cleanUp() {
		vao.delete();
	}

	protected static float[] createEmptyFloatArray(int length, float defaultValue) {
		float[] result = new float[length];
		Arrays.fill(result, defaultValue);
		return result;
	}

	protected static int[] createEmptyIntArray(int length, int defaultValue) {
		int[] result = new int[length];
		Arrays.fill(result, defaultValue);
		return result;
	}

}
