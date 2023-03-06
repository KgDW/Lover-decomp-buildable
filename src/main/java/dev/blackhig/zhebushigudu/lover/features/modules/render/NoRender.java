package dev.blackhig.zhebushigudu.lover.features.modules.render;

import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import dev.blackhig.zhebushigudu.lover.event.events.NoRenderEvent;
import net.minecraft.init.MobEffects;
import dev.blackhig.zhebushigudu.lover.util.Util;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.features.modules.Module;

public class NoRender extends Module
{
    private static NoRender INSTANCE;
    public Setting<Boolean> armor;
    public Setting<Boolean> blind;
    public Setting<Boolean> fire;
    public Setting<Boolean> hurtCam;
    public Setting<Boolean> nausea;
    public Setting<Boolean> skyLightUpdate;
    
    public NoRender() {
        super("NoRender", "No Render", Category.RENDER, true, false, false);
        this.armor = this.register(new Setting<>("Armor", true));
        this.blind = this.register(new Setting<>("Blind", true));
        this.fire = this.register(new Setting<>("Frie", true));
        this.hurtCam = this.register(new Setting<>("HurtCam", true));
        this.nausea = this.register(new Setting<>("Nausea", true));
        this.skyLightUpdate = this.register(new Setting<>("SkyLightUpdate", true));
        this.setInstance();
    }
    
    public static NoRender getInstance() {
        if (NoRender.INSTANCE == null) {
            NoRender.INSTANCE = new NoRender();
        }
        return NoRender.INSTANCE;
    }
    
    private void setInstance() {
        NoRender.INSTANCE = this;
    }
    
    @Override
    public void onUpdate() {
        if (this.blind.getValue() && Util.mc.player.isPotionActive(MobEffects.BLINDNESS)) {
            Util.mc.player.removePotionEffect(MobEffects.BLINDNESS);
        }
        if (this.nausea.getValue() && Util.mc.player.isPotionActive(MobEffects.NAUSEA)) {
            Util.mc.player.removePotionEffect(MobEffects.NAUSEA);
        }
    }
    
    @SubscribeEvent
    public void NoRenderEventListener(final NoRenderEvent event) {
        if (event.getStage() == 0 && this.armor.getValue()) {
            event.setCanceled(true);
        }
        else if (event.getStage() == 1 && this.hurtCam.getValue()) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent
    public void blockOverlayEventListener(final RenderBlockOverlayEvent event) {
        if (this.fire.getValue() && event.getOverlayType() == RenderBlockOverlayEvent.OverlayType.FIRE) {
            event.setCanceled(true);
        }
    }
    
    static {
        NoRender.INSTANCE = new NoRender();
    }
}
