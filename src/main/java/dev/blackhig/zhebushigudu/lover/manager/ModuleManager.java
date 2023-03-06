package dev.blackhig.zhebushigudu.lover.manager;

import java.util.concurrent.TimeUnit;
import dev.blackhig.zhebushigudu.lover.lover;
import dev.blackhig.zhebushigudu.lover.util.Util;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import dev.blackhig.zhebushigudu.lover.features.gui.loverGui;
import org.lwjgl.input.Keyboard;
import java.util.Collection;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Comparator;
import dev.blackhig.zhebushigudu.lover.event.events.Render3DEvent;
import dev.blackhig.zhebushigudu.lover.event.events.Render2DEvent;
import java.util.function.Consumer;
import net.minecraftforge.common.MinecraftForge;
import java.util.Arrays;
import java.util.Iterator;
import dev.blackhig.zhebushigudu.lover.features.modules.render.CityESP;
import dev.blackhig.zhebushigudu.lover.features.modules.render.SomeRenders;
import dev.blackhig.zhebushigudu.lover.features.modules.render.ChinaHat;
import dev.blackhig.zhebushigudu.lover.features.modules.render.ViewModel;
import dev.blackhig.zhebushigudu.lover.features.modules.render.AntiCtiyRenderPlus;
import dev.blackhig.zhebushigudu.lover.features.modules.render.AntiCtiyRender;
import dev.blackhig.zhebushigudu.lover.features.modules.render.CityRenderPlus;
import dev.blackhig.zhebushigudu.lover.features.modules.render.CityRender;
import dev.blackhig.zhebushigudu.lover.features.modules.render.BreakESP;
import dev.blackhig.zhebushigudu.lover.features.modules.render.SkyColor;
import dev.blackhig.zhebushigudu.lover.features.modules.render.FullBright;
import dev.blackhig.zhebushigudu.lover.features.modules.render.NoRender;
import dev.blackhig.zhebushigudu.lover.features.modules.render.Wireframe;
import dev.blackhig.zhebushigudu.lover.features.modules.render.Trajectories;
import dev.blackhig.zhebushigudu.lover.features.modules.render.SmallShield;
import dev.blackhig.zhebushigudu.lover.features.modules.render.Skeleton;
import dev.blackhig.zhebushigudu.lover.features.modules.render.HoleESP;
import dev.blackhig.zhebushigudu.lover.features.modules.render.HandChams;
import dev.blackhig.zhebushigudu.lover.features.modules.render.ESP;
import dev.blackhig.zhebushigudu.lover.features.modules.render.BlockHighlight;
import dev.blackhig.zhebushigudu.lover.features.modules.render.ArrowESP;
import dev.blackhig.zhebushigudu.lover.features.modules.player.XpThrower;
import dev.blackhig.zhebushigudu.lover.features.modules.player.AntiShulkerBoxPlus;
import dev.blackhig.zhebushigudu.lover.features.modules.player.Auto32k;
import dev.blackhig.zhebushigudu.lover.features.modules.player.AntiHoleKick;
import dev.blackhig.zhebushigudu.lover.features.modules.player.Anti32k;
import dev.blackhig.zhebushigudu.lover.features.modules.player.NameTags;
import dev.blackhig.zhebushigudu.lover.features.modules.player.AntiContainer;
import dev.blackhig.zhebushigudu.lover.features.modules.player.PacketXP;
import dev.blackhig.zhebushigudu.lover.features.modules.player.XCarry;
import dev.blackhig.zhebushigudu.lover.features.modules.player.TpsSync;
import dev.blackhig.zhebushigudu.lover.features.modules.player.Speedmine;
import dev.blackhig.zhebushigudu.lover.features.modules.player.Replenish;
import dev.blackhig.zhebushigudu.lover.features.modules.player.MultiTask;
import dev.blackhig.zhebushigudu.lover.features.modules.player.MCP;
import dev.blackhig.zhebushigudu.lover.features.modules.player.LiquidInteract;
import dev.blackhig.zhebushigudu.lover.features.modules.player.Burrow;
import dev.blackhig.zhebushigudu.lover.features.modules.player.FakePlayer;
import dev.blackhig.zhebushigudu.lover.features.modules.player.AntiShulkerBox;
import dev.blackhig.zhebushigudu.lover.features.modules.movement.PositionBug;
import dev.blackhig.zhebushigudu.lover.features.modules.movement.HoleSnap;
import dev.blackhig.zhebushigudu.lover.features.modules.movement.TimerModule;
import dev.blackhig.zhebushigudu.lover.features.modules.movement.Strafe;
import dev.blackhig.zhebushigudu.lover.features.modules.movement.Sprint;
import dev.blackhig.zhebushigudu.lover.features.modules.movement.PlayerTweaks;
import dev.blackhig.zhebushigudu.lover.features.modules.movement.NoFall;
import dev.blackhig.zhebushigudu.lover.features.modules.movement.Step;
import dev.blackhig.zhebushigudu.lover.features.modules.movement.Speed;
import dev.blackhig.zhebushigudu.lover.features.modules.movement.Scaffold;
import dev.blackhig.zhebushigudu.lover.features.modules.movement.ReverseStep;
import dev.blackhig.zhebushigudu.lover.features.modules.movement.PacketFly;
import dev.blackhig.zhebushigudu.lover.features.modules.movement.NoVoid;
import dev.blackhig.zhebushigudu.lover.features.modules.movement.Flight;
import dev.blackhig.zhebushigudu.lover.features.modules.misc.AutoPen;
import dev.blackhig.zhebushigudu.lover.features.modules.misc.ReplenishBox2;
import dev.blackhig.zhebushigudu.lover.features.modules.misc.ReplenishBox;
import dev.blackhig.zhebushigudu.lover.features.modules.combat.TrapSelf;
import dev.blackhig.zhebushigudu.lover.features.modules.misc.PacketEat;
import dev.blackhig.zhebushigudu.lover.features.modules.misc.AutoQueue;
import dev.blackhig.zhebushigudu.lover.features.modules.misc.InstantMine;
import dev.blackhig.zhebushigudu.lover.features.modules.misc.Tracker;
import dev.blackhig.zhebushigudu.lover.features.modules.misc.ToolTips;
import dev.blackhig.zhebushigudu.lover.features.modules.misc.PopCounter;
import dev.blackhig.zhebushigudu.lover.features.modules.misc.PearlNotify;
import dev.blackhig.zhebushigudu.lover.features.modules.misc.NoHitBox;
import dev.blackhig.zhebushigudu.lover.features.modules.misc.NoHandShake;
import dev.blackhig.zhebushigudu.lover.features.modules.misc.MCF;
import dev.blackhig.zhebushigudu.lover.features.modules.misc.ExtraTab;
import dev.blackhig.zhebushigudu.lover.features.modules.misc.ChatSuffix;
import dev.blackhig.zhebushigudu.lover.features.modules.misc.ChatModifier;
import dev.blackhig.zhebushigudu.lover.features.modules.misc.BuildHeight;
import dev.blackhig.zhebushigudu.lover.features.modules.misc.AutoGG;
import dev.blackhig.zhebushigudu.lover.features.modules.combat.BedMiner;
import dev.blackhig.zhebushigudu.lover.features.modules.combat.FaceMiner;
import dev.blackhig.zhebushigudu.lover.features.modules.combat.AutoHoleKick;
import dev.blackhig.zhebushigudu.lover.features.modules.combat.Anti32KTotem;
import dev.blackhig.zhebushigudu.lover.features.modules.combat.BreakCheck;
import dev.blackhig.zhebushigudu.lover.features.modules.combat.PistonAura;
import dev.blackhig.zhebushigudu.lover.features.modules.combat.SmartAntiCity;
import dev.blackhig.zhebushigudu.lover.features.modules.combat.AutoRedStone;
import dev.blackhig.zhebushigudu.lover.features.modules.combat.AutoCev1;
import dev.blackhig.zhebushigudu.lover.features.modules.combat.TrapHead;
import dev.blackhig.zhebushigudu.lover.features.modules.combat.OffHandCrystal;
import dev.blackhig.zhebushigudu.lover.features.modules.combat.FastBow;
import dev.blackhig.zhebushigudu.lover.features.modules.combat.AntiFacePlace;
import dev.blackhig.zhebushigudu.lover.features.modules.combat.WebFlatten;
import dev.blackhig.zhebushigudu.lover.features.modules.combat.WebTrapHead;
import dev.blackhig.zhebushigudu.lover.features.modules.combat.HoleFillPlus;
import dev.blackhig.zhebushigudu.lover.features.modules.combat.WebHoleFill;
import dev.blackhig.zhebushigudu.lover.features.modules.combat.Blocker;
import dev.blackhig.zhebushigudu.lover.features.modules.combat.AutoCityPlus;
import dev.blackhig.zhebushigudu.lover.features.modules.combat.AntiCity;
import dev.blackhig.zhebushigudu.lover.features.modules.combat.Flatten;
import dev.blackhig.zhebushigudu.lover.features.modules.combat.AntiBurrow;
import dev.blackhig.zhebushigudu.lover.features.modules.combat.AutoCity;
import dev.blackhig.zhebushigudu.lover.features.modules.combat.AutoCev;
import dev.blackhig.zhebushigudu.lover.features.modules.combat.Surround;
import dev.blackhig.zhebushigudu.lover.features.modules.combat.Selftrap;
import dev.blackhig.zhebushigudu.lover.features.modules.combat.Offhand;
import dev.blackhig.zhebushigudu.lover.features.modules.combat.Killaura;
import dev.blackhig.zhebushigudu.lover.features.modules.combat.EliteBow;
import dev.blackhig.zhebushigudu.lover.features.modules.combat.Criticals;
import dev.blackhig.zhebushigudu.lover.features.modules.combat.AutoWeb;
import dev.blackhig.zhebushigudu.lover.features.modules.combat.AutoTrap;
import dev.blackhig.zhebushigudu.lover.features.modules.combat.AutoMinecart;
import dev.blackhig.zhebushigudu.lover.features.modules.combat.AutoArmor;
import dev.blackhig.zhebushigudu.lover.features.modules.client.GUIBlur;
import dev.blackhig.zhebushigudu.lover.features.modules.client.HUD;
import dev.blackhig.zhebushigudu.lover.features.modules.client.FontMod;
import dev.blackhig.zhebushigudu.lover.features.modules.client.ClickGui;
import java.util.List;
import dev.blackhig.zhebushigudu.lover.features.modules.Module;
import java.util.ArrayList;
import dev.blackhig.zhebushigudu.lover.features.Feature;

