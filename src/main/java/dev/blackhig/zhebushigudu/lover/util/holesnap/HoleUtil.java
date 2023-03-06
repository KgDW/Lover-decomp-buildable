package dev.blackhig.zhebushigudu.lover.util.holesnap;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.Block;
import java.util.Iterator;
import net.minecraft.util.math.Vec3i;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import java.util.List;

public class HoleUtil
{
    public static final List<BlockPos> holeBlocks;
    public static Minecraft mc;
    
    public static boolean isInHole(final EntityPlayer entityPlayer) {
        return isHole(new BlockPos(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ));
    }
    
    public static boolean isVoidHole(final BlockPos blockPos) {
        return (HoleUtil.mc.player.dimension == -1) ? ((blockPos.getY() == 0 || blockPos.getY() == 127) && BlockUtil.getBlockResistance(blockPos) == BlockUtil.BlockResistance.Blank) : (blockPos.getY() == 0 && BlockUtil.getBlockResistance(blockPos) == BlockUtil.BlockResistance.Blank);
    }
    
    public static boolean isDoubleBedrockHoleX(final BlockPos blockPos) {
        if (!HoleUtil.mc.world.getBlockState(blockPos).getBlock().equals(Blocks.AIR) || !HoleUtil.mc.world.getBlockState(blockPos.add(1, 0, 0)).getBlock().equals(Blocks.AIR) || (!HoleUtil.mc.world.getBlockState(blockPos.add(0, 1, 0)).getBlock().equals(Blocks.AIR) && !HoleUtil.mc.world.getBlockState(blockPos.add(1, 1, 0)).getBlock().equals(Blocks.AIR)) || (!HoleUtil.mc.world.getBlockState(blockPos.add(0, 2, 0)).getBlock().equals(Blocks.AIR) && !HoleUtil.mc.world.getBlockState(blockPos.add(1, 2, 0)).getBlock().equals(Blocks.AIR))) {
            return false;
        }
        for (final BlockPos blockPos2 : new BlockPos[] { blockPos.add(2, 0, 0), blockPos.add(1, 0, 1), blockPos.add(1, 0, -1), blockPos.add(-1, 0, 0), blockPos.add(0, 0, 1), blockPos.add(0, 0, -1), blockPos.add(0, -1, 0), blockPos.add(1, -1, 0) }) {
            final IBlockState iBlockState = HoleUtil.mc.world.getBlockState(blockPos2);
            if (iBlockState.getBlock() == Blocks.AIR || iBlockState.getBlock() != Blocks.BEDROCK) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isDoubleBedrockHoleZ(final BlockPos blockPos) {
        if (!HoleUtil.mc.world.getBlockState(blockPos).getBlock().equals(Blocks.AIR) || !HoleUtil.mc.world.getBlockState(blockPos.add(0, 0, 1)).getBlock().equals(Blocks.AIR) || (!HoleUtil.mc.world.getBlockState(blockPos.add(0, 1, 0)).getBlock().equals(Blocks.AIR) && !HoleUtil.mc.world.getBlockState(blockPos.add(0, 1, 1)).getBlock().equals(Blocks.AIR)) || (!HoleUtil.mc.world.getBlockState(blockPos.add(0, 2, 0)).getBlock().equals(Blocks.AIR) && !HoleUtil.mc.world.getBlockState(blockPos.add(0, 2, 1)).getBlock().equals(Blocks.AIR))) {
            return false;
        }
        for (final BlockPos blockPos2 : new BlockPos[] { blockPos.add(0, 0, 2), blockPos.add(1, 0, 1), blockPos.add(-1, 0, 1), blockPos.add(0, 0, -1), blockPos.add(1, 0, 0), blockPos.add(-1, 0, 0), blockPos.add(0, -1, 0), blockPos.add(0, -1, 1) }) {
            final IBlockState iBlockState = HoleUtil.mc.world.getBlockState(blockPos2);
            if (iBlockState.getBlock() == Blocks.AIR || iBlockState.getBlock() != Blocks.BEDROCK) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isDoubleObsidianHoleX(final BlockPos blockPos) {
        if (!HoleUtil.mc.world.getBlockState(blockPos).getBlock().equals(Blocks.AIR) || !HoleUtil.mc.world.getBlockState(blockPos.add(1, 0, 0)).getBlock().equals(Blocks.AIR) || (!HoleUtil.mc.world.getBlockState(blockPos.add(0, 1, 0)).getBlock().equals(Blocks.AIR) && !HoleUtil.mc.world.getBlockState(blockPos.add(1, 1, 0)).getBlock().equals(Blocks.AIR)) || (!HoleUtil.mc.world.getBlockState(blockPos.add(0, 2, 0)).getBlock().equals(Blocks.AIR) && !HoleUtil.mc.world.getBlockState(blockPos.add(1, 2, 0)).getBlock().equals(Blocks.AIR))) {
            return false;
        }
        for (final BlockPos blockPos2 : new BlockPos[] { blockPos.add(2, 0, 0), blockPos.add(1, 0, 1), blockPos.add(1, 0, -1), blockPos.add(-1, 0, 0), blockPos.add(0, 0, 1), blockPos.add(0, 0, -1), blockPos.add(0, -1, 0), blockPos.add(1, -1, 0) }) {
            final IBlockState iBlockState = HoleUtil.mc.world.getBlockState(blockPos2);
            if (iBlockState.getBlock() == Blocks.AIR || iBlockState.getBlock() != Blocks.OBSIDIAN) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isDoubleObsidianHoleZ(final BlockPos blockPos) {
        if (!HoleUtil.mc.world.getBlockState(blockPos).getBlock().equals(Blocks.AIR) || !HoleUtil.mc.world.getBlockState(blockPos.add(0, 0, 1)).getBlock().equals(Blocks.AIR) || (!HoleUtil.mc.world.getBlockState(blockPos.add(0, 1, 0)).getBlock().equals(Blocks.AIR) && !HoleUtil.mc.world.getBlockState(blockPos.add(0, 1, 1)).getBlock().equals(Blocks.AIR)) || (!HoleUtil.mc.world.getBlockState(blockPos.add(0, 2, 0)).getBlock().equals(Blocks.AIR) && !HoleUtil.mc.world.getBlockState(blockPos.add(0, 2, 1)).getBlock().equals(Blocks.AIR))) {
            return false;
        }
        for (final BlockPos blockPos2 : new BlockPos[] { blockPos.add(0, 0, 2), blockPos.add(1, 0, 1), blockPos.add(-1, 0, 1), blockPos.add(0, 0, -1), blockPos.add(1, 0, 0), blockPos.add(-1, 0, 0), blockPos.add(0, -1, 0), blockPos.add(0, -1, 1) }) {
            final IBlockState iBlockState = HoleUtil.mc.world.getBlockState(blockPos2);
            if (iBlockState.getBlock() == Blocks.AIR || iBlockState.getBlock() != Blocks.OBSIDIAN) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isObsidianHole(final BlockPos blockPos) {
        return BlockUtil.getBlockResistance(blockPos.add(0, 1, 0)) == BlockUtil.BlockResistance.Blank && !isBedRockHole(blockPos) && BlockUtil.getBlockResistance(blockPos.add(0, 0, 0)) == BlockUtil.BlockResistance.Blank && BlockUtil.getBlockResistance(blockPos.add(0, 2, 0)) == BlockUtil.BlockResistance.Blank && (BlockUtil.getBlockResistance(blockPos.add(0, 0, -1)) == BlockUtil.BlockResistance.Resistant || BlockUtil.getBlockResistance(blockPos.add(0, 0, -1)) == BlockUtil.BlockResistance.Unbreakable) && (BlockUtil.getBlockResistance(blockPos.add(1, 0, 0)) == BlockUtil.BlockResistance.Resistant || BlockUtil.getBlockResistance(blockPos.add(1, 0, 0)) == BlockUtil.BlockResistance.Unbreakable) && (BlockUtil.getBlockResistance(blockPos.add(-1, 0, 0)) == BlockUtil.BlockResistance.Resistant || BlockUtil.getBlockResistance(blockPos.add(-1, 0, 0)) == BlockUtil.BlockResistance.Unbreakable) && (BlockUtil.getBlockResistance(blockPos.add(0, 0, 1)) == BlockUtil.BlockResistance.Resistant || BlockUtil.getBlockResistance(blockPos.add(0, 0, 1)) == BlockUtil.BlockResistance.Unbreakable) && BlockUtil.getBlockResistance(blockPos.add(0.5, 0.5, 0.5)) == BlockUtil.BlockResistance.Blank && (BlockUtil.getBlockResistance(blockPos.add(0, -1, 0)) == BlockUtil.BlockResistance.Resistant || BlockUtil.getBlockResistance(blockPos.add(0, -1, 0)) == BlockUtil.BlockResistance.Unbreakable);
    }
    
    public static boolean isBedRockHole(final BlockPos blockPos) {
        return BlockUtil.getBlockResistance(blockPos.add(0, 1, 0)) == BlockUtil.BlockResistance.Blank && BlockUtil.getBlockResistance(blockPos.add(0, 0, 0)) == BlockUtil.BlockResistance.Blank && BlockUtil.getBlockResistance(blockPos.add(0, 2, 0)) == BlockUtil.BlockResistance.Blank && BlockUtil.getBlockResistance(blockPos.add(0, 0, -1)) == BlockUtil.BlockResistance.Unbreakable && BlockUtil.getBlockResistance(blockPos.add(1, 0, 0)) == BlockUtil.BlockResistance.Unbreakable && BlockUtil.getBlockResistance(blockPos.add(-1, 0, 0)) == BlockUtil.BlockResistance.Unbreakable && BlockUtil.getBlockResistance(blockPos.add(0, 0, 1)) == BlockUtil.BlockResistance.Unbreakable && BlockUtil.getBlockResistance(blockPos.add(0.5, 0.5, 0.5)) == BlockUtil.BlockResistance.Blank && BlockUtil.getBlockResistance(blockPos.add(0, -1, 0)) == BlockUtil.BlockResistance.Unbreakable;
    }
    
    public static boolean isHole(final BlockPos blockPos) {
        return BlockUtil.getBlockResistance(blockPos.add(0, 1, 0)) == BlockUtil.BlockResistance.Blank && BlockUtil.getBlockResistance(blockPos.add(0, 0, 0)) == BlockUtil.BlockResistance.Blank && BlockUtil.getBlockResistance(blockPos.add(0, 2, 0)) == BlockUtil.BlockResistance.Blank && (BlockUtil.getBlockResistance(blockPos.add(0, 0, -1)) == BlockUtil.BlockResistance.Resistant || BlockUtil.getBlockResistance(blockPos.add(0, 0, -1)) == BlockUtil.BlockResistance.Unbreakable) && (BlockUtil.getBlockResistance(blockPos.add(1, 0, 0)) == BlockUtil.BlockResistance.Resistant || BlockUtil.getBlockResistance(blockPos.add(1, 0, 0)) == BlockUtil.BlockResistance.Unbreakable) && (BlockUtil.getBlockResistance(blockPos.add(-1, 0, 0)) == BlockUtil.BlockResistance.Resistant || BlockUtil.getBlockResistance(blockPos.add(-1, 0, 0)) == BlockUtil.BlockResistance.Unbreakable) && (BlockUtil.getBlockResistance(blockPos.add(0, 0, 1)) == BlockUtil.BlockResistance.Resistant || BlockUtil.getBlockResistance(blockPos.add(0, 0, 1)) == BlockUtil.BlockResistance.Unbreakable) && BlockUtil.getBlockResistance(blockPos.add(0.5, 0.5, 0.5)) == BlockUtil.BlockResistance.Blank && (BlockUtil.getBlockResistance(blockPos.add(0, -1, 0)) == BlockUtil.BlockResistance.Resistant || BlockUtil.getBlockResistance(blockPos.add(0, -1, 0)) == BlockUtil.BlockResistance.Unbreakable);
    }
    
    public static boolean is2HoleB(final BlockPos pos) {
        return is2Hole(pos) != null;
    }
    
    public static BlockPos is2Hole(final BlockPos pos) {
        if (isHole(pos)) {
            return null;
        }
        BlockPos blockpos2 = null;
        int size = 0;
        int size2 = 0;
        if (HoleUtil.mc.world.getBlockState(pos).getBlock() != Blocks.AIR) {
            return null;
        }
        for (final BlockPos bPos : HoleUtil.holeBlocks) {
            if (HoleUtil.mc.world.getBlockState(pos.add((Vec3i)bPos)).getBlock() == Blocks.AIR) {
                if (pos.add((Vec3i)bPos).equals((Object)new BlockPos(bPos.getX(), bPos.getY() - 1, bPos.getZ()))) {
                    continue;
                }
                blockpos2 = pos.add((Vec3i)bPos);
                ++size;
            }
        }
        if (size == 1) {
            for (final BlockPos bPoss : HoleUtil.holeBlocks) {
                if (HoleUtil.mc.world.getBlockState(pos.add((Vec3i)bPoss)).getBlock() != Blocks.BEDROCK && HoleUtil.mc.world.getBlockState(pos.add((Vec3i)bPoss)).getBlock() != Blocks.OBSIDIAN) {
                    continue;
                }
                ++size2;
            }
            for (final BlockPos bPoss : HoleUtil.holeBlocks) {
                if (HoleUtil.mc.world.getBlockState(blockpos2.add((Vec3i)bPoss)).getBlock() != Blocks.BEDROCK && HoleUtil.mc.world.getBlockState(blockpos2.add((Vec3i)bPoss)).getBlock() != Blocks.OBSIDIAN) {
                    continue;
                }
                ++size2;
            }
        }
        if (size2 == 8) {
            return blockpos2;
        }
        return null;
    }
    
    public static BlockPos getLocalPlayerPosFloored() {
        return new BlockPos(Math.floor(HoleUtil.mc.player.posX), Math.floor(HoleUtil.mc.player.posY), Math.floor(HoleUtil.mc.player.posZ));
    }
    
    public static boolean isPlayerInHole() {
        final BlockPos blockPos = getLocalPlayerPosFloored();
        final IBlockState blockState = HoleUtil.mc.world.getBlockState(blockPos);
        if (blockState.getBlock() != Blocks.AIR) {
            return false;
        }
        if (HoleUtil.mc.world.getBlockState(blockPos.up()).getBlock() != Blocks.AIR) {
            return false;
        }
        if (HoleUtil.mc.world.getBlockState(blockPos.down()).getBlock() == Blocks.AIR) {
            return false;
        }
        final BlockPos[] touchingBlocks = { blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west() };
        int validHorizontalBlocks = 0;
        for (final BlockPos touching : touchingBlocks) {
            final IBlockState touchingState = HoleUtil.mc.world.getBlockState(touching);
            if (touchingState.getBlock() != Blocks.AIR) {
                if (touchingState.isFullBlock()) {
                    ++validHorizontalBlocks;
                }
            }
        }
        return validHorizontalBlocks >= 4;
    }
    
    public static BlockSafety isBlockSafe(final Block block) {
        if (block == Blocks.BEDROCK) {
            return BlockSafety.UNBREAKABLE;
        }
        if (block == Blocks.OBSIDIAN || block == Blocks.ENDER_CHEST || block == Blocks.ANVIL) {
            return BlockSafety.RESISTANT;
        }
        return BlockSafety.BREAKABLE;
    }
    
    public static HoleInfo isHole(final BlockPos centreBlock, final boolean onlyOneWide, final boolean ignoreDown) {
        final HoleInfo output = new HoleInfo();
        final HashMap<BlockOffset, BlockSafety> unsafeSides = getUnsafeSides(centreBlock);
        if (unsafeSides.containsKey(BlockOffset.DOWN) && unsafeSides.remove(BlockOffset.DOWN, BlockSafety.BREAKABLE) && !ignoreDown) {
            output.setSafety(BlockSafety.BREAKABLE);
            return output;
        }
        int size = unsafeSides.size();
        unsafeSides.entrySet().removeIf(entry -> entry.getValue() == BlockSafety.RESISTANT);
        if (unsafeSides.size() != size) {
            output.setSafety(BlockSafety.RESISTANT);
        }
        if ((size = unsafeSides.size()) == 0) {
            output.setType(HoleType.SINGLE);
            output.setCentre(new AxisAlignedBB(centreBlock));
            return output;
        }
        if (size == 1 && !onlyOneWide) {
            return isDoubleHole(output, centreBlock, unsafeSides.keySet().stream().findFirst().get());
        }
        output.setSafety(BlockSafety.BREAKABLE);
        return output;
    }
    
    private static HoleInfo isDoubleHole(final HoleInfo info, final BlockPos centreBlock, final BlockOffset weakSide) {
        final BlockPos unsafePos = weakSide.offset(centreBlock);
        final HashMap<BlockOffset, BlockSafety> unsafeSides = getUnsafeSides(unsafePos);
        final int size = unsafeSides.size();
        unsafeSides.entrySet().removeIf(entry -> entry.getValue() == BlockSafety.RESISTANT);
        if (unsafeSides.size() != size) {
            info.setSafety(BlockSafety.RESISTANT);
        }
        if (unsafeSides.containsKey(BlockOffset.DOWN)) {
            info.setType(HoleType.CUSTOM);
            unsafeSides.remove(BlockOffset.DOWN);
        }
        if (unsafeSides.size() > 1) {
            info.setType(HoleType.NONE);
            return info;
        }
        final double minX = Math.min(centreBlock.getX(), unsafePos.getX());
        final double maxX = Math.max(centreBlock.getX(), unsafePos.getX()) + 1;
        final double minZ = Math.min(centreBlock.getZ(), unsafePos.getZ());
        final double maxZ = Math.max(centreBlock.getZ(), unsafePos.getZ()) + 1;
        info.setCentre(new AxisAlignedBB(minX, (double)centreBlock.getY(), minZ, maxX, (double)(centreBlock.getY() + 1), maxZ));
        if (info.getType() != HoleType.CUSTOM) {
            info.setType(HoleType.DOUBLE);
        }
        return info;
    }
    
    public static HashMap<BlockOffset, BlockSafety> getUnsafeSides(final BlockPos pos) {
        final HashMap<BlockOffset, BlockSafety> output = new HashMap<BlockOffset, BlockSafety>();
        BlockSafety temp = isBlockSafe(HoleUtil.mc.world.getBlockState(BlockOffset.DOWN.offset(pos)).getBlock());
        if (temp != BlockSafety.UNBREAKABLE) {
            output.put(BlockOffset.DOWN, temp);
        }
        if ((temp = isBlockSafe(HoleUtil.mc.world.getBlockState(BlockOffset.NORTH.offset(pos)).getBlock())) != BlockSafety.UNBREAKABLE) {
            output.put(BlockOffset.NORTH, temp);
        }
        if ((temp = isBlockSafe(HoleUtil.mc.world.getBlockState(BlockOffset.SOUTH.offset(pos)).getBlock())) != BlockSafety.UNBREAKABLE) {
            output.put(BlockOffset.SOUTH, temp);
        }
        if ((temp = isBlockSafe(HoleUtil.mc.world.getBlockState(BlockOffset.EAST.offset(pos)).getBlock())) != BlockSafety.UNBREAKABLE) {
            output.put(BlockOffset.EAST, temp);
        }
        if ((temp = isBlockSafe(HoleUtil.mc.world.getBlockState(BlockOffset.WEST.offset(pos)).getBlock())) != BlockSafety.UNBREAKABLE) {
            output.put(BlockOffset.WEST, temp);
        }
        return output;
    }
    
    static {
        holeBlocks = Arrays.asList(new BlockPos(0, -1, 0), new BlockPos(0, 0, -1), new BlockPos(-1, 0, 0), new BlockPos(1, 0, 0), new BlockPos(0, 0, 1));
        HoleUtil.mc = Minecraft.getMinecraft();
    }
    
    public static class HoleInfo
    {
        private HoleType type;
        private BlockSafety safety;
        private AxisAlignedBB centre;
        
        public HoleInfo() {
            this(BlockSafety.UNBREAKABLE, HoleType.NONE);
        }
        
        public HoleInfo(final BlockSafety safety, final HoleType type) {
            this.type = type;
            this.safety = safety;
        }
        
        public HoleType getType() {
            return this.type;
        }
        
        public void setType(final HoleType type) {
            this.type = type;
        }
        
        public BlockSafety getSafety() {
            return this.safety;
        }
        
        public void setSafety(final BlockSafety safety) {
            this.safety = safety;
        }
        
        public AxisAlignedBB getCentre() {
            return this.centre;
        }
        
        public void setCentre(final AxisAlignedBB centre) {
            this.centre = centre;
        }
    }
    
    public static class Hole
    {
        public Type type;
        public Facing facing;
        public BlockPos hole;
        public BlockPos offset;
        
        public Hole(final Type type, final Facing facing, final BlockPos hole, final BlockPos offset) {
            this.type = type;
            this.facing = facing;
            this.hole = hole;
            this.offset = offset;
        }
        
        public Hole(final Type type, final Facing facing, final BlockPos hole) {
            this.type = type;
            this.facing = facing;
            this.hole = hole;
        }
        
        public Facing opposite() {
            if (this.facing == Facing.West) {
                return Facing.East;
            }
            if (this.facing == Facing.East) {
                return Facing.West;
            }
            if (this.facing == Facing.North) {
                return Facing.South;
            }
            if (this.facing == Facing.South) {
                return Facing.North;
            }
            return Facing.None;
        }
        
        public enum Type
        {
            Obsidian, 
            Bedrock, 
            Double;
        }
        
        public enum Facing
        {
            West, 
            South, 
            North, 
            East, 
            None;
        }
    }
    
    public enum BlockOffset
    {
        DOWN(0, -1, 0), 
        UP(0, 1, 0), 
        NORTH(0, 0, -1), 
        EAST(1, 0, 0), 
        SOUTH(0, 0, 1), 
        WEST(-1, 0, 0);
        
        private final int x;
        private final int y;
        private final int z;
        
        private BlockOffset(final int x, final int y, final int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
        
        public BlockPos offset(final BlockPos pos) {
            return pos.add(this.x, this.y, this.z);
        }
        
        public BlockPos forward(final BlockPos pos, final int scale) {
            return pos.add(this.x * scale, 0, this.z * scale);
        }
        
        public BlockPos backward(final BlockPos pos, final int scale) {
            return pos.add(-this.x * scale, 0, -this.z * scale);
        }
        
        public BlockPos left(final BlockPos pos, final int scale) {
            return pos.add(this.z * scale, 0, -this.x * scale);
        }
        
        public BlockPos right(final BlockPos pos, final int scale) {
            return pos.add(-this.z * scale, 0, this.x * scale);
        }
    }
    
    public enum HoleType
    {
        SINGLE, 
        DOUBLE, 
        CUSTOM, 
        NONE;
    }
    
    public enum BlockSafety
    {
        UNBREAKABLE, 
        RESISTANT, 
        BREAKABLE;
    }
}
