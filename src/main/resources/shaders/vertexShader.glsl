#version 330

in vec3 position;

uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;

void main() {

	vec4 worldPosition = model * vec4(position, 1.0);
	vec4 positionRelativeToCamera = view * worldPosition;
	gl_Position = projection * positionRelativeToCamera;

}
