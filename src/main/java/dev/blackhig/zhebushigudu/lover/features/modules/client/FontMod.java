package dev.blackhig.zhebushigudu.lover.features.modules.client;

import dev.blackhig.zhebushigudu.lover.lover;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.mojang.realmsclient.gui.ChatFormatting;
import dev.blackhig.zhebushigudu.lover.event.events.ClientEvent;
import dev.blackhig.zhebushigudu.lover.features.command.Command;
import java.awt.GraphicsEnvironment;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.features.modules.Module;

public class FontMod extends Module
{
    private static FontMod INSTANCE;
    public Setting<String> fontName;
    public Setting<Boolean> antiAlias;
    public Setting<Boolean> fractionalMetrics;
    public Setting<Integer> fontSize;
    public Setting<Integer> fontStyle;
    private boolean reloadFont;
    
    public FontMod() {
        super("CustomFont", "CustomFont for all of the clients text. Use the font command.", Category.CLIENT, true, false, false);
        this.fontName = this.register(new Setting<>("FontName", "Arial", "Name of the font."));
        this.antiAlias = this.register(new Setting<>("AntiAlias", true, "Smoother font."));
        this.fractionalMetrics = this.register(new Setting<>("Metrics", true, "Thinner font."));
        this.fontSize = this.register(new Setting<>("Size", 18, 12, 30, "Size of the font."));
        this.fontStyle = this.register(new Setting<>("Style", 0, 0, 3, "Style of the font."));
        this.reloadFont = false;
        this.setInstance();
    }
    
    public static FontMod getInstance() {
        if (FontMod.INSTANCE == null) {
            FontMod.INSTANCE = new FontMod();
        }
        return FontMod.INSTANCE;
    }
    
    public static boolean checkFont(final String font, final boolean message) {
        final String[] availableFontFamilyNames;
        final String[] fonts = availableFontFamilyNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        for (final String s : availableFontFamilyNames) {
            if (!message && s.equals(font)) {
                return true;
            }
            if (message) {
                Command.sendMessage(s);
            }
        }
        return false;
    }
    
    private void setInstance() {
        FontMod.INSTANCE = this;
    }
    
    @SubscribeEvent
    public void onSettingChange(final ClientEvent event) {
        final Setting setting;
        if (event.getStage() == 2 && (setting = event.getSetting()) != null && setting.getFeature().equals(this)) {
            if (setting.getName().equals("FontName") && !checkFont(setting.getPlannedValue().toString(), false)) {
                Command.sendMessage(ChatFormatting.RED + "That font doesnt exist.");
                event.setCanceled(true);
                return;
            }
            this.reloadFont = true;
        }
    }
    
    @Override
    public void onTick() {
        if (this.reloadFont) {
            lover.textManager.init(false);
            this.reloadFont = false;
        }
    }
    
    static {
        FontMod.INSTANCE = new FontMod();
    }
}
