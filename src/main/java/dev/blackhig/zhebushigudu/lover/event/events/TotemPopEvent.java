package dev.blackhig.zhebushigudu.lover.event.events;

import net.minecraft.entity.player.EntityPlayer;
import dev.blackhig.zhebushigudu.lover.event.EventStage;

public class TotemPopEvent extends EventStage
{
    private final EntityPlayer entity;
    
    public TotemPopEvent(final EntityPlayer entity) {
        this.entity = entity;
    }
    
    public EntityPlayer getEntity() {
        return this.entity;
    }
}
