package dev.blackhig.zhebushigudu.lover.features.modules.player;

import dev.blackhig.zhebushigudu.lover.features.modules.Module;
import dev.blackhig.zhebushigudu.lover.features.modules.misc.InstantMine;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.util.BlockUtil;
import dev.blackhig.zhebushigudu.lover.util.InventoryUtil;
import dev.blackhig.zhebushigudu.lover.util.MathUtil;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;

public class AntiShulkerBox
        extends Module {
    private static AntiShulkerBox INSTANCE = new AntiShulkerBox();
    private final Setting<Double> range = this.register(new Setting<>("Range", 6.0, 1.0, 6.0));
    private final Setting<Double> saferange = this.register(new Setting<>("SafeRange", 5.5, 0.0, 6.0));

    public AntiShulkerBox() {
        super("AntiShulkerBox", "Automatically dig someone else's box", Module.Category.PLAYER, true, false, false);
        this.setInstance();
    }

    public static AntiShulkerBox getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AntiShulkerBox();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onTick() {
        if (AntiShulkerBox.fullNullCheck()) {
            return;
        }
        int mainSlot = AntiShulkerBox.mc.player.inventory.currentItem;
        if (InstantMine.getInstance().isOff()) {
            InstantMine.getInstance().enable();
        }
        for (BlockPos blockPos : this.breakPos(this.range.getValue().intValue())) {
            int slotPick = InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE);
            if (slotPick == -1) {
                return;
            }
            if (AntiShulkerBox.mc.player.getDistanceSq(blockPos) < MathUtil.square(this.saferange.getValue().intValue())) continue;
            if (AntiShulkerBox.mc.world.getBlockState(blockPos).getBlock() instanceof BlockShulkerBox) {
                AntiShulkerBox.mc.player.inventory.currentItem = slotPick;
                AntiShulkerBox.mc.player.swingArm(EnumHand.MAIN_HAND);
                AntiShulkerBox.mc.playerController.onPlayerDamageBlock(blockPos, BlockUtil.getRayTraceFacing(blockPos));
                continue;
            }
            AntiShulkerBox.mc.player.inventory.currentItem = mainSlot;
        }
    }

    private NonNullList<BlockPos> breakPos(float placeRange) {
        NonNullList positions = NonNullList.create();
        positions.addAll(BlockUtil.getSphere(new BlockPos(Math.floor(AntiShulkerBox.mc.player.posX), Math.floor(AntiShulkerBox.mc.player.posY), Math.floor(AntiShulkerBox.mc.player.posZ)), placeRange, 0, false, true, 0));
        return positions;
    }
}
 