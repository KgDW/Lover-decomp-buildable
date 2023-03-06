package dev.blackhig.zhebushigudu.lover.features.modules.movement;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.features.modules.Module;

public class ReverseStep extends Module
{
    private static ReverseStep INSTANCE;
    private final Setting<Boolean> twoBlocks;
    
    public ReverseStep() {
        super("ReverseStep", "ReverseStep.", Category.MOVEMENT, true, false, false);
        this.twoBlocks = this.register(new Setting<>("2Blocks", Boolean.FALSE));
        this.setInstance();
    }
    
    public static ReverseStep getInstance() {
        if (ReverseStep.INSTANCE == null) {
            ReverseStep.INSTANCE = new ReverseStep();
        }
        return ReverseStep.INSTANCE;
    }
    
    private void setInstance() {
        ReverseStep.INSTANCE = this;
    }
    
    @Override
    public void onUpdate() {
        if (fullNullCheck()) {
            return;
        }
        final IBlockState touchingState = ReverseStep.mc.world.getBlockState(new BlockPos(ReverseStep.mc.player.posX, ReverseStep.mc.player.posY, ReverseStep.mc.player.posZ).down(2));
        final IBlockState touchingState2 = ReverseStep.mc.world.getBlockState(new BlockPos(ReverseStep.mc.player.posX, ReverseStep.mc.player.posY, ReverseStep.mc.player.posZ).down(3));
        if (ReverseStep.mc.player.isInLava() || ReverseStep.mc.player.isInWater()) {
            return;
        }
        if (touchingState.getBlock() == Blocks.BEDROCK || touchingState.getBlock() == Blocks.OBSIDIAN) {
            if (ReverseStep.mc.player.onGround) {
                final EntityPlayerSP player = ReverseStep.mc.player;
                --player.motionY;
            }
        }
        else if (((this.twoBlocks.getValue() && touchingState2.getBlock() == Blocks.BEDROCK) || (this.twoBlocks.getValue() && touchingState2.getBlock() == Blocks.OBSIDIAN)) && ReverseStep.mc.player.onGround) {
            final EntityPlayerSP player2 = ReverseStep.mc.player;
            --player2.motionY;
        }
    }
    
    static {
        ReverseStep.INSTANCE = new ReverseStep();
    }
}
