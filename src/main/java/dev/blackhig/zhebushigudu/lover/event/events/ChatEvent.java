package dev.blackhig.zhebushigudu.lover.event.events;

import net.minecraftforge.fml.common.eventhandler.Cancelable;
import dev.blackhig.zhebushigudu.lover.event.EventStage;

@Cancelable
public class ChatEvent extends EventStage
{
    private final String msg;
    
    public ChatEvent(final String msg) {
        this.msg = msg;
    }
    
    public String getMsg() {
        return this.msg;
    }
}
