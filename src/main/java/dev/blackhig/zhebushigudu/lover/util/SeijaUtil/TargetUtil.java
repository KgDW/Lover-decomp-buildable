package dev.blackhig.zhebushigudu.lover.util.SeijaUtil;

import net.minecraft.init.Items;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.Minecraft;

public class TargetUtil
{
    Minecraft mc;
    
    public TargetUtil() {
        this.mc = Minecraft.getMinecraft();
    }
    
    public static byte getArmorPieces(final EntityPlayer target) {
        byte i = 0;
        if (target.inventoryContainer.getSlot(5).getStack().getItem().equals(Items.DIAMOND_HELMET)) {
            ++i;
        }
        if (target.inventoryContainer.getSlot(6).getStack().getItem().equals(Items.DIAMOND_CHESTPLATE)) {
            ++i;
        }
        if (target.inventoryContainer.getSlot(7).getStack().getItem().equals(Items.DIAMOND_LEGGINGS)) {
            ++i;
        }
        if (target.inventoryContainer.getSlot(8).getStack().getItem().equals(Items.DIAMOND_BOOTS)) {
            ++i;
        }
        return i;
    }
}
