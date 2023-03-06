package dev.blackhig.zhebushigudu.lover.features.modules.movement;

import dev.blackhig.zhebushigudu.lover.features.Feature;
import dev.blackhig.zhebushigudu.lover.util.Util;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.features.modules.Module;

public class Sprint extends Module
{
    private static Sprint INSTANCE;
    public Setting<Mode> mode;
    
    public Sprint() {
        super("Sprint", "Modifies sprinting", Category.MOVEMENT, true, false, false);
        this.mode = this.register(new Setting<>("Mode", Mode.LEGIT));
        this.setInstance();
    }
    
    private void setInstance() {
        Sprint.INSTANCE = this;
    }
    
    public static Sprint getInstance() {
        if (Sprint.INSTANCE == null) {
            Sprint.INSTANCE = new Sprint();
        }
        return Sprint.INSTANCE;
    }
    
    @Override
    public void onUpdate() {
        switch (this.mode.getValue()) {
            case RAGE: {
                if ((Util.mc.gameSettings.keyBindForward.isKeyDown() || Util.mc.gameSettings.keyBindBack.isKeyDown() || Util.mc.gameSettings.keyBindLeft.isKeyDown() || Util.mc.gameSettings.keyBindRight.isKeyDown()) && !Util.mc.player.isSneaking() && !Util.mc.player.collidedHorizontally && Util.mc.player.getFoodStats().getFoodLevel() > 6.0f) {
                    Util.mc.player.setSprinting(true);
                }
                return;
            }
            case LEGIT: {
                if (Util.mc.gameSettings.keyBindForward.isKeyDown() && !Util.mc.player.isSneaking() && !Util.mc.player.isHandActive() && !Util.mc.player.collidedHorizontally && Util.mc.player.getFoodStats().getFoodLevel() > 6.0f && Util.mc.currentScreen == null) {
                    Util.mc.player.setSprinting(true);
                }
            }
            default: {}
        }
    }
    
    @Override
    public void onDisable() {
        if (!Feature.nullCheck()) {
            Util.mc.player.setSprinting(false);
        }
    }
    
    @Override
    public String getDisplayInfo() {
        return this.mode.currentEnumName();
    }
    
    static {
        Sprint.INSTANCE = new Sprint();
    }
    
    public enum Mode
    {
        LEGIT, 
        RAGE
    }
}