public class ModuleManager extends Feature
{
    public ArrayList<Module> modules;
    public List<Module> sortedModules;
    public List<String> sortedModulesABC;
    public Animation animationThread;
    
    public ModuleManager() {
        this.modules = new ArrayList<>();
        this.sortedModules = new ArrayList<>();
        this.sortedModulesABC = new ArrayList<>();
    }
    
    public void init() {
        this.modules.add(new ClickGui());
        this.modules.add(new FontMod());
        this.modules.add(new HUD());
        this.modules.add(new GUIBlur());
        this.modules.add(new AutoArmor());
        this.modules.add(new AutoMinecart());
        this.modules.add(new AutoTrap());
        this.modules.add(new AutoWeb());
        this.modules.add(new Criticals());
        this.modules.add(new EliteBow());
        this.modules.add(new Killaura());
        this.modules.add(new Offhand());
        this.modules.add(new Selftrap());
        this.modules.add(new Surround());
        this.modules.add(new AutoCev());
        this.modules.add(new AutoCity());
        this.modules.add(new AntiBurrow());
        this.modules.add(new Flatten());
        this.modules.add(new AntiCity());
        this.modules.add(new AutoCityPlus());
        this.modules.add(new Blocker());
        this.modules.add(new WebHoleFill());
        this.modules.add(new HoleFillPlus());
        this.modules.add(new WebTrapHead());
        this.modules.add(new WebFlatten());
        this.modules.add(new AntiFacePlace());
        this.modules.add(new FastBow());
        this.modules.add(new OffHandCrystal());
        this.modules.add(new TrapHead());
        this.modules.add(new AutoCev1());
        this.modules.add(new AutoRedStone());
        this.modules.add(new SmartAntiCity());
        this.modules.add(new PistonAura());
        this.modules.add(new BreakCheck());
        this.modules.add(new Anti32KTotem());
        this.modules.add(new AutoHoleKick());
        this.modules.add(new FaceMiner());
        this.modules.add(new BedMiner());
        this.modules.add(new AutoGG());
        this.modules.add(new BuildHeight());
        this.modules.add(new ChatModifier());
        this.modules.add(new ChatSuffix());
        this.modules.add(new ExtraTab());
        this.modules.add(new MCF());
        this.modules.add(new NoHandShake());
        this.modules.add(new NoHitBox());
        this.modules.add(new PearlNotify());
        this.modules.add(new PopCounter());
        this.modules.add(new ToolTips());
        this.modules.add(new Tracker());
        this.modules.add(new InstantMine());
        this.modules.add(new AutoQueue());
        this.modules.add(new PacketEat());
        this.modules.add(new TrapSelf());
        this.modules.add(new ReplenishBox());
        this.modules.add(new ReplenishBox2());
        this.modules.add(new AutoPen());
        this.modules.add(new Flight());
        this.modules.add(new NoVoid());
        this.modules.add(new PacketFly());
        this.modules.add(new ReverseStep());
        this.modules.add(new Scaffold());
        this.modules.add(new Speed());
        this.modules.add(new Step());
        this.modules.add(new NoFall());
        this.modules.add(new PlayerTweaks());
        this.modules.add(new Sprint());
        this.modules.add(new Strafe());
        this.modules.add(new TimerModule());
        this.modules.add(new HoleSnap());
        this.modules.add(new PositionBug());
        this.modules.add(new AntiShulkerBox());
        this.modules.add(new FakePlayer());
        this.modules.add(new Burrow());
        this.modules.add(new LiquidInteract());
        this.modules.add(new MCP());
        this.modules.add(new MultiTask());
        this.modules.add(new Replenish());
        this.modules.add(new Speedmine());
        this.modules.add(new TpsSync());
        this.modules.add(new XCarry());
        this.modules.add(new PacketXP());
        this.modules.add(new AntiContainer());
        this.modules.add(new NameTags());
        this.modules.add(new Anti32k());
        this.modules.add(new AntiHoleKick());
        this.modules.add(new Auto32k());
        this.modules.add(new AntiShulkerBoxPlus());
        this.modules.add(new XpThrower());
        this.modules.add(new ArrowESP());
        this.modules.add(new BlockHighlight());
        this.modules.add(new ESP());
        this.modules.add(new HandChams());
        this.modules.add(new HoleESP());
        this.modules.add(new Skeleton());
        this.modules.add(new SmallShield());
        this.modules.add(new Trajectories());
        this.modules.add(new Wireframe());
        this.modules.add(new NoRender());
        this.modules.add(new FullBright());
        this.modules.add(new SkyColor());
        this.modules.add(new BreakESP());
        this.modules.add(new CityRender());
        this.modules.add(new CityRenderPlus());
        this.modules.add(new AntiCtiyRender());
        this.modules.add(new AntiCtiyRenderPlus());
        this.modules.add(new ViewModel());
        this.modules.add(new ChinaHat());
        this.modules.add(new SomeRenders());
        this.modules.add(new CityESP());
    }
    
