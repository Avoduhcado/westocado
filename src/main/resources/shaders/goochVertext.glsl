#version 330

// attributes
layout(location = 0) in vec3 i_position; // xyz - position
layout(location = 1) in vec3 i_normal; // xyz - normal
layout(location = 1) in vec2 i_texcoord0; // xy - texture coords

// uniforms
uniform mat4 u_model_mat;
uniform mat4 u_view_mat;
uniform mat4 u_proj_mat;
uniform mat3 u_normal_mat;
uniform vec3 u_light_position;
uniform vec3 u_camera_position;

// output data from vertex to fragment shader
out vec3 o_normal;
out vec3 o_lightVector;
out vec3 o_viewVector;
out vec2 o_texcoords;

///////////////////////////////////////////////////////////////////

void main(void)
{
   // transform position and normal to world space
   vec4 positionWorld = u_model_mat * vec4(i_position, 1.0);
   vec3 normalWorld = u_normal_mat * i_normal;

   // calculate and pass vectors required for lighting
   o_lightVector = u_light_position - positionWorld.xyz;
   o_viewVector = u_camera_position - positionWorld.xyz;
   o_texcoords = i_texcoord0;
   o_normal = normalWorld;

   // project world space position to the screen and output it
   gl_Position = u_proj_mat * u_view_mat * positionWorld;
}
