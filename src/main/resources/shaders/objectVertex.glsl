#version 330

in vec3 position;
in vec3 color;
in vec2 textureCoords;
in vec3 normals;

uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;

out vec3 passColor;
out vec2 passTextureCoords;
out float visibility;

const float density = 0.075;
const float gradient = 0.5;

void main(void) {
	vec4 worldPosition = model * vec4(position, 1.0);
	vec4 positionRelativeToCamera = view * worldPosition;
	gl_Position = projection * positionRelativeToCamera;
	
	passColor = color;
	passTextureCoords = textureCoords;
	
	float distance = length(positionRelativeToCamera.xyz);
	visibility = exp(-pow((distance * density), gradient));
	visibility = clamp(visibility, 0.0, 1.0);
}
