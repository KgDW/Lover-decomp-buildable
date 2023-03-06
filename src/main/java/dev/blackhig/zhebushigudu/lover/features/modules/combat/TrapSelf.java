package dev.blackhig.zhebushigudu.lover.features.modules.combat;

import dev.blackhig.zhebushigudu.lover.features.modules.Module;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.lover;
import dev.blackhig.zhebushigudu.lover.util.BlockUtil;
import dev.blackhig.zhebushigudu.lover.util.EntityUtil;
import dev.blackhig.zhebushigudu.lover.util.InventoryUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class TrapSelf extends Module
{
    private final Setting<Boolean> rotate;
    private final Setting<Boolean> hole;
    private final Setting<Boolean> center;
    private final Setting<Boolean> toggle;
    private int obsidian;
    
    public TrapSelf() {
        super("TrapSelf", "One Self Trap", Category.COMBAT, true, false, false);
        this.rotate = this.register(new Setting<>("Rotate", false));
        this.hole = this.register(new Setting<>("Hole or Burrrow", false));
        this.center = this.register(new Setting<>("TPCenter", false));
        this.toggle = this.register(new Setting<>("Toggle", false));
        this.obsidian = -1;
    }
    
    @Override
    public void onEnable() {
        final BlockPos startPos = EntityUtil.getRoundedBlockPos(Surround.mc.player);
        if (this.center.getValue()) {
            lover.positionManager.setPositionPacket(startPos.getX() + 0.5, startPos.getY(), startPos.getZ() + 0.5, true, true, true);
        }
    }
    
    @Override
    public void onTick() {
        if (fullNullCheck()) {
            return;
        }
        this.obsidian = InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN);
        if (this.obsidian == -1) {
            return;
        }
        final BlockPos pos = new BlockPos(TrapSelf.mc.player.posX, TrapSelf.mc.player.posY, TrapSelf.mc.player.posZ);
        if ((!EntityUtil.isInHole(TrapSelf.mc.player) || !this.isBurrowed(TrapSelf.mc.player)) && this.hole.getValue()) {
            return;
        }
        if (this.getBlock(pos.add(1, 0, 0)).getBlock() == Blocks.AIR) {
            this.place(pos.add(1, 0, 0));
        }
        if (this.getBlock(pos.add(1, 1, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(1, 0, 0)).getBlock() != Blocks.AIR) {
            this.place(pos.add(1, 1, 0));
        }
        if (this.getBlock(pos.add(1, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(1, 1, 0)).getBlock() != Blocks.AIR) {
            this.place(pos.add(1, 2, 0));
        }
        if (this.getBlock(pos.add(-1, 0, 0)).getBlock() == Blocks.AIR) {
            this.place(pos.add(-1, 0, 0));
        }
        if (this.getBlock(pos.add(-1, 1, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(-1, 0, 0)).getBlock() != Blocks.AIR) {
            this.place(pos.add(-1, 1, 0));
        }
        if (this.getBlock(pos.add(-1, 2, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(-1, 1, 0)).getBlock() != Blocks.AIR) {
            this.place(pos.add(-1, 2, 0));
        }
        if (this.getBlock(pos.add(0, 0, 1)).getBlock() == Blocks.AIR) {
            this.place(pos.add(0, 0, 1));
        }
        if (this.getBlock(pos.add(0, 1, 1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(0, 0, 1)).getBlock() != Blocks.AIR) {
            this.place(pos.add(0, 1, 1));
        }
        if (this.getBlock(pos.add(0, 2, 1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(0, 1, 1)).getBlock() != Blocks.AIR) {
            this.place(pos.add(0, 2, 1));
        }
        if (this.getBlock(pos.add(0, 0, -1)).getBlock() == Blocks.AIR) {
            this.place(pos.add(0, 0, -1));
        }
        if (this.getBlock(pos.add(0, 1, -1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(0, 0, -1)).getBlock() != Blocks.AIR) {
            this.place(pos.add(0, 1, -1));
        }
        if (this.getBlock(pos.add(0, 2, -1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(0, 1, -1)).getBlock() != Blocks.AIR) {
            this.place(pos.add(0, 2, -1));
        }
        if (this.getBlock(pos.add(0, 2, 0)).getBlock() == Blocks.AIR) {
            this.place(pos.add(0, 2, 0));
        }
        if (this.toggle.getValue() || lover.speedManager.getPlayerSpeed(TrapSelf.mc.player) > 10.0) {
            this.toggle();
        }
    }
    
    private void switchToSlot(final int slot) {
        TrapSelf.mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
        TrapSelf.mc.player.inventory.currentItem = slot;
        TrapSelf.mc.playerController.updateController();
    }
    
    private void place(final BlockPos pos) {
        final int old = TrapSelf.mc.player.inventory.currentItem;
        this.switchToSlot(this.obsidian);
        BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
        this.switchToSlot(old);
    }
    
    private IBlockState getBlock(final BlockPos block) {
        return TrapSelf.mc.world.getBlockState(block);
    }
    
    private boolean isBurrowed(final EntityPlayer entityPlayer) {
        final BlockPos blockPos = new BlockPos(Math.floor(entityPlayer.posX), Math.floor(entityPlayer.posY + 0.2), Math.floor(entityPlayer.posZ));
        return TrapSelf.mc.world.getBlockState(blockPos).getBlock() == Blocks.ENDER_CHEST || TrapSelf.mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN || TrapSelf.mc.world.getBlockState(blockPos).getBlock() == Blocks.CHEST;
    }
}
