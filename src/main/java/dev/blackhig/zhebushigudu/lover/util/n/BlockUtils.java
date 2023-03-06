package dev.blackhig.zhebushigudu.lover.util.n;

import net.minecraft.util.math.MathHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.BlockAir;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.entity.Entity;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.Block;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.Minecraft;

public class BlockUtils
{
    protected static Minecraft mc;
    public BlockPos pos;
    public int a;
    public EnumFacing f;
    public double dist;
    public double rotx;
    public double roty;
    
    public static boolean doPlace(final BlockUtils event, final boolean swing) {
        return event != null && event.doPlace(swing);
    }
    
    public static boolean doPlaceSilent(final BlockUtils event, final int slot, final boolean swing) {
        if (event != null) {
            final InventoryPlayer inventory = BlockUtils.mc.player.inventory;
            if (InventoryPlayer.isHotbar(slot)) {
                if (swing) {
                    BlockUtils.mc.player.swingArm(EnumHand.MAIN_HAND);
                }
                return event.silentPlace(slot) == EnumActionResult.SUCCESS;
            }
        }
        return false;
    }
    
    public static BlockUtils isPlaceable(final BlockPos pos, final double dist, final boolean Collide) {
        final BlockUtils event = new BlockUtils(pos, 0, null, dist);
        if (!isAir(pos)) {
            return null;
        }
        final AxisAlignedBB axisalignedbb = Block.FULL_BLOCK_AABB;
        if (!isAir(pos) && BlockUtils.mc.world.getBlockState(pos).getBlock() instanceof BlockLiquid) {
            final Block block = BlockUtils.mc.world.getBlockState(pos.offset(EnumFacing.UP)).getBlock();
            if (block instanceof BlockLiquid) {
                event.f = EnumFacing.DOWN;
                event.pos.offset(EnumFacing.UP);
            }
            else {
                event.f = EnumFacing.UP;
                event.pos.offset(EnumFacing.DOWN);
            }
            return event;
        }
        final EnumFacing[] values = EnumFacing.values();
        final int length = values.length;
        int i = 0;
        while (i < length) {
            final EnumFacing f = values[i];
            if (!isAir(new BlockPos(pos.getX() - f.getDirectionVec().getX(), pos.getY() - f.getDirectionVec().getY(), pos.getZ() - f.getDirectionVec().getZ()))) {
                event.f = f;
                if (Collide && axisalignedbb != Block.NULL_AABB && !BlockUtils.mc.world.checkNoEntityCollision(axisalignedbb.offset(pos), (Entity)null)) {
                    return null;
                }
                return event;
            }
            else {
                ++i;
            }
        }
        if (!isRePlaceable(pos)) {
            return null;
        }
        event.f = EnumFacing.UP;
        event.pos.offset(EnumFacing.UP);
        pos.offset(EnumFacing.DOWN);
        if (Collide && axisalignedbb != Block.NULL_AABB && !BlockUtils.mc.world.checkNoEntityCollision(axisalignedbb.offset(pos), (Entity)null)) {
            return null;
        }
        return event;
    }
    
    public static boolean isAir(final BlockPos pos) {
        final Block block = BlockUtils.mc.world.getBlockState(pos).getBlock();
        return block instanceof BlockAir;
    }
    
    public static boolean isRePlaceable(final BlockPos pos) {
        final Block block = BlockUtils.mc.world.getBlockState(pos).getBlock();
        return block.isReplaceable((IBlockAccess)BlockUtils.mc.world, pos) && !(block instanceof BlockAir);
    }
    
    public BlockUtils(final BlockPos pos, final int a, final EnumFacing f, final double dist) {
        this.pos = pos;
        this.a = a;
        this.f = f;
        this.dist = dist;
    }
    
