package dev.blackhig.zhebushigudu.lover.features.modules.client;

import dev.blackhig.zhebushigudu.lover.event.events.ClientEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Items;
import dev.blackhig.zhebushigudu.lover.util.RenderUtil;
import net.minecraft.client.gui.ScaledResolution;
import dev.blackhig.zhebushigudu.lover.util.EntityUtil;
import net.minecraft.potion.PotionEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import dev.blackhig.zhebushigudu.lover.features.Feature;
import dev.blackhig.zhebushigudu.lover.event.events.Render2DEvent;

import java.util.List;
import com.mojang.realmsclient.gui.ChatFormatting;
import dev.blackhig.zhebushigudu.lover.lover;
import net.minecraft.entity.player.EntityPlayer;
import java.util.ArrayList;
import dev.blackhig.zhebushigudu.lover.util.ColorUtil;
import dev.blackhig.zhebushigudu.lover.util.Util;
import java.util.HashMap;
import dev.blackhig.zhebushigudu.lover.util.TextUtil;
import java.util.Map;
import dev.blackhig.zhebushigudu.lover.util.Timer;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import dev.blackhig.zhebushigudu.lover.features.modules.Module;

public class HUD extends Module
{
    private static final ResourceLocation box;
    private static final ItemStack totem;
    private static final ItemStack Crystal;
    private static final ItemStack xp;
    private static final ItemStack ap;
    private static RenderItem itemRender;
    private static HUD INSTANCE;
    private final Setting<Boolean> grayNess;
    private final Setting<Boolean> renderingUp;
    private final Setting<Boolean> waterMark;
    private final Setting<Boolean> waterMark2;
    private final Setting<Boolean> waterMark3;
    public Setting<Integer> waterMarkY;
    public Setting<Integer> waterMark2Y;
    public Setting<Integer> waterMark3Y;
    private final Setting<Boolean> arrayList;
    private final Setting<Boolean> hitMarkers;
    private final Setting<Boolean> coords;
    private final Setting<Boolean> direction;
    private final Setting<Boolean> armor;
    private final Setting<Boolean> totems;
    private final Setting<Boolean> Crystalsss;
    private final Setting<Boolean> APSSS;
    private final Setting<Boolean> XPSSS;
    private Setting<Integer> insss;
    private final Setting<Boolean> greeter;
    private final Setting<Boolean> speed;
    private final Setting<Boolean> potions;
    private final Setting<Boolean> ping;
    private final Setting<Boolean> tps;
    private final Setting<Boolean> fps;
    private final Setting<Boolean> Friends;
    private final Setting<Boolean> customFont;
    public Setting<Integer> Friendsx;
    public Setting<Integer> Friendsy;
    private final Timer timer;
    private final Map<String, Integer> players;
    public Setting<String> command;
    public Setting<TextUtil.Color> bracketColor;
    public Setting<TextUtil.Color> commandColor;
    public Setting<Boolean> rainbowPrefix;
    public Setting<Integer> rainbowSpeed;
    public Setting<String> commandBracket;
    public Setting<String> commandBracket2;
    public Setting<Boolean> notifyToggles;
    public Setting<Boolean> notification;
    public Setting<Boolean> magenDavid;
    public Setting<Integer> animationHorizontalTime;
    public Setting<Integer> animationVerticalTime;
    public Setting<RenderingMode> renderingMode;
    private int color;
    public float hue;
    private boolean shouldIncrement;
    private int hitMarkerTimer;
    
    public HUD() {
        super("HUD", "HUD Elements rendered on your screen", Category.CLIENT, true, false, true);
        this.grayNess = this.register(new Setting<>("Gray", true));
        this.renderingUp = this.register(new Setting<>("RenderingUp", true, "Orientation of the HUD-Elements."));
        this.waterMark = this.register(new Setting<>("Watermark", true, "displays watermark"));
        this.waterMark2 = this.register(new Setting<>("Watermark2", false, "displays watermark"));
        this.waterMark3 = this.register(new Setting<>("slolwatermark", false, "displays watermark"));
        this.waterMarkY = this.register(new Setting<>("WatermarkPosY", 2, 0, 20, v -> this.waterMark.getValue()));
        this.waterMark2Y = this.register(new Setting<>("Watermark2PosY", 2, 0, 100, v -> this.waterMark2.getValue()));
        this.waterMark3Y = this.register(new Setting<>("slolwatermarkY", 2, 0, 100, v -> this.waterMark3.getValue()));
        this.arrayList = this.register(new Setting<>("ActiveModules", true, "Lists the active modules."));
        this.hitMarkers = this.register(new Setting<>("HitMarkers", true));
        this.coords = this.register(new Setting<>("Coords", true, "Your current coordinates"));
        this.direction = this.register(new Setting<>("Direction", true, "The Direction you are facing."));
        this.armor = this.register(new Setting<>("Armor", true, "ArmorHUD"));
        this.totems = this.register(new Setting<>("Totems", true, "TotemHUD"));
        this.Crystalsss = this.register(new Setting<>("Crystal", true, "CrystalHUD"));
        this.APSSS = this.register(new Setting<>("APPLE", true, "APPLEHUD"));
        this.XPSSS = this.register(new Setting<>("XP", true, "XPHUD"));
        this.insss = this.register(new Setting<>("distance", 16, 0, 100));
        this.greeter = this.register(new Setting<>("Welcomer", true, "The time"));
        this.speed = this.register(new Setting<>("Speed", true, "Your Speed"));
        this.potions = this.register(new Setting<>("Potions", true, "Your Speed"));
        this.ping = this.register(new Setting<>("Ping", true, "Your response time to the server."));
        this.tps = this.register(new Setting<>("TPS", true, "Ticks per second of the server."));
        this.fps = this.register(new Setting<>("FPS", true, "Your frames per second."));
        this.Friends = this.register(new Setting<>("Friends", false, "Friends"));
        this.customFont = this.register(new Setting<>("Custom Font", false, v -> this.Friends.getValue()));
        this.Friendsx = this.register(new Setting<>("Friendsx", 10, 0, 1000, v -> this.Friends.getValue()));
        this.Friendsy = this.register(new Setting<>("Friendsy", 72, 0, 1000, v -> this.Friends.getValue()));
        this.timer = new Timer();
        this.players = new HashMap<>();
        this.command = this.register(new Setting<>("Command", "Lover"));
        this.bracketColor = this.register(new Setting<>("BracketColor", TextUtil.Color.RED));
        this.commandColor = this.register(new Setting<>("NameColor", TextUtil.Color.GRAY));
        this.rainbowPrefix = this.register(new Setting<>("RainbowPrefix", true));
        this.rainbowSpeed = this.register(new Setting("PrefixSpeed", 20, 0, 100, v -> this.rainbowPrefix.getValue()));
        this.commandBracket = this.register(new Setting<>("Bracket", "["));
        this.commandBracket2 = this.register(new Setting<>("Bracket2", "]"));
        this.notifyToggles = this.register(new Setting<>("Notify", Boolean.TRUE, "notifys"));
        this.notification = this.register(new Setting<>("Notification", Boolean.TRUE, "Notification"));
        this.magenDavid = this.register(new Setting<>("MagenDavid", true, "draws magen david"));
        this.animationHorizontalTime = this.register(new Setting("AnimationHTime", 500, 1, 1000, v -> this.arrayList.getValue()));
        this.animationVerticalTime = this.register(new Setting("AnimationVTime", 50, 1, 500, v -> this.arrayList.getValue()));
        this.renderingMode = this.register(new Setting<>("Ordering", RenderingMode.Length));
        this.setInstance();
    }
    
