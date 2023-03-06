package dev.blackhig.zhebushigudu.lover.features.modules.player;

import dev.blackhig.zhebushigudu.lover.features.modules.Module;
import dev.blackhig.zhebushigudu.lover.features.modules.combat.AutoCity;
import dev.blackhig.zhebushigudu.lover.features.modules.combat.AutoCityPlus;
import dev.blackhig.zhebushigudu.lover.features.modules.misc.InstantMine;
import dev.blackhig.zhebushigudu.lover.features.modules.misc.ReplenishBox;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.lover;
import dev.blackhig.zhebushigudu.lover.mixin.mixins.AccessorGuiShulkerBox;
import dev.blackhig.zhebushigudu.lover.util.BlockUtil;
import dev.blackhig.zhebushigudu.lover.util.Enums;
import dev.blackhig.zhebushigudu.lover.util.Timer;
import net.minecraft.client.gui.inventory.GuiShulkerBox;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Objects;

public class AntiShulkerBoxPlus extends Module
{
    Setting<Integer> range;
    Setting<takeType> type;
    Setting<Integer> delay;
    Setting<Enums.breakMode> breakMode;
    Timer takeTimer;
    int l_I;
    boolean first;
    BlockPos pos;
    
    public AntiShulkerBoxPlus() {
        super("AntiShulk+", "ex,steal enemy's box item", Category.PLAYER, true, false, false);
        this.range = this.register(new Setting<>("Range", 5, 0, 6));
        this.type = this.register(new Setting<>("TakeType", takeType.dropper));
        this.delay = this.register(new Setting<>("Delay", 0, 0, 1000));
        this.breakMode = this.register(new Setting<>("BoxBreakMode", Enums.breakMode.LoverMine));
        this.takeTimer = new Timer();
        this.l_I = 0;
        this.first = false;
        this.pos = null;
    }
    
    @Override
    public void onTick() {
        if (lover.moduleManager.getModuleByClass(ReplenishBox.class).isEnabled()) {
            return;
        }
        if (this.pos == null) {
            this.pos = BlockUtil.getSphere(AntiShulkerBoxPlus.mc.player.getPosition(), this.range.getValue(), 5, false, true, 0).stream().filter(poss -> ReplenishBox.shulkers.contains(AntiShulkerBoxPlus.mc.world.getBlockState(poss).getBlock())).findFirst().orElse(null);
            this.first = true;
            this.takeTimer.reset();
            this.l_I = 0;
        }
        else if (!(AntiShulkerBoxPlus.mc.currentScreen instanceof GuiShulkerBox) && this.first) {
            this.first = false;
            final Vec3d hitVec = new Vec3d(this.pos).add(0.5, 0.5, 0.5).add(new Vec3d(EnumFacing.UP.getDirectionVec()).scale(0.5));
            dev.blackhig.zhebushigudu.lover.util.e.Util.rightClickBlock(this.pos, hitVec, EnumHand.MAIN_HAND, EnumFacing.UP, true);
        }
        else {
            if (!(AntiShulkerBoxPlus.mc.currentScreen instanceof GuiShulkerBox)) {
                this.pos = null;
                return;
            }
            if (this.delay.getValue() != 0 && !this.takeTimer.passedMs(this.delay.getValue())) {
                return;
            }
            this.takeTimer.reset();
            final GuiShulkerBox l_Chest = (GuiShulkerBox)AntiShulkerBoxPlus.mc.currentScreen;
            final IInventory inventory = ((AccessorGuiShulkerBox)l_Chest).getInventory();
            if (this.l_I < Objects.requireNonNull(inventory).getSizeInventory()) {
                final ItemStack stack = inventory.getStackInSlot(this.l_I);
                if (stack.isEmpty) {
                    ++this.l_I;
                }
                switch (this.type.getValue()) {
                    case dropper: {
                        AntiShulkerBoxPlus.mc.playerController.windowClick(l_Chest.inventorySlots.windowId, this.l_I, -999, ClickType.THROW, AntiShulkerBoxPlus.mc.player);
                        break;
                    }
                    case stealer: {
                        AntiShulkerBoxPlus.mc.playerController.windowClick(l_Chest.inventorySlots.windowId, this.l_I, 0, ClickType.QUICK_MOVE, AntiShulkerBoxPlus.mc.player);
                        break;
                    }
                }
                ++this.l_I;
            }
            else {
                l_Chest.onGuiClosed();
                AntiShulkerBoxPlus.mc.currentScreen.onGuiClosed();
                AntiShulkerBoxPlus.mc.player.closeScreen();
                if (Objects.requireNonNull(this.breakMode.getValue()) == Enums.breakMode.LoverMine) {
                    final InstantMine Instance = lover.moduleManager.getModuleByClass(InstantMine.class);
                    if (Instance.isOff()) {
                        Instance.enable();
                        return;
                    }
                    if (!Instance.isOn()) {
                        return;
                    }
                    if (InstantMine.breakPos != null && InstantMine.breakPos.equals((Object) this.pos)) {
                        return;
                    }
                    InstantMine.ondeve(this.pos);
                    InstantMine.ondeve(this.pos);
                    AutoCity.mc.playerController.onPlayerDamageBlock(this.pos, BlockUtil.getRayTraceFacing(this.pos));
                    AutoCity.mc.playerController.onPlayerDamageBlock(this.pos, BlockUtil.getRayTraceFacing(this.pos));
                    AutoCityPlus.mc.playerController.onPlayerDamageBlock(this.pos, BlockUtil.getRayTraceFacing(this.pos));
                    AutoCityPlus.mc.playerController.onPlayerDamageBlock(this.pos, BlockUtil.getRayTraceFacing(this.pos));
                }
                this.first = false;
                this.pos = null;
                this.l_I = 0;
            }
        }
    }
    
    enum takeType
    {
        dropper, 
        stealer;
    }
}
