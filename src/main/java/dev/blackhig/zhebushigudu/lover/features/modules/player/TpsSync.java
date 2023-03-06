package dev.blackhig.zhebushigudu.lover.features.modules.player;

import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.features.modules.Module;

public class TpsSync extends Module
{
    private static TpsSync INSTANCE;
    public Setting<Boolean> attack;
    public Setting<Boolean> mining;
    
    public TpsSync() {
        super("TpsSync", "Syncs your client with the TPS.", Category.PLAYER, true, false, false);
        this.attack = this.register(new Setting<>("Attack", Boolean.FALSE));
        this.mining = this.register(new Setting<>("Mine", Boolean.TRUE));
        this.setInstance();
    }
    
    public static TpsSync getInstance() {
        if (TpsSync.INSTANCE == null) {
            TpsSync.INSTANCE = new TpsSync();
        }
        return TpsSync.INSTANCE;
    }
    
    private void setInstance() {
        TpsSync.INSTANCE = this;
    }
    
    static {
        TpsSync.INSTANCE = new TpsSync();
    }
}
