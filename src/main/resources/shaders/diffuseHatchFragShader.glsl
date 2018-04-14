#version 330

uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;
uniform sampler2D tex;
uniform sampler2D tex2;
uniform vec3 lightPosition;
uniform vec3 lightColor;

in vec3 fragVert;
in vec2 fragTextureCoords;
in vec3 fragColor;
in vec3 fragNormal;

out vec4 finalColor;

vec3 hatching(vec2 uv, float intensity) {
	vec3 intensity3 = vec3(intensity, intensity, intensity);

    vec3 overbright = vec3(max(0, intensity - 1.0));

    vec3 weightsA = clamp((intensity * 6.0) + vec3(-0, -1, -2), 0.0, 1.0);
	vec3 weightsB = clamp((intensity * 6.0) + vec3(-3, -4, -5), 0.0, 1.0);
	weightsA.xy -= weightsA.yz;
	weightsA.z -= weightsB.x;
	weightsB.xy -= weightsB.zy;

	vec3 hatch0 = texture(tex, uv * 8).rgb;
	vec3 hatch1 = texture(tex2, uv * 8).rgb;

	hatch0 = hatch0 * weightsA;
	hatch1 = hatch1 * weightsB;

	return vec3(overbright + hatch0.r +
			hatch0.g + hatch0.b +
			hatch1.r + hatch1.g +
			hatch1.b);
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
	//float brightness = dot(normal, surfaceToLight) / (length(surfaceToLight) * length(normal));
	float brightness = dot(normal, surfaceToLight) / (length(surfaceToLight) * length(normal));
	brightness = clamp(brightness, 0, 1);

	//vec3 hatchValue = hatching(fragTextureCoords, brightness * 1.2);
	vec3 hatchValue = hatching(fragTextureCoords, brightness);

	//calculate final color of the pixel, based on:
	// 1. The angle of incidence: brightness
	// 2. The color/intensities of the light: light.intensities
	// 3. The texture and texture coord: texture(tex, fragTexCoord)
	//vec4 surfaceColor = texture(tex, fragTextureCoords);
	//vec4 surface2Color = texture(tex2, fragTextureCoords);
	//finalColor = vec4(brightness * lightColor * surfaceColor.rgb, surfaceColor.a);
	//float lumins = (surfaceColor.r + surfaceColor.g + surfaceColor.b) / 3.0;
	//finalColor = mix(surface2Color, vec4(lumins, lumins, lumins, 1.0), brightness);
	finalColor = vec4(hatchValue, 1.0);
	//finalColor = mix(surface2Color, surfaceColor, brightness);
	//finalColor = vec4(mix(surfaceColor.rgb, lightColor, brightness), surfaceColor.a);
	//finalColor = vec4(surfaceColor.rgb, surfaceColor.a);
}
