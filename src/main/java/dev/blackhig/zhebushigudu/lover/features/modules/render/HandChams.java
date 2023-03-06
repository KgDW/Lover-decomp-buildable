package dev.blackhig.zhebushigudu.lover.features.modules.render;

import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.features.modules.Module;

public class HandChams extends Module
{
    private static HandChams INSTANCE;
    public Setting<RenderMode> mode;
    public Setting<Integer> red;
    public Setting<Integer> green;
    public Setting<Integer> blue;
    public Setting<Integer> alpha;
    
    public HandChams() {
        super("HandChams", "Changes your hand color.", Category.RENDER, false, false, false);
        this.mode = this.register(new Setting<>("Mode", RenderMode.SOLID));
        this.red = this.register(new Setting<>("Red", 255, 0, 255));
        this.green = this.register(new Setting<>("Green", 0, 0, 255));
        this.blue = this.register(new Setting<>("Blue", 0, 0, 255));
        this.alpha = this.register(new Setting<>("Alpha", 240, 0, 255));
        this.setInstance();
    }
    
    public static HandChams getINSTANCE() {
        if (HandChams.INSTANCE == null) {
            HandChams.INSTANCE = new HandChams();
        }
        return HandChams.INSTANCE;
    }
    
    private void setInstance() {
        HandChams.INSTANCE = this;
    }
    
    static {
        HandChams.INSTANCE = new HandChams();
    }
    
    public enum RenderMode
    {
        SOLID, 
        WIREFRAME
    }
}
