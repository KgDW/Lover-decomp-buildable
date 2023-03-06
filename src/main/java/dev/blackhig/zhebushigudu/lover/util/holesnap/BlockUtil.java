package dev.blackhig.zhebushigudu.lover.util.holesnap;

import java.util.Arrays;
import net.minecraft.entity.EntityLivingBase;
import dev.blackhig.zhebushigudu.lover.event.events.a.UpdateWalkingPlayerEventTwo;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.math.MathHelper;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.entity.Entity;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockDeadBush;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockTallGrass;
import java.util.Iterator;
import java.util.ArrayList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockAir;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.Block;
import java.util.List;
import net.minecraft.client.Minecraft;

public class BlockUtil
{
    private static final Minecraft mc;
    public static List blackList;
    public static List shulkerList;
    public static List<Block> emptyBlocks;
    public static List<Block> rightclickableBlocks;
    
    public static boolean isReallyOnGround() {
        final EntityPlayerSP entity = Minecraft.getMinecraft().player;
        final double y = entity.getEntityBoundingBox().offset(0.0, -0.01, 0.0).minY;
        final Block block = Minecraft.getMinecraft().world.getBlockState(new BlockPos(entity.posX, y, entity.posZ)).getBlock();
        return block != null && !(block instanceof BlockAir) && !(block instanceof BlockLiquid) && entity.onGround;
    }
    
    public static Vec3d[] getHelpingBlocks(final Vec3d vec3d) {
        return new Vec3d[] { new Vec3d(vec3d.x, vec3d.y - 1.0, vec3d.z), new Vec3d((vec3d.x != 0.0) ? (vec3d.x * 2.0) : vec3d.x, vec3d.y, (vec3d.x != 0.0) ? vec3d.z : (vec3d.z * 2.0)), new Vec3d((vec3d.x == 0.0) ? (vec3d.x + 1.0) : vec3d.x, vec3d.y, (vec3d.x == 0.0) ? vec3d.z : (vec3d.z + 1.0)), new Vec3d((vec3d.x == 0.0) ? (vec3d.x - 1.0) : vec3d.x, vec3d.y, (vec3d.x == 0.0) ? vec3d.z : (vec3d.z - 1.0)), new Vec3d(vec3d.x, vec3d.y + 1.0, vec3d.z) };
    }
    
    public static boolean canBlockBeSeen(final double x, final double y, final double z) {
        return BlockUtil.mc.world.rayTraceBlocks(new Vec3d(BlockUtil.mc.player.posX, BlockUtil.mc.player.posY + BlockUtil.mc.player.getEyeHeight(), BlockUtil.mc.player.posZ), new Vec3d(x, y + 1.7, z), false, true, false) == null;
    }
    
    public static EnumFacing getRayTraceFacing(final BlockPos pos) {
        final RayTraceResult result = BlockUtil.mc.world.rayTraceBlocks(new Vec3d(BlockUtil.mc.player.posX, BlockUtil.mc.player.posY + BlockUtil.mc.player.getEyeHeight(), BlockUtil.mc.player.posZ), new Vec3d(pos.getX() + 0.5, pos.getX() - 0.5, pos.getX() + 0.5));
        if (result == null || result.sideHit == null) {
            return EnumFacing.UP;
        }
        return result.sideHit;
    }
    
