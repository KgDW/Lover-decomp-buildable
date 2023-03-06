package dev.blackhig.zhebushigudu.lover.features.modules.player;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.blackhig.zhebushigudu.lover.features.command.Command;
import dev.blackhig.zhebushigudu.lover.features.modules.Module;
import dev.blackhig.zhebushigudu.lover.features.modules.combat.Surround;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.lover;
import dev.blackhig.zhebushigudu.lover.util.BlockUtil;
import dev.blackhig.zhebushigudu.lover.util.EntityUtil;
import dev.blackhig.zhebushigudu.lover.util.InventoryUtil;
import dev.blackhig.zhebushigudu.lover.util.SeijaUtil.SeijaBlockUtil;
import dev.blackhig.zhebushigudu.lover.util.Util;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTool;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.List;
import java.util.Objects;

public class Burrow extends Module
{
    private boolean isSneaking;
    private final Setting<Boolean> tpcenter;
    private Setting<Boolean> rotate;
    private final Setting<Boolean> smartOffset;
    private final Setting<Double> offsetX;
    private final Setting<Double> offsetY;
    private final Setting<Double> offsetZ;
    private final Setting<Boolean> breakCrystal;
    private final Setting<Boolean> antiWk;
    private final Setting<Boolean> multiPlace;
    private final Setting<BlockMode> mode;
    
    public Burrow() {
        super("Burrow", "test burrow...", Category.PLAYER, true, false, false);
        this.multiPlace = this.register(new Setting<>("multiPlace", false));
        this.smartOffset = this.register(new Setting<>("smartOffset", true));
        this.tpcenter = this.register(new Setting<>("TPCenter", false));
        this.rotate = this.register(new Setting<>("Rotate", true));
        this.offsetX = this.register(new Setting<>("OffsetX", (-7.0), (-10.0), 10.0));
        this.offsetY = this.register(new Setting<>("OffsetY", (-7.0), (-10.0), 10.0));
        this.offsetZ = this.register(new Setting<>("OffsetZ", (-7.0), (-10.0), 10.0));
        this.breakCrystal = this.register(new Setting<>("BreakCrystal", true));
        this.antiWk = this.register(new Setting<>("AntiWeak", true, v -> this.breakCrystal.getValue()));
        this.mode = this.register(new Setting<>("BlockMode", BlockMode.Obsidian));
        this.isSneaking = false;
    }
    
    @Override
    public void onDisable() {
        this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
    }
    
