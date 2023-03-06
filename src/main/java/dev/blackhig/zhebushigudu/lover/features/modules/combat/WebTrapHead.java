package dev.blackhig.zhebushigudu.lover.features.modules.combat;

import dev.blackhig.zhebushigudu.lover.features.modules.Module;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.lover;
import dev.blackhig.zhebushigudu.lover.util.BlockUtil;
import dev.blackhig.zhebushigudu.lover.util.EntityUtil;
import dev.blackhig.zhebushigudu.lover.util.InventoryUtil;
import net.minecraft.block.BlockWeb;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class WebTrapHead extends Module
{
    private final Setting<Float> range;
    public EntityPlayer target;
    
    public WebTrapHead() {
        super("WebTrapHead", "Trap Head", Category.COMBAT, true, false, false);
        this.range = this.register(new Setting<>("Range", 5.0f, 1.0f, 6.0f));
    }
    
    @Override
    public void onTick() {
        if (fullNullCheck()) {
            return;
        }
        this.target = this.getTarget(this.range.getValue());
        if (this.target == null) {
            return;
        }
        final BlockPos people = new BlockPos(this.target.posX, this.target.posY, this.target.posZ);
        final int obbySlot = InventoryUtil.findHotbarBlock(BlockWeb.class);
        if (obbySlot == -1) {
            return;
        }
        final int old = WebTrapHead.mc.player.inventory.currentItem;
        if (this.getBlock(people.add(0, 2, 0)).getBlock() == Blocks.AIR) {
            if (this.getBlock(people.add(1, 2, 0)).getBlock() != Blocks.AIR || this.getBlock(people.add(0, 2, 1)).getBlock() != Blocks.AIR || this.getBlock(people.add(-1, 2, 0)).getBlock() != Blocks.AIR || this.getBlock(people.add(0, 2, -1)).getBlock() != Blocks.AIR) {
                this.switchToSlot(obbySlot);
                BlockUtil.placeBlock(people.add(0, 2, 0), EnumHand.MAIN_HAND, false, true, false);
                this.switchToSlot(old);
            }
            else if (this.getBlock(people.add(1, 1, 0)).getBlock() != Blocks.AIR) {
                this.switchToSlot(obbySlot);
                BlockUtil.placeBlock(people.add(1, 2, 0), EnumHand.MAIN_HAND, false, true, false);
                this.switchToSlot(old);
            }
            else if (this.getBlock(people.add(-1, 1, 0)).getBlock() != Blocks.AIR) {
                this.switchToSlot(obbySlot);
                BlockUtil.placeBlock(people.add(-1, 2, 0), EnumHand.MAIN_HAND, false, true, false);
                this.switchToSlot(old);
            }
            else if (this.getBlock(people.add(0, 1, 1)).getBlock() != Blocks.AIR) {
                this.switchToSlot(obbySlot);
                BlockUtil.placeBlock(people.add(0, 2, 1), EnumHand.MAIN_HAND, false, true, false);
                this.switchToSlot(old);
            }
            else if (this.getBlock(people.add(0, 1, -1)).getBlock() != Blocks.AIR) {
                this.switchToSlot(obbySlot);
                BlockUtil.placeBlock(people.add(0, 2, -1), EnumHand.MAIN_HAND, false, true, false);
                this.switchToSlot(old);
            }
        }
    }
    
    private EntityPlayer getTarget(final double range) {
        EntityPlayer target = null;
        double distance = range;
        for (final EntityPlayer player : WebTrapHead.mc.world.playerEntities) {
            if (EntityUtil.isntValid(player, range)) {
                continue;
            }
            if (lover.friendManager.isFriend(player.getName())) {
                continue;
            }
            if (WebTrapHead.mc.player.posY - player.posY >= 5.0) {
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
        WebTrapHead.mc.player.inventory.currentItem = slot;
        WebTrapHead.mc.playerController.updateController();
    }
    
    private IBlockState getBlock(final BlockPos block) {
        return WebTrapHead.mc.world.getBlockState(block);
    }
}
