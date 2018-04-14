#version 330

uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;
uniform sampler2D tex;
uniform sampler2D texHatch0;
uniform sampler2D texHatch1;
uniform vec3 lightPosition;
uniform vec3 lightColor;

in vec3 fragVert;
in vec2 fragTextureCoords;
in vec3 fragColor;
in vec3 fragNormal;

out vec4 finalColor;

vec3 Hatching(vec2 uv, float intensity) {
	vec3 hatch0 = texture(texHatch0, uv).rgb;
	vec3 hatch1 = texture(texHatch1, uv).rgb;

	//float overbright = max(0, intensity - 1.0);

	vec3 weightsA = clamp((intensity * 6.0) + vec3(-0, -1, -2), 0.0, 1.0);
	vec3 weightsB = clamp((intensity * 6.0) + vec3(-3, -4, -5), 0.0, 1.0);

	weightsA.xy -= weightsA.yz;
	weightsA.z -= weightsB.x;
	weightsB.xy -= weightsB.zy;

	hatch0 = hatch0 * weightsA;
	hatch1 = hatch1 * weightsB;

	vec3 hatching = vec3(hatch0.r +
			hatch0.g, hatch0.b +
			hatch1.r, hatch1.g +
			hatch1.b);

	return hatching;

}

void main() {
    //calculate normal in world coordinates
    mat3 normalMatrix = transpose(inverse(mat3(model)));
    vec3 normal = normalize(normalMatrix * fragNormal);
    
    //calculate the location of this fragment (pixel) in world coordinates
    vec3 fragPosition = vec3(model * vec4(fragVert, 1));
    
    //calculate the vector from this pixels surface to the light source
    vec3 surfaceToLight = lightPosition - fragPosition;

    //calculate the cosine of the angle of incidence
    float brightness = dot(normal, surfaceToLight) / (length(surfaceToLight) * length(normal));
    brightness = clamp(brightness, 0, 1);

    //calculate final color of the pixel, based on:
    // 1. The angle of incidence: brightness
    // 2. The color/intensities of the light: light.intensities
    // 3. The texture and texture coord: texture(tex, fragTexCoord)
    //vec4 surfaceColor = texture(tex, fragTextureCoords);
	float intensity = dot(lightColor, vec3(0.2326, 0.7152, 0.0722));
	finalColor = vec4(Hatching(fragTextureCoords * 8, brightness), 1.0);
    //finalColor = vec4(intensity * lightColor * surfaceColor.rgb, surfaceColor.a);
}
