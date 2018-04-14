#version 330

in vec2 textureCoords;

uniform sampler2D colorTexture;

out vec4 color;

void main() {

	color = texture(colorTexture, textureCoords);

}