    public Module getModuleByName(final String name) {
        for (final Module module : this.modules) {
            if (!module.getName().equalsIgnoreCase(name)) {
                continue;
            }
            return module;
        }
        return null;
    }
    
    public <T extends Module> T getModuleByClass(final Class<T> clazz) {
        for (final Module module : this.modules) {
            if (!clazz.isInstance(module)) {
                continue;
            }
            return (T)module;
        }
        return null;
    }
    
    public void enableModule(final Class<Module> clazz) {
        final Module module = this.getModuleByClass(clazz);
        if (module != null) {
            module.enable();
        }
    }
    
    public void disableModule(final Class<Module> clazz) {
        final Module module = this.getModuleByClass(clazz);
        if (module != null) {
            module.disable();
        }
    }
    
    public void enableModule(final String name) {
        final Module module = this.getModuleByName(name);
        if (module != null) {
            module.enable();
        }
    }
    
    public void disableModule(final String name) {
        final Module module = this.getModuleByName(name);
        if (module != null) {
            module.disable();
        }
    }
    
    public boolean isModuleEnabled(final String name) {
        final Module module = this.getModuleByName(name);
        return module != null && module.isOn();
    }
    
    public boolean isModuleEnabled(final Class<Module> clazz) {
        final Module module = this.getModuleByClass(clazz);
        return module != null && module.isOn();
    }
    
    public Module getModuleByDisplayName(final String displayName) {
        for (final Module module : this.modules) {
            if (!module.getDisplayName().equalsIgnoreCase(displayName)) {
                continue;
            }
            return module;
        }
        return null;
    }
    
    public ArrayList<Module> getEnabledModules() {
        final ArrayList<Module> enabledModules = new ArrayList<Module>();
        for (final Module module : this.modules) {
            if (!module.isEnabled()) {
                continue;
            }
            enabledModules.add(module);
        }
        return enabledModules;
    }
    
    public ArrayList<String> getEnabledModulesName() {
        final ArrayList<String> enabledModules = new ArrayList<String>();
        for (final Module module : this.modules) {
            if (module.isEnabled()) {
                if (!module.isDrawn()) {
                    continue;
                }
                enabledModules.add(module.getFullArrayString());
            }
        }
        return enabledModules;
    }
    
    public ArrayList<Module> getModulesByCategory(final Module.Category category) {
        final ArrayList<Module> modulesCategory = new ArrayList<Module>();
        this.modules.forEach(module -> {
            if (module.getCategory() == category) {
                modulesCategory.add(module);
            }
            return;
        });
        return modulesCategory;
    }
    
    public List<Module.Category> getCategories() {
        return Arrays.asList(Module.Category.values());
    }
    
