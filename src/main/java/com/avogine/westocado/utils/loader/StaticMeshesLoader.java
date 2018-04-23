package com.avogine.westocado.utils.loader;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIColor4D;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMaterial;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIString;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.assimp.Assimp;

import com.avogine.westocado.render.data.Material;
import com.avogine.westocado.render.data.Mesh;
import com.avogine.westocado.render.data.Texture;
import com.avogine.westocado.render.utils.TextureCache;

public class StaticMeshesLoader {

	private static final String MODEL_LOCATION = "models/";

	public static Mesh[] load(String resourcePath) throws Exception {
		return load(resourcePath, Assimp.aiProcess_JoinIdenticalVertices | Assimp.aiProcess_Triangulate | Assimp.aiProcess_FixInfacingNormals);
	}

	public static Mesh[] load(String resourcePath, int flags) throws Exception {
		// TODO Don't package resources with jar, OR look into why Assimp won't load files from the jar (prolly the former TBH)
		File assimpFile = new File(ClassLoader.getSystemResource(MODEL_LOCATION + resourcePath).getFile());
		if(!assimpFile.exists()) {
			throw new FileNotFoundException("No such file at " + MODEL_LOCATION + resourcePath);
		}

		AIScene aiScene = Assimp.aiImportFile(assimpFile.getAbsolutePath(), flags);
		if (aiScene == null) {
			throw new Exception("Error loading model");
		}

		int numMaterials = aiScene.mNumMaterials();
		PointerBuffer aiMaterials = aiScene.mMaterials();
		List<Material> materials = new ArrayList<>();
		for (int i = 0; i < numMaterials; i++) {
			AIMaterial aiMaterial = AIMaterial.create(aiMaterials.get(i));
			processMaterial(aiMaterial, materials);
		}

		int numMeshes = aiScene.mNumMeshes();
		PointerBuffer aiMeshes = aiScene.mMeshes();
		Mesh[] meshes = new Mesh[numMeshes];
		for (int i = 0; i < numMeshes; i++) {
			AIMesh aiMesh = AIMesh.create(aiMeshes.get(i));
			Mesh mesh = processMesh(aiMesh, materials);
			meshes[i] = mesh;
		}

		return meshes;
	}

	private static void processMaterial(AIMaterial aiMaterial, List<Material> materials) {
		AIColor4D color = AIColor4D.create();

		AIString path = AIString.calloc();
		Assimp.aiGetMaterialTexture(aiMaterial, Assimp.aiTextureType_DIFFUSE, 0, path, (IntBuffer) null, null, null, null, null, null);
		String texturePath = path.dataString();
		Texture texture = null;
		if (texturePath != null && texturePath.length() > 0) {
			TextureCache textCache = TextureCache.getInstance();
			texture = textCache.getTexture(texturePath);
		}

		Vector4f ambient = Material.DEFAULT_COLOR;
		int result = Assimp.aiGetMaterialColor(aiMaterial, Assimp.AI_MATKEY_COLOR_AMBIENT, Assimp.aiTextureType_NONE, 0, color);
		if (result == 0) {
			ambient = new Vector4f(color.r(), color.g(), color.b(), color.a());
		}

		Vector4f diffuse = Material.DEFAULT_COLOR;
		result = Assimp.aiGetMaterialColor(aiMaterial, Assimp.AI_MATKEY_COLOR_DIFFUSE, Assimp.aiTextureType_NONE, 0, color);
		if (result == 0) {
			diffuse = new Vector4f(color.r(), color.g(), color.b(), color.a());
		}

		Vector4f specular = Material.DEFAULT_COLOR;
		result = Assimp.aiGetMaterialColor(aiMaterial, Assimp.AI_MATKEY_COLOR_SPECULAR, Assimp.aiTextureType_NONE, 0, color);
		if (result == 0) {
			specular = new Vector4f(color.r(), color.g(), color.b(), color.a());
		}

		Material material = new Material(ambient, diffuse, specular, 1.0f);
		material.setTexture(texture);
		materials.add(material);
	}

