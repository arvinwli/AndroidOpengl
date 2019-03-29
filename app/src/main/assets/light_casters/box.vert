#version 300 es
//OpenGL ES 与 OpenGL 之间的一个区别就是在 GLSL 中引入了精度限定符。
//精度限定符可使着色器的编写者明确定义着色器变量计算时使用的精度，变量可以选择被声明为低、中或高精度。
//精度限定符可告知编译器使其在计算时缩小变量潜在的精度变化范围，
//当使用低精度时，OpenGL ES 的实现可以更快速和低功耗地运行着色器，效率的提高来自于精度的舍弃，如果精度选择不合理，着色器运行的结果会很失真。
precision mediump float;
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec3 aNormal;
layout (location = 2) in vec2 aTexCoords;
out vec3 Normal;
out vec3 FragPos;
out vec2 TexCoords;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main() {
    gl_Position =projection * view * model * vec4(aPos,1.0);
    FragPos = vec3(model * vec4(aPos, 1.0));
    //Normal = mat3(transpose(inverse(model))) * aNormal;
    //齐次变量设置为0，使法向量只进行缩放和旋转
    Normal = vec3(model * vec4(aNormal, 0.0));
    TexCoords =  aTexCoords;
}