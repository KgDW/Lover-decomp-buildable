package dev.blackhig.zhebushigudu.lover.util.SeijaUtil;

import dev.blackhig.zhebushigudu.lover.util.InventoryUtil;
import net.minecraft.item.Item;
import net.minecraft.client.Minecraft;

public class SeijaInvUtil
{
    Minecraft mc;
    
    public SeijaInvUtil() {
        this.mc = Minecraft.getMinecraft();
    }
    
    public static int switchToItem(final Item itemIn) {
        final int slot = InventoryUtil.getItemHotbar(itemIn);
        if (slot == -1) {
            return -1;
        }
        InventoryUtil.switchToHotbarSlot(slot, false);
        return slot;
    }
}