	private static Mesh processMesh(AIMesh aiMesh, List<Material> materials) {
		FloatBuffer vertices = BufferUtils.createFloatBuffer(aiMesh.mNumVertices() * 3 * Float.BYTES);
		FloatBuffer colors = BufferUtils.createFloatBuffer(aiMesh.mNumVertices() * 3 * Float.BYTES);
		FloatBuffer textures = BufferUtils.createFloatBuffer(aiMesh.mNumVertices() * 2 * Float.BYTES);
		FloatBuffer normals = BufferUtils.createFloatBuffer(aiMesh.mNumVertices() * 3 * Float.BYTES);
		IntBuffer indices = BufferUtils.createIntBuffer(aiMesh.mNumFaces() * 3);

		processVertices(aiMesh, vertices);
		processColors(aiMesh, colors);
		processTextureCoords(aiMesh, textures);
		processNormals(aiMesh, normals);
		processIndices(aiMesh, indices);
		
		float[] vertexArray = new float[vertices.limit()];
		vertices.get(vertexArray);
		float[] colorArray = new float[colors.limit()];
		colors.get(colorArray);
		float[] textureCoordArray = new float[textures.limit()];
		textures.get(textureCoordArray);
		float[] normalArray = new float[normals.limit()];
		normals.get(normalArray);
		int[] indexArray = new int[indices.limit()];
		indices.get(indexArray);

		// XXX: Colors are currently not implemented
		Mesh mesh = new Mesh(vertexArray,
				textureCoordArray,
				normalArray,
				indexArray);
		Material material;
		int materialIdx = aiMesh.mMaterialIndex();
		if (materialIdx >= 0 && materialIdx < materials.size()) {
			material = materials.get(materialIdx);
		} else {
			material = new Material();
		}
		mesh.setMaterial(material);

		return mesh;
	}

	private static void processVertices(AIMesh aiMesh, FloatBuffer vertices) {
		AIVector3D.Buffer aiVertices = aiMesh.mVertices();
		while (aiVertices.remaining() > 0) {
			AIVector3D aiVertex = aiVertices.get();
			vertices.put(aiVertex.x());
			vertices.put(aiVertex.y());
			vertices.put(aiVertex.z());
		}
		vertices.flip();
	}
	
	private static void processColors(AIMesh aiMesh, FloatBuffer colors) {
		AIColor4D.Buffer colorBuffer = aiMesh.mColors(0);
		int numColors = colorBuffer != null ? colorBuffer.remaining() : 0;
		for(int i = 0; i < numColors; i++) {
			AIColor4D color = colorBuffer.get(i);
			colors.put(color.r());
			colors.put(color.g());
			colors.put(color.b());
			//colors.put(color.a());
		}
		colors.flip();
	}

	private static void processTextureCoords(AIMesh aiMesh, FloatBuffer textures) {
		AIVector3D.Buffer textCoords = aiMesh.mTextureCoords(0);
		int numTextCoords = textCoords != null ? textCoords.remaining() : 0;
		for (int i = 0; i < numTextCoords; i++) {
			AIVector3D textCoord = textCoords.get();
			textures.put(textCoord.x());
			// XXX Looks like we're inverting some coordinates, keep an eye on this
			textures.put(1 - textCoord.y());
		}
		textures.flip();
	}

	private static void processNormals(AIMesh aiMesh, FloatBuffer normals) {
		AIVector3D.Buffer aiNormals = aiMesh.mNormals();
		while (aiNormals != null && aiNormals.remaining() > 0) {
			AIVector3D aiNormal = aiNormals.get();
			normals.put(aiNormal.x());
			normals.put(aiNormal.y());
			normals.put(aiNormal.z());
		}
		normals.flip();
	}

	private static void processIndices(AIMesh aiMesh, IntBuffer indices) {
		int numFaces = aiMesh.mNumFaces();
		AIFace.Buffer aiFaces = aiMesh.mFaces();
		for (int i = 0; i < numFaces; i++) {
			AIFace aiFace = aiFaces.get(i);
			IntBuffer buffer = aiFace.mIndices();
			while (buffer.remaining() > 0) {
				indices.put(buffer.get());
			}
		}
		indices.flip();
	}

}
