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
import com.avogine.westocado.render.data.VAO;
import com.avogine.westocado.render.utils.TextureCache;

public class ModelLoader {

	private static final String MODEL_LOCATION = "models/";
	private static final String TEXTURE_LOCATION = "graphics/";

	public static List<Mesh> loadAIModel(String filename) throws FileNotFoundException {
		// TODO Don't package resources with jar, OR look into why Assimp won't load files from the jar (prolly the former TBH)
		File assimpFile = new File(ClassLoader.getSystemResource(MODEL_LOCATION + filename).getFile());
		if(!assimpFile.exists()) {
			throw new FileNotFoundException("No such file at " + MODEL_LOCATION + filename);
		}

		AIScene aiScene = Assimp.aiImportFile(assimpFile.getAbsolutePath(), Assimp.aiProcess_JoinIdenticalVertices | Assimp.aiProcess_Triangulate | Assimp.aiProcess_FixInfacingNormals);
		if(aiScene == null) {
			throw new NullPointerException("The loaded file did not contain a transformable mesh");
		}

		int numMeshes = aiScene.mNumMeshes();
		PointerBuffer aiMeshes = aiScene.mMeshes();
		AIMesh[] meshes = new AIMesh[numMeshes];
		for(int i = 0; i < numMeshes; i++) {
			meshes[i] = AIMesh.create(aiMeshes.get(i));
		}

		int numMaterials = aiScene.mNumMaterials();
		PointerBuffer aiMaterials = aiScene.mMaterials();
		List<Material> materials = new ArrayList<>();
		for (int i = 0; i < numMaterials; i++) {
		    AIMaterial aiMaterial = AIMaterial.create(aiMaterials.get(i));
		    processMaterial(aiMaterial, materials, TEXTURE_LOCATION);
		}
		// TODO Textures
		
		List<Mesh> meshList = new ArrayList<>();
		for(AIMesh mesh : meshes) {
			meshList.add(initAIMesh(mesh));
		}
		
		return meshList;
	}

	private static void processMaterial(AIMaterial aiMaterial, List<Material> materials, String texturesDir) {
	    AIColor4D color = AIColor4D.create();

	    AIString path = AIString.calloc();
	    Assimp.aiGetMaterialTexture(aiMaterial, Assimp.aiTextureType_DIFFUSE, 0, path, (IntBuffer) null, null, null, null, null, null);
	    String texturePath = path.dataString();
	    Texture texture = null;
	    if (texturePath != null && texturePath.length() > 0) {
	        TextureCache textCache = TextureCache.getInstance();
	        texture = textCache.getTexture(texturesDir + texturePath);
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
