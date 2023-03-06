package dev.blackhig.zhebushigudu.lover.features.modules.client;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import dev.blackhig.zhebushigudu.lover.features.command.Command;
import com.mojang.realmsclient.gui.ChatFormatting;
import dev.blackhig.zhebushigudu.lover.lover;
import dev.blackhig.zhebushigudu.lover.event.events.ClientEvent;
import net.minecraft.client.settings.GameSettings;
import dev.blackhig.zhebushigudu.lover.features.gui.loverGui;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.features.modules.Module;

public class ClickGui extends Module
{
    private static ClickGui INSTANCE;
    public Setting<String> prefix;
    public Setting<Boolean> customFov;
    public Setting<Float> fov;
    public Setting<Boolean> mainmenu;
    public Setting<Integer> red;
    public Setting<Integer> green;
    public Setting<Integer> blue;
    public Setting<Integer> hoverAlpha;
    public Setting<Integer> topRed;
    public Setting<Integer> topGreen;
    public Setting<Integer> topBlue;
    public Setting<Integer> alpha;
    public Setting<Boolean> rainbow;
    public Setting<Boolean> outline;
    public Setting<Integer> o_red;
    public Setting<Integer> o_green;
    public Setting<Integer> o_blue;
    public Setting<Integer> o_alpha;
    public Setting<rainbowMode> rainbowModeHud;
    public Setting<rainbowModeArray> rainbowModeA;
    public Setting<Integer> rainbowHue;
    public Setting<Float> rainbowBrightness;
    public Setting<Float> rainbowSaturation;
    private loverGui click;
    
    public ClickGui() {
        super("ClickGui", "Opens the ClickGui", Category.CLIENT, true, false, false);
        this.prefix = this.register(new Setting<>("Prefix", "."));
        this.customFov = this.register(new Setting<>("CustomFov", false));
        this.fov = this.register(new Setting<>("Fov", 150.0f, (-180.0f), 180.0f));
        this.mainmenu = this.register(new Setting<>("GUIMainMenu", true));
        this.red = this.register(new Setting<>("Red", 0, 0, 255));
        this.green = this.register(new Setting<>("Green", 0, 0, 255));
        this.blue = this.register(new Setting<>("Blue", 255, 0, 255));
        this.hoverAlpha = this.register(new Setting<>("Alpha", 180, 0, 255));
        this.topRed = this.register(new Setting<>("SecondRed", 0, 0, 255));
        this.topGreen = this.register(new Setting<>("SecondGreen", 0, 0, 255));
        this.topBlue = this.register(new Setting<>("SecondBlue", 150, 0, 255));
        this.alpha = this.register(new Setting<>("HoverAlpha", 240, 0, 255));
        this.rainbow = this.register(new Setting<>("Rainbow", false));
        this.outline = this.register(new Setting<>("Outline", true));
        this.o_red = this.register(new Setting<>("OutlineRed", 0, 0, 255));
        this.o_green = this.register(new Setting<>("OutlineGreen", 0, 0, 255));
        this.o_blue = this.register(new Setting<>("OutlineBlue", 255, 0, 255));
        this.o_alpha = this.register(new Setting<>("OutlineAlpha", 180, 0, 255));
        this.rainbowModeHud = this.register(new Setting("HRainbowMode", rainbowMode.Static, v -> this.rainbow.getValue()));
        this.rainbowModeA = this.register(new Setting("ARainbowMode", rainbowModeArray.Static, v -> this.rainbow.getValue()));
        this.rainbowHue = this.register(new Setting("Delay", 240, 0, 600, v -> this.rainbow.getValue()));
        this.rainbowBrightness = this.register(new Setting("Brightness ", 150.0f, 1.0f, 255.0f, v -> this.rainbow.getValue()));
        this.rainbowSaturation = this.register(new Setting("Saturation", 150.0f, 1.0f, 255.0f, v -> this.rainbow.getValue()));
        this.setInstance();
    }
    
    public static ClickGui getInstance() {
        if (ClickGui.INSTANCE == null) {
            ClickGui.INSTANCE = new ClickGui();
        }
        return ClickGui.INSTANCE;
    }
    
    private void setInstance() {
        ClickGui.INSTANCE = this;
    }
    
    @Override
    public void onUpdate() {
        if (this.customFov.getValue()) {
            ClickGui.mc.gameSettings.setOptionFloatValue(GameSettings.Options.FOV, (float)this.fov.getValue());
        }
    }
    
    @SubscribeEvent
    public void onSettingChange(final ClientEvent event) {
        if (event.getStage() == 2 && event.getSetting().getFeature().equals(this)) {
            if (event.getSetting().equals(this.prefix)) {
                lover.commandManager.setPrefix(this.prefix.getPlannedValue());
                Command.sendMessage("Prefix set to " + ChatFormatting.DARK_GRAY + lover.commandManager.getPrefix());
            }
            lover.colorManager.setColor(this.red.getPlannedValue(), this.green.getPlannedValue(), this.blue.getPlannedValue(), this.hoverAlpha.getPlannedValue());
        }
    }
    
    @Override
    public void onEnable() {
        ClickGui.mc.displayGuiScreen(loverGui.getClickGui());
    }
    
    @Override
    public void onLoad() {
        lover.colorManager.setColor(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.hoverAlpha.getValue());
        lover.commandManager.setPrefix(this.prefix.getValue());
    }
    
    @Override
    public void onTick() {
        if (!(ClickGui.mc.currentScreen instanceof loverGui)) {
            this.disable();
        }
    }
    
    static {
        ClickGui.INSTANCE = new ClickGui();
    }
    
    public enum rainbowModeArray
    {
        Static, 
        Up;
    }
    
    public enum rainbowMode
    {
        Static, 
        Sideway;
    }
}
