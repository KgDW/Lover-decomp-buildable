package dev.blackhig.zhebushigudu.lover.event.events;

import dev.blackhig.zhebushigudu.lover.event.EventStage;

public class KeyEvent extends EventStage
{
    private final int key;
    
    public KeyEvent(final int key) {
        this.key = key;
    }
    
    public int getKey() {
        return this.key;
    }
}
