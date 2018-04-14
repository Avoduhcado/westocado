package com.avogine.westocado.utils.math;

public class GeomUtils {

	public static final int[] CUBE_INDEX_BUFFER = {
			// The front
			0, 1, 2,
			2, 3, 0,
			
			// The left
			7, 0, 3,
			3, 4, 7,
			
			// The right
			1, 6, 5,
			5, 2, 1,
			
			// the top
			7, 6, 1,
			1, 0, 7,

			// the bottom
			3, 2, 6,
			6, 5, 3,
			
			// the back
			4, 5, 6,
			6, 7, 4
	};
	
	/*public static final int[] CUBE_INDEX_BUFFER = {
			// The back
			0, 1, 3,
			3, 1, 2,
			
			// The front
			5, 7, 6,
			6, 4, 5,
			
			// The left
			0, 3, 6,
			3, 4, 6,
			
			// the right
			7, 5, 1,
			5, 2, 1,

			// the top
			7, 1, 0,
			0, 6, 7,
			
			// the bottom
			3, 2, 5,
			5, 4, 3
	};*/
	
	public static float[] generateCube(int halfSize) {
		return new float[] {
			-halfSize, halfSize, halfSize,		// 0
			halfSize, halfSize, halfSize,		// 1
			halfSize, -halfSize, halfSize,		// 2
			-halfSize, -halfSize, halfSize,		// 3

			-halfSize, halfSize, -halfSize,		// 0
			halfSize, halfSize, -halfSize,		// 1
			halfSize, -halfSize, -halfSize,		// 2
			-halfSize, -halfSize, -halfSize		// 3
			/*-halfSize, -halfSize, -halfSize,	// 4
			halfSize, -halfSize, -halfSize,		// 5
			halfSize, halfSize, -halfSize,		// 6
			-halfSize, halfSize, -halfSize		// 7
*/		};
		
		
		/*return new float[] {
				-size,  size, -size, // Top-left			0
				size,  size, -size, // Top-right			1
				size, -size, -size, // Bottom-right			2
				-size, -size, -size,  // Bottom-left		3
				
				-size, -size, size,  // Bottom-back-left	4
				size, -size, size, // Bottom-back-right		5
				
				-size,  size, size, // Top-back-left		6
				size,  size,  size, // Top-back-right		7
		};*/
	}
	
}
