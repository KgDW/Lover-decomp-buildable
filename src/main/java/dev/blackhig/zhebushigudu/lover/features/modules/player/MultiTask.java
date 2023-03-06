package dev.blackhig.zhebushigudu.lover.features.modules.player;

import dev.blackhig.zhebushigudu.lover.features.modules.Module;

public class MultiTask extends Module
{
    private static MultiTask INSTANCE;
    
    public MultiTask() {
        super("MultiTask", "Allows you to eat while mining.", Category.PLAYER, false, false, false);
        this.setInstance();
    }
    
    public static MultiTask getInstance() {
        if (MultiTask.INSTANCE == null) {
            MultiTask.INSTANCE = new MultiTask();
        }
        return MultiTask.INSTANCE;
    }
    
    private void setInstance() {
        MultiTask.INSTANCE = this;
    }
    
    static {
        MultiTask.INSTANCE = new MultiTask();
    }
}
