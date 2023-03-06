package dev.blackhig.zhebushigudu.lover.features.modules.render;

import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.features.modules.Module;

public class SmallShield extends Module
{
    private static SmallShield INSTANCE;
    public Setting<Float> offX;
    public Setting<Float> offY;
    public Setting<Float> mainX;
    public Setting<Float> mainY;
    
    public SmallShield() {
        super("SmallShield", "Makes you offhand lower.", Category.RENDER, false, false, false);
        this.offX = this.register(new Setting<>("OffHandX", 0.0f, (-1.0f), 1.0f));
        this.offY = this.register(new Setting<>("OffHandY", 0.0f, (-1.0f), 1.0f));
        this.mainX = this.register(new Setting<>("MainHandX", 0.0f, (-1.0f), 1.0f));
        this.mainY = this.register(new Setting<>("MainHandY", 0.0f, (-1.0f), 1.0f));
        this.setInstance();
    }
    
    public static SmallShield getINSTANCE() {
        if (SmallShield.INSTANCE == null) {
            SmallShield.INSTANCE = new SmallShield();
        }
        return SmallShield.INSTANCE;
    }
    
    private void setInstance() {
        SmallShield.INSTANCE = this;
    }
    
    static {
        SmallShield.INSTANCE = new SmallShield();
    }
}
