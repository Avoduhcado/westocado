#version 330

in vec2 textureCoords;

uniform sampler2D texture1;
uniform sampler2D texture2;

out vec4 outColor;

void main() {

	vec4 texture1Color = texture(texture1, textureCoords);
	vec4 texture2Color = texture(texture2, textureCoords);
	outColor = mix(texture1Color, vec4(0), 1 - texture2Color.r);

}
