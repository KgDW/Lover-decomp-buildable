package dev.blackhig.zhebushigudu.lover.event;

import net.minecraft.client.Minecraft;

public interface MixinInterface
{
    Minecraft mc = Minecraft.getMinecraft();
    boolean nullCheck = MixinInterface.mc.player == null || MixinInterface.mc.world == null;
}
