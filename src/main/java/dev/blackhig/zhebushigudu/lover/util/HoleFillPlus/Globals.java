package dev.blackhig.zhebushigudu.lover.util.HoleFillPlus;

import java.util.Random;
import net.minecraft.client.Minecraft;

public interface Globals
{
    Minecraft mc = Minecraft.getMinecraft();
    Random random = new Random();

    default boolean nullCheck() {
        return Globals.mc.player == null || Globals.mc.world == null;
    }
}
