package dev.blackhig.zhebushigudu.lover.features.modules.render;

import net.minecraft.potion.PotionEffect;
import net.minecraft.init.MobEffects;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.features.modules.Module;

public class FullBright extends Module
{
    float gammabackup;
    Setting<mode> Mode;
    
    public FullBright() {
        super("FullBright", "Gamma to full", Category.RENDER, true, false, false);
        this.Mode = this.register(new Setting<>("Bright Mode", mode.Gamma));
    }
    
    @Override
    public void onTick() {
        if (this.Mode.getValue().equals(mode.Gamma)) {
            FullBright.mc.player.removePotionEffect(MobEffects.NIGHT_VISION);
            this.gammabackup = FullBright.mc.gameSettings.gammaSetting;
            FullBright.mc.gameSettings.gammaSetting = 255.0f;
        }
        if (this.Mode.getValue().equals(mode.Potion)) {
            FullBright.mc.gameSettings.gammaSetting = this.gammabackup;
            FullBright.mc.player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 32548, 127));
        }
    }
    
    @Override
    public void onDisable() {
        if (this.Mode.getValue().equals(mode.Gamma)) {
            FullBright.mc.gameSettings.gammaSetting = this.gammabackup;
        }
        if (this.Mode.getValue().equals(mode.Potion)) {
            FullBright.mc.player.removePotionEffect(MobEffects.NIGHT_VISION);
        }
    }
    
    enum mode
    {
        Potion, 
        Gamma;
    }
}