    public static HUD getInstance() {
        if (HUD.INSTANCE == null) {
            HUD.INSTANCE = new HUD();
        }
        return HUD.INSTANCE;
    }
    
    private void setInstance() {
        HUD.INSTANCE = this;
    }
    
    @Override
    public void onUpdate() {
        if (this.shouldIncrement) {
            ++this.hitMarkerTimer;
        }
        if (this.hitMarkerTimer == 10) {
            this.hitMarkerTimer = 0;
            this.shouldIncrement = false;
        }
    }
    
    private void drawString(final String string, final int x, final int y) {
        Util.mc.fontRenderer.drawStringWithShadow(string, (float)x, (float)y, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB());
    }
    
    private void renderFriends() {
        final List<String> friends = new ArrayList<String>();
        for (final EntityPlayer player : Util.mc.world.playerEntities) {
            if (lover.friendManager.isFriend(player.getName())) {
                friends.add(player.getName());
            }
        }
        int y = this.Friendsy.getValue();
        if (friends.isEmpty()) {
            this.drawString(ChatFormatting.BOLD + "You have no friends on this server", this.Friendsx.getValue(), y);
        }
        else {
            this.drawString(ChatFormatting.BOLD + "The_Friends_list", this.Friendsx.getValue(), y);
            y += 12;
            for (final String friend : friends) {
                this.drawString(friend, this.Friendsx.getValue(), y);
                y += 12;
            }
        }
    }
    
