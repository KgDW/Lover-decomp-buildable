package dev.blackhig.zhebushigudu.lover.features;

import dev.blackhig.zhebushigudu.lover.features.gui.loverGui;
import dev.blackhig.zhebushigudu.lover.features.modules.Module;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.lover;
import dev.blackhig.zhebushigudu.lover.manager.TextManager;
import dev.blackhig.zhebushigudu.lover.util.Util;

import java.util.ArrayList;
import java.util.List;

public class Feature implements Util
{
    public List<Setting> settings;
    public TextManager renderer;
    private String name;
    
    public Feature() {
        this.settings = new ArrayList<>();
        this.renderer = lover.textManager;
    }
    
    public Feature(final String name) {
        this.settings = new ArrayList<>();
        this.renderer = lover.textManager;
        this.name = name;
    }
    
    public static boolean nullCheck() {
        return Feature.mc.player == null;
    }
    
    public static boolean fullNullCheck() {
        return Feature.mc.player == null || Feature.mc.world == null;
    }
    
    public String getName() {
        return this.name;
    }
    
    public List<Setting> getSettings() {
        return this.settings;
    }
    
    public boolean hasSettings() {
        return !this.settings.isEmpty();
    }
    
    public boolean isEnabled() {
        return this instanceof Module && ((Module)this).isOn();
    }
    
    public boolean isDisabled() {
        return !this.isEnabled();
    }
    
    public Setting register(final Setting setting) {
        setting.setFeature(this);
        this.settings.add(setting);
        if (this instanceof Module && Feature.mc.currentScreen instanceof loverGui) {
            loverGui.getInstance().updateModule((Module)this);
        }
        return setting;
    }
    
    public void unregister(final Setting settingIn) {
        final ArrayList<Setting> removeList = new ArrayList<Setting>();
        for (final Setting setting : this.settings) {
            if (!setting.equals(settingIn)) {
                continue;
            }
            removeList.add(setting);
        }
        if (!removeList.isEmpty()) {
            this.settings.removeAll(removeList);
        }
        if (this instanceof Module && Feature.mc.currentScreen instanceof loverGui) {
            loverGui.getInstance().updateModule((Module)this);
        }
    }
    
    public Setting getSettingByName(final String name) {
        for (final Setting setting : this.settings) {
            if (!setting.getName().equalsIgnoreCase(name)) {
                continue;
            }
            return setting;
        }
        return null;
    }
    
    public void reset() {
        for (final Setting setting : this.settings) {
            setting.setValue(setting.getDefaultValue());
        }
    }
    
    public void clearSettings() {
        this.settings = new ArrayList<Setting>();
    }
}
