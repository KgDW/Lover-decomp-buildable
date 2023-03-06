package dev.blackhig.zhebushigudu.lover.event.events;

import net.minecraft.entity.player.EntityPlayer;
import dev.blackhig.zhebushigudu.lover.event.EventStage;

public class DeathEvent extends EventStage
{
    public EntityPlayer player;
    
    public DeathEvent(final EntityPlayer player) {
        this.player = player;
    }
}
