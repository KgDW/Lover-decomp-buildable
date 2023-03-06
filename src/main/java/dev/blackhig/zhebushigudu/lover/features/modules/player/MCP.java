package dev.blackhig.zhebushigudu.lover.features.modules.player;

import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.init.Items;
import dev.blackhig.zhebushigudu.lover.util.InventoryUtil;
import net.minecraft.item.ItemEnderPearl;
import org.lwjgl.input.Mouse;
import dev.blackhig.zhebushigudu.lover.features.modules.Module;

public class MCP extends Module
{
    private boolean clicked;
    
    public MCP() {
        super("MCP", "Throws a pearl", Category.PLAYER, false, false, false);
        this.clicked = false;
    }
    
    @Override
    public void onEnable() {
        if (fullNullCheck()) {
            this.disable();
        }
    }
    
    @Override
    public void onTick() {
        if (Mouse.isButtonDown(2)) {
            if (!this.clicked) {
                this.throwPearl();
            }
            this.clicked = true;
        }
        else {
            this.clicked = false;
        }
    }
    
    private void throwPearl() {
        final int pearlSlot = InventoryUtil.findHotbarBlock(ItemEnderPearl.class);
        final boolean offhand = (MCP.mc.player.getHeldItemOffhand().getItem() == Items.ENDER_PEARL);
        if (pearlSlot != -1 || offhand) {
            final int oldslot = MCP.mc.player.inventory.currentItem;
            if (!offhand) {
                InventoryUtil.switchToHotbarSlot(pearlSlot, false);
            }
            MCP.mc.playerController.processRightClick(MCP.mc.player, MCP.mc.world, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
            if (!offhand) {
                InventoryUtil.switchToHotbarSlot(oldslot, false);
            }
        }
    }
}
