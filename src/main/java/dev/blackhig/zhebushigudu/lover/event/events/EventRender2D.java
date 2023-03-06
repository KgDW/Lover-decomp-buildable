package dev.blackhig.zhebushigudu.lover.event.events;

import dev.blackhig.zhebushigudu.lover.event.EventStage;

public class EventRender2D extends EventStage
{
    private float partialTicks;
    
    public EventRender2D(final float partialTicks) {
        this.partialTicks = partialTicks;
    }
    
    public float getPartialTicks() {
        return this.partialTicks;
    }
    
    public void setPartialTicks(final float partialTicks) {
        this.partialTicks = partialTicks;
    }
}
