#version 330

in vec2 textureCoords;

out vec4 color;

uniform sampler2D depthTexture;

float LinearizeDepth(float zoverw) {
	float n = 1.0; // camera z near
	float f = 20000.0; // camera z far
	return (2.0 * n) / (f + n - zoverw * (f - n));
}

float threshold(float thr1, float thr2 , float val) {
	if (val < thr1) {
		return 1.0;
	}
	if (val > thr2) {
		return 0.0;
	}
	return val;
}

// averaged pixel intensity from 3 color channels
float avg_intensity(vec4 pix) {
	//return LinearizeDepth(pix.r) * 77;
	return pix.r * 77; /* Magic numberzz */
	//return (pix.r + pix.g + pix.b)/3.;
}

vec4 get_pixel(vec2 coords, float dx, float dy) {
	return texture(depthTexture, coords + vec2(dx, dy));
}

// returns pixel color
float IsEdge(vec2 coords){
	//float dxtex = 1.0 / 512.0 /*image width*/;
	//float dytex = 1.0 / 512.0 /*image height*/;
	// TODO Get the actual FBO texture size? uniforms probably
	float dxtex = 1.0 / 1280.0 /*image width*/;
	float dytex = 1.0 / 720.0 /*image height*/;
	float pix[9];
	int k = -1;
	float delta;

	// read neighboring pixel intensities
	for (int i=-1; i<2; i++) {
		for(int j=-1; j<2; j++) {
			k++;
			pix[k] = avg_intensity(get_pixel(coords, float(i) * dxtex, float(j)*dytex));
		}
	}

	// average color differences around neighboring pixels
	delta = (abs(pix[1]-pix[7])
			+ abs(pix[5]-pix[3])
			+ abs(pix[0]-pix[8])
			+ abs(pix[2]-pix[6])
	) / 4.0;

	return threshold(0.25, 0.4, clamp(1.8 * delta, 0.0, 1.0));
}

void main() {

	/*float depth = texture(colorTexture, textureCoords).r;
	depth = LinearizeDepth(depth) * 77;
	color = vec4(depth, depth, depth, 1.0);
	//color = texture(colorTexture, textureCoords);*/

	vec4 edgeColor;
	float edge = IsEdge(textureCoords);
	edgeColor = vec4(edge, edge, edge, 1.0);
	//edgeColor.g = IsEdge(textureCoords);
	color = edgeColor;

}
