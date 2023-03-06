package dev.blackhig.zhebushigudu.lover;

import net.minecraft.client.entity.EntityPlayerSP;
import dev.blackhig.zhebushigudu.lover.manager.FileManager;
import net.minecraft.client.Minecraft;

public class Wrapper
{
    static final Minecraft mc;
    public static FileManager fileManager;
    
    public static Minecraft GetMC() {
        return Wrapper.mc;
    }
    
    public static EntityPlayerSP GetPlayer() {
        return Wrapper.mc.player;
    }
    
    public static FileManager getFileManager() {
        if (Wrapper.fileManager == null) {
            Wrapper.fileManager = new FileManager();
        }
        return Wrapper.fileManager;
    }
    
    static {
        mc = Minecraft.getMinecraft();
    }
}
