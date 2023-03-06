package dev.blackhig.zhebushigudu.lover.features.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.blackhig.zhebushigudu.lover.features.command.Command;
import dev.blackhig.zhebushigudu.lover.features.modules.Module;
import dev.blackhig.zhebushigudu.lover.features.modules.combat.AutoCity;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.lover;
import dev.blackhig.zhebushigudu.lover.mixin.mixins.AccessorGuiShulkerBox;
import dev.blackhig.zhebushigudu.lover.util.*;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiShulkerBox;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class ReplenishBox extends Module
{
    public static List<Block> shulkers;
    BlockPos blockpos;
    Setting<Mode> mode;
    Setting<Double> range;
    Setting<Boolean> place;
    Setting<Boolean> open;
    Setting<Boolean> take;
    Setting<Boolean> off;
    Setting<Enums.breakMode> breakMode;
    List<BlockPos> placeables;
    
    public ReplenishBox() {
        super("ReplenishBox", "Repbox", Category.MISC, true, false, false);
        this.mode = this.register(new Setting<>("Mode", Mode.stealer));
        this.range = this.register(new Setting<>("Range", 5.5, 0.0, 6.0));
        this.place = this.register(new Setting<>("Place", true));
        this.open = this.register(new Setting("Open", true, v -> this.place.getValue()));
        this.take = this.register(new Setting("Take", false, v -> this.place.getValue() && this.open.getValue()));
        this.off = this.register(new Setting("Close", false, v -> this.place.getValue() && this.open.getValue() && this.take.getValue()));
        this.breakMode = this.register(new Setting("BoxBreakMode", Enums.breakMode.LoverMine, v -> this.place.getValue() && this.open.getValue() && this.take.getValue() && this.off.getValue()));
        this.placeables = new ArrayList<>();
    }
    
    public int findShulk() {
        final AtomicInteger intt = new AtomicInteger(-1);
        ReplenishBox.shulkers.forEach(e -> {
            if (InventoryUtil.findHotbarBlock(e) != -1) {
                intt.set(InventoryUtil.findHotbarBlock(e));
            }
            return;
        });
        return intt.get();
    }
    
    private List<EntityPlayer> getTargets() {
        final ArrayList<EntityPlayer> targets = new ArrayList<EntityPlayer>();
        final double range = 9.0;
        for (final EntityPlayer player : ReplenishBox.mc.world.playerEntities) {
            if (!EntityUtil.isntValid(player, range)) {
                if (ReplenishBox.mc.player.getDistanceSq(player) >= range) {
                    continue;
                }
                targets.add(player);
            }
        }
        return targets;
    }
    
    @Override
    public void onEnable() {
        if (!this.place.getValue()) {
            this.disable();
            return;
        }
        this.placeables = BlockUtil.getSphere(ReplenishBox.mc.player.getPosition(), this.range.getValue().floatValue(), 4, false, true, 0);
        if (this.findShulk() == -1) {
            this.disable();
            Command.sendMessage(ChatFormatting.RED + "No Shulk Found");
            return;
        }
        InventoryUtil.switchToHotbarSlot(this.findShulk(), false);
        this.getTargets().forEach(e -> {
            final List<BlockPos> posList = BlockUtil.getSphere(EntityUtil.getPlayerPos(e), 5.0f, 5, false, true, 0);
            posList.forEach(c -> this.placeables.remove(c));
        });
        final ArrayList needRemove = new ArrayList();
        this.placeables.forEach(e -> {
            if (BlockUtils.isPlaceable(e, 0.0, true) == null || !ReplenishBox.mc.world.isAirBlock(e.up())) {
                needRemove.add(e);
            }
        });
        needRemove.forEach(x -> this.placeables.remove(x));
        BlockUtils bu;
        try {
            bu = BlockUtils.isPlaceable(this.placeables.get(0), 0.0, true);
        }
        catch (final IndexOutOfBoundsException x2) {
            this.disable();
            Command.sendMessage(ChatFormatting.RED + "No place to place shulk");
            return;
        }
        if (bu == null) {
            this.disable();
            Command.sendMessage(ChatFormatting.RED + "INVALID ERROR1");
            return;
        }
        Objects.requireNonNull(bu).doPlace(true);
        if (!this.open.getValue()) {
            this.disable();
            return;
        }
        this.blockpos = this.placeables.get(0);
        final Vec3d hitVec = new Vec3d(this.blockpos).add(0.5, 0.5, 0.5).add(new Vec3d(EnumFacing.UP.getDirectionVec()).scale(0.5));
        dev.blackhig.zhebushigudu.lover.util.Anti.Util.rightClickBlock(this.blockpos, hitVec, EnumHand.MAIN_HAND, EnumFacing.UP, true);
    }
    
    @Override
    public void onTick() {
        if (!(ReplenishBox.mc.currentScreen instanceof GuiShulkerBox)) {
            return;
        }
        if (!this.take.getValue()) {
            this.disable();
            return;
        }
        final GuiShulkerBox l_Chest = (GuiShulkerBox)ReplenishBox.mc.currentScreen;
        final IInventory inventory = ((AccessorGuiShulkerBox)l_Chest).getInventory();
        switch (this.mode.getValue()) {
            case dropper: {
                for (int l_I = 0; l_I < Objects.requireNonNull(inventory).getSizeInventory(); ++l_I) {
                    final ItemStack stack = inventory.getStackInSlot(l_I);
                    if (!stack.isEmpty) {
                        ReplenishBox.mc.playerController.windowClick(l_Chest.inventorySlots.windowId, l_I, -999, ClickType.THROW, ReplenishBox.mc.player);
                    }
                }
            }
            case stealer: {
                for (int l_I = 0; l_I < Objects.requireNonNull(inventory).getSizeInventory(); ++l_I) {
                    final ItemStack stack = inventory.getStackInSlot(l_I);
                    if (!stack.isEmpty) {
                        ReplenishBox.mc.playerController.windowClick(l_Chest.inventorySlots.windowId, l_I, 0, ClickType.QUICK_MOVE, ReplenishBox.mc.player);
                    }
                }
                break;
            }
        }
        if (!this.off.getValue()) {
            this.disable();
            return;
        }
        if (Objects.requireNonNull(this.breakMode.getValue()) == Enums.breakMode.LoverMine) {
            final InstantMine Instance2 = lover.moduleManager.getModuleByClass(InstantMine.class);
            if (Instance2.isOff()) {
                Instance2.enable();
                return;
            }
            if (!Instance2.isOn()) {
                return;
            }
            if (InstantMine.breakPos != null && InstantMine.breakPos.equals(this.blockpos)) {
                return;
            }
            InstantMine.ondeve(this.blockpos);
            InstantMine.ondeve(this.blockpos);
            AutoCity.mc.playerController.onPlayerDamageBlock(this.blockpos, BlockUtil.getRayTraceFacing(this.blockpos));
            AutoCity.mc.playerController.onPlayerDamageBlock(this.blockpos, BlockUtil.getRayTraceFacing(this.blockpos));
        }
        this.disable();
    }
    
    static {
        ReplenishBox.shulkers = Arrays.asList(Blocks.BLACK_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.SILVER_SHULKER_BOX, Blocks.WHITE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX);
    }
    
    enum Mode
    {
        stealer, 
        dropper
    }
}
