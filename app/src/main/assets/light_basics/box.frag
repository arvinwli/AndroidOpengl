#version 300 es
out vec4 FragColor;

uniform vec3 objectColor;
uniform vec3 lightColor;
uniform vec3 lightPos;
uniform vec3 viewPos;
in vec3 Normal;
in vec3 FragPos;

void main() {
    //环境光计算
    float ambientStrength = 0.1;
    vec3 ambient;
    ambient = ambientStrength * lightColor;

    //漫反射计算
    vec3 norm = normalize(Normal);
    vec3 lightDir = normalize(lightPos - FragPos);

    float diff = max(dot(norm, lightDir), 0.0);
    vec3 diffuse;
    diffuse= diff * lightColor;

    float specularStrength = 0.5;
    vec3 viewDir = normalize(viewPos - FragPos);
    vec3 reflectDir = reflect(-lightDir, norm);
    //这里有个坑，pow函数的第二个参数要用64.0，函数的声明是pow(float,float),
    //不要写成整型，否则会计算失败，因为找不到pow(float ,int)的定义
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), 64.0);
    vec3 specular;
    specular= specularStrength * spec * lightColor;

    vec3 result=(ambient + diffuse+ specular)*objectColor;

    FragColor =vec4(result, 1.0);
}