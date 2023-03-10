package dev.blackhig.zhebushigudu.lover.util;

import java.util.Arrays;
import net.minecraft.world.World;
import java.util.Iterator;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3i;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.Block;
import java.util.List;

public class WorldUtil implements Util
{
    public static List<Block> emptyBlocks;
    
    public static void placeBlock(final BlockPos pos) {
        for (final EnumFacing enumFacing : EnumFacing.values()) {
            if (!WorldUtil.mc.world.getBlockState(pos.offset(enumFacing)).getBlock().equals(Blocks.AIR) && !isIntercepted(pos)) {
                final Vec3d vec = new Vec3d(pos.getX() + 0.5 + enumFacing.getXOffset() * 0.5, pos.getY() + 0.5 + enumFacing.getYOffset() * 0.5, pos.getZ() + 0.5 + enumFacing.getZOffset() * 0.5);
                final float[] old = { WorldUtil.mc.player.rotationYaw, WorldUtil.mc.player.rotationPitch };
                WorldUtil.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation((float)Math.toDegrees(Math.atan2(vec.z - WorldUtil.mc.player.posZ, vec.x - WorldUtil.mc.player.posX)) - 90.0f, (float)(-Math.toDegrees(Math.atan2(vec.y - (WorldUtil.mc.player.posY + WorldUtil.mc.player.getEyeHeight()), Math.sqrt((vec.x - WorldUtil.mc.player.posX) * (vec.x - WorldUtil.mc.player.posX) + (vec.z - WorldUtil.mc.player.posZ) * (vec.z - WorldUtil.mc.player.posZ))))), WorldUtil.mc.player.onGround));
                WorldUtil.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)WorldUtil.mc.player, CPacketEntityAction.Action.START_SNEAKING));
                WorldUtil.mc.playerController.processRightClickBlock(WorldUtil.mc.player, WorldUtil.mc.world, pos.offset(enumFacing), enumFacing.getOpposite(), new Vec3d((Vec3i)pos), EnumHand.MAIN_HAND);
                WorldUtil.mc.player.swingArm(EnumHand.MAIN_HAND);
                WorldUtil.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)WorldUtil.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                WorldUtil.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(old[0], old[1], WorldUtil.mc.player.onGround));
                return;
            }
        }
    }
    
    public static void openBlock(final BlockPos pos) {
        final EnumFacing[] facings;
        final EnumFacing[] array;
        final EnumFacing[] values = array = (facings = EnumFacing.values());
        for (final EnumFacing f : array) {
            final Block neighborBlock = WorldUtil.mc.world.getBlockState(pos.offset(f)).getBlock();
            if (WorldUtil.emptyBlocks.contains(neighborBlock)) {
                WorldUtil.mc.playerController.processRightClickBlock(WorldUtil.mc.player, WorldUtil.mc.world, pos, f.getOpposite(), new Vec3d((Vec3i)pos), EnumHand.MAIN_HAND);
                return;
            }
        }
    }
    
    public static void placeBlock(final BlockPos pos, final int slot) {
        if (slot == -1) {
            return;
        }
        final int prev = WorldUtil.mc.player.inventory.currentItem;
        WorldUtil.mc.player.inventory.currentItem = slot;
        placeBlock(pos);
        WorldUtil.mc.player.inventory.currentItem = prev;
    }
    
    public static boolean isIntercepted(final BlockPos pos) {
        for (final Entity entity : WorldUtil.mc.world.loadedEntityList) {
            if (new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox())) {
                return true;
            }
        }
        return false;
    }
    
    public static BlockPos GetLocalPlayerPosFloored() {
        return new BlockPos(Math.floor(WorldUtil.mc.player.posX), Math.floor(WorldUtil.mc.player.posY), Math.floor(WorldUtil.mc.player.posZ));
    }
    
    public static boolean canBreak(final BlockPos pos) {
        return WorldUtil.mc.world.getBlockState(pos).getBlock().getBlockHardness(WorldUtil.mc.world.getBlockState(pos), (World)WorldUtil.mc.world, pos) != -1.0f;
    }
    
    static {
        WorldUtil.emptyBlocks = Arrays.asList(Blocks.AIR, (Block)Blocks.FLOWING_LAVA, (Block)Blocks.LAVA, (Block)Blocks.FLOWING_WATER, (Block)Blocks.WATER, Blocks.VINE, Blocks.SNOW_LAYER, (Block)Blocks.TALLGRASS, (Block)Blocks.FIRE);
    }
}
