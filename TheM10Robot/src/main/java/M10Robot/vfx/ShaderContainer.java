package M10Robot.vfx;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class ShaderContainer {

    public static ShaderProgram shade = new ShaderProgram(
            "attribute vec4 a_position;\nattribute vec4 a_color;\nattribute vec2 a_texCoord0;\nuniform mat4 u_projTrans;\nvarying vec4 v_color;\nvarying vec2 v_texCoords;\nvarying float v_lightFix;\n\nvoid main()\n{\n   v_color = a_color;\n   v_texCoords = a_texCoord0;\n   v_color.a = pow(v_color.a * (255.0/254.0) + 0.5, 1.709);\n   v_lightFix = 1.0 + pow(v_color.a, 1.41421356);\n   gl_Position =  u_projTrans * a_position;\n}\n",
            "#ifdef GL_ES\n#define LOWP lowp\nprecision mediump float;\n#else\n#define LOWP \n#endif\nvarying vec2 v_texCoords;\nvarying float v_lightFix;\nvarying LOWP vec4 v_color;\nuniform sampler2D u_texture;\nconst float eps = 1.0e-10;\nvec4 rgb2hsl(vec4 c)\n{\n    const vec4 J = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);\n    vec4 p = mix(vec4(c.bg, J.wz), vec4(c.gb, J.xy), step(c.b, c.g));\n    vec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));\n    float d = q.x - min(q.w, q.y);\n    float l = q.x * (1.0 - 0.5 * d / (q.x + eps));\n    return vec4(abs(q.z + (q.w - q.y) / (6.0 * d + eps)), (q.x - l) / (min(l, 1.0 - l) + eps), l, c.a);\n}\n\nvec4 hsl2rgb(vec4 c)\n{\n    const vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);\n    vec3 p = abs(fract(c.x + K.xyz) * 6.0 - K.www);\n    float v = (c.z + c.y * min(c.z, 1.0 - c.z));\n    return vec4(v * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), 2.0 * (1.0 - c.z / (v + eps))), c.w);\n}void main()\n{\n    float hue = (v_color.x - 0.5);\n    float saturation = v_color.y * 2.0;\n    float brightness = v_color.z - 0.5;\n    vec4 tgt = texture2D( u_texture, v_texCoords );\n    tgt = rgb2hsl(tgt);\n    tgt.r = fract(tgt.r + hue);\n    tgt = hsl2rgb(tgt);\n    tgt.rgb = vec3(\n     (0.5 * pow(dot(tgt.rgb, vec3(0.375, 0.5, 0.125)), v_color.w) * v_lightFix + brightness),\n     ((tgt.r - tgt.b) * saturation),\n     ((tgt.g - tgt.b) * saturation));\n    gl_FragColor = clamp(vec4(\n     dot(tgt.rgb, vec3(1.0, 0.625, -0.5)),\n     dot(tgt.rgb, vec3(1.0, -0.375, 0.5)),\n     dot(tgt.rgb, vec3(1.0, -0.375, -0.5)),\n     tgt.a), 0.0, 1.0);\n}");


}
