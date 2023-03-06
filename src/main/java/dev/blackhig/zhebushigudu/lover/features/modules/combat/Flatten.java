package dev.blackhig.zhebushigudu.lover.features.modules.combat;

import dev.blackhig.zhebushigudu.lover.features.Feature;
import dev.blackhig.zhebushigudu.lover.features.modules.Module;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.lover;
import dev.blackhig.zhebushigudu.lover.util.BlockUtil;
import dev.blackhig.zhebushigudu.lover.util.EntityUtil;
import dev.blackhig.zhebushigudu.lover.util.InventoryUtil;
import dev.blackhig.zhebushigudu.lover.util.Util;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class Flatten extends Module
{
    public EntityPlayer target;
    private final Setting<Float> range;
    private final Setting<Boolean> autoDisable;
    private final Setting<Boolean> chestplace;
    private final Setting<Boolean> xzchestplace;
    private final Setting<Boolean> negative;
    
    public Flatten() {
        super("Flatten", "Automatic feetobsidian", Category.COMBAT, true, false, false);
        this.range = this.register(new Setting<>("Range", 5.0f, 1.0f, 6.0f));
        this.autoDisable = this.register(new Setting<>("AutoDisable", true));
        this.chestplace = this.register(new Setting<>("Y Chest Place", true));
        this.xzchestplace = this.register(new Setting<>("X|Z Chest Place", false));
        this.negative = this.register(new Setting<>("-X|-Z Chest Place", false));
    }
    
    @Override
    public void onUpdate() {
        if (Feature.fullNullCheck()) {
            return;
        }
        this.target = this.getTarget(this.range.getValue());
        if (this.target == null) {
            return;
        }
        final BlockPos people = new BlockPos(this.target.posX, this.target.posY, this.target.posZ);
        final int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        final int chestSlot = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
        if (obbySlot == -1) {
            return;
        }
        final int old = Util.mc.player.inventory.currentItem;
        if (this.getBlock(people.add(0, -1, 0)).getBlock() == Blocks.AIR && this.getBlock(people.add(0, -2, 0)).getBlock() != Blocks.AIR) {
            if (this.chestplace.getValue() && InventoryUtil.findHotbarBlock(BlockEnderChest.class) != -1) {
                this.switchToSlot(chestSlot);
            }
            else {
                this.switchToSlot(obbySlot);
            }
            BlockUtil.placeBlock(people.add(0, -1, 0), EnumHand.MAIN_HAND, false, true, false);
            this.switchToSlot(old);
        }
        if (this.getBlock(people.add(0, -1, 0)).getBlock() != Blocks.AIR && this.getBlock(people.add(1, -1, 0)).getBlock() == Blocks.AIR && this.getBlock(people.add(1, 0, 0)).getBlock() == Blocks.AIR) {
            if (this.negative.getValue() && InventoryUtil.findHotbarBlock(BlockEnderChest.class) != -1) {
                this.switchToSlot(chestSlot);
            }
            else {
                this.switchToSlot(obbySlot);
            }
            BlockUtil.placeBlock(people.add(1, -1, 0), EnumHand.MAIN_HAND, false, true, false);
            this.switchToSlot(old);
        }
        else if (this.getBlock(people.add(0, -1, 0)).getBlock() != Blocks.AIR && this.getBlock(people.add(-1, -1, 0)).getBlock() == Blocks.AIR && this.getBlock(people.add(-1, 0, 0)).getBlock() == Blocks.AIR) {
            if (this.xzchestplace.getValue() && InventoryUtil.findHotbarBlock(BlockEnderChest.class) != -1) {
                this.switchToSlot(chestSlot);
            }
            else {
                this.switchToSlot(obbySlot);
            }
            BlockUtil.placeBlock(people.add(-1, -1, 0), EnumHand.MAIN_HAND, false, true, false);
            this.switchToSlot(old);
        }
        else if (this.getBlock(people.add(0, -1, 0)).getBlock() != Blocks.AIR && this.getBlock(people.add(0, -1, 1)).getBlock() == Blocks.AIR && this.getBlock(people.add(0, 0, 1)).getBlock() == Blocks.AIR) {
            if (this.negative.getValue() && InventoryUtil.findHotbarBlock(BlockEnderChest.class) != -1) {
                this.switchToSlot(chestSlot);
            }
            else {
                this.switchToSlot(obbySlot);
            }
            BlockUtil.placeBlock(people.add(0, -1, 1), EnumHand.MAIN_HAND, false, true, false);
            this.switchToSlot(old);
        }
        else if (this.getBlock(people.add(0, -1, 0)).getBlock() != Blocks.AIR && this.getBlock(people.add(0, -1, -1)).getBlock() == Blocks.AIR && this.getBlock(people.add(0, 0, -1)).getBlock() == Blocks.AIR) {
            if (this.xzchestplace.getValue() && InventoryUtil.findHotbarBlock(BlockEnderChest.class) != -1) {
                this.switchToSlot(chestSlot);
            }
            else {
                this.switchToSlot(obbySlot);
            }
            BlockUtil.placeBlock(people.add(0, -1, -1), EnumHand.MAIN_HAND, false, true, false);
            this.switchToSlot(old);
        }
        if (this.autoDisable.getValue()) {
            this.disable();
        }
    }
    
    private EntityPlayer getTarget(final double range) {
        EntityPlayer target = null;
        double distance = range;
        for (final EntityPlayer player : Util.mc.world.playerEntities) {
            if (EntityUtil.isntValid(player, range)) {
                continue;
            }
            if (lover.friendManager.isFriend(player.getName())) {
                continue;
            }
            if (Util.mc.player.posY - player.posY >= 5.0) {
                continue;
            }
            if (target == null) {
                target = player;
                distance = EntityUtil.mc.player.getDistanceSq(player);
            }
            else {
                if (EntityUtil.mc.player.getDistanceSq(player) >= distance) {
                    continue;
                }
                target = player;
                distance = EntityUtil.mc.player.getDistanceSq(player);
            }
        }
        return target;
    }
    
    private void switchToSlot(final int slot) {
        Util.mc.player.inventory.currentItem = slot;
        Util.mc.playerController.updateController();
    }
    
    private IBlockState getBlock(final BlockPos block) {
        return Util.mc.world.getBlockState(block);
    }
}