    public static int getSlotByDmg(final Double minDmg) {
        for (int i = 0; i < 9; ++i) {
            if (Burrow.mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemTool) {
                final ItemTool currItemTool = (ItemTool)Burrow.mc.player.inventory.getStackInSlot(i).getItem();
                if (currItemTool.attackDamage >= minDmg) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    public void breakcrystal() {
        final AxisAlignedBB axisAlignedBB = new AxisAlignedBB(SeijaBlockUtil.getFlooredPosition(Burrow.mc.player));
        final List<Entity> l = Burrow.mc.world.getEntitiesWithinAABBExcludingEntity(null, axisAlignedBB);
        for (final Entity entity : l) {
            if (entity instanceof EntityEnderCrystal) {
                if (Burrow.mc.player.isPotionActive(Objects.requireNonNull(Potion.getPotionById(18))) && this.antiWk.getValue()) {
                    final int toolSlot = getSlotByDmg(4.0);
                    if (toolSlot != -1) {
                        final int oldSlot = Burrow.mc.player.inventory.currentItem;
                        InventoryUtil.switchToHotbarSlot(toolSlot, false);
                        InventoryUtil.switchToHotbarSlot(oldSlot, false);
                    }
                }
                Burrow.mc.player.connection.sendPacket(new CPacketUseEntity(entity));
                Burrow.mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.OFF_HAND));
            }
        }
    }
    
    @Override
    public void onTick() {
        this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
        if (this.breakCrystal.getValue()) {
            this.breakcrystal();
        }
        if (!Util.mc.world.isBlockLoaded(Util.mc.player.getPosition())) {
            return;
        }
        if (!Util.mc.player.onGround || Util.mc.world.getBlockState(new BlockPos(Util.mc.player.posX, Util.mc.player.posY + 2.0, Util.mc.player.posZ)).getBlock() != Blocks.AIR) {
            this.disable();
            return;
        }
        if (Util.mc.world.getBlockState(new BlockPos(Util.mc.player.posX, (double)Math.round(Util.mc.player.posY), Util.mc.player.posZ)).getBlock() != Blocks.AIR) {
            this.disable();
            return;
        }
        if (this.mode.getValue() == BlockMode.Obsidian && InventoryUtil.getItemHotbar(Item.getItemFromBlock(Blocks.OBSIDIAN)) == -1) {
            Command.sendMessage(ChatFormatting.RED + "Obsidian ?");
            this.disable();
            return;
        }
        if (this.mode.getValue() == BlockMode.Chest && InventoryUtil.getItemHotbar(Item.getItemFromBlock(Blocks.ENDER_CHEST)) == -1) {
            Command.sendMessage(ChatFormatting.RED + "Ender Chest ?");
            this.disable();
            return;
        }
        if (this.mode.getValue() == BlockMode.Smart && InventoryUtil.getItemHotbar(Item.getItemFromBlock(Blocks.OBSIDIAN)) == -1 && InventoryUtil.getItemHotbar(Item.getItemFromBlock(Blocks.ENDER_CHEST)) == -1) {
            Command.sendMessage(ChatFormatting.RED + "Obsidian/Ender Chest ?");
            this.disable();
            return;
        }
        if (this.tpcenter.getValue()) {
            final BlockPos startPos = EntityUtil.getRoundedBlockPos(Surround.mc.player);
            lover.positionManager.setPositionPacket(startPos.getX() + 0.5, startPos.getY(), startPos.getZ() + 0.5, true, true, true);
        }
        Util.mc.player.connection.sendPacket(new CPacketPlayer.Position(Math.floor(Burrow.mc.player.posX) + 0.5, Util.mc.player.posY + 0.419999986886978, Math.floor(Burrow.mc.player.posZ) + 0.5, false));
        Util.mc.player.connection.sendPacket(new CPacketPlayer.Position(Math.floor(Burrow.mc.player.posX) + 0.5, Util.mc.player.posY + 0.7531999805212015, Math.floor(Burrow.mc.player.posZ) + 0.5, false));
        Util.mc.player.connection.sendPacket(new CPacketPlayer.Position(Math.floor(Burrow.mc.player.posX) + 0.5, Util.mc.player.posY + 1.001335979112147, Math.floor(Burrow.mc.player.posZ) + 0.5, false));
        Util.mc.player.connection.sendPacket(new CPacketPlayer.Position(Math.floor(Burrow.mc.player.posX) + 0.5, Util.mc.player.posY + 1.166109260938214, Math.floor(Burrow.mc.player.posZ) + 0.5, false));
        final int a = Util.mc.player.inventory.currentItem;
        if (this.mode.getValue() == BlockMode.Obsidian) {
            InventoryUtil.switchToHotbarSlot(InventoryUtil.getItemHotbar(Item.getItemFromBlock(Blocks.OBSIDIAN)), false);
        }
        if (this.mode.getValue() == BlockMode.Chest) {
            InventoryUtil.switchToHotbarSlot(InventoryUtil.getItemHotbar(Item.getItemFromBlock(Blocks.ENDER_CHEST)), false);
        }
        if (this.mode.getValue() == BlockMode.Smart) {
            if (InventoryUtil.getItemHotbar(Item.getItemFromBlock(Blocks.ENDER_CHEST)) != -1) {
                InventoryUtil.switchToHotbarSlot(InventoryUtil.getItemHotbar(Item.getItemFromBlock(Blocks.ENDER_CHEST)), false);
            }
            else if (InventoryUtil.getItemHotbar(Item.getItemFromBlock(Blocks.OBSIDIAN)) != -1) {
                InventoryUtil.switchToHotbarSlot(InventoryUtil.getItemHotbar(Item.getItemFromBlock(Blocks.OBSIDIAN)), false);
            }
        }
        if (!this.multiPlace.getValue()) {
            this.isSneaking = BlockUtil.placeBlock(new BlockPos(getPlayerPosFixY(Burrow.mc.player)), EnumHand.MAIN_HAND, this.rotate.getValue(), true, this.isSneaking);
        }
        else {
            final Vec3d baseVec = new Vec3d(Burrow.mc.player.posX, Burrow.mc.player.posY, Burrow.mc.player.posZ);
            SeijaBlockUtil.placeBlock(baseVec.add(0.3, 0.0, 0.3), EnumHand.MAIN_HAND, false, true);
            SeijaBlockUtil.placeBlock(baseVec.add(-0.3, 0.0, 0.3), EnumHand.MAIN_HAND, false, true);
            SeijaBlockUtil.placeBlock(baseVec.add(0.3, 0.0, -0.3), EnumHand.MAIN_HAND, false, true);
            SeijaBlockUtil.placeBlock(baseVec.add(-0.3, 0.0, -0.3), EnumHand.MAIN_HAND, false, true);
        }
        Util.mc.playerController.updateController();
        Util.mc.player.connection.sendPacket(new CPacketHeldItemChange(a));
        Util.mc.player.inventory.currentItem = a;
        Util.mc.playerController.updateController();
        if (this.smartOffset.getValue()) {
            boolean defaultOffset = true;
            if (Burrow.mc.player.posY >= 3.0) {
                for (int i = -10; i < 10; ++i) {
                    if (i == -1) {
                        i = 3;
                    }
                    if (Burrow.mc.world.getBlockState(SeijaBlockUtil.getFlooredPosition(Burrow.mc.player).add(0, i, 0)).getBlock().equals(Blocks.AIR) && Burrow.mc.world.getBlockState(SeijaBlockUtil.getFlooredPosition(Burrow.mc.player).add(0, i + 1, 0)).getBlock().equals(Blocks.AIR)) {
                        final BlockPos pos = SeijaBlockUtil.getFlooredPosition(Burrow.mc.player).add(0, i, 0);
                        Util.mc.player.connection.sendPacket(new CPacketPlayer.Position(pos.getX() + 0.3, (double)pos.getY(), pos.getZ() + 0.3, true));
                        defaultOffset = false;
                        break;
                    }
                }
            }
            if (defaultOffset) {
                Util.mc.player.connection.sendPacket((Packet<net.minecraft.network.play.INetHandlerPlayServer>)new CPacketPlayer.Position(Util.mc.player.posX + this.offsetX.getValue(), Util.mc.player.posY + this.offsetY.getValue(), Util.mc.player.posZ + this.offsetZ.getValue(), true));
            }
        }
        else {
            Util.mc.player.connection.sendPacket((Packet<net.minecraft.network.play.INetHandlerPlayServer>)new CPacketPlayer.Position(Util.mc.player.posX + this.offsetX.getValue(), Util.mc.player.posY + this.offsetY.getValue(), Util.mc.player.posZ + this.offsetZ.getValue(), true));
        }
        this.disable();
    }
    
    public static BlockPos getPlayerPosFixY(final EntityPlayer player) {
        return new BlockPos(Math.floor(player.posX), (double)Math.round(player.posY), Math.floor(player.posZ));
    }
    
    enum BlockMode
    {
        Obsidian, 
        Chest, 
        Smart;
    }
}
