#version 330

in vec2 textureCoords;

uniform sampler2D depthTexture;
uniform sampler2D hatchTexture;
uniform float near;
uniform float far;

out vec4 outColor;

float LinearizeDepth(float depth) {
	float z = (2 * near) / (far + near - depth * (far - near));
	return z;
}

void main() {

	float depth = texture(depthTexture, textureCoords).r;
	depth = LinearizeDepth(depth);
	outColor = vec4(depth, depth, depth, 1.0);
	outColor = mix(outColor, texture(hatchTexture, textureCoords), 1 - depth);

}