    public boolean doPlace(final boolean swing) {
        final double dx = this.pos.getX() + 0.5 - BlockUtils.mc.player.posX - this.f.getDirectionVec().getX() / 2.0;
        final double dy = this.pos.getY() + 0.5 - BlockUtils.mc.player.posY - this.f.getDirectionVec().getY() / 2.0 - BlockUtils.mc.player.getEyeHeight();
        final double dz = this.pos.getZ() + 0.5 - BlockUtils.mc.player.posZ - this.f.getDirectionVec().getZ() / 2.0;
        final double x = getDirection2D(dz, dx);
        final double y = getDirection2D(dy, Math.sqrt(dx * dx + dz * dz));
        final Vec3d vec = this.getVectorForRotation(-y, x - 90.0);
        this.roty = -y;
        this.rotx = x - 90.0;
        final EnumActionResult enumactionresult = BlockUtils.mc.playerController.processRightClickBlock(BlockUtils.mc.player, BlockUtils.mc.world, this.pos.offset(this.f, -1), this.f, vec, EnumHand.MAIN_HAND);
        if (enumactionresult == EnumActionResult.SUCCESS) {
            if (swing) {
                BlockUtils.mc.player.swingArm(EnumHand.MAIN_HAND);
            }
            return true;
        }
        return false;
    }
    
    public static boolean doBreak(final BlockPos pos, final EnumFacing f) {
        return BlockUtils.mc.playerController.clickBlock(pos, f);
    }
    
    public void doBreak() {
        BlockUtils.mc.playerController.onPlayerDamageBlock(this.pos.offset(this.f, -1), this.f);
    }
    
    public EnumActionResult silentPlace(final int slot) {
        final InventoryPlayer inventory = BlockUtils.mc.player.inventory;
        if (!InventoryPlayer.isHotbar(slot)) {
            return EnumActionResult.FAIL;
        }
        final int s = BlockUtils.mc.player.inventory.currentItem;
        BlockUtils.mc.player.inventory.currentItem = slot;
        final ItemBlock block = (ItemBlock)((ItemStack)BlockUtils.mc.player.inventory.mainInventory.get(slot)).getItem();
        final IBlockState a = block.getBlock().getStateForPlacement((World)BlockUtils.mc.world, this.pos.offset(this.f, -1), this.f, 0.0f, 0.0f, 0.0f, 0, (EntityLivingBase)BlockUtils.mc.player);
        BlockUtils.mc.world.setBlockState(this.pos, a);
        BlockUtils.mc.getConnection().sendPacket((Packet)new CPacketHeldItemChange(slot));
        BlockUtils.mc.getConnection().sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(this.pos.offset(this.f, -1), this.f, EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
        BlockUtils.mc.player.inventory.currentItem = s;
        return EnumActionResult.SUCCESS;
    }
    
    protected final Vec3d getVectorForRotation(final float pitch, final float yaw) {
        final float f = MathHelper.cos(-yaw * 0.017453292f - 3.1415927f);
        final float f2 = MathHelper.sin(-yaw * 0.017453292f - 3.1415927f);
        final float f3 = -MathHelper.cos(-pitch * 0.017453292f);
        final float f4 = MathHelper.sin(-pitch * 0.017453292f);
        return new Vec3d((double)(f2 * f3), (double)f4, (double)(f * f3));
    }
    
    protected final Vec3d getVectorForRotation(final double pitch, final double yaw) {
        final float f = MathHelper.cos((float)(-yaw * 0.01745329238474369 - 3.1415927410125732));
        final float f2 = MathHelper.sin((float)(-yaw * 0.01745329238474369 - 3.1415927410125732));
        final float f3 = -MathHelper.cos((float)(-pitch * 0.01745329238474369));
        final float f4 = MathHelper.sin((float)(-pitch * 0.01745329238474369));
        return new Vec3d((double)(f2 * f3), (double)f4, (double)(f * f3));
    }
    
    public static double getDirection2D(final double dx, final double dy) {
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
    
    static {
        BlockUtils.mc = Minecraft.getMinecraft();
    }
}
