package dev.blackhig.zhebushigudu.lover.util.Anti;

import java.util.Arrays;
import net.minecraft.init.Blocks;
import dev.blackhig.zhebushigudu.lover.util.RotationUtil;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.math.Vec3i;
import java.util.Iterator;
import java.util.ArrayList;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.Minecraft;
import net.minecraft.block.Block;
import java.util.List;

public class BlockUtil
{
    public static final List<Block> blackList;
    public static final List<Block> shulkerList;
    public static List<Block> unSolidBlocks;
    static Minecraft mc;
    
    public static void rightClickBlock(final BlockPos blockPos, final Vec3d vec3d, final EnumHand enumHand, final EnumFacing enumFacing, final boolean bl) {
        if (bl) {
            final float f = (float)(vec3d.x - blockPos.getX());
            final float f2 = (float)(vec3d.y - blockPos.getY());
            final float f3 = (float)(vec3d.z - blockPos.getZ());
            BlockUtil.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(blockPos, enumFacing, enumHand, f, f2, f3));
        }
        else {
            BlockUtil.mc.playerController.processRightClickBlock(BlockUtil.mc.player, BlockUtil.mc.world, blockPos, enumFacing, vec3d, enumHand);
        }
        BlockUtil.mc.player.swingArm(EnumHand.MAIN_HAND);
        BlockUtil.mc.rightClickDelayTimer = 4;
    }
    
    public static boolean isBlockUnSolid(final BlockPos blockPos) {
        return isBlockUnSolid(BlockUtil.mc.world.getBlockState(blockPos).getBlock());
    }
    
    public static List<EnumFacing> getPossibleSides(final BlockPos blockPos) {
        final ArrayList<EnumFacing> arrayList = new ArrayList<EnumFacing>();
        for (final EnumFacing enumFacing : EnumFacing.values()) {
            final BlockPos blockPos2 = blockPos.offset(enumFacing);
            if (BlockUtil.mc.world.getBlockState(blockPos2).getBlock().canCollideCheck(BlockUtil.mc.world.getBlockState(blockPos2), false) && !BlockUtil.mc.world.getBlockState(blockPos2).getMaterial().isReplaceable()) {
                arrayList.add(enumFacing);
            }
        }
        return arrayList;
    }
    
    public static EnumFacing getFirstFacing(final BlockPos blockPos) {
        final Iterator<EnumFacing> iterator = getPossibleSides(blockPos).iterator();
        if (iterator.hasNext()) {
            final EnumFacing enumFacing = iterator.next();
            return enumFacing;
        }
        return null;
    }
    
    public static BlockPos[] toBlockPos(final Vec3d[] vec3dArray) {
        final BlockPos[] blockPosArray = new BlockPos[vec3dArray.length];
        for (int i = 0; i < vec3dArray.length; ++i) {
            blockPosArray[i] = new BlockPos(vec3dArray[i]);
        }
        return blockPosArray;
    }
    
    public static List<BlockPos> getSphere(final BlockPos blockPos, final float f, final int n, final boolean bl, final boolean bl2, final int n2) {
        final ArrayList<BlockPos> arrayList = new ArrayList<BlockPos>();
        final int n3 = blockPos.getX();
        final int n4 = blockPos.getY();
        final int n5 = blockPos.getZ();
        for (int n6 = n3 - (int)f; n6 <= n3 + f; ++n6) {
            for (int n7 = n5 - (int)f; n7 <= n5 + f; ++n7) {
                int n8 = bl2 ? (n4 - (int)f) : n4;
                while (true) {
                    final float f2 = (float)n8;
                    final float f3 = bl2 ? (n4 + f) : ((float)(n4 + n));
                    if (f2 >= f3) {
                        break;
                    }
                    final double d = (n3 - n6) * (n3 - n6) + (n5 - n7) * (n5 - n7) + (bl2 ? ((n4 - n8) * (n4 - n8)) : 0);
                    if (d < f * f && (!bl || d >= (f - 1.0f) * (f - 1.0f))) {
                        final BlockPos blockPos2 = new BlockPos(n6, n8 + n2, n7);
                        arrayList.add(blockPos2);
                    }
                    ++n8;
                }
            }
        }
        return arrayList;
    }
    
    public static boolean isBlockUnSolid(final Block block) {
        return BlockUtil.unSolidBlocks.contains(block);
    }
    
    public static boolean placeBlock(final BlockPos blockPos, final EnumHand enumHand, final boolean bl, final boolean bl2, final boolean bl3) {
        boolean n = false;
        final EnumFacing enumFacing = getFirstFacing(blockPos);
        if (enumFacing == null) {
            return bl3;
        }
        final BlockPos blockPos2 = blockPos.offset(enumFacing);
        final EnumFacing enumFacing2 = enumFacing.getOpposite();
        final Vec3d vec3d = new Vec3d((Vec3i)blockPos2).add(0.5, 0.5, 0.5).add(new Vec3d(enumFacing2.getDirectionVec()).scale(0.5));
        final Block block = BlockUtil.mc.world.getBlockState(blockPos2).getBlock();
        if (!BlockUtil.mc.player.isSneaking() && (BlockUtil.blackList.contains(block) || BlockUtil.shulkerList.contains(block))) {
            BlockUtil.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)BlockUtil.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            BlockUtil.mc.player.setSneaking(true);
            n = true;
        }
        if (bl) {
            RotationUtil.faceVector(vec3d, true);
        }
        rightClickBlock(blockPos2, vec3d, enumHand, enumFacing2, bl2);
        BlockUtil.mc.player.swingArm(EnumHand.MAIN_HAND);
        BlockUtil.mc.rightClickDelayTimer = 4;
        return n || bl3;
    }
    
    public static boolean isBlockSolid(final BlockPos blockPos) {
        return !isBlockUnSolid(blockPos);
    }
    
    static {
        BlockUtil.mc = Minecraft.getMinecraft();
        blackList = Arrays.asList(Blocks.ENDER_CHEST, (Block)Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.CRAFTING_TABLE, Blocks.ANVIL, Blocks.BREWING_STAND, (Block)Blocks.HOPPER, Blocks.DROPPER, Blocks.DISPENSER, Blocks.TRAPDOOR, Blocks.ENCHANTING_TABLE);
        shulkerList = Arrays.asList(Blocks.WHITE_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.SILVER_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.BLACK_SHULKER_BOX);
        BlockUtil.unSolidBlocks = Arrays.asList((Block)Blocks.FLOWING_LAVA, Blocks.FLOWER_POT, Blocks.SNOW, Blocks.CARPET, Blocks.END_ROD, (Block)Blocks.SKULL, Blocks.FLOWER_POT, Blocks.TRIPWIRE, (Block)Blocks.TRIPWIRE_HOOK, Blocks.WOODEN_BUTTON, Blocks.LEVER, Blocks.STONE_BUTTON, Blocks.LADDER, (Block)Blocks.UNPOWERED_COMPARATOR, (Block)Blocks.POWERED_COMPARATOR, (Block)Blocks.UNPOWERED_REPEATER, (Block)Blocks.POWERED_REPEATER, Blocks.UNLIT_REDSTONE_TORCH, Blocks.REDSTONE_TORCH, (Block)Blocks.REDSTONE_WIRE, Blocks.AIR, (Block)Blocks.PORTAL, Blocks.END_PORTAL, (Block)Blocks.WATER, (Block)Blocks.FLOWING_WATER, (Block)Blocks.LAVA, (Block)Blocks.FLOWING_LAVA, Blocks.SAPLING, (Block)Blocks.RED_FLOWER, (Block)Blocks.YELLOW_FLOWER, (Block)Blocks.BROWN_MUSHROOM, (Block)Blocks.RED_MUSHROOM, Blocks.WHEAT, Blocks.CARROTS, Blocks.POTATOES, Blocks.BEETROOTS, (Block)Blocks.REEDS, Blocks.PUMPKIN_STEM, Blocks.MELON_STEM, Blocks.WATERLILY, Blocks.NETHER_WART, Blocks.COCOA, Blocks.CHORUS_FLOWER, Blocks.CHORUS_PLANT, (Block)Blocks.TALLGRASS, (Block)Blocks.DEADBUSH, Blocks.VINE, (Block)Blocks.FIRE, Blocks.RAIL, Blocks.ACTIVATOR_RAIL, Blocks.DETECTOR_RAIL, Blocks.GOLDEN_RAIL, Blocks.TORCH);
    }
}
