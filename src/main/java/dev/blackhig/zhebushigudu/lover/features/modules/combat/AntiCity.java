package dev.blackhig.zhebushigudu.lover.features.modules.combat;

import dev.blackhig.zhebushigudu.lover.features.modules.Module;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.lover;
import dev.blackhig.zhebushigudu.lover.util.BlockUtil;
import dev.blackhig.zhebushigudu.lover.util.EntityUtil;
import dev.blackhig.zhebushigudu.lover.util.InventoryUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class AntiCity extends Module
{
    private final Setting<Boolean> rotate;
    private final Setting<Boolean> toggle;
    private final Setting<Double> range;
    private int obsidian;
    
    public AntiCity() {
        super("AntiCity", "AntiCity", Category.COMBAT, true, false, false);
        this.rotate = this.register(new Setting<>("Rotate", false));
        this.toggle = this.register(new Setting<>("Toggle", false));
        this.range = this.register(new Setting<>("Range", 5.0, 1.0, 10.0));
        this.obsidian = -1;
    }
    
    public static boolean isHard(final Block block) {
        return block == Blocks.BEDROCK;
    }
    
    @Override
    public void onUpdate() {
        if (AntiCity.mc.player == null || AntiCity.mc.world == null) {
            return;
        }
        if (!lover.moduleManager.isModuleEnabled("Surround")) {
            return;
        }
        this.obsidian = InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN);
        if (this.obsidian == -1) {
            return;
        }
        final BlockPos pos = new BlockPos(AntiCity.mc.player.posX, AntiCity.mc.player.posY, AntiCity.mc.player.posZ);
        if (this.getTarget(this.range.getValue()) == null) {
            return;
        }
        if (!isHard(this.getBlock(pos.add(1, 0, 0)).getBlock())) {
            if (this.getBlock(pos.add(2, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(1, 0, 0)).getBlock() == Blocks.OBSIDIAN) {
                this.perform(pos.add(2, 0, 0));
            }
            if (this.getBlock(pos.add(1, 0, 1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(1, 0, 0)).getBlock() == Blocks.OBSIDIAN) {
                this.perform(pos.add(1, 0, 1));
            }
            if (this.getBlock(pos.add(1, 0, -1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(1, 0, 0)).getBlock() == Blocks.OBSIDIAN) {
                this.perform(pos.add(1, 0, -1));
            }
            if (this.getBlock(pos.add(0, 2, 0)).getBlock() == Blocks.OBSIDIAN && this.getBlock(pos.add(2, 1, 0)).getBlock() == Blocks.AIR) {
                this.perform(pos.add(2, 1, 0));
            }
        }
        if (!isHard(this.getBlock(pos.add(-1, 0, 0)).getBlock())) {
            if (this.getBlock(pos.add(-2, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(-1, 0, 0)).getBlock() == Blocks.OBSIDIAN) {
                this.perform(pos.add(-2, 0, 0));
            }
            if (this.getBlock(pos.add(-1, 0, 1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(-1, 0, 0)).getBlock() == Blocks.OBSIDIAN) {
                this.perform(pos.add(-1, 0, 1));
            }
            if (this.getBlock(pos.add(-1, 0, -1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(-1, 0, 0)).getBlock() == Blocks.OBSIDIAN) {
                this.perform(pos.add(-1, 0, -1));
            }
            if (this.getBlock(pos.add(0, 2, 0)).getBlock() == Blocks.OBSIDIAN && this.getBlock(pos.add(-2, 1, 0)).getBlock() == Blocks.AIR) {
                this.perform(pos.add(-2, 1, 0));
            }
        }
        if (!isHard(this.getBlock(pos.add(0, 0, 1)).getBlock())) {
            if (this.getBlock(pos.add(0, 0, 2)).getBlock() == Blocks.AIR && this.getBlock(pos.add(0, 0, 1)).getBlock() == Blocks.OBSIDIAN) {
                this.perform(pos.add(0, 0, 2));
            }
            if (this.getBlock(pos.add(1, 0, 1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(0, 0, 1)).getBlock() == Blocks.OBSIDIAN) {
                this.perform(pos.add(1, 0, 1));
            }
            if (this.getBlock(pos.add(-1, 0, 1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(0, 0, 1)).getBlock() == Blocks.OBSIDIAN) {
                this.perform(pos.add(-1, 0, 1));
            }
            if (this.getBlock(pos.add(0, 2, 0)).getBlock() == Blocks.OBSIDIAN && this.getBlock(pos.add(0, 1, 2)).getBlock() == Blocks.AIR) {
                this.perform(pos.add(0, 1, 2));
            }
        }
        if (!isHard(this.getBlock(pos.add(0, 0, -1)).getBlock())) {
            if (this.getBlock(pos.add(0, 0, -2)).getBlock() == Blocks.AIR && this.getBlock(pos.add(0, 0, -1)).getBlock() == Blocks.OBSIDIAN) {
                this.perform(pos.add(0, 0, -2));
            }
            if (this.getBlock(pos.add(1, 0, -1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(0, 0, -1)).getBlock() == Blocks.OBSIDIAN) {
                this.perform(pos.add(1, 0, -1));
            }
            if (this.getBlock(pos.add(-1, 0, -1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(0, 0, -1)).getBlock() == Blocks.OBSIDIAN) {
                this.perform(pos.add(-1, 0, -1));
            }
            if (this.getBlock(pos.add(0, 2, 0)).getBlock() == Blocks.OBSIDIAN && this.getBlock(pos.add(0, 1, -2)).getBlock() == Blocks.AIR) {
                this.perform(pos.add(0, 1, -2));
            }
        }
        if (this.toggle.getValue()) {
            this.toggle();
        }
    }
    
    private void switchToSlot(final int slot) {
        AntiCity.mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
        AntiCity.mc.player.inventory.currentItem = slot;
        AntiCity.mc.playerController.updateController();
    }
    
    private IBlockState getBlock(final BlockPos block) {
        return AntiCity.mc.world.getBlockState(block);
    }
    
    private void perform(final BlockPos pos) {
        final int old = AntiCity.mc.player.inventory.currentItem;
        this.switchToSlot(this.obsidian);
        BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
        this.switchToSlot(old);
    }
    
    private EntityPlayer getTarget(final double range) {
        EntityPlayer target = null;
        double distance = Math.pow(range, 2.0) + 1.0;
        for (final EntityPlayer player : AutoTrap.mc.world.playerEntities) {
            if (!EntityUtil.isntValid(player, range)) {
                if (lover.speedManager.getPlayerSpeed(player) > 10.0) {
                    continue;
                }
                if (target == null) {
                    target = player;
                    distance = AutoTrap.mc.player.getDistanceSq(player);
                }
                else {
                    if (AutoTrap.mc.player.getDistanceSq(player) >= distance) {
                        continue;
                    }
                    target = player;
                    distance = AutoTrap.mc.player.getDistanceSq(player);
                }
            }
        }
        return target;
    }
}
