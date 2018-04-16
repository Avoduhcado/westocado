#version 330

uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;
uniform sampler2D tex;
uniform vec3 cameraPosition;
uniform float materialShininess;
uniform vec3 materialSpecularColor;
//uniform vec3 lightPosition;
//uniform vec3 lightColor;

// array of lights
#define MAX_LIGHTS 10
uniform float numLights;
uniform struct Light {
   vec4 position;
   vec3 intensities; //a.k.a the color of the light
   float attenuation;
   float ambientCoefficient;
   float coneAngle;
   vec3 coneDirection;
} lights[MAX_LIGHTS];

in vec3 fragVert;
in vec2 fragTextureCoords;
in vec3 fragColor;
in vec3 fragNormal;

out vec4 finalColor;

vec3 ApplyLight(Light light, vec3 surfaceColor, vec3 normal, vec3 surfacePos, vec3 surfaceToCamera) {
    vec3 surfaceToLight;
    float attenuation = 1.0;
    if(light.position.w == 0.0) {
        //directional light
        surfaceToLight = normalize(light.position.xyz);
        attenuation = 1.0; //no attenuation for directional lights
    } else {
        //point light
        surfaceToLight = normalize(light.position.xyz - surfacePos);
        float distanceToLight = length(light.position.xyz - surfacePos);
        attenuation = 1.0 / (1.0 + light.attenuation * pow(distanceToLight, 2));

        //cone restrictions (affects attenuation)
        float lightToSurfaceAngle = degrees(acos(dot(-surfaceToLight, normalize(light.coneDirection))));
        if(lightToSurfaceAngle > light.coneAngle){
            attenuation = 0.0;
        }
    }

    //ambient
    vec3 ambient = light.ambientCoefficient * surfaceColor.rgb * light.intensities;

    //diffuse
    float diffuseCoefficient = max(0.0, dot(normal, surfaceToLight));
    vec3 diffuse = diffuseCoefficient * surfaceColor.rgb * light.intensities;

    //specular
    float specularCoefficient = 0.0;
    if(diffuseCoefficient > 0.0)
        specularCoefficient = pow(max(0.0, dot(surfaceToCamera, reflect(-surfaceToLight, normal))), materialShininess);
    vec3 specular = specularCoefficient * materialSpecularColor * light.intensities;

    //linear color (color before gamma correction)
    return ambient + attenuation*(diffuse + specular);
}

void main() {
	//calculate normal in world coordinates
	mat3 normalMatrix = transpose(inverse(mat3(model)));
	vec3 normal = normalize(normalMatrix * fragNormal);

	//calculate the location of this fragment (pixel) in world coordinates
	vec3 fragPosition = vec3(model * vec4(fragVert, 1));

	//calculate the vector from this pixels surface to the light source
	//vec3 surfaceToLight = lightPosition - fragPosition;
	vec3 surfaceToCamera = normalize(cameraPosition - fragPosition);

	//calculate the cosine of the angle of incidence
	//float brightness = dot(normal, surfaceToLight) / (length(surfaceToLight) * length(normal));
	//float brightness = dot(normal, surfaceToLight) / (length(surfaceToLight) * length(normal));
	//brightness = clamp(brightness, 0, 1);

	//calculate final color of the pixel, based on:
	// 1. The angle of incidence: brightness
	// 2. The color/intensities of the light: light.intensities
	// 3. The texture and texture coord: texture(tex, fragTexCoord)
	vec4 surfaceColor = texture(tex, fragTextureCoords);
	vec3 linearColor = vec3(0);
	for(int i = 0; i < numLights; ++i){
	    linearColor += ApplyLight(lights[i], surfaceColor.rgb, normal, fragPosition, surfaceToCamera);
	}
	//finalColor = vec4(brightness * lightColor * surfaceColor.rgb, surfaceColor.a);
	//finalColor = vec4(linearColor, surfaceColor.a);

    //final color (after gamma correction)
    vec3 gamma = vec3(1.0/2.2);
    finalColor = vec4(pow(linearColor, gamma), surfaceColor.a);
	//finalColor = vec4(linearColor, surfaceColor.a);
	//finalColor = surfaceColor;
	//finalColor = vec4(mix(linearColor, surfaceColor.rgb, 0.9), surfaceColor.a);
}
