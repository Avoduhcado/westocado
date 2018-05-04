#version 150

const int MAX_WEIGHTS = 3;
const int MAX_JOINTS = 150;

in vec3 position;
in vec2 textureCoords;
in vec3 normals;
in ivec3 jointIndices;
in vec3 weights;

uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;
uniform mat4 jointsMatrix[MAX_JOINTS];

out vec3 fragVert;
out vec2 fragTextureCoords;
out vec3 fragNormal;

void main() {

	vec4 totalLocalPos = vec4(0.0);
	vec4 totalNormal = vec4(0.0);

	int count = 0;
	for(int i = 0; i < MAX_WEIGHTS; i++) {
		float weight = weights[i];
		if(weight > 0) {
			count++;
			mat4 jointTransform = jointsMatrix[jointIndices[i]];
			vec4 posePosition = jointTransform * vec4(position, 1.0);
			totalLocalPos += posePosition * weights[i];

			vec4 worldNormal = jointTransform * vec4(normals, 0.0);
			totalNormal += worldNormal * weights[i];
		}
	}
	if(count == 0) {
		totalLocalPos = vec4(position, 1.0);
		totalNormal = vec4(normals, 0.0);
	}

	vec4 worldPosition = model * totalLocalPos;
	vec4 positionRelativeToCamera = view * worldPosition;
	gl_Position = projection * positionRelativeToCamera;

	fragVert = totalLocalPos.xyz;
	fragTextureCoords = textureCoords;
	fragNormal = totalNormal.xyz;
}
