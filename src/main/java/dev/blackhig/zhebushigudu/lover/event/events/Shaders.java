package dev.blackhig.zhebushigudu.lover.event.events;

public class Shaders
{
    public static String packNameNone;
    static IShaderPack shaderPack;
    
    public static String getShaderPackName() {
        return (Shaders.shaderPack == null) ? null : ((Shaders.shaderPack instanceof ShaderPackNone) ? null : Shaders.shaderPack.getName());
    }
    
    static {
        Shaders.shaderPack = null;
    }
}