    @Override
    public void onRender2D(final Render2DEvent event) {
        if (Feature.fullNullCheck()) {
            return;
        }
        lover.notificationsManager.draw();
        final int width = this.renderer.scaledWidth;
        final int height = this.renderer.scaledHeight;
        this.color = ColorUtil.toRGBA(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue());
        if (this.waterMark.getValue()) {
            final String string = this.command.getPlannedValue() + " " + "0.8.0";
            if (ClickGui.getInstance().rainbow.getValue()) {
                if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    this.renderer.drawString(string, 2.0f, this.waterMarkY.getValue(), ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                }
                else {
                    final int[] arrayOfInt = { 1 };
                    final char[] stringToCharArray = string.toCharArray();
                    float f = 0.0f;
                    for (final char c : stringToCharArray) {
                        this.renderer.drawString(String.valueOf(c), 2.0f + f, this.waterMarkY.getValue(), ColorUtil.rainbow(arrayOfInt[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                        f += this.renderer.getStringWidth(String.valueOf(c));
                        ++arrayOfInt[0];
                    }
                }
            }
            else {
                this.renderer.drawString(string, 2.0f, this.waterMarkY.getValue(), this.color, true);
            }
        }
        if (this.Friends.getValue()) {
            this.renderFriends();
        }
        if (this.waterMark2.getValue()) {
            final String string = "Oyvey cool person edition";
            if (ClickGui.getInstance().rainbow.getValue()) {
                if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    this.renderer.drawString(string, 2.0f, this.waterMark2Y.getValue(), ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                }
                else {
                    final int[] arrayOfInt = { 1 };
                    final char[] stringToCharArray = string.toCharArray();
                    float f = 0.0f;
                    for (final char c : stringToCharArray) {
                        this.renderer.drawString(String.valueOf(c), 2.0f + f, this.waterMark2Y.getValue(), ColorUtil.rainbow(arrayOfInt[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                        f += this.renderer.getStringWidth(String.valueOf(c));
                        ++arrayOfInt[0];
                    }
                }
            }
            else {
                this.renderer.drawString(string, 2.0f, this.waterMark2Y.getValue(), this.color, true);
            }
        }
        if (this.waterMark3.getValue()) {
            final String string = "Bright in China";
            if (ClickGui.getInstance().rainbow.getValue()) {
                if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    this.renderer.drawString(string, 2.0f, this.waterMark3Y.getValue(), ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                }
                else {
                    final int[] arrayOfInt = { 1 };
                    final char[] stringToCharArray = string.toCharArray();
                    float f = 0.0f;
                    for (final char c : stringToCharArray) {
                        this.renderer.drawString(String.valueOf(c), 2.0f + f, this.waterMark3Y.getValue(), ColorUtil.rainbow(arrayOfInt[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                        f += this.renderer.getStringWidth(String.valueOf(c));
                        ++arrayOfInt[0];
                    }
                }
            }
            else {
                this.renderer.drawString(string, 2.0f, this.waterMark3Y.getValue(), this.color, true);
            }
        }
        final int[] counter1 = { 1 };
        int j = (Util.mc.currentScreen instanceof GuiChat && !this.renderingUp.getValue()) ? 14 : 0;
        if (this.arrayList.getValue()) {
            if (this.renderingUp.getValue()) {
                if (this.renderingMode.getValue() == RenderingMode.ABC) {
                    for (int k = 0; k < lover.moduleManager.sortedModulesABC.size(); ++k) {
                        final String str = lover.moduleManager.sortedModulesABC.get(k);
                        this.renderer.drawString(str, (float)(width - 2 - this.renderer.getStringWidth(str)), (float)(2 + j * 10), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                        ++j;
                        ++counter1[0];
                    }
                }
                else {
                    for (int k = 0; k < lover.moduleManager.sortedModules.size(); ++k) {
                        final Module module = lover.moduleManager.sortedModules.get(k);
                        final String str2 = module.getDisplayName() + ChatFormatting.GRAY + ((module.getDisplayInfo() != null) ? (" [" + ChatFormatting.WHITE + module.getDisplayInfo() + ChatFormatting.GRAY + "]") : "");
                        this.renderer.drawString(str2, (float)(width - 2 - this.renderer.getStringWidth(str2)), (float)(2 + j * 10), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                        ++j;
                        ++counter1[0];
                    }
                }
            }
            else if (this.renderingMode.getValue() == RenderingMode.ABC) {
                for (int k = 0; k < lover.moduleManager.sortedModulesABC.size(); ++k) {
                    final String str = lover.moduleManager.sortedModulesABC.get(k);
                    j += 10;
                    this.renderer.drawString(str, (float)(width - 2 - this.renderer.getStringWidth(str)), (float)(height - j), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    ++counter1[0];
                }
            }
            else {
                for (int k = 0; k < lover.moduleManager.sortedModules.size(); ++k) {
                    final Module module = lover.moduleManager.sortedModules.get(k);
                    final String str2 = module.getDisplayName() + ChatFormatting.GRAY + ((module.getDisplayInfo() != null) ? (" [" + ChatFormatting.WHITE + module.getDisplayInfo() + ChatFormatting.GRAY + "]") : "");
                    j += 10;
                    this.renderer.drawString(str2, (float)(width - 2 - this.renderer.getStringWidth(str2)), (float)(height - j), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    ++counter1[0];
                }
            }
        }
        final String grayString = this.grayNess.getValue() ? String.valueOf(ChatFormatting.GRAY) : "";
        int i = (Util.mc.currentScreen instanceof GuiChat && this.renderingUp.getValue()) ? 13 : (this.renderingUp.getValue() ? -2 : 0);
        if (this.renderingUp.getValue()) {
            if (this.potions.getValue()) {
                final List<PotionEffect> effects = new ArrayList<PotionEffect>(Minecraft.getMinecraft().player.getActivePotionEffects());
                for (final PotionEffect potionEffect : effects) {
                    final String str3 = lover.potionManager.getColoredPotionString(potionEffect);
                    i += 10;
                    this.renderer.drawString(str3, (float)(width - this.renderer.getStringWidth(str3) - 2), (float)(height - 2 - i), potionEffect.getPotion().getLiquidColor(), true);
                }
            }
            if (this.speed.getValue()) {
                final String str2 = grayString + "Speed " + ChatFormatting.WHITE + lover.speedManager.getSpeedKpH() + " km/h";
                i += 10;
                this.renderer.drawString(str2, (float)(width - this.renderer.getStringWidth(str2) - 2), (float)(height - 2 - i), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                ++counter1[0];
            }
            if (this.tps.getValue()) {
                final String str2 = grayString + "TPS " + ChatFormatting.WHITE + lover.serverManager.getTPS();
                i += 10;
                this.renderer.drawString(str2, (float)(width - this.renderer.getStringWidth(str2) - 2), (float)(height - 2 - i), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                ++counter1[0];
            }
            final String fpsText = grayString + "FPS " + ChatFormatting.WHITE + Minecraft.getDebugFPS();
            final String str4 = grayString + "Ping " + ChatFormatting.WHITE + lover.serverManager.getPing();
            if (this.renderer.getStringWidth(str4) > this.renderer.getStringWidth(fpsText)) {
                if (this.ping.getValue()) {
                    i += 10;
                    this.renderer.drawString(str4, (float)(width - this.renderer.getStringWidth(str4) - 2), (float)(height - 2 - i), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    ++counter1[0];
                }
                if (this.fps.getValue()) {
                    i += 10;
                    this.renderer.drawString(fpsText, (float)(width - this.renderer.getStringWidth(fpsText) - 2), (float)(height - 2 - i), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    ++counter1[0];
                }
            }
            else {
                if (this.fps.getValue()) {
                    i += 10;
                    this.renderer.drawString(fpsText, (float)(width - this.renderer.getStringWidth(fpsText) - 2), (float)(height - 2 - i), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    ++counter1[0];
                }
                if (this.ping.getValue()) {
                    i += 10;
                    this.renderer.drawString(str4, (float)(width - this.renderer.getStringWidth(str4) - 2), (float)(height - 2 - i), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    ++counter1[0];
                }
            }
        }
        else {
            if (this.potions.getValue()) {
                final List<PotionEffect> effects = new ArrayList<PotionEffect>(Minecraft.getMinecraft().player.getActivePotionEffects());
                for (final PotionEffect potionEffect : effects) {
                    final String str3 = lover.potionManager.getColoredPotionString(potionEffect);
                    this.renderer.drawString(str3, (float)(width - this.renderer.getStringWidth(str3) - 2), (float)(2 + i++ * 10), potionEffect.getPotion().getLiquidColor(), true);
                }
            }
            if (this.speed.getValue()) {
                final String str2 = grayString + "Speed " + ChatFormatting.WHITE + lover.speedManager.getSpeedKpH() + " km/h";
                this.renderer.drawString(str2, (float)(width - this.renderer.getStringWidth(str2) - 2), (float)(2 + i++ * 10), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                ++counter1[0];
            }
            if (this.tps.getValue()) {
                final String str2 = grayString + "TPS " + ChatFormatting.WHITE + lover.serverManager.getTPS();
                this.renderer.drawString(str2, (float)(width - this.renderer.getStringWidth(str2) - 2), (float)(2 + i++ * 10), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                ++counter1[0];
            }
            final String fpsText = grayString + "FPS " + ChatFormatting.WHITE + Minecraft.getDebugFPS();
            final String str4 = grayString + "Ping " + ChatFormatting.WHITE + lover.serverManager.getPing();
            if (this.renderer.getStringWidth(str4) > this.renderer.getStringWidth(fpsText)) {
                if (this.ping.getValue()) {
                    this.renderer.drawString(str4, (float)(width - this.renderer.getStringWidth(str4) - 2), (float)(2 + i++ * 10), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    ++counter1[0];
                }
                if (this.fps.getValue()) {
                    this.renderer.drawString(fpsText, (float)(width - this.renderer.getStringWidth(fpsText) - 2), (float)(2 + i++ * 10), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    ++counter1[0];
                }
            }
            else {
                if (this.fps.getValue()) {
                    this.renderer.drawString(fpsText, (float)(width - this.renderer.getStringWidth(fpsText) - 2), (float)(2 + i++ * 10), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    ++counter1[0];
                }
                if (this.ping.getValue()) {
                    this.renderer.drawString(str4, (float)(width - this.renderer.getStringWidth(str4) - 2), (float)(2 + i++ * 10), ((boolean)ClickGui.getInstance().rainbow.getValue()) ? ((ClickGui.getInstance().rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    ++counter1[0];
                }
            }
        }
        final boolean inHell = Util.mc.world.getBiome(Util.mc.player.getPosition()).getBiomeName().equals("Hell");
        final int posX = (int)Util.mc.player.posX;
        final int posY = (int)Util.mc.player.posY;
        final int posZ = (int)Util.mc.player.posZ;
        final float nether = inHell ? 8.0f : 0.125f;
        final int hposX = (int)(Util.mc.player.posX * nether);
        final int hposZ = (int)(Util.mc.player.posZ * nether);
        i = ((Util.mc.currentScreen instanceof GuiChat) ? 14 : 0);
        final String coordinates = ChatFormatting.WHITE + "XYZ " + ChatFormatting.RESET + (inHell ? (posX + ", " + posY + ", " + posZ + ChatFormatting.WHITE + " [" + ChatFormatting.RESET + hposX + ", " + hposZ + ChatFormatting.WHITE + "]" + ChatFormatting.RESET) : (posX + ", " + posY + ", " + posZ + ChatFormatting.WHITE + " [" + ChatFormatting.RESET + hposX + ", " + hposZ + ChatFormatting.WHITE + "]"));
        final String direction = this.direction.getValue() ? lover.rotationManager.getDirection4D(false) : "";
        final String coords = this.coords.getValue() ? coordinates : "";
        i += 10;
        if (ClickGui.getInstance().rainbow.getValue()) {
            final String rainbowCoords = this.coords.getValue() ? ("XYZ " + (inHell ? (posX + ", " + posY + ", " + posZ + " [" + hposX + ", " + hposZ + "]") : (posX + ", " + posY + ", " + posZ + " [" + hposX + ", " + hposZ + "]"))) : "";
            if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                this.renderer.drawString(direction, 2.0f, (float)(height - i - 11), ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                this.renderer.drawString(rainbowCoords, 2.0f, (float)(height - i), ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
            }
            else {
                final int[] counter2 = { 1 };
                final char[] stringToCharArray2 = direction.toCharArray();
                float s = 0.0f;
                for (final char c2 : stringToCharArray2) {
                    this.renderer.drawString(String.valueOf(c2), 2.0f + s, (float)(height - i - 11), ColorUtil.rainbow(counter2[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                    s += this.renderer.getStringWidth(String.valueOf(c2));
                    ++counter2[0];
                }
                final int[] counter3 = { 1 };
                final char[] stringToCharArray3 = rainbowCoords.toCharArray();
                float u = 0.0f;
                for (final char c3 : stringToCharArray3) {
                    this.renderer.drawString(String.valueOf(c3), 2.0f + u, (float)(height - i), ColorUtil.rainbow(counter3[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                    u += this.renderer.getStringWidth(String.valueOf(c3));
                    ++counter3[0];
                }
            }
        }
        else {
            this.renderer.drawString(direction, 2.0f, (float)(height - i - 11), this.color, true);
            this.renderer.drawString(coords, 2.0f, (float)(height - i), this.color, true);
        }
        if (this.armor.getValue()) {
            this.renderArmorHUD(true);
        }
        if (this.totems.getValue()) {
            this.renderTotemHUD();
        }
        if (this.greeter.getValue()) {
            this.renderGreeter();
        }
        if (this.Crystalsss.getValue()) {
            this.renderCrystalHUD();
        }
        if (this.XPSSS.getValue()) {
            this.renderxpHUD();
        }
        if (this.APSSS.getValue()) {
            this.renderAPHUD();
        }
        if (!this.hitMarkers.getValue()) {
            return;
        }
        if (this.hitMarkerTimer <= 0) {
            return;
        }
        this.drawHitMarkers();
    }
    
    public Map<String, Integer> getTextRadarPlayers() {
        return EntityUtil.getTextRadarPlayers();
    }
    
    public void drawHitMarkers() {
        final ScaledResolution resolution = new ScaledResolution(Util.mc);
        RenderUtil.drawLine(resolution.getScaledWidth() / 2.0f - 4.0f, resolution.getScaledHeight() / 2.0f - 4.0f, resolution.getScaledWidth() / 2.0f - 8.0f, resolution.getScaledHeight() / 2.0f - 8.0f, 1.0f, ColorUtil.toRGBA(255, 255, 255, 255));
        RenderUtil.drawLine(resolution.getScaledWidth() / 2.0f + 4.0f, resolution.getScaledHeight() / 2.0f - 4.0f, resolution.getScaledWidth() / 2.0f + 8.0f, resolution.getScaledHeight() / 2.0f - 8.0f, 1.0f, ColorUtil.toRGBA(255, 255, 255, 255));
        RenderUtil.drawLine(resolution.getScaledWidth() / 2.0f - 4.0f, resolution.getScaledHeight() / 2.0f + 4.0f, resolution.getScaledWidth() / 2.0f - 8.0f, resolution.getScaledHeight() / 2.0f + 8.0f, 1.0f, ColorUtil.toRGBA(255, 255, 255, 255));
        RenderUtil.drawLine(resolution.getScaledWidth() / 2.0f + 4.0f, resolution.getScaledHeight() / 2.0f + 4.0f, resolution.getScaledWidth() / 2.0f + 8.0f, resolution.getScaledHeight() / 2.0f + 8.0f, 1.0f, ColorUtil.toRGBA(255, 255, 255, 255));
    }
    
    public void renderGreeter() {
        final int width = this.renderer.scaledWidth;
        String text = "Welcome, ";
        if (this.greeter.getValue()) {
            text += Util.mc.player.getDisplayNameString();
        }
        if (ClickGui.getInstance().rainbow.getValue()) {
            if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                this.renderer.drawString(text, width / 2.0f - this.renderer.getStringWidth(text) / 2.0f + 2.0f, 2.0f, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
            }
            else {
                final int[] counter1 = { 1 };
                final char[] stringToCharArray = text.toCharArray();
                float i = 0.0f;
                for (final char c : stringToCharArray) {
                    this.renderer.drawString(String.valueOf(c), width / 2.0f - this.renderer.getStringWidth(text) / 2.0f + 2.0f + i, 2.0f, ColorUtil.rainbow(counter1[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                    i += this.renderer.getStringWidth(String.valueOf(c));
                    ++counter1[0];
                }
            }
        }
        else {
            this.renderer.drawString(text, width / 2.0f - this.renderer.getStringWidth(text) / 2.0f + 2.0f, 2.0f, this.color, true);
        }
    }
    
    public void renderLag() {
        final int width = this.renderer.scaledWidth;
    }
    
    public void renderTotemHUD() {
        final int width = this.renderer.scaledWidth;
        final int height = this.renderer.scaledHeight;
        int totems = HUD.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::getCount).sum();
        if (HUD.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
            totems += HUD.mc.player.getHeldItemOffhand().getCount();
        }
        if (totems > 0) {
            GlStateManager.enableTexture2D();
            final int i = width / 2;
            final int iteration = 0;
            final int y = height - 55 - ((HUD.mc.player.isInWater() && HUD.mc.playerController.gameIsSurvivalOrAdventure()) ? 10 : 0);
            final int x = i - 189 + 180 + 2;
            GlStateManager.enableDepth();
            RenderUtil.itemRender.zLevel = 200.0f;
            RenderUtil.itemRender.renderItemAndEffectIntoGUI(HUD.totem, x, y);
            RenderUtil.itemRender.renderItemOverlayIntoGUI(HUD.mc.fontRenderer, HUD.totem, x, y, "");
            RenderUtil.itemRender.zLevel = 0.0f;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            this.renderer.drawStringWithShadow(totems + "", (float)(x + 19 - 2 - this.renderer.getStringWidth(totems + "")), (float)(y + 9), 16777215);
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
        }
    }
    
    public void renderCrystalHUD() {
        final int width = this.renderer.scaledWidth;
        final int height = this.renderer.scaledHeight;
        int Crystals = HUD.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.END_CRYSTAL).mapToInt(ItemStack::getCount).sum();
        if (HUD.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
            Crystals += HUD.mc.player.getHeldItemOffhand().getCount();
        }
        if (Crystals > 0) {
            GlStateManager.enableTexture2D();
            final int i = width / 2;
            int y = height - 55 - ((HUD.mc.player.isInWater() && HUD.mc.playerController.gameIsSurvivalOrAdventure()) ? 10 : 0);
            final int x = i - 189 + 180 + 2;
            if (this.totems.getValue()) {
                y -= this.insss.getValue();
            }
            GlStateManager.enableDepth();
            RenderUtil.itemRender.zLevel = 200.0f;
            RenderUtil.itemRender.renderItemAndEffectIntoGUI(HUD.Crystal, x, y);
            RenderUtil.itemRender.renderItemOverlayIntoGUI(HUD.mc.fontRenderer, HUD.Crystal, x, y, "");
            RenderUtil.itemRender.zLevel = 0.0f;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            this.renderer.drawStringWithShadow(Crystals + "", (float)(x + 19 - 2 - this.renderer.getStringWidth(Crystals + "")), (float)(y + 9), 16777215);
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
        }
    }
    
    public void renderxpHUD() {
        final int width = this.renderer.scaledWidth;
        final int height = this.renderer.scaledHeight;
        int Crystals = HUD.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.EXPERIENCE_BOTTLE).mapToInt(ItemStack::getCount).sum();
        if (HUD.mc.player.getHeldItemOffhand().getItem() == Items.EXPERIENCE_BOTTLE) {
            Crystals += HUD.mc.player.getHeldItemOffhand().getCount();
        }
        if (Crystals > 0) {
            GlStateManager.enableTexture2D();
            final int i = width / 2;
            int y = height - 55 - ((HUD.mc.player.isInWater() && HUD.mc.playerController.gameIsSurvivalOrAdventure()) ? 10 : 0);
            final int x = i - 189 + 180 + 2;
            if (this.totems.getValue()) {
                y -= this.insss.getValue();
            }
            if (this.Crystalsss.getValue()) {
                y -= this.insss.getValue();
            }
            GlStateManager.enableDepth();
            RenderUtil.itemRender.zLevel = 200.0f;
            RenderUtil.itemRender.renderItemAndEffectIntoGUI(HUD.xp, x, y);
            RenderUtil.itemRender.renderItemOverlayIntoGUI(HUD.mc.fontRenderer, HUD.xp, x, y, "");
            RenderUtil.itemRender.zLevel = 0.0f;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            this.renderer.drawStringWithShadow(Crystals + "", (float)(x + 19 - 2 - this.renderer.getStringWidth(Crystals + "")), (float)(y + 9), 16777215);
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
        }
    }
    
    public void renderAPHUD() {
        final int width = this.renderer.scaledWidth;
        final int height = this.renderer.scaledHeight;
        int Crystals = HUD.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.GOLDEN_APPLE).mapToInt(ItemStack::getCount).sum();
        if (HUD.mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE) {
            Crystals += HUD.mc.player.getHeldItemOffhand().getCount();
        }
        if (Crystals > 0) {
            GlStateManager.enableTexture2D();
            final int i = width / 2;
            final int iteration = 0;
            int y = height - 55 - ((HUD.mc.player.isInWater() && HUD.mc.playerController.gameIsSurvivalOrAdventure()) ? 10 : 0);
            final int x = i - 189 + 180 + 2;
            if (this.totems.getValue()) {
                y -= this.insss.getValue();
            }
            if (this.Crystalsss.getValue()) {
                y -= this.insss.getValue();
            }
            if (this.XPSSS.getValue()) {
                y -= this.insss.getValue();
            }
            GlStateManager.enableDepth();
            RenderUtil.itemRender.zLevel = 200.0f;
            RenderUtil.itemRender.renderItemAndEffectIntoGUI(HUD.ap, x, y);
            RenderUtil.itemRender.renderItemOverlayIntoGUI(HUD.mc.fontRenderer, HUD.ap, x, y, "");
            RenderUtil.itemRender.zLevel = 0.0f;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            this.renderer.drawStringWithShadow(Crystals + "", (float)(x + 19 - 2 - this.renderer.getStringWidth(Crystals + "")), (float)(y + 9), 16777215);
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
        }
    }
    
    public void renderArmorHUD(final boolean percent) {
        final int width = this.renderer.scaledWidth;
        final int height = this.renderer.scaledHeight;
        GlStateManager.enableTexture2D();
        final int i = width / 2;
        int iteration = 0;
        final int y = height - 55 - ((Util.mc.player.isInWater() && Util.mc.playerController.gameIsSurvivalOrAdventure()) ? 10 : 0);
        for (final ItemStack is : Util.mc.player.inventory.armorInventory) {
            ++iteration;
            if (is.isEmpty()) {
                continue;
            }
            final int x = i - 90 + (9 - iteration) * 20 + 2;
            GlStateManager.enableDepth();
            RenderUtil.itemRender.zLevel = 200.0f;
            RenderUtil.itemRender.renderItemAndEffectIntoGUI(is, x, y);
            RenderUtil.itemRender.renderItemOverlayIntoGUI(Util.mc.fontRenderer, is, x, y, "");
            RenderUtil.itemRender.zLevel = 0.0f;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            final String s = (is.getCount() > 1) ? (is.getCount() + "") : "";
            this.renderer.drawStringWithShadow(s, (float)(x + 19 - 2 - this.renderer.getStringWidth(s)), (float)(y + 9), 16777215);
            if (!percent) {
                continue;
            }
            int dmg = 0;
            final int itemDurability = is.getMaxDamage() - is.getItemDamage();
            final float green = (is.getMaxDamage() - (float)is.getItemDamage()) / is.getMaxDamage();
            final float red = 1.0f - green;
            if (percent) {
                dmg = 100 - (int)(red * 100.0f);
            }
            else {
                dmg = itemDurability;
            }
            this.renderer.drawStringWithShadow(dmg + "", (float)(x + 8 - this.renderer.getStringWidth(dmg + "") / 2), (float)(y - 11), ColorUtil.toRGBA((int)(red * 255.0f), (int)(green * 255.0f), 0));
        }
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
    }
    
    public void renderPvpInfo() {
        final String caOn = "CA:" + ChatFormatting.GREEN + " TRUE";
        final String caOff = "CA:" + ChatFormatting.DARK_RED + " FALSE";
        final String atOn = "AT:" + ChatFormatting.GREEN + " TRUE";
        final String atOff = "AT:" + ChatFormatting.DARK_RED + " FALSE";
        final String suOn = "SU:" + ChatFormatting.GREEN + " TRUE";
        final String suOff = "SU:" + ChatFormatting.DARK_RED + " FALSE";
        final String hfOn = "HF:" + ChatFormatting.GREEN + " TRUE";
        final String hfOff = "HF:" + ChatFormatting.DARK_RED + " FALSE";
        final String kOn = "32k:" + ChatFormatting.GREEN + " TRUE";
        final String kOff = "32k:" + ChatFormatting.DARK_RED + " FALSE";
        if (lover.moduleManager.getModuleByName("AutoXin32k").isEnabled()) {
            if (ClickGui.getInstance().rainbow.getValue()) {
                if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    this.renderer.drawString(kOn, 2.0f, 50.0f, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                }
                else {
                    final int[] arrayOfInt = { 1 };
                    final char[] stringToCharArray = kOn.toCharArray();
                    float f = 0.0f;
                    for (final char c : stringToCharArray) {
                        this.renderer.drawString(String.valueOf(c), 2.0f + f, 50.0f, ColorUtil.rainbow(arrayOfInt[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                        f += this.renderer.getStringWidth(String.valueOf(c));
                        ++arrayOfInt[0];
                    }
                }
            }
            else {
                this.renderer.drawString(kOn, 2.0f, 50.0f, this.color, true);
            }
        }
        if (lover.moduleManager.getModuleByName("AutoCrystal").isEnabled()) {
            if (ClickGui.getInstance().rainbow.getValue()) {
                if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    this.renderer.drawString(caOn, 2.0f, 10.0f, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                }
                else {
                    final int[] arrayOfInt = { 1 };
                    final char[] stringToCharArray = caOn.toCharArray();
                    float f = 0.0f;
                    for (final char c : stringToCharArray) {
                        this.renderer.drawString(String.valueOf(c), 2.0f + f, 10.0f, ColorUtil.rainbow(arrayOfInt[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                        f += this.renderer.getStringWidth(String.valueOf(c));
                        ++arrayOfInt[0];
                    }
                }
            }
            else {
                this.renderer.drawString(caOn, 2.0f, 10.0f, this.color, true);
            }
        }
        if (lover.moduleManager.getModuleByName("AutoTrap").isEnabled()) {
            if (ClickGui.getInstance().rainbow.getValue()) {
                if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    this.renderer.drawString(atOn, 2.0f, 20.0f, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                }
                else {
                    final int[] arrayOfInt = { 1 };
                    final char[] stringToCharArray = atOn.toCharArray();
                    float f = 0.0f;
                    for (final char c : stringToCharArray) {
                        this.renderer.drawString(String.valueOf(c), 2.0f + f, 20.0f, ColorUtil.rainbow(arrayOfInt[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                        f += this.renderer.getStringWidth(String.valueOf(c));
                        ++arrayOfInt[0];
                    }
                }
            }
            else {
                this.renderer.drawString(atOn, 2.0f, 20.0f, this.color, true);
            }
        }
        if (lover.moduleManager.getModuleByName("Surround").isEnabled()) {
            if (ClickGui.getInstance().rainbow.getValue()) {
                if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    this.renderer.drawString(suOn, 2.0f, 30.0f, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                }
                else {
                    final int[] arrayOfInt = { 1 };
                    final char[] stringToCharArray = suOn.toCharArray();
                    float f = 0.0f;
                    for (final char c : stringToCharArray) {
                        this.renderer.drawString(String.valueOf(c), 2.0f + f, 30.0f, ColorUtil.rainbow(arrayOfInt[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                        f += this.renderer.getStringWidth(String.valueOf(c));
                        ++arrayOfInt[0];
                    }
                }
            }
            else {
                this.renderer.drawString(suOn, 2.0f, 30.0f, this.color, true);
            }
        }
        if (lover.moduleManager.getModuleByName("HoleFill").isEnabled()) {
            if (ClickGui.getInstance().rainbow.getValue()) {
                if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    this.renderer.drawString(hfOn, 2.0f, 40.0f, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                }
                else {
                    final int[] arrayOfInt = { 1 };
                    final char[] stringToCharArray = hfOn.toCharArray();
                    float f = 0.0f;
                    for (final char c : stringToCharArray) {
                        this.renderer.drawString(String.valueOf(c), 2.0f + f, 40.0f, ColorUtil.rainbow(arrayOfInt[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                        f += this.renderer.getStringWidth(String.valueOf(c));
                        ++arrayOfInt[0];
                    }
                }
            }
            else {
                this.renderer.drawString(hfOn, 2.0f, 40.0f, this.color, true);
            }
        }
        if (lover.moduleManager.getModuleByName("AutoCrystal").isDisabled()) {
            if (ClickGui.getInstance().rainbow.getValue()) {
                if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    this.renderer.drawString(caOff, 2.0f, 10.0f, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                }
                else {
                    final int[] arrayOfInt = { 1 };
                    final char[] stringToCharArray = caOff.toCharArray();
                    float f = 0.0f;
                    for (final char c : stringToCharArray) {
                        this.renderer.drawString(String.valueOf(c), 2.0f + f, 10.0f, ColorUtil.rainbow(arrayOfInt[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                        f += this.renderer.getStringWidth(String.valueOf(c));
                        ++arrayOfInt[0];
                    }
                }
            }
            else {
                this.renderer.drawString(caOff, 2.0f, 10.0f, this.color, true);
            }
        }
        if (lover.moduleManager.getModuleByName("AutoTrap").isDisabled()) {
            if (ClickGui.getInstance().rainbow.getValue()) {
                if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    this.renderer.drawString(atOff, 2.0f, 20.0f, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                }
                else {
                    final int[] arrayOfInt = { 1 };
                    final char[] stringToCharArray = atOff.toCharArray();
                    float f = 0.0f;
                    for (final char c : stringToCharArray) {
                        this.renderer.drawString(String.valueOf(c), 2.0f + f, 20.0f, ColorUtil.rainbow(arrayOfInt[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                        f += this.renderer.getStringWidth(String.valueOf(c));
                        ++arrayOfInt[0];
                    }
                }
            }
            else {
                this.renderer.drawString(atOff, 2.0f, 20.0f, this.color, true);
            }
        }
        if (lover.moduleManager.getModuleByName("Surround").isDisabled()) {
            if (ClickGui.getInstance().rainbow.getValue()) {
                if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    this.renderer.drawString(suOff, 2.0f, 30.0f, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                }
                else {
                    final int[] arrayOfInt = { 1 };
                    final char[] stringToCharArray = suOff.toCharArray();
                    float f = 0.0f;
                    for (final char c : stringToCharArray) {
                        this.renderer.drawString(String.valueOf(c), 2.0f + f, 30.0f, ColorUtil.rainbow(arrayOfInt[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                        f += this.renderer.getStringWidth(String.valueOf(c));
                        ++arrayOfInt[0];
                    }
                }
            }
            else {
                this.renderer.drawString(suOff, 2.0f, 30.0f, this.color, true);
            }
        }
        if (lover.moduleManager.getModuleByName("HoleFill").isDisabled()) {
            if (ClickGui.getInstance().rainbow.getValue()) {
                if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    this.renderer.drawString(hfOff, 2.0f, 40.0f, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                }
                else {
                    final int[] arrayOfInt = { 1 };
                    final char[] stringToCharArray = hfOff.toCharArray();
                    float f = 0.0f;
                    for (final char c : stringToCharArray) {
                        this.renderer.drawString(String.valueOf(c), 2.0f + f, 40.0f, ColorUtil.rainbow(arrayOfInt[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                        f += this.renderer.getStringWidth(String.valueOf(c));
                        ++arrayOfInt[0];
                    }
                }
            }
            else {
                this.renderer.drawString(hfOff, 2.0f, 40.0f, this.color, true);
            }
        }
        if (lover.moduleManager.getModuleByName("AutoXin32k").isDisabled()) {
            if (ClickGui.getInstance().rainbow.getValue()) {
                if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    this.renderer.drawString(kOff, 2.0f, 50.0f, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                }
                else {
                    final int[] arrayOfInt = { 1 };
                    final char[] stringToCharArray = kOff.toCharArray();
                    float f = 0.0f;
                    for (final char c : stringToCharArray) {
                        this.renderer.drawString(String.valueOf(c), 2.0f + f, 50.0f, ColorUtil.rainbow(arrayOfInt[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                        f += this.renderer.getStringWidth(String.valueOf(c));
                        ++arrayOfInt[0];
                    }
                }
            }
            else {
                this.renderer.drawString(kOff, 2.0f, 50.0f, this.color, true);
            }
        }
        if (lover.moduleManager.getModuleByName("AutoCrystal+").isEnabled()) {
            if (ClickGui.getInstance().rainbow.getValue()) {
                if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    this.renderer.drawString(caOn, 2.0f, 10.0f, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                }
                else {
                    final int[] arrayOfInt = { 1 };
                    final char[] stringToCharArray = caOn.toCharArray();
                    float f = 0.0f;
                    for (final char c : stringToCharArray) {
                        this.renderer.drawString(String.valueOf(c), 2.0f + f, 10.0f, ColorUtil.rainbow(arrayOfInt[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                        f += this.renderer.getStringWidth(String.valueOf(c));
                        ++arrayOfInt[0];
                    }
                }
            }
            else {
                this.renderer.drawString(caOn, 2.0f, 10.0f, this.color, true);
            }
        }
        if (lover.moduleManager.getModuleByName("AutoCrystal+").isDisabled()) {
            if (ClickGui.getInstance().rainbow.getValue()) {
                if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    this.renderer.drawString(caOff, 2.0f, 10.0f, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                }
                else {
                    final int[] arrayOfInt = { 1 };
                    final char[] stringToCharArray = caOff.toCharArray();
                    float f = 0.0f;
                    for (final char c : stringToCharArray) {
                        this.renderer.drawString(String.valueOf(c), 2.0f + f, 10.0f, ColorUtil.rainbow(arrayOfInt[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                        f += this.renderer.getStringWidth(String.valueOf(c));
                        ++arrayOfInt[0];
                    }
                }
            }
            else {
                this.renderer.drawString(caOff, 2.0f, 10.0f, this.color, true);
            }
        }
        if (lover.moduleManager.getModuleByName("StormCrystal").isEnabled()) {
            if (ClickGui.getInstance().rainbow.getValue()) {
                if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    this.renderer.drawString(caOn, 2.0f, 10.0f, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                }
                else {
                    final int[] arrayOfInt = { 1 };
                    final char[] stringToCharArray = caOn.toCharArray();
                    float f = 0.0f;
                    for (final char c : stringToCharArray) {
                        this.renderer.drawString(String.valueOf(c), 2.0f + f, 10.0f, ColorUtil.rainbow(arrayOfInt[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                        f += this.renderer.getStringWidth(String.valueOf(c));
                        ++arrayOfInt[0];
                    }
                }
            }
            else {
                this.renderer.drawString(caOn, 2.0f, 10.0f, this.color, true);
            }
        }
        if (lover.moduleManager.getModuleByName("StormCrystal").isDisabled()) {
            if (ClickGui.getInstance().rainbow.getValue()) {
                if (ClickGui.getInstance().rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    this.renderer.drawString(caOff, 2.0f, 10.0f, ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                }
                else {
                    final int[] arrayOfInt = { 1 };
                    final char[] stringToCharArray = caOff.toCharArray();
                    float f = 0.0f;
                    for (final char c : stringToCharArray) {
                        this.renderer.drawString(String.valueOf(c), 2.0f + f, 10.0f, ColorUtil.rainbow(arrayOfInt[0] * ClickGui.getInstance().rainbowHue.getValue()).getRGB(), true);
                        f += this.renderer.getStringWidth(String.valueOf(c));
                        ++arrayOfInt[0];
                    }
                }
            }
            else {
                this.renderer.drawString(caOff, 2.0f, 10.0f, this.color, true);
            }
        }
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayer(final AttackEntityEvent event) {
        this.shouldIncrement = true;
    }
    
    @Override
    public void onLoad() {
        lover.commandManager.setClientMessage(this.getCommandMessage());
    }
    
    @SubscribeEvent
    public void onSettingChange(final ClientEvent event) {
        if (event.getStage() == 2 && this.equals(event.getSetting().getFeature())) {
            lover.commandManager.setClientMessage(this.getCommandMessage());
        }
    }
    
    public String getCommandMessage() {
        if (this.rainbowPrefix.getPlannedValue()) {
            final StringBuilder stringBuilder = new StringBuilder(this.getRawCommandMessage());
            stringBuilder.insert(0, "\u00a7+");
            stringBuilder.append("\u00a7+r");
            return stringBuilder.toString();
        }
        return TextUtil.coloredString(this.commandBracket.getPlannedValue(), this.bracketColor.getPlannedValue()) + TextUtil.coloredString(this.command.getPlannedValue(), this.commandColor.getPlannedValue()) + TextUtil.coloredString(this.commandBracket2.getPlannedValue(), this.bracketColor.getPlannedValue());
    }
    
    public String getRainbowCommandMessage() {
        final StringBuilder stringBuilder = new StringBuilder(this.getRawCommandMessage());
        stringBuilder.insert(0, "\u00a7+");
        stringBuilder.append("\u00a7r");
        return stringBuilder.toString();
    }
    
    public String getRawCommandMessage() {
        return this.commandBracket.getValue() + this.command.getValue() + this.commandBracket2.getValue();
    }
    
    public void drawTextRadar(final int yOffset) {
        if (!this.players.isEmpty()) {
            int y = this.renderer.getFontHeight() + 7 + yOffset;
            for (final Map.Entry<String, Integer> player : this.players.entrySet()) {
                final String text = player.getKey() + " ";
                final int textheight = this.renderer.getFontHeight() + 1;
                this.renderer.drawString(text, 2.0f, (float)y, this.color, true);
                y += textheight;
            }
        }
    }
    
    static {
        box = new ResourceLocation("textures/gui/container/shulker_box.png");
        totem = new ItemStack(Items.TOTEM_OF_UNDYING);
        Crystal = new ItemStack(Items.END_CRYSTAL);
        xp = new ItemStack(Items.EXPERIENCE_BOTTLE);
        ap = new ItemStack(Items.GOLDEN_APPLE);
        HUD.INSTANCE = new HUD();
    }
    
    public enum RenderingMode
    {
        Length, 
        ABC;
    }
}
