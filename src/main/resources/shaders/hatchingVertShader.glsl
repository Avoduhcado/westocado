#version 330

in vec3 position;
in vec3 color;
in vec2 textureCoords;
in vec3 normals;

uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;

out vec3 fragVert;
out vec3 fragColor;
out vec2 fragTextureCoords;
out vec3 fragNormal;

void main(void) {
	vec4 worldPosition = model * vec4(position, 1.0);
	vec4 positionRelativeToCamera = view * worldPosition;
	gl_Position = projection * positionRelativeToCamera;
	
	fragVert = position;
	fragColor = color;
	fragTextureCoords = textureCoords;
	fragNormal = normals;
}