    public void onLoad() {
        this.modules.stream().filter(Module::listening).forEach(MinecraftForge.EVENT_BUS::register);
        this.modules.forEach(Module::onLoad);
    }
    
    public void onUpdate() {
        this.modules.stream().filter(Feature::isEnabled).forEach(Module::onUpdate);
    }
    
    public void onTick() {
        this.modules.stream().filter(Feature::isEnabled).forEach(Module::onTick);
    }
    
    public void onRender2D(final Render2DEvent event) {
        this.modules.stream().filter(Feature::isEnabled).forEach(module -> module.onRender2D(event));
    }
    
    public void onRender3D(final Render3DEvent event) {
        this.modules.stream().filter(Feature::isEnabled).forEach(module -> module.onRender3D(event));
    }

    public void sortModules(boolean reverse) {
        this.sortedModules = this.getEnabledModules().stream().filter(Module::isDrawn).sorted(Comparator.comparing(module -> this.renderer.getStringWidth(module.getFullArrayString()) * (reverse ? -1 : 1))).collect(Collectors.toList());
    }
    
    public void sortModulesABC() {
        (this.sortedModulesABC = new ArrayList<String>(this.getEnabledModulesName())).sort(String.CASE_INSENSITIVE_ORDER);
    }
    
    public void onLogout() {
        this.modules.forEach(Module::onLogout);
    }
    
    public void onLogin() {
        this.modules.forEach(Module::onLogin);
    }
    
    public void onUnload() {
        this.modules.forEach(MinecraftForge.EVENT_BUS::unregister);
        this.modules.forEach(Module::onUnload);
    }
    
    public void onUnloadPost() {
        for (final Module module : this.modules) {
            module.enabled.setValue(false);
        }
    }
    
