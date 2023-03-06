package dev.blackhig.zhebushigudu.lover.features.modules.combat;

import dev.blackhig.zhebushigudu.lover.features.modules.Module;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.math.BlockPos;

public class FastBow extends Module
{
    public FastBow() {
        super("FastBow", "Accelerates bow shots.", Category.COMBAT, true, false, false);
    }
    
    @Override
    public void onUpdate() {
        if (FastBow.mc.player.inventory.getCurrentItem().getItem() instanceof ItemBow && FastBow.mc.player.isHandActive() && FastBow.mc.player.getItemInUseMaxCount() >= 3) {
            FastBow.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, FastBow.mc.player.getHorizontalFacing()));
            FastBow.mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(FastBow.mc.player.getActiveHand()));
            FastBow.mc.player.stopActiveHand();
        }
    }
}
