package dev.blackhig.zhebushigudu.lover.util.Anti;

import org.lwjgl.input.Keyboard;
import net.minecraft.world.World;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.Minecraft;

public class Wrapper
{
    public static Minecraft mc;
    private static FontRenderer fontRenderer;
    
    public static Minecraft getMinecraft() {
        return Minecraft.getMinecraft();
    }
    
    public static EntityPlayerSP getPlayer() {
        return getMinecraft().player;
    }
    
    public static World getWorld() {
        return (World)getMinecraft().world;
    }
    
    public static int getKey(final String keyname) {
        return Keyboard.getKeyIndex(keyname.toUpperCase());
    }
    
    public static FontRenderer getFontRenderer() {
        return Wrapper.fontRenderer;
    }
    
    static {
        Wrapper.mc = getMinecraft();
    }
}
