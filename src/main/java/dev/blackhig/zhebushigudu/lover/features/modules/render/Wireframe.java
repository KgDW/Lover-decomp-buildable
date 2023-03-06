package dev.blackhig.zhebushigudu.lover.features.modules.render;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.features.modules.Module;

public class Wireframe extends Module
{
    private static Wireframe INSTANCE;
    public final Setting<Float> alpha;
    public final Setting<Float> cAlpha;
    public final Setting<Float> lineWidth;
    public final Setting<Float> crystalLineWidth;
    public Setting<RenderMode> mode;
    public Setting<RenderMode> cMode;
    public Setting<Boolean> players;
    public Setting<Boolean> playerModel;
    public Setting<Boolean> crystals;
    public Setting<Boolean> crystalModel;
    
    public Wireframe() {
        super("Wireframe", "Draws a wireframe esp around other players.", Category.RENDER, false, false, false);
        this.alpha = this.register(new Setting<>("PAlpha", 255.0f, 0.1f, 255.0f));
        this.cAlpha = this.register(new Setting<>("CAlpha", 255.0f, 0.1f, 255.0f));
        this.lineWidth = this.register(new Setting<>("PLineWidth", 1.0f, 0.1f, 3.0f));
        this.crystalLineWidth = this.register(new Setting<>("CLineWidth", 1.0f, 0.1f, 3.0f));
        this.mode = this.register(new Setting<>("PMode", RenderMode.SOLID));
        this.cMode = this.register(new Setting<>("CMode", RenderMode.SOLID));
        this.players = this.register(new Setting<>("Players", Boolean.FALSE));
        this.playerModel = this.register(new Setting<>("PlayerModel", Boolean.FALSE));
        this.crystals = this.register(new Setting<>("Crystals", Boolean.FALSE));
        this.crystalModel = this.register(new Setting<>("CrystalModel", Boolean.FALSE));
        this.setInstance();
    }
    
    public static Wireframe getINSTANCE() {
        if (Wireframe.INSTANCE == null) {
            Wireframe.INSTANCE = new Wireframe();
        }
        return Wireframe.INSTANCE;
    }
    
    private void setInstance() {
        Wireframe.INSTANCE = this;
    }
    
    @SubscribeEvent
    public void onRenderPlayerEvent(final RenderPlayerEvent.Pre event) {
        event.getEntityPlayer().hurtTime = 0;
    }
    
    static {
        Wireframe.INSTANCE = new Wireframe();
    }
    
    public enum RenderMode
    {
        SOLID, 
        WIREFRAME
    }
}
