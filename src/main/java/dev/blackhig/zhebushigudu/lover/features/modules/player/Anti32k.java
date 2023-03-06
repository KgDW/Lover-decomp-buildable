package dev.blackhig.zhebushigudu.lover.features.modules.player;

import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumFacing;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import dev.blackhig.zhebushigudu.lover.util.InventoryUtil;
import net.minecraft.init.Items;
import java.util.Comparator;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.block.BlockHopper;
import dev.blackhig.zhebushigudu.lover.util.Anti.BlockInteractionHelper;
import dev.blackhig.zhebushigudu.lover.util.n.EntityUtil;
import java.util.HashMap;
import net.minecraft.util.math.BlockPos;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.features.modules.Module;

public class Anti32k extends Module
{
    private static Anti32k INSTANCE;
    private Setting<Integer> range;
    private Setting<Boolean> packetMine;
    public static BlockPos min;
    int oldslot;
    int shulkInt;
    HashMap<BlockPos, Integer> opendShulk;
    
    public Anti32k() {
        super("Anti32k", "Anti32k", Category.PLAYER, true, false, false);
        this.range = this.register(new Setting<>("Range", 5, 3, 8));
        this.packetMine = this.register(new Setting("PacketMine", false));
        this.oldslot = -1;
        this.opendShulk = new HashMap<>();
    }
    
    @Override
    public void onDisable() {
        this.oldslot = -1;
        this.shulkInt = 0;
        this.opendShulk.clear();
    }
    
    @Override
    public void onTick() {
        if (fullNullCheck()) {
            return;
        }
        final boolean b = false;
        final BlockPos hopperPos = BlockInteractionHelper.getSphere(EntityUtil.getLocalPlayerPosFloored(), this.range.getValue(), this.range.getValue(), false, true, 0).stream().filter(e -> {
            if (Anti32k.mc.world.getBlockState(e).getBlock() instanceof BlockHopper) {
                if (Anti32k.mc.world.getBlockState(new BlockPos(e.getX(), e.getY() + 1, e.getZ())).getBlock() instanceof BlockShulkerBox) {
                    return false;
                }
            }
            return false;
        }).min(Comparator.comparing(e -> Anti32k.mc.player.getDistanceSq(e))).orElse(null);
        final int slot = InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE);
        if (slot != -1 && hopperPos != null) {
            if (Anti32k.mc.player.getDistance(hopperPos.getX(), hopperPos.getY(), hopperPos.getZ()) > this.range.getValue()) {
                return;
            }
            if (Anti32k.mc.player.inventory.currentItem != slot) {
                this.oldslot = Anti32k.mc.player.inventory.currentItem;
                Anti32k.mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
            }
            if (this.packetMine.getValue()) {
                Anti32k.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, hopperPos, EnumFacing.UP));
                Anti32k.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, hopperPos, EnumFacing.UP));
            }
            else {
                Anti32k.mc.playerController.onPlayerDamageBlock(hopperPos, EnumFacing.UP);
                Anti32k.mc.playerController.onPlayerDestroyBlock(hopperPos);
            }
            Anti32k.mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
            if (this.oldslot != -1) {
                Anti32k.mc.player.connection.sendPacket(new CPacketHeldItemChange(this.oldslot));
            }
        }
    }
    
    static {
        Anti32k.INSTANCE = new Anti32k();
        Anti32k.min = null;
    }
}
