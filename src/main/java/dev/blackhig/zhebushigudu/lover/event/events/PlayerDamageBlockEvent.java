package dev.blackhig.zhebushigudu.lover.event.events;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import dev.blackhig.zhebushigudu.lover.event.EventStage;

@Cancelable
public class PlayerDamageBlockEvent extends EventStage
{
    public BlockPos pos;
    public EnumFacing facing;
    
    public PlayerDamageBlockEvent(final int stage, final BlockPos pos, final EnumFacing facing) {
        super(stage);
        this.pos = pos;
        this.facing = facing;
    }
}
