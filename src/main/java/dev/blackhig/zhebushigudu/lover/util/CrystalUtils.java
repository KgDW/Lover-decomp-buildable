package dev.blackhig.zhebushigudu.lover.util;

import javax.annotation.Nullable;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.EnumActionResult;
import net.minecraft.entity.Entity;
import net.minecraft.block.BlockObsidian;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.BlockEmptyDrops;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.client.Minecraft;

public class CrystalUtils
{
    protected static Minecraft mc;
    public static final AxisAlignedBB CRYSTAL_AABB;
    
    public static boolean canPlace(final BlockPos pos) {
        return CrystalUtils.mc.world.getBlockState(pos.offset(EnumFacing.DOWN)).getBlock() instanceof BlockEmptyDrops && CrystalUtils.mc.world.getBlockState(pos.offset(EnumFacing.DOWN)).getBlock() instanceof BlockObsidian && CrystalUtils.mc.world.checkNoEntityCollision(new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 2.0, 1.0).offset(pos), (Entity)null);
    }
    
    public static EnumActionResult doPlace(final BlockPos pos) {
        final double dx = pos.getX() + 0.5 - CrystalUtils.mc.player.posX;
        final double dy = pos.getY() - 1 + 0.5 - CrystalUtils.mc.player.posY - 0.5 - CrystalUtils.mc.player.getEyeHeight();
        final double dz = pos.getZ() + 0.5 - CrystalUtils.mc.player.posZ;
        final double x = getDirection2D(dz, dx);
        final double y = getDirection2D(dy, Math.sqrt(dx * dx + dz * dz));
        final Vec3d vec = getVectorForRotation(-y, x - 90.0);
        return CrystalUtils.mc.playerController.processRightClickBlock(CrystalUtils.mc.player, CrystalUtils.mc.world, pos.offset(EnumFacing.DOWN), EnumFacing.UP, vec, CrystalUtils.mc.player.getActiveHand());
    }
    
    protected static final double getDirection2D(final double dx, final double dy) {
        double d;
        if (dy == 0.0) {
            if (dx > 0.0) {
                d = 90.0;
            }
            else {
                d = -90.0;
            }
        }
        else {
            d = Math.atan(dx / dy) * 57.2957796;
            if (dy < 0.0) {
                if (dx > 0.0) {
                    d += 180.0;
                }
                else if (dx < 0.0) {
                    d -= 180.0;
                }
                else {
                    d = 180.0;
                }
            }
        }
        return d;
    }
    
    protected static final Vec3d getVectorForRotation(final double pitch, final double yaw) {
        final float f = MathHelper.cos((float)(-yaw * 0.01745329238474369 - 3.1415927410125732));
        final float f2 = MathHelper.sin((float)(-yaw * 0.01745329238474369 - 3.1415927410125732));
        final float f3 = -MathHelper.cos((float)(-pitch * 0.01745329238474369));
        final float f4 = MathHelper.sin((float)(-pitch * 0.01745329238474369));
        return new Vec3d((double)(f2 * f3), (double)f4, (double)(f * f3));
    }
    
    public static EnumActionResult placeCrystal(final BlockPos pos) {
        pos.offset(EnumFacing.DOWN);
        final double dx = pos.getX() + 0.5 - CrystalUtils.mc.player.posX;
        final double dy = pos.getY() + 0.5 - CrystalUtils.mc.player.posY - 0.5 - CrystalUtils.mc.player.getEyeHeight();
        final double dz = pos.getZ() + 0.5 - CrystalUtils.mc.player.posZ;
        final double x = getDirection2D(dz, dx);
        final double y = getDirection2D(dy, Math.sqrt(dx * dx + dz * dz));
        final Vec3d vec = getVectorForRotation(-y, x - 90.0);
        if (((ItemStack)CrystalUtils.mc.player.inventory.offHandInventory.get(0)).getItem().getClass().equals(Item.getItemById(426).getClass())) {
            return CrystalUtils.mc.playerController.processRightClickBlock(CrystalUtils.mc.player, CrystalUtils.mc.world, pos.offset(EnumFacing.DOWN), EnumFacing.UP, vec, EnumHand.OFF_HAND);
        }
        if (InventoryUtil.pickItem(426, false) != -1) {
            InventoryUtil.setSlot(InventoryUtil.pickItem(426, false));
            return CrystalUtils.mc.playerController.processRightClickBlock(CrystalUtils.mc.player, CrystalUtils.mc.world, pos.offset(EnumFacing.DOWN), EnumFacing.UP, vec, EnumHand.MAIN_HAND);
        }
        return EnumActionResult.FAIL;
    }
    
    public static boolean placeCrystalSilent(final BlockPos pos) {
        pos.offset(EnumFacing.DOWN);
        final double dx = pos.getX() + 0.5 - CrystalUtils.mc.player.posX;
        final double dy = pos.getY() + 0.5 - CrystalUtils.mc.player.posY - 0.5 - CrystalUtils.mc.player.getEyeHeight();
        final double dz = pos.getZ() + 0.5 - CrystalUtils.mc.player.posZ;
        final double x = getDirection2D(dz, dx);
        final double y = getDirection2D(dy, Math.sqrt(dx * dx + dz * dz));
        final int slot = InventoryUtil.pickItem(426, false);
        if (slot == -1 && ((ItemStack)CrystalUtils.mc.player.inventory.offHandInventory.get(0)).getItem() != Items.END_CRYSTAL) {
            return false;
        }
        final Vec3d vec = getVectorForRotation(-y, x - 90.0);
        if (((ItemStack)CrystalUtils.mc.player.inventory.offHandInventory.get(0)).getItem() == Items.END_CRYSTAL) {
            CrystalUtils.mc.getConnection().sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(pos.offset(EnumFacing.DOWN), EnumFacing.UP, EnumHand.OFF_HAND, 0.0f, 0.0f, 0.0f));
        }
        else if (InventoryUtil.pickItem(426, false) != -1) {
            CrystalUtils.mc.getConnection().sendPacket((Packet)new CPacketHeldItemChange(slot));
            CrystalUtils.mc.getConnection().sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(pos.offset(EnumFacing.DOWN), EnumFacing.UP, EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
        }
        return true;
    }
    
    public static double getDamage(final Vec3d pos, @Nullable final Entity target) {
        final Entity entity = (Entity)((target == null) ? CrystalUtils.mc.player : target);
        final float damage = 6.0f;
        final float f3 = damage * 2.0f;
        final Vec3d vec3d = pos;
        if (!entity.isImmuneToExplosions()) {
            final double d12 = entity.getDistance(pos.x, pos.y, pos.z) / f3;
            if (d12 <= 1.0) {
                double d13 = entity.posX - pos.x;
                double d14 = entity.posY + entity.getEyeHeight() - pos.y;
                double d15 = entity.posZ - pos.z;
                final double d16 = MathHelper.sqrt(d13 * d13 + d14 * d14 + d15 * d15);
                if (d16 != 0.0) {
                    d13 /= d16;
                    d14 /= d16;
                    d15 /= d16;
                    final double d17 = CrystalUtils.mc.world.getBlockDensity(pos, entity.getEntityBoundingBox());
                    final double d18 = (1.0 - d12) * d17;
                    return (float)(int)((d18 * d18 + d18) / 2.0 * 7.0 * f3 + 1.0);
                }
            }
        }
        return 0.0;
    }
    
    static {
        CrystalUtils.mc = Minecraft.getMinecraft();
        CRYSTAL_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 2.0, 1.0);
    }
}
