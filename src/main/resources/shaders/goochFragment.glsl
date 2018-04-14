#version 330

// data from vertex sahder
in vec3 o_normal;
in vec3 o_lightVector;
in vec3 o_viewVector;
in vec2 o_texcoords;

// sampler for 3D texture
layout(location = 0) uniform sampler3D u_colorTexture;

// output from fragment shader to framebuffer
out vec4 resultingColor;

///////////////////////////////////////////////////////////

void main(void)
{
   // normalize vectors for lighting
   vec3 normalVector = normalize(o_normal);
   vec3 lightVector = normalize(o_lightVector);
   vec3 viewVector = normalize(o_viewVector);

   // calculate intensity of lighting
   float ambient = 0.025f;
   float diffuse = clamp(dot(lightVector, normalVector), 0, 1);
   vec3 reflectedVector = reflect(-lightVector, normalVector);
   float specular = 0;
   if(diffuse > 0)
   {
      specular = pow(dot(viewVector, reflectedVector), 32);
   }
   float lightIntensity = clamp(diffuse + specular + ambient, 0, 1);

   // get size of 3D texture
   ivec3 sizeOfTex = textureSize(u_colorTexture, 0);

   // texture coordinates by XY as position of fragment on the screen
   // divided by width of the texture. So each pixel of the texture will
   // correspond to pixel on the screen
   vec2 texCoordXY = gl_FragCoord.xy/sizeOfTex.x;
   // sample depth of the texture by light intensity
   float texCoordZ = lightIntensity;

   // sample 3D texture to get hatching intensity
   vec3 hatching = texture(u_colorTexture, vec3(texCoordXY, texCoordZ)).rgb;

   // modulate hatching with lighting (not required)
   resultingColor.rgb = hatching * (1.0 + lightIntensity * 2)/3;
   resultingColor.a = 1;
} 