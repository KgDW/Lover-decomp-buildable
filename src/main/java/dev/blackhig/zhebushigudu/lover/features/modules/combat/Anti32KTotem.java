package dev.blackhig.zhebushigudu.lover.features.modules.combat;

import dev.blackhig.zhebushigudu.lover.features.modules.Module;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;

public class Anti32KTotem extends Module
{
    public Anti32KTotem() {
        super("Anti32KTotem", "Anti32KTotem", Category.COMBAT, true, false, false);
    }
    
    @Override
    public void onUpdate() {
        if (!(Anti32KTotem.mc.currentScreen instanceof GuiContainer) && Anti32KTotem.mc.player.inventory.getStackInSlot(0).getItem() != Items.TOTEM_OF_UNDYING) {
            for (int i = 9; i < 35; ++i) {
                if (Anti32KTotem.mc.player.inventory.getStackInSlot(i).getItem() == Items.TOTEM_OF_UNDYING) {
                    Anti32KTotem.mc.playerController.windowClick(Anti32KTotem.mc.player.inventoryContainer.windowId, i, 0, ClickType.SWAP, Anti32KTotem.mc.player);
                    break;
                }
            }
        }
    }
}
