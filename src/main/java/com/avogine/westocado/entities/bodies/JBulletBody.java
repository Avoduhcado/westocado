package com.avogine.westocado.entities.bodies;

import java.io.File;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.assimp.Assimp;

import com.avogine.westocado.render.data.Mesh;
import com.avogine.westocado.render.data.VAO;
import com.avogine.westocado.utils.math.ConversionUtils;
import com.avogine.westocado.utils.system.AvoEvent;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.Transform;

public class JBulletBody extends Body {

	private RigidBody rigidBody;
	private final Transform transform = new Transform();
	
	private Mesh debugMesh;
	
	public JBulletBody(long entity, RigidBody rigidBody) {
		super(entity);
		this.rigidBody = rigidBody;
		//loadDebugMesh();
		//loadAssimpDebugMesh();
		loadMesh();
	}
	
	@Override
	public Vector3f getPosition() {
		transform.setIdentity();
		rigidBody.getMotionState().getWorldTransform(transform);
		return ConversionUtils.convertVecmathToJoml(transform.origin);
	}
	
	private void loadMesh() {
		File file = new File(ClassLoader.getSystemResource("models/testsphere.nff").getFile());

		AIScene scene = Assimp.aiImportFile(file.getAbsolutePath(), 0);
		if(scene == null) {
			System.err.println("Failed to import file");
			System.err.println(Assimp.aiGetErrorString());
		}
		
		int numMeshes = scene.mNumMeshes();
		PointerBuffer aiMeshes = scene.mMeshes();
		AIMesh[] meshes = new AIMesh[numMeshes];
		for(int i = 0; i < numMeshes; i++){
			meshes[i] = AIMesh.create(aiMeshes.get(i));
		}
		
		AIMesh mesh = meshes[0];
		
		AIVector3D.Buffer vertexBuffer = mesh.mVertices();
		FloatBuffer vertexArrayBufferData = BufferUtils.createFloatBuffer(mesh.mNumVertices() * 3 * Float.BYTES);
		
		for(int i = 0; i < mesh.mNumVertices(); i++){
			AIVector3D vert = vertexBuffer.get(i);
			vertexArrayBufferData.put(vert.x());
			vertexArrayBufferData.put(vert.y());
			vertexArrayBufferData.put(vert.z());

			/*AIVector3D norm = normals.get(i);
			normalArrayBufferData.putFloat(norm.x());
			normalArrayBufferData.putFloat(norm.y());
			normalArrayBufferData.putFloat(norm.z());

			if(mesh.mNumUVComponents().get(0) != 0) {
				AIVector3D texture = mesh.mTextureCoords(0).get(i);
				texArrayBufferData.putFloat(texture.x()).putFloat(texture.y());
			} else {
				texArrayBufferData.putFloat(0).putFloat(0);
			}*/
		}
		vertexArrayBufferData.flip();
		
		int faceCount = mesh.mNumFaces();
		int elementCount = faceCount * 3;
		IntBuffer elementArrayBufferData = BufferUtils.createIntBuffer(elementCount);
		AIFace.Buffer facesBuffer = mesh.mFaces();
		for(int i = 0; i < faceCount; i++) {
			AIFace face = facesBuffer.get(i);
			if(face.mNumIndices() != 3) {
				throw new IllegalStateException("AIFace.mNumIndices() != 3");
			}
			elementArrayBufferData.put(face.mIndices());
		}
		elementArrayBufferData.flip();
		
		VAO vao = VAO.create();
		vao.bind(0);
		
		float[] vertices = new float[vertexArrayBufferData.limit()];
		vertexArrayBufferData.get(vertices);
		vao.createAttribute(0, vertices, 3);
		int[] indices = new int[elementArrayBufferData.limit()];
		elementArrayBufferData.get(indices);
		vao.createIndexBuffer(indices);
		vao.unbind(0);
		
		debugMesh = new Mesh(vao);
	}
	
	@Override
	public Mesh getDebugMesh() {
		return debugMesh;
	}
	
	@Override
	public void applyForce(Vector3f force) {
		rigidBody.applyCentralImpulse(ConversionUtils.convertJomlToVecmath(force));
	}
	
	public RigidBody getRigidBody() {
		return rigidBody;
	}

	@Override
	public void fireEvent(AvoEvent e) {
		// TODO Auto-generated method stub
		
	}

}
