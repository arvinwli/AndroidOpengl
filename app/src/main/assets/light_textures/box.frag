#version 300 es
struct Material {
    sampler2D diffuse;
    sampler2D specular;
    float shininess;
};

struct Light {
    vec3 position;
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
};

uniform Material material;
uniform Light light;

out vec4 FragColor;

uniform vec3 viewPos;
in vec3 Normal;
in vec3 FragPos;
in vec2 TexCoords;


void main() {
    //环境光计算
    float ambientStrength = 0.1;
    vec3 ambient;
    ambient = ambientStrength * light.ambient * vec3(texture(material.diffuse, TexCoords));

    //漫反射计算
    vec3 norm = normalize(Normal);
    vec3 lightDir = normalize(light.position - FragPos);

    float diff = max(dot(norm, lightDir), 0.0);
    vec3 diffuse;
    diffuse= diff * light.diffuse * vec3(texture(material.diffuse, TexCoords));

    float specularStrength = 0.5;
    vec3 viewDir = normalize(viewPos - FragPos);
    vec3 reflectDir = reflect(-lightDir, norm);
    //这里有个坑，pow函数的第二个参数要用64.0，函数的声明是pow(float,float),
    //不要写成整型，否则会计算失败，因为找不到pow(float ,int)的定义
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);
    vec3 specular;
    specular= specularStrength * spec * light.specular * vec3(texture(material.specular, TexCoords));

    vec3 result=ambient + diffuse+ specular;

    FragColor =vec4(result, 1.0);
}