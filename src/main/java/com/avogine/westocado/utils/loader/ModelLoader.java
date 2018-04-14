package com.avogine.westocado.utils.loader;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIColor4D;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.assimp.Assimp;

import com.avogine.westocado.render.data.Mesh;
import com.avogine.westocado.render.data.VAO;

public class ModelLoader {

	private static final String RES_LOC = "models/";

	/**
	 * 
	 * @param filename
	 * @throws FileNotFoundException
	 */
	public static List<Mesh> loadAIModel(String filename) throws FileNotFoundException {
		File assimpFile = new File(ClassLoader.getSystemResource(RES_LOC + filename).getFile());
		if(!assimpFile.exists()) {
			throw new FileNotFoundException();
		}

		AIScene modelScene = Assimp.aiImportFile(assimpFile.getAbsolutePath(), Assimp.aiProcessPreset_TargetRealtime_MaxQuality);
		if(modelScene == null) {
			throw new NullPointerException("The loaded file did not contain a transformable mesh");
		}

		int numMeshes = modelScene.mNumMeshes();
		PointerBuffer aiMeshes = modelScene.mMeshes();
		AIMesh[] meshes = new AIMesh[numMeshes];
		for(int i = 0; i < numMeshes; i++) {
			meshes[i] = AIMesh.create(aiMeshes.get(i));
		}

		// TODO Materials
		// TODO Textures
		
		List<Mesh> meshList = new ArrayList<>();
		for(AIMesh mesh : meshes) {
			meshList.add(initAIMesh(mesh));
		}
		
		return meshList;
	}

	/**
	 * 
	 * @param mesh
	 * @return
	 */
	private static Mesh initAIMesh(AIMesh mesh) {
		FloatBuffer vertexArrayBufferData = BufferUtils.createFloatBuffer(mesh.mNumVertices() * 3 * Float.BYTES);
		AIVector3D.Buffer vertices = mesh.mVertices();

		FloatBuffer colorArrayBufferData = BufferUtils.createFloatBuffer(mesh.mNumVertices() * 3 * Float.BYTES);
		
		FloatBuffer normalArrayBufferData = BufferUtils.createFloatBuffer(mesh.mNumVertices() * 3 * Float.BYTES);
		AIVector3D.Buffer normals = mesh.mNormals();

		FloatBuffer texArrayBufferData = BufferUtils.createFloatBuffer(mesh.mNumVertices() * 2 * Float.BYTES);

		for(int i = 0; i < mesh.mNumVertices(); i++) {
			AIVector3D vert = vertices.get(i);
			vertexArrayBufferData.put(vert.x());
			vertexArrayBufferData.put(vert.y());
			vertexArrayBufferData.put(vert.z());
			//vertexArrayBufferData.put(1f);

			if(mesh.mColors(0) != null) {
				AIColor4D.Buffer color = mesh.mColors(i);
				colorArrayBufferData.put(color.r());
				colorArrayBufferData.put(color.g());
				colorArrayBufferData.put(color.b());
				//colorArrayBufferData.put(color.a());
			}

			if(mesh.mNumUVComponents().get(0) != 0) {
				AIVector3D texture = mesh.mTextureCoords(0).get(i);
				texArrayBufferData.put(texture.x()).put(texture.y());
			} else {
				texArrayBufferData.put(0).put(0);
			}
			
			AIVector3D norm = normals.get(i);
			normalArrayBufferData.put(norm.x());
			normalArrayBufferData.put(norm.y());
			normalArrayBufferData.put(norm.z());
		}
		
		vertexArrayBufferData.flip();
		colorArrayBufferData.flip();
		texArrayBufferData.flip();
		normalArrayBufferData.flip();

		int faceCount = mesh.mNumFaces();
		int elementCount = faceCount * 3;
		IntBuffer elementArrayBufferData = BufferUtils.createIntBuffer(elementCount);
		AIFace.Buffer facesBuffer = mesh.mFaces();
		for(int i = 0; i < faceCount; i++){
			AIFace face = facesBuffer.get(i);
			/*if(face.mNumIndices() != 3) {
				throw new IllegalStateException("AIFace.mNumIndices() != 3");
			}*/
			elementArrayBufferData.put(face.mIndices());
		}
		elementArrayBufferData.flip();
		
		// TODO Materials
		
		VAO vao = VAO.create();
		vao.bind(0, 1, 2, 3);
		
		float[] vertexArray = new float[vertexArrayBufferData.limit()];
		vertexArrayBufferData.get(vertexArray);
		vao.createAttribute(0, vertexArray, 3);
		
		float[] colorArray = new float[colorArrayBufferData.limit()];
		colorArrayBufferData.get(colorArray);
		vao.createAttribute(1, colorArray, 3);
		
		float[] texArray = new float[texArrayBufferData.limit()];
		texArrayBufferData.get(texArray);
		vao.createAttribute(2, texArray, 2);
		
		float[] normalArray = new float[normalArrayBufferData.limit()];
		normalArrayBufferData.get(normalArray);
		vao.createAttribute(3, normalArray, 3);
		
		int[] indexArray = new int[elementArrayBufferData.limit()];
		elementArrayBufferData.get(indexArray);
		vao.createIndexBuffer(indexArray);
		vao.unbind(0, 1, 2, 3);
		
		return new Mesh(vao);
	}

}
