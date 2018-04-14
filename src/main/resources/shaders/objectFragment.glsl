#version 330

in vec3 passColor;
in vec2 passTextureCoords;
in float visibility;

uniform sampler2D tex;

out vec4 outColor;

float near = 0.1;
float far = 1000.0;

float LinearizeDepth(float depth) {
    float z = depth * 2.0 - 1.0; // back to NDC 
    return (2.0 * near * far) / (far + near - z * (far - near));
}

void main(void) {
	vec4 texColor = texture(tex, passTextureCoords);

  	float depth = LinearizeDepth(gl_FragCoord.z) / far; // divide by far for demonstration
  	//outColor = mix(vec4(vec3(depth), 1.0), texColor, visibility);
  	//float z = gl_FragCoord.z * 2.0 - 1.0;
  	//float depth = (2.0 * near * far) / (far + near - z * (far - near));
  	//outColor = mix(vec4(vec3(-depth), 1.0), texColor, clamp(visibility / 10, 0.5, 1));
  	//outColor = mix(vec4(vec3(-depth), 1.0), texColor, visibility);
  	outColor = texColor;
  	//outColor = vec4(vec3(gl_FragCoord.z), 1.0);
}
