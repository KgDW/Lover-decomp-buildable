package dev.blackhig.zhebushigudu.lover.features.modules.player;

import dev.blackhig.zhebushigudu.lover.features.modules.Module;
import dev.blackhig.zhebushigudu.lover.features.modules.combat.BreakCheck;
import dev.blackhig.zhebushigudu.lover.features.modules.misc.InstantMine;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.util.BlockUtil;
import dev.blackhig.zhebushigudu.lover.util.InventoryUtil;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class AntiHoleKick extends Module
{
    private final Setting<Boolean> rotate;
    private final Setting<Boolean> head;
    private final Setting<Boolean> mine;
    BlockPos pos;
    
    public AntiHoleKick() {
        super("AntiHoleKick", "Anti piston push.", Category.PLAYER, true, false, false);
        this.rotate = this.register(new Setting("Rotate", true));
        this.head = this.register(new Setting("TrapHead", false));
        this.mine = this.register(new Setting("Mine", true));
    }
    
    @Override
    public void onUpdate() {
        this.pos = new BlockPos(AntiHoleKick.mc.player.posX, AntiHoleKick.mc.player.posY, AntiHoleKick.mc.player.posZ);
        if (this.getBlock(this.pos.add(0, 1, 1)).getBlock() == Blocks.PISTON || this.getBlock(this.pos.add(0, 2, 1)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(this.pos.add(0, 1, 2)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(this.pos.add(1, 1, 1)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(this.pos.add(-1, 1, 1)).getBlock() == Blocks.REDSTONE_BLOCK) {
            if (this.getBlock(this.pos.add(0, 1, -1)).getBlock() == Blocks.AIR) {
                this.perform(this.pos.add(0, 1, -1));
            }
            if (this.getBlock(this.pos.add(0, 1, 1)).getBlock() == Blocks.PISTON && this.mine.getValue()) {
                AntiHoleKick.mc.playerController.onPlayerDamageBlock(this.pos.add(0, 1, 1), BlockUtil.getRayTraceFacing(this.pos.add(0, 1, 1)));
            }
            if (this.getBlock(this.pos.add(0, 2, 0)).getBlock() == Blocks.AIR && this.head.getValue()) {
                this.perform(this.pos.add(0, 2, -1));
                this.perform(this.pos.add(0, 2, 0));
            }
        }
        if (this.getBlock(this.pos.add(0, 1, -1)).getBlock() == Blocks.PISTON || this.getBlock(this.pos.add(0, 2, -1)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(this.pos.add(0, 1, -2)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(this.pos.add(1, 1, -1)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(this.pos.add(-1, 1, -1)).getBlock() == Blocks.REDSTONE_BLOCK) {
            if (this.getBlock(this.pos.add(0, 1, 1)).getBlock() == Blocks.AIR) {
                this.perform(this.pos.add(0, 1, 1));
            }
            if (this.getBlock(this.pos.add(0, 1, -1)).getBlock() == Blocks.PISTON && this.mine.getValue()) {
                AntiHoleKick.mc.playerController.onPlayerDamageBlock(this.pos.add(0, 1, -1), BlockUtil.getRayTraceFacing(this.pos.add(0, 1, -1)));
            }
            if (this.getBlock(this.pos.add(0, 2, 0)).getBlock() == Blocks.AIR && this.head.getValue()) {
                this.perform(this.pos.add(0, 2, 1));
                this.perform(this.pos.add(0, 2, 0));
            }
        }
        if (this.getBlock(this.pos.add(1, 1, 0)).getBlock() == Blocks.PISTON || this.getBlock(this.pos.add(1, 2, 0)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(this.pos.add(2, 1, 0)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(this.pos.add(1, 1, 1)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(this.pos.add(1, 1, -1)).getBlock() == Blocks.REDSTONE_BLOCK) {
            if (this.getBlock(this.pos.add(-1, 1, 0)).getBlock() == Blocks.AIR) {
                this.perform(this.pos.add(-1, 1, 0));
            }
            if (this.getBlock(this.pos.add(1, 1, 0)).getBlock() == Blocks.PISTON && this.mine.getValue()) {
                AntiHoleKick.mc.playerController.onPlayerDamageBlock(this.pos.add(1, 1, 0), BlockUtil.getRayTraceFacing(this.pos.add(1, 1, 0)));
            }
            if (this.getBlock(this.pos.add(0, 2, 0)).getBlock() == Blocks.AIR && this.head.getValue()) {
                this.perform(this.pos.add(-1, 2, 0));
                this.perform(this.pos.add(0, 2, 0));
            }
        }
        if (this.getBlock(this.pos.add(-1, 1, 0)).getBlock() == Blocks.PISTON || this.getBlock(this.pos.add(-1, 2, 0)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(this.pos.add(-2, 1, 0)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(this.pos.add(-1, 1, 1)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(this.pos.add(-1, 1, -1)).getBlock() == Blocks.REDSTONE_BLOCK) {
            if (this.getBlock(this.pos.add(1, 1, 0)).getBlock() == Blocks.AIR) {
                this.perform(this.pos.add(1, 1, 0));
            }
            if (this.getBlock(this.pos.add(-1, 1, 0)).getBlock() == Blocks.PISTON && this.mine.getValue()) {
                AntiHoleKick.mc.playerController.onPlayerDamageBlock(this.pos.add(-1, 1, 0), BlockUtil.getRayTraceFacing(this.pos.add(-1, 1, 0)));
            }
            if (this.getBlock(this.pos.add(0, 2, 0)).getBlock() == Blocks.AIR && this.head.getValue()) {
                this.perform(this.pos.add(1, 2, 0));
                this.perform(this.pos.add(0, 2, 0));
            }
        }
        if (this.getBlock(this.pos.add(0, 1, 1)).getBlock() == Blocks.STICKY_PISTON || this.getBlock(this.pos.add(0, 2, 1)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(this.pos.add(0, 1, 2)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(this.pos.add(1, 1, 1)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(this.pos.add(-1, 1, 1)).getBlock() == Blocks.REDSTONE_BLOCK) {
            if (this.getBlock(this.pos.add(0, 1, -1)).getBlock() == Blocks.AIR) {
                this.perform(this.pos.add(0, 1, -1));
            }
            if (this.getBlock(this.pos.add(0, 1, 1)).getBlock() == Blocks.STICKY_PISTON && this.mine.getValue()) {
                AntiHoleKick.mc.playerController.onPlayerDamageBlock(this.pos.add(0, 1, 1), BlockUtil.getRayTraceFacing(this.pos.add(0, 1, 1)));
            }
            if (this.getBlock(this.pos.add(0, 2, 0)).getBlock() == Blocks.AIR && this.head.getValue()) {
                this.perform(this.pos.add(0, 2, -1));
                this.perform(this.pos.add(0, 2, 0));
            }
        }
        if (this.getBlock(this.pos.add(0, 1, -1)).getBlock() == Blocks.STICKY_PISTON || this.getBlock(this.pos.add(0, 2, -1)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(this.pos.add(0, 1, -2)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(this.pos.add(1, 1, -1)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(this.pos.add(-1, 1, -1)).getBlock() == Blocks.REDSTONE_BLOCK) {
            if (this.getBlock(this.pos.add(0, 1, 1)).getBlock() == Blocks.AIR) {
                this.perform(this.pos.add(0, 1, 1));
            }
            if (this.getBlock(this.pos.add(0, 1, -1)).getBlock() == Blocks.STICKY_PISTON && this.mine.getValue()) {
                AntiHoleKick.mc.playerController.onPlayerDamageBlock(this.pos.add(0, 1, -1), BlockUtil.getRayTraceFacing(this.pos.add(0, 1, -1)));
            }
            if (this.getBlock(this.pos.add(0, 2, 0)).getBlock() == Blocks.AIR && this.head.getValue()) {
                this.perform(this.pos.add(0, 2, 1));
                this.perform(this.pos.add(0, 2, 0));
            }
        }
        if (this.getBlock(this.pos.add(1, 1, 0)).getBlock() == Blocks.STICKY_PISTON || this.getBlock(this.pos.add(1, 2, 0)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(this.pos.add(2, 1, 0)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(this.pos.add(1, 1, 1)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(this.pos.add(1, 1, -1)).getBlock() == Blocks.REDSTONE_BLOCK) {
            if (this.getBlock(this.pos.add(-1, 1, 0)).getBlock() == Blocks.AIR) {
                this.perform(this.pos.add(-1, 1, 0));
            }
            if (this.getBlock(this.pos.add(1, 1, 0)).getBlock() == Blocks.STICKY_PISTON && this.mine.getValue()) {
                AntiHoleKick.mc.playerController.onPlayerDamageBlock(this.pos.add(1, 1, 0), BlockUtil.getRayTraceFacing(this.pos.add(1, 1, 0)));
            }
            if (this.getBlock(this.pos.add(0, 2, 0)).getBlock() == Blocks.AIR && this.head.getValue()) {
                this.perform(this.pos.add(-1, 2, 0));
                this.perform(this.pos.add(0, 2, 0));
            }
        }
        if (this.getBlock(this.pos.add(-1, 1, 0)).getBlock() == Blocks.STICKY_PISTON || this.getBlock(this.pos.add(-1, 2, 0)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(this.pos.add(-2, 1, 0)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(this.pos.add(-1, 1, 1)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(this.pos.add(-1, 1, -1)).getBlock() == Blocks.REDSTONE_BLOCK) {
            if (this.getBlock(this.pos.add(1, 1, 0)).getBlock() == Blocks.AIR) {
                this.perform(this.pos.add(1, 1, 0));
            }
            if (this.getBlock(this.pos.add(-1, 1, 0)).getBlock() == Blocks.STICKY_PISTON && this.mine.getValue()) {
                AntiHoleKick.mc.playerController.onPlayerDamageBlock(this.pos.add(-1, 1, 0), BlockUtil.getRayTraceFacing(this.pos.add(-1, 1, 0)));
            }
            if (this.getBlock(this.pos.add(0, 2, 0)).getBlock() == Blocks.AIR && this.head.getValue()) {
                this.perform(this.pos.add(1, 2, 0));
                this.perform(this.pos.add(0, 2, 0));
            }
        }
    }
    
    private IBlockState getBlock(final BlockPos block) {
        return AntiHoleKick.mc.world.getBlockState(block);
    }
    
    @Override
    public String getDisplayInfo() {
        if (AntiHoleKick.mc.player != null) {
            if (this.getBlock(this.pos.add(-1, 1, 0)).getBlock() == Blocks.PISTON || this.getBlock(this.pos.add(-1, 2, 0)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(this.pos.add(-2, 1, 0)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(this.pos.add(-1, 1, 1)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(this.pos.add(-1, 1, -1)).getBlock() == Blocks.REDSTONE_BLOCK) {
                return "Working";
            }
            if (this.getBlock(this.pos.add(1, 1, 0)).getBlock() == Blocks.PISTON || this.getBlock(this.pos.add(1, 2, 0)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(this.pos.add(2, 1, 0)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(this.pos.add(1, 1, 1)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(this.pos.add(1, 1, -1)).getBlock() == Blocks.REDSTONE_BLOCK) {
                return "Working";
            }
            if (this.getBlock(this.pos.add(0, 1, -1)).getBlock() == Blocks.PISTON || this.getBlock(this.pos.add(0, 2, -1)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(this.pos.add(0, 1, -2)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(this.pos.add(1, 1, -1)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(this.pos.add(-1, 1, -1)).getBlock() == Blocks.REDSTONE_BLOCK) {
                return "Working";
            }
            if (this.getBlock(this.pos.add(0, 1, 1)).getBlock() == Blocks.PISTON || this.getBlock(this.pos.add(0, 2, 1)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(this.pos.add(0, 1, 2)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(this.pos.add(1, 1, 1)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(this.pos.add(-1, 1, 1)).getBlock() == Blocks.REDSTONE_BLOCK) {
                return "Working";
            }
            if (this.getBlock(this.pos.add(-1, 1, 0)).getBlock() == Blocks.STICKY_PISTON || this.getBlock(this.pos.add(-1, 2, 0)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(this.pos.add(-2, 1, 0)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(this.pos.add(-1, 1, 1)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(this.pos.add(-1, 1, -1)).getBlock() == Blocks.REDSTONE_BLOCK) {
                return "Working";
            }
            if (this.getBlock(this.pos.add(1, 1, 0)).getBlock() == Blocks.STICKY_PISTON || this.getBlock(this.pos.add(1, 2, 0)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(this.pos.add(2, 1, 0)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(this.pos.add(1, 1, 1)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(this.pos.add(1, 1, -1)).getBlock() == Blocks.REDSTONE_BLOCK) {
                return "Working";
            }
            if (this.getBlock(this.pos.add(0, 1, -1)).getBlock() == Blocks.STICKY_PISTON || this.getBlock(this.pos.add(0, 2, -1)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(this.pos.add(0, 1, -2)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(this.pos.add(1, 1, -1)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(this.pos.add(-1, 1, -1)).getBlock() == Blocks.REDSTONE_BLOCK) {
                return "Working";
            }
            if (this.getBlock(this.pos.add(0, 1, 1)).getBlock() == Blocks.STICKY_PISTON || this.getBlock(this.pos.add(0, 2, 1)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(this.pos.add(0, 1, 2)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(this.pos.add(1, 1, 1)).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(this.pos.add(-1, 1, 1)).getBlock() == Blocks.REDSTONE_BLOCK) {
                return "Working";
            }
        }
        return null;
    }
    
    private void perform(final BlockPos pos2) {
        final int old = AntiHoleKick.mc.player.inventory.currentItem;
        if (AntiHoleKick.mc.world.getBlockState(pos2).getBlock() == Blocks.AIR) {
            if (InstantMine.breakPos != null && new BlockPos(InstantMine.breakPos).equals(new BlockPos(pos2))) {
                return;
            }
            if (BreakCheck.Instance().BrokenPos != null && new BlockPos(BreakCheck.Instance().BrokenPos).equals(new BlockPos(pos2))) {
                return;
            }
            if (InventoryUtil.findHotbarBlock(BlockObsidian.class) != -1) {
                AntiHoleKick.mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(BlockObsidian.class);
                AntiHoleKick.mc.playerController.updateController();
                BlockUtil.placeBlock(pos2, EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
                AntiHoleKick.mc.player.inventory.currentItem = old;
                AntiHoleKick.mc.playerController.updateController();
            }
        }
    }
}
