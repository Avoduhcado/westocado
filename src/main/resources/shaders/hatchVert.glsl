varying float LightIntensity;
varying float V;

void main(void)
{
	vec3 LightPos = gl_LightSource[0].position.xyz;

	V = gl_Vertex.x + gl_Vertex.y + gl_Vertex.z;

	vec3 ECposition = vec3(gl_ModelViewMatrix * gl_Vertex);
	vec3 tnorm = normalize(vec3(gl_NormalMatrix * gl_Normal));
	LightIntensity = dot(normalize(LightPos - ECposition), tnorm) * 1.3;
	gl_Position = ftransform();
}