    public static void placeCrystalOnBlock(final BlockPos pos, final EnumHand hand, final boolean swing) {
        final RayTraceResult result = BlockUtil.mc.world.rayTraceBlocks(new Vec3d(BlockUtil.mc.player.posX, BlockUtil.mc.player.posY + BlockUtil.mc.player.getEyeHeight(), BlockUtil.mc.player.posZ), new Vec3d(pos.getX() + 0.5, pos.getY() - 0.5, pos.getZ() + 0.5));
        final EnumFacing facing = (result == null || result.sideHit == null) ? EnumFacing.UP : result.sideHit;
        BlockUtil.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(pos, facing, hand, 0.0f, 0.0f, 0.0f));
        if (swing) {
            BlockUtil.mc.player.connection.sendPacket((Packet)new CPacketAnimation(hand));
        }
    }
    
    public static BlockPos[] toBlockPos(final Vec3d[] vec3ds) {
        final BlockPos[] list = new BlockPos[vec3ds.length];
        for (int i = 0; i < vec3ds.length; ++i) {
            list[i] = new BlockPos(vec3ds[i]);
        }
        return list;
    }
    
    public static EnumFacing getFacing(final BlockPos pos) {
        for (final EnumFacing facing : EnumFacing.values()) {
            final RayTraceResult rayTraceResult = BlockUtil.mc.world.rayTraceBlocks(new Vec3d(BlockUtil.mc.player.posX, BlockUtil.mc.player.posY + BlockUtil.mc.player.getEyeHeight(), BlockUtil.mc.player.posZ), new Vec3d(pos.getX() + 0.5 + facing.getDirectionVec().getX() * 1.0 / 2.0, pos.getY() + 0.5 + facing.getDirectionVec().getY() * 1.0 / 2.0, pos.getZ() + 0.5 + facing.getDirectionVec().getZ() * 1.0 / 2.0), false, true, false);
            if (rayTraceResult == null || (rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK && rayTraceResult.getBlockPos().equals((Object)pos))) {
                return facing;
            }
        }
        if (pos.getY() > BlockUtil.mc.player.posY + BlockUtil.mc.player.getEyeHeight()) {
            return EnumFacing.DOWN;
        }
        return EnumFacing.UP;
    }
    
    public static boolean isElseHole(final BlockPos blockPos) {
        for (final BlockPos pos : getTouchingBlocks(blockPos)) {
            final IBlockState touchingState = BlockUtil.mc.world.getBlockState(pos);
            if (touchingState.getBlock() == Blocks.AIR || !touchingState.isFullBlock()) {
                return false;
            }
        }
        return true;
    }
    
    public static BlockPos[] getTouchingBlocks(final BlockPos blockPos) {
        return new BlockPos[] { blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down() };
    }
    
    public static List<EnumFacing> getPossibleSides(final BlockPos pos) {
        final ArrayList<EnumFacing> facings = new ArrayList<EnumFacing>();
        if (BlockUtil.mc.world == null || pos == null) {
            return facings;
        }
        for (final EnumFacing side : EnumFacing.values()) {
            final BlockPos neighbour = pos.offset(side);
            final IBlockState blockState = BlockUtil.mc.world.getBlockState(neighbour);
            if (blockState != null && blockState.getBlock().canCollideCheck(blockState, false)) {
                if (!blockState.getMaterial().isReplaceable()) {
                    facings.add(side);
                }
            }
        }
        return facings;
    }
    
    public static EnumFacing getFirstFacing(final BlockPos pos) {
        final Iterator<EnumFacing> iterator = getPossibleSides(pos).iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        }
        return null;
    }
    
    public static int isPositionPlaceable(final BlockPos pos, final boolean rayTrace) {
        return isPositionPlaceable(pos, rayTrace, true);
    }
    
    public static int isPositionPlaceable(final BlockPos pos, final boolean rayTrace, final boolean entityCheck) {
        final Block block = BlockUtil.mc.world.getBlockState(pos).getBlock();
        if (!(block instanceof BlockAir) && !(block instanceof BlockLiquid) && !(block instanceof BlockTallGrass) && !(block instanceof BlockFire) && !(block instanceof BlockDeadBush) && !(block instanceof BlockSnow)) {
            return 0;
        }
        if (!rayTracePlaceCheck(pos, rayTrace, 0.0f)) {
            return -1;
        }
        if (entityCheck) {
            for (final Object entity : BlockUtil.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(pos))) {
                if (!(entity instanceof EntityItem)) {
                    if (entity instanceof EntityXPOrb) {
                        continue;
                    }
                    return 1;
                }
            }
        }
        for (final EnumFacing side : getPossibleSides(pos)) {
            if (!canBeClicked(pos.offset(side))) {
                continue;
            }
            return 3;
        }
        return 2;
    }
    
    public static Vec3d[] convertVec3ds(final Vec3d vec3d, final Vec3d[] input) {
        final Vec3d[] output = new Vec3d[input.length];
        for (int i = 0; i < input.length; ++i) {
            output[i] = vec3d.add(input[i]);
        }
        return output;
    }
    
    public static Vec3d[] convertVec3ds(final EntityPlayer entity, final Vec3d[] input) {
        return convertVec3ds(entity.getPositionVector(), input);
    }
    
    public static void rightClickBlock(final BlockPos pos, final Vec3d vec, final EnumHand hand, final EnumFacing direction, final boolean packet) {
        if (packet) {
            BlockUtil.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(pos, direction, hand, 0.5f, 1.0f, 0.5f));
        }
        else {
            BlockUtil.mc.playerController.processRightClickBlock(BlockUtil.mc.player, BlockUtil.mc.world, pos, direction, vec, hand);
        }
        BlockUtil.mc.player.connection.sendPacket((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
        BlockUtil.mc.rightClickDelayTimer = 4;
    }
    
    public static boolean canBreak(final BlockPos pos, final boolean air) {
        final IBlockState blockState = BlockUtil.mc.world.getBlockState(pos);
        final Block block = blockState.getBlock();
        if (BlockUtil.mc.world.getBlockState(pos).getBlock() == Blocks.AIR) {
            return air;
        }
        return BlockUtil.mc.world.getBlockState(pos).getBlock() != Blocks.BEDROCK && BlockUtil.mc.world.getBlockState(pos).getBlock() != Blocks.END_PORTAL_FRAME && BlockUtil.mc.world.getBlockState(pos).getBlock() != Blocks.END_PORTAL && BlockUtil.mc.world.getBlockState(pos).getBlock() != Blocks.WATER && BlockUtil.mc.world.getBlockState(pos).getBlock() != Blocks.FLOWING_WATER && BlockUtil.mc.world.getBlockState(pos).getBlock() != Blocks.LAVA && BlockUtil.mc.world.getBlockState(pos).getBlock() != Blocks.FLOWING_LAVA && block.getBlockHardness(blockState, (World)BlockUtil.mc.world, pos) != -1.0f;
    }
    
    public static EnumFacing getPlaceableSide(final BlockPos pos) {
        for (final EnumFacing side : EnumFacing.values()) {
            final BlockPos neighbour = pos.offset(side);
            final IBlockState blockState;
            if (BlockUtil.mc.world.getBlockState(neighbour).getBlock().canCollideCheck(BlockUtil.mc.world.getBlockState(neighbour), false) && !(blockState = BlockUtil.mc.world.getBlockState(neighbour)).getMaterial().isReplaceable()) {
                return side;
            }
        }
        return null;
    }
    
    public static void faceVectorPacketInstant(final Vec3d vec) {
        final float[] rotations = getNeededRotations2(vec);
        BlockUtil.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(rotations[0], rotations[1], BlockUtil.mc.player.onGround));
    }
    
    private static float[] getNeededRotations2(final Vec3d vec) {
        final Vec3d eyesPos = getEyesPos();
        final double diffX = vec.x - eyesPos.x;
        final double diffY = vec.y - eyesPos.y;
        final double diffZ = vec.z - eyesPos.z;
        final double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        final float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[] { BlockUtil.mc.player.rotationYaw + MathHelper.wrapDegrees(yaw - BlockUtil.mc.player.rotationYaw), BlockUtil.mc.player.rotationPitch + MathHelper.wrapDegrees(pitch - BlockUtil.mc.player.rotationPitch) };
    }
    
    public static Vec3d getEyesPos() {
        return new Vec3d(BlockUtil.mc.player.posX, BlockUtil.mc.player.posY + BlockUtil.mc.player.getEyeHeight(), BlockUtil.mc.player.posZ);
    }
    
    public static boolean canBeClicked(final BlockPos pos) {
        return getBlock(pos).canCollideCheck(getState(pos), false);
    }
    
    public static IBlockState getState(final BlockPos pos) {
        return BlockUtil.mc.world.getBlockState(pos);
    }
    
    public static Block getBlock(final BlockPos pos) {
        return getState(pos).getBlock();
    }
    
    public static int getDirection4D() {
        return MathHelper.floor(BlockUtil.mc.player.rotationYaw * 4.0f / 360.0f + 0.5) & 0x3;
    }
    
    public static String getDirection4D(final boolean northRed) {
        final int dirnumber = getDirection4D();
        if (dirnumber == 0) {
            return "South (+Z)";
        }
        if (dirnumber == 1) {
            return "West (-X)";
        }
        if (dirnumber == 2) {
            return (northRed ? "??c" : "") + "North (-Z)";
        }
        if (dirnumber == 3) {
            return "East (+X)";
        }
        return "Loading...";
    }
    
    public static Boolean isPosInFov(final BlockPos pos) {
        final int dirnumber = getDirection4D();
        if (dirnumber == 0 && pos.getZ() - BlockUtil.mc.player.getPositionVector().z < 0.0) {
            return false;
        }
        if (dirnumber == 1 && pos.getX() - BlockUtil.mc.player.getPositionVector().x > 0.0) {
            return false;
        }
        if (dirnumber == 2 && pos.getZ() - BlockUtil.mc.player.getPositionVector().z > 0.0) {
            return false;
        }
        return dirnumber != 3 || pos.getX() - BlockUtil.mc.player.getPositionVector().x >= 0.0;
    }
    
    public static boolean canPlaceCrystal(final BlockPos blockPos) {
        final BlockPos boost = blockPos.add(0, 1, 0);
        final BlockPos boost2 = blockPos.add(0, 2, 0);
        try {
            return (BlockUtil.mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK || BlockUtil.mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN) && BlockUtil.mc.world.getBlockState(boost).getBlock() == Blocks.AIR && BlockUtil.mc.world.getBlockState(boost2).getBlock() == Blocks.AIR && BlockUtil.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost)).isEmpty() && BlockUtil.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost2)).isEmpty();
        }
        catch (final Exception e) {
            return false;
        }
    }
    
    public static boolean canPlaceCrystal(final BlockPos blockPos, final boolean specialEntityCheck, final boolean oneDot15) {
        final BlockPos boost = blockPos.add(0, 1, 0);
        final BlockPos boost2 = blockPos.add(0, 2, 0);
        try {
            if (BlockUtil.mc.world.getBlockState(blockPos).getBlock() != Blocks.BEDROCK && BlockUtil.mc.world.getBlockState(blockPos).getBlock() != Blocks.OBSIDIAN) {
                return false;
            }
            if ((BlockUtil.mc.world.getBlockState(boost).getBlock() != Blocks.AIR || BlockUtil.mc.world.getBlockState(boost2).getBlock() != Blocks.AIR) && !oneDot15) {
                return false;
            }
            if (!specialEntityCheck) {
                return BlockUtil.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost)).isEmpty() && (oneDot15 || BlockUtil.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost2)).isEmpty());
            }
            for (final Object entity : BlockUtil.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost))) {
                if (entity instanceof EntityEnderCrystal) {
                    continue;
                }
                return false;
            }
            if (!oneDot15) {
                for (final Object entity : BlockUtil.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost2))) {
                    if (entity instanceof EntityEnderCrystal) {
                        continue;
                    }
                    return false;
                }
            }
        }
        catch (final Exception ignored) {
            return false;
        }
        return true;
    }
    
    public static List<BlockPos> getSphere(final BlockPos pos, final float r, final int h, final boolean hollow, final boolean sphere, final int plus_y) {
        final ArrayList<BlockPos> circleblocks = new ArrayList<BlockPos>();
        final int cx = pos.getX();
        final int cy = pos.getY();
        final int cz = pos.getZ();
        for (int x = cx - (int)r; x <= cx + r; ++x) {
            int z = cz - (int)r;
            if (z <= cz + r) {
                int y = sphere ? (cy - (int)r) : cy;
                while (true) {
                    final float f = (float)y;
                    final float f2 = sphere ? (cy + r) : ((float)(cy + h));
                    if (f < f2) {
                        final double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? ((cy - y) * (cy - y)) : 0);
                        if (dist < r * r && (!hollow || dist >= (r - 1.0f) * (r - 1.0f))) {
                            final BlockPos l = new BlockPos(x, y + plus_y, z);
                            circleblocks.add(l);
                        }
                        ++y;
                    }
                    ++z;
                }
            }
            else {}
        }
        return circleblocks;
    }
    
    public static List<BlockPos> getSphere(final float radius) {
        return getSphere(getPosition(), radius);
    }
    
    public static BlockPos getPosition() {
        return getPosition((Entity)BlockUtil.mc.player);
    }
    
    public static BlockPos getPosition(final Entity entity) {
        return new BlockPos(entity.posX, entity.posY, entity.posZ);
    }
    
    public static List<BlockPos> getSphere(final BlockPos pos, final float radius) {
        final ArrayList<BlockPos> sphere = new ArrayList<BlockPos>();
        final int posX = pos.getX();
        final int posY = pos.getY();
        final int posZ = pos.getZ();
        for (int x = posX - (int)radius; x <= posX + radius; ++x) {
            for (int z = posZ - (int)radius; z <= posZ + radius; ++z) {
                for (int y = posY - (int)radius; y < posY + radius; ++y) {
                    final double dist = (posX - x) * (posX - x) + (posZ - z) * (posZ - z) + (posY - y) * (posY - y);
                    if (dist < radius * radius) {
                        final BlockPos position = new BlockPos(x, y, z);
                        sphere.add(position);
                    }
                }
            }
        }
        return sphere;
    }
    
    public static void placeCrystalOnBlock(final BlockPos pos, final EnumHand hand, final boolean swing, final boolean exactHand) {
        final RayTraceResult result = BlockUtil.mc.world.rayTraceBlocks(new Vec3d(BlockUtil.mc.player.posX, BlockUtil.mc.player.posY + BlockUtil.mc.player.getEyeHeight(), BlockUtil.mc.player.posZ), new Vec3d(pos.getX() + 0.5, pos.getY() - 0.5, pos.getZ() + 0.5));
        final EnumFacing facing = (result == null || result.sideHit == null) ? EnumFacing.UP : result.sideHit;
        BlockUtil.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(pos, facing, hand, 0.5f, 0.5f, 0.5f));
        if (swing) {
            BlockUtil.mc.player.connection.sendPacket((Packet)new CPacketAnimation(exactHand ? hand : EnumHand.MAIN_HAND));
        }
    }
    
    public static void placeCrystalOnBlock(final BlockPos pos, final EnumHand hand) {
        final RayTraceResult result = BlockUtil.mc.world.rayTraceBlocks(new Vec3d(BlockUtil.mc.player.posX, BlockUtil.mc.player.posY + BlockUtil.mc.player.getEyeHeight(), BlockUtil.mc.player.posZ), new Vec3d(pos.getX() + 0.5, pos.getY() - 0.5, pos.getZ() + 0.5));
        final EnumFacing facing = (result == null || result.sideHit == null) ? EnumFacing.UP : result.sideHit;
        BlockUtil.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(pos, facing, hand, 0.0f, 0.0f, 0.0f));
    }
    
    public static boolean rayTracePlaceCheck(final BlockPos pos, final boolean shouldCheck, final float height) {
        return !shouldCheck || BlockUtil.mc.world.rayTraceBlocks(new Vec3d(BlockUtil.mc.player.posX, BlockUtil.mc.player.posY + BlockUtil.mc.player.getEyeHeight(), BlockUtil.mc.player.posZ), new Vec3d((double)pos.getX(), (double)(pos.getY() + height), (double)pos.getZ()), false, true, false) == null;
    }
    
    public static boolean rayTracePlaceCheck(final BlockPos pos, final boolean shouldCheck) {
        return rayTracePlaceCheck(pos, shouldCheck, 1.0f);
    }
    
    public static void openBlock(final BlockPos pos) {
        final EnumFacing[] values;
        final EnumFacing[] facings = values = EnumFacing.values();
        for (final EnumFacing f : values) {
            final Block neighborBlock = BlockUtil.mc.world.getBlockState(pos.offset(f)).getBlock();
            if (BlockUtil.emptyBlocks.contains(neighborBlock)) {
                BlockUtil.mc.playerController.processRightClickBlock(BlockUtil.mc.player, BlockUtil.mc.world, pos, f.getOpposite(), new Vec3d((Vec3i)pos), EnumHand.MAIN_HAND);
                return;
            }
        }
    }
    
    public static void placeBlock(final BlockPos pos, final EnumHand hand, final boolean rotate, final boolean packet) {
        final EnumFacing side = getFirstFacing(pos);
        if (side == null) {
            return;
        }
        final BlockPos neighbour = pos.offset(side);
        final EnumFacing opposite = side.getOpposite();
        final Vec3d hitVec = new Vec3d((Vec3i)neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        final Block neighbourBlock = BlockUtil.mc.world.getBlockState(neighbour).getBlock();
        if (!BlockUtil.mc.player.isSneaking() && (BlockUtil.blackList.contains(neighbourBlock) || BlockUtil.shulkerList.contains(neighbourBlock))) {
            BlockUtil.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)BlockUtil.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            BlockUtil.mc.player.setSneaking(true);
        }
        if (rotate && UpdateWalkingPlayerEventTwo.INSTANCE != null) {
            faceVectorPacketInstant(hitVec);
        }
        rightClickBlock(neighbour, hitVec, hand, opposite, packet);
        BlockUtil.mc.rightClickDelayTimer = 4;
    }
    
    public static boolean placeBlock(final BlockPos pos, final int slot, final boolean rotate, final boolean rotateBack) {
        if (isBlockEmpty(pos)) {
            int old_slot = -1;
            if (slot != BlockUtil.mc.player.inventory.currentItem) {
                old_slot = BlockUtil.mc.player.inventory.currentItem;
                BlockUtil.mc.player.inventory.currentItem = slot;
            }
            final EnumFacing[] values;
            final EnumFacing[] facings = values = EnumFacing.values();
            for (final EnumFacing f : values) {
                final Block neighborBlock = BlockUtil.mc.world.getBlockState(pos.offset(f)).getBlock();
                final Vec3d vec = new Vec3d(pos.getX() + 0.5 + f.getXOffset() * 0.5, pos.getY() + 0.5 + f.getYOffset() * 0.5, pos.getZ() + 0.5 + f.getZOffset() * 0.5);
                if (!BlockUtil.emptyBlocks.contains(neighborBlock) && BlockUtil.mc.player.getPositionEyes(BlockUtil.mc.getRenderPartialTicks()).distanceTo(vec) <= 4.25) {
                    final float[] rot = { BlockUtil.mc.player.rotationYaw, BlockUtil.mc.player.rotationPitch };
                    if (rotate) {
                        rotatePacket(vec.x, vec.y, vec.z);
                    }
                    if (BlockUtil.rightclickableBlocks.contains(neighborBlock)) {
                        BlockUtil.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)BlockUtil.mc.player, CPacketEntityAction.Action.START_SNEAKING));
                    }
                    BlockUtil.mc.playerController.processRightClickBlock(BlockUtil.mc.player, BlockUtil.mc.world, pos.offset(f), f.getOpposite(), new Vec3d((Vec3i)pos), EnumHand.MAIN_HAND);
                    if (BlockUtil.rightclickableBlocks.contains(neighborBlock)) {
                        BlockUtil.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)BlockUtil.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                    }
                    if (rotateBack) {
                        BlockUtil.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(rot[0], rot[1], BlockUtil.mc.player.onGround));
                    }
                    BlockUtil.mc.player.swingArm(EnumHand.OFF_HAND);
                    if (old_slot != -1) {
                        BlockUtil.mc.player.inventory.currentItem = old_slot;
                    }
                    return true;
                }
            }
        }
        return false;
    }
    
    public static boolean isBlockEmpty(final BlockPos pos) {
        try {
            if (BlockUtil.emptyBlocks.contains(BlockUtil.mc.world.getBlockState(pos).getBlock())) {
                final AxisAlignedBB box = new AxisAlignedBB(pos);
                final Iterator entityIter = BlockUtil.mc.world.loadedEntityList.iterator();
                while (entityIter.hasNext()) {
                    final Entity e;
                    if ((e = (Entity) entityIter.next()) instanceof EntityLivingBase && box.intersects(e.getEntityBoundingBox())) {
                        return false;
                    }
                }
                return true;
            }
        }
        catch (final Exception ex) {}
        return false;
    }
    
    public static boolean canPlaceBlock(final BlockPos pos) {
        if (isBlockEmpty(pos)) {
            final EnumFacing[] values;
            final EnumFacing[] facings = values = EnumFacing.values();
            for (final EnumFacing f : values) {
                if (!BlockUtil.emptyBlocks.contains(BlockUtil.mc.world.getBlockState(pos.offset(f)).getBlock())) {
                    final Vec3d vec3d = new Vec3d(pos.getX() + 0.5 + f.getXOffset() * 0.5, pos.getY() + 0.5 + f.getYOffset() * 0.5, pos.getZ() + 0.5 + f.getZOffset() * 0.5);
                    if (BlockUtil.mc.player.getPositionEyes(BlockUtil.mc.getRenderPartialTicks()).distanceTo(vec3d) <= 4.25) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public static void rotatePacket(final double x, final double y, final double z) {
        final double diffX = x - BlockUtil.mc.player.posX;
        final double diffY = y - (BlockUtil.mc.player.posY + BlockUtil.mc.player.getEyeHeight());
        final double diffZ = z - BlockUtil.mc.player.posZ;
        final double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        final float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        BlockUtil.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(yaw, pitch, BlockUtil.mc.player.onGround));
    }
    
    public static ValidResult valid(final BlockPos pos) {
        if (!BlockUtil.mc.world.checkNoEntityCollision(new AxisAlignedBB(pos))) {
            return ValidResult.NoEntityCollision;
        }
        if (!checkForNeighbours(pos)) {
            return ValidResult.NoNeighbors;
        }
        final IBlockState l_State = BlockUtil.mc.world.getBlockState(pos);
        if (l_State.getBlock() == Blocks.AIR) {
            final BlockPos[] array;
            final BlockPos[] l_Blocks = array = new BlockPos[] { pos.north(), pos.south(), pos.east(), pos.west(), pos.up(), pos.down() };
            for (final BlockPos l_Pos : array) {
                final IBlockState l_State2 = BlockUtil.mc.world.getBlockState(l_Pos);
                if (l_State2.getBlock() != Blocks.AIR) {
                    for (final EnumFacing side : EnumFacing.values()) {
                        final BlockPos neighbor = pos.offset(side);
                        if (BlockUtil.mc.world.getBlockState(neighbor).getBlock().canCollideCheck(BlockUtil.mc.world.getBlockState(neighbor), false)) {
                            return ValidResult.Ok;
                        }
                    }
                }
            }
            return ValidResult.NoNeighbors;
        }
        return ValidResult.AlreadyBlockThere;
    }
    
    public static boolean checkForNeighbours(final BlockPos blockPos) {
        if (!hasNeighbour(blockPos)) {
            for (final EnumFacing side : EnumFacing.values()) {
                final BlockPos neighbour = blockPos.offset(side);
                if (hasNeighbour(neighbour)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
    
    private static boolean hasNeighbour(final BlockPos blockPos) {
        for (final EnumFacing side : EnumFacing.values()) {
            final BlockPos neighbour = blockPos.offset(side);
            if (!BlockUtil.mc.world.getBlockState(neighbour).getMaterial().isReplaceable()) {
                return true;
            }
        }
        return false;
    }
    
    public static BlockResistance getBlockResistance(final BlockPos block) {
        if (BlockUtil.mc.world.isAirBlock(block)) {
            return BlockResistance.Blank;
        }
        if (BlockUtil.mc.world.getBlockState(block).getBlock().getBlockHardness(BlockUtil.mc.world.getBlockState(block), (World)BlockUtil.mc.world, block) != -1.0f && !BlockUtil.mc.world.getBlockState(block).getBlock().equals(Blocks.OBSIDIAN) && !BlockUtil.mc.world.getBlockState(block).getBlock().equals(Blocks.ANVIL) && !BlockUtil.mc.world.getBlockState(block).getBlock().equals(Blocks.ENCHANTING_TABLE) && !BlockUtil.mc.world.getBlockState(block).getBlock().equals(Blocks.ENDER_CHEST)) {
            return BlockResistance.Breakable;
        }
        if (BlockUtil.mc.world.getBlockState(block).getBlock().equals(Blocks.OBSIDIAN) || BlockUtil.mc.world.getBlockState(block).getBlock().equals(Blocks.ANVIL) || BlockUtil.mc.world.getBlockState(block).getBlock().equals(Blocks.ENCHANTING_TABLE) || BlockUtil.mc.world.getBlockState(block).getBlock().equals(Blocks.ENDER_CHEST)) {
            return BlockResistance.Resistant;
        }
        if (BlockUtil.mc.world.getBlockState(block).getBlock().equals(Blocks.BEDROCK)) {
            return BlockResistance.Unbreakable;
        }
        return null;
    }
    
    static {
        mc = Minecraft.getMinecraft();
        BlockUtil.emptyBlocks = Arrays.asList(Blocks.AIR, (Block)Blocks.FLOWING_LAVA, (Block)Blocks.LAVA, (Block)Blocks.FLOWING_WATER, (Block)Blocks.WATER, Blocks.VINE, Blocks.SNOW_LAYER, (Block)Blocks.TALLGRASS, (Block)Blocks.FIRE);
        BlockUtil.rightclickableBlocks = Arrays.asList((Block)Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.ENDER_CHEST, Blocks.WHITE_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.SILVER_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.BLACK_SHULKER_BOX, Blocks.ANVIL, Blocks.WOODEN_BUTTON, Blocks.STONE_BUTTON, (Block)Blocks.UNPOWERED_COMPARATOR, (Block)Blocks.UNPOWERED_REPEATER, (Block)Blocks.POWERED_REPEATER, (Block)Blocks.POWERED_COMPARATOR, Blocks.OAK_FENCE_GATE, Blocks.SPRUCE_FENCE_GATE, Blocks.BIRCH_FENCE_GATE, Blocks.JUNGLE_FENCE_GATE, Blocks.DARK_OAK_FENCE_GATE, Blocks.ACACIA_FENCE_GATE, Blocks.BREWING_STAND, Blocks.DISPENSER, Blocks.DROPPER, Blocks.LEVER, Blocks.NOTEBLOCK, Blocks.JUKEBOX, (Block)Blocks.BEACON, Blocks.BED, Blocks.FURNACE, (Block)Blocks.OAK_DOOR, (Block)Blocks.SPRUCE_DOOR, (Block)Blocks.BIRCH_DOOR, (Block)Blocks.JUNGLE_DOOR, (Block)Blocks.ACACIA_DOOR, (Block)Blocks.DARK_OAK_DOOR, Blocks.CAKE, Blocks.ENCHANTING_TABLE, Blocks.DRAGON_EGG, (Block)Blocks.HOPPER, Blocks.REPEATING_COMMAND_BLOCK, Blocks.COMMAND_BLOCK, Blocks.CHAIN_COMMAND_BLOCK, Blocks.CRAFTING_TABLE);
        BlockUtil.blackList = Arrays.asList(Blocks.ENDER_CHEST, (Block)Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.CRAFTING_TABLE, Blocks.ANVIL, Blocks.BREWING_STAND, (Block)Blocks.HOPPER, Blocks.DROPPER, Blocks.DISPENSER);
        BlockUtil.shulkerList = Arrays.asList(Blocks.WHITE_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.SILVER_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.BLACK_SHULKER_BOX);
    }
    
    public enum ValidResult
    {
        NoEntityCollision, 
        AlreadyBlockThere, 
        NoNeighbors, 
        Ok;
    }
    
    public enum BlockResistance
    {
        Blank, 
        Breakable, 
        Resistant, 
        Unbreakable;
    }
}
