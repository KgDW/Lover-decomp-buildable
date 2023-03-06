package dev.blackhig.zhebushigudu.lover.event.events;

import dev.blackhig.zhebushigudu.lover.util.Util;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import dev.blackhig.zhebushigudu.lover.event.EventStage;

public class BlockBreakEvent extends EventStage
{
    private final int breakId;
    private final BlockPos position;
    private final int progress;
    
    public BlockBreakEvent(final int breakId, final BlockPos position, final int progress) {
        this.breakId = breakId;
        this.position = position;
        this.progress = progress;
    }
    
    public BlockPos getPosition() {
        return this.position;
    }
    
    public int getBreakId() {
        return this.breakId;
    }
    
    public int getProgress() {
        return this.progress;
    }
    
    public EntityPlayer getBreaker() {
        return (EntityPlayer)Util.mc.world.getEntityByID(this.getBreakId());
    }
}