    public void onKeyPressed(final int eventKey) {
        if (eventKey == 0 || !Keyboard.getEventKeyState() || ModuleManager.mc.currentScreen instanceof loverGui) {
            return;
        }
        this.modules.forEach(module -> {
            if (module.getBind().getKey() == eventKey) {
                module.toggle();
            }
        });
    }
    
    private class Animation extends Thread
    {
        public Module module;
        public float offset;
        public float vOffset;
        ScheduledExecutorService service;
        
        public Animation() {
            super("Animation");
            this.service = Executors.newSingleThreadScheduledExecutor();
        }
        
        @Override
        public void run() {
            if (HUD.getInstance().renderingMode.getValue() == HUD.RenderingMode.Length) {
                for (final Module module : ModuleManager.this.sortedModules) {
                    final String text = module.getDisplayName() + ChatFormatting.GRAY + ((module.getDisplayInfo() != null) ? (" [" + ChatFormatting.WHITE + module.getDisplayInfo() + ChatFormatting.GRAY + "]") : "");
                    module.offset = ModuleManager.this.renderer.getStringWidth(text) / (float)HUD.getInstance().animationHorizontalTime.getValue();
                    module.vOffset = ModuleManager.this.renderer.getFontHeight() / (float)HUD.getInstance().animationVerticalTime.getValue();
                    if (module.isEnabled() && HUD.getInstance().animationHorizontalTime.getValue() != 1) {
                        if (module.arrayListOffset <= module.offset) {
                            continue;
                        }
                        if (Util.mc.world == null) {
                            continue;
                        }
                        final Module module3 = module;
                        module3.arrayListOffset -= module.offset;
                        module.sliding = true;
                    }
                    else {
                        if (!module.isDisabled()) {
                            continue;
                        }
                        if (HUD.getInstance().animationHorizontalTime.getValue() == 1) {
                            continue;
                        }
                        if (module.arrayListOffset < ModuleManager.this.renderer.getStringWidth(text) && Util.mc.world != null) {
                            final Module module4 = module;
                            module4.arrayListOffset += module.offset;
                            module.sliding = true;
                        }
                        else {
                            module.sliding = false;
                        }
                    }
                }
            }
            else {
                for (final String e : ModuleManager.this.sortedModulesABC) {
                    final Module module2 = lover.moduleManager.getModuleByName(e);
                    final String text2 = module2.getDisplayName() + ChatFormatting.GRAY + ((module2.getDisplayInfo() != null) ? (" [" + ChatFormatting.WHITE + module2.getDisplayInfo() + ChatFormatting.GRAY + "]") : "");
                    module2.offset = ModuleManager.this.renderer.getStringWidth(text2) / (float)HUD.getInstance().animationHorizontalTime.getValue();
                    module2.vOffset = ModuleManager.this.renderer.getFontHeight() / (float)HUD.getInstance().animationVerticalTime.getValue();
                    if (module2.isEnabled() && HUD.getInstance().animationHorizontalTime.getValue() != 1) {
                        if (module2.arrayListOffset <= module2.offset) {
                            continue;
                        }
                        if (Util.mc.world == null) {
                            continue;
                        }
                        final Module module5 = module2;
                        module5.arrayListOffset -= module2.offset;
                        module2.sliding = true;
                    }
                    else {
                        if (!module2.isDisabled()) {
                            continue;
                        }
                        if (HUD.getInstance().animationHorizontalTime.getValue() == 1) {
                            continue;
                        }
                        if (module2.arrayListOffset < ModuleManager.this.renderer.getStringWidth(text2) && Util.mc.world != null) {
                            final Module module6 = module2;
                            module6.arrayListOffset += module2.offset;
                            module2.sliding = true;
                        }
                        else {
                            module2.sliding = false;
                        }
                    }
                }
            }
        }
        
        @Override
        public void start() {
            System.out.println("Starting animation thread.");
            this.service.scheduleAtFixedRate(this, 0L, 1L, TimeUnit.MILLISECONDS);
        }
    }
}
