#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform mat4 u_projTrans;

void main() {
        vec3 color = texture2D(u_texture, v_texCoords).rgb;
        float gray = (color.r + color.g + color.b) / 3.0;
        vec3 grayscale = vec3(gray);

        gl_FragColor = vec4(grayscale, 1.0);

/*
                        sum += texture2D(Texture, vec2(TexCoordOut.x - 4.0*blurSize.x, TexCoordOut.y)) * 0.05;
                        sum += texture2D(Texture, vec2(TexCoordOut.x - 3.0*blurSize.x, TexCoordOut.y)) * 0.09;
                        sum += texture2D(Texture, vec2(TexCoordOut.x - 2.0*blurSize.x, TexCoordOut.y)) * 0.12;
                        sum += texture2D(Texture, vec2(TexCoordOut.x - blurSize.x, TexCoordOut.y)) * 0.15;
                        sum += texture2D(Texture, vec2(TexCoordOut.x, TexCoordOut.y)) * 0.16;
                        sum += texture2D(Texture, vec2(TexCoordOut.x + blurSize.x, TexCoordOut.y)) * 0.15;
                        sum += texture2D(Texture, vec2(TexCoordOut.x + 2.0*blurSize.x, TexCoordOut.y)) * 0.12;
                        sum += texture2D(Texture, vec2(TexCoordOut.x + 3.0*blurSize.x, TexCoordOut.y)) * 0.09;
                        sum += texture2D(Texture, vec2(TexCoordOut.x + 4.0*blurSize.x, TexCoordOut.y)) * 0.05;
                    //
                        sum += texture2D(Texture, vec2(TexCoordOut.x, TexCoordOut.y - 4.0*blurSize.y)) * 0.05;
                        sum += texture2D(Texture, vec2(TexCoordOut.x, TexCoordOut.y - 3.0*blurSize.y)) * 0.09;
                        sum += texture2D(Texture, vec2(TexCoordOut.x, TexCoordOut.y - 2.0*blurSize.y)) * 0.12;
                        sum += texture2D(Texture, vec2(TexCoordOut.x, TexCoordOut.y - 1.0*blurSize.y)) * 0.15;
                        sum += texture2D(Texture, vec2(TexCoordOut.x, TexCoordOut.y)) * 0.16;
                        sum += texture2D(Texture, vec2(TexCoordOut.x, TexCoordOut.y - 1.0*blurSize.y)) * 0.15;
                        sum += texture2D(Texture, vec2(TexCoordOut.x, TexCoordOut.y - 2.0*blurSize.y)) * 0.12;
                        sum += texture2D(Texture, vec2(TexCoordOut.x, TexCoordOut.y - 3.0*blurSize.y)) * 0.09;
                        sum += texture2D(Texture, vec2(TexCoordOut.x, TexCoordOut.y - 4.0*blurSize.y)) * 0.05;
                        gl_FragColor = sum*0.33;*/

}