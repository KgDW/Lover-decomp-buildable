package dev.blackhig.zhebushigudu.lover.util.holesnap;

import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import net.minecraft.util.math.Vec3i;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.Minecraft;

public class SurroundUtils
{
    public static final SurroundUtils INSTANCE;
    private static Minecraft mc;
    private static final BlockPos[] surroundOffset;
    
    private SurroundUtils() {
    }
    
    public final Minecraft getMc() {
        return SurroundUtils.mc;
    }
    
    public final void setMc(final Minecraft minecraft) {
        SurroundUtils.mc = minecraft;
    }
    
    public final BlockPos getFlooredPosition(final Entity entity) {
        return new BlockPos((int)Math.floor(entity.posX), (int)Math.floor(entity.posY), (int)Math.floor(entity.posZ));
    }
    
    public final BlockPos[] getSurroundOffset() {
        return SurroundUtils.surroundOffset;
    }
    
    public final HoleType checkHole(final Entity entity) {
        return this.checkHole(this.getFlooredPosition(entity));
    }
    
    public final HoleType checkHole(final BlockPos pos) {
        if (!SurroundUtils.mc.world.isAirBlock(pos) || !SurroundUtils.mc.world.isAirBlock(pos.up()) || !SurroundUtils.mc.world.isAirBlock(pos.up().up())) {
            return HoleType.NONE;
        }
        HoleType type = HoleType.BEDROCK;
        for (final BlockPos offset : SurroundUtils.surroundOffset) {
            final Block block = SurroundUtils.mc.world.getBlockState(pos.add((Vec3i)offset)).getBlock();
            if (!this.checkBlock(block)) {
                type = HoleType.NONE;
                break;
            }
        }
        return type;
    }
    
    private boolean checkBlock(final Block block) {
        return block == Blocks.BEDROCK || block == Blocks.OBSIDIAN || block == Blocks.ENDER_CHEST || block == Blocks.ANVIL;
    }
    
    static {
        INSTANCE = new SurroundUtils();
        SurroundUtils.mc = Minecraft.getMinecraft();
        surroundOffset = new BlockPos[] { new BlockPos(0, -1, 0), new BlockPos(0, 0, -1), new BlockPos(1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(-1, 0, 0) };
    }
    
    public enum HoleType
    {
        NONE, 
        OBBY, 
        BEDROCK;
    }
}
