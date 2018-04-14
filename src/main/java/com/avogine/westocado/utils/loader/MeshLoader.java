package com.avogine.westocado.utils.loader;

import java.util.HashMap;
import java.util.Map;

import com.avogine.westocado.render.data.Mesh;
import com.avogine.westocado.render.data.ModelData;
import com.avogine.westocado.render.data.VAO;

public class MeshLoader {

	private static Map<String, Mesh> meshMap = new HashMap<>();

	public static Mesh loadMesh(String objFileName) {
		Mesh mesh = meshMap.get(objFileName);
		if(mesh != null) {
			return mesh;
		}

		ModelData data = OBJFileLoader.loadOBJ(objFileName);

		VAO vao = VAO.create();
		vao.bind(0, 1, 2, 3);

		vao.createAttribute(0, data.getVertices(), 3);
		float[] color = {
				1.0f, 0.0f, 0.0f, // Top-left
				0.0f, 1.0f, 0.0f, // Top-right
				0.0f, 0.0f, 1.0f, // Bottom-right
				1.0f, 1.0f, 1.0f  // Bottom-left
		};
		vao.createAttribute(1, color, 3);
		vao.createAttribute(2, data.getTextureCoords(), 2);
		vao.createAttribute(3, data.getNormals(), 3);
		vao.createIndexBuffer(data.getIndices());
		vao.unbind(0, 1, 2, 3);

		mesh = new Mesh(vao);
		meshMap.put(objFileName, mesh);

		return mesh;
	}

}
