package dev.blackhig.zhebushigudu.lover.features.modules.render;

import java.awt.Color;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.features.modules.Module;

public class SkyColor extends Module
{
    private static SkyColor INSTANCE;
    private final Setting<Integer> red;
    private final Setting<Integer> green;
    private final Setting<Integer> blue;
    private final Setting<Boolean> rainbow;
    
    public SkyColor() {
        super("SkyColor", "Sky Render.", Category.RENDER, false, false, false);
        this.red = this.register(new Setting<>("Red", 255, 0, 255));
        this.green = this.register(new Setting<>("Green", 255, 0, 255));
        this.blue = this.register(new Setting<>("Blue", 255, 0, 255));
        this.rainbow = this.register(new Setting<>("Rainbow", true));
    }
    
    public static SkyColor getInstance() {
        if (SkyColor.INSTANCE == null) {
            SkyColor.INSTANCE = new SkyColor();
        }
        return SkyColor.INSTANCE;
    }
    
    private void setInstance() {
        SkyColor.INSTANCE = this;
    }
    
    @SubscribeEvent
    public void fogColors(final EntityViewRenderEvent.FogColors event) {
        event.setRed(this.red.getValue() / 255.0f);
        event.setGreen(this.green.getValue() / 255.0f);
        event.setBlue(this.blue.getValue() / 255.0f);
    }
    
    @SubscribeEvent
    public void fog_density(final EntityViewRenderEvent.FogDensity event) {
        event.setDensity(0.0f);
        event.setCanceled(true);
    }
    
    @Override
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }
    
    @Override
    public void onUpdate() {
        if (this.rainbow.getValue()) {
            this.doRainbow();
        }
    }
    
    public void doRainbow() {
        final float[] tick_color = { System.currentTimeMillis() % 11520L / 11520.0f };
        final int color_rgb_o = Color.HSBtoRGB(tick_color[0], 0.8f, 0.8f);
        this.red.setValue(color_rgb_o >> 16 & 0xFF);
        this.green.setValue(color_rgb_o >> 8 & 0xFF);
        this.blue.setValue(color_rgb_o & 0xFF);
    }
    
    static {
        SkyColor.INSTANCE = new SkyColor();
    }
}
