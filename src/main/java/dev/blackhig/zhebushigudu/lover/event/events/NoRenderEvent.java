package dev.blackhig.zhebushigudu.lover.event.events;

import net.minecraftforge.fml.common.eventhandler.Cancelable;
import dev.blackhig.zhebushigudu.lover.event.EventStage;

@Cancelable
public class NoRenderEvent extends EventStage
{
    public NoRenderEvent(final int a) {
        super(a);
    }
}
