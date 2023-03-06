package dev.blackhig.zhebushigudu.lover;

import java.awt.Toolkit;
import org.apache.logging.log4j.LogManager;
import org.lwjgl.opengl.Display;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import java.awt.TrayIcon;
import java.awt.Image;
import java.awt.SystemTray;
import dev.blackhig.zhebushigudu.lover.manager.TextManager;
import dev.blackhig.zhebushigudu.lover.manager.TargetManager;
import dev.blackhig.zhebushigudu.lover.manager.EventManager;
import dev.blackhig.zhebushigudu.lover.manager.ServerManager;
import dev.blackhig.zhebushigudu.lover.manager.ConfigManager;
import dev.blackhig.zhebushigudu.lover.manager.FileManager;
import dev.blackhig.zhebushigudu.lover.manager.ReloadManager;
import dev.blackhig.zhebushigudu.lover.manager.SpeedManager;
import dev.blackhig.zhebushigudu.lover.manager.PositionManager;
import dev.blackhig.zhebushigudu.lover.manager.RotationManager;
import dev.blackhig.zhebushigudu.lover.manager.PotionManager;
import dev.blackhig.zhebushigudu.lover.manager.InventoryManager;
import dev.blackhig.zhebushigudu.lover.manager.HoleManager;
import dev.blackhig.zhebushigudu.lover.manager.ColorManager;
import dev.blackhig.zhebushigudu.lover.manager.PacketManager;
import dev.blackhig.zhebushigudu.lover.manager.ModuleManager;
import dev.blackhig.zhebushigudu.lover.manager.FriendManager;
import dev.blackhig.zhebushigudu.lover.notification.NotificationsManager;
import dev.blackhig.zhebushigudu.lover.manager.CommandManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.fml.common.Mod;

@Mod(modid = "lover", name = "Lover", version = "0.8.0")
public class lover
{
    public static final String MODID = "lover";
    public static final String MODNAME = "Lover";
    public static final String MODVER = "0.8.0";
    public static final String ID = "0.8.0";
    public static final Logger LOGGER;
    public static CommandManager commandManager;
    public static NotificationsManager notificationsManager;
    public static FriendManager friendManager;
    public static ModuleManager moduleManager;
    public static PacketManager packetManager;
    public static ColorManager colorManager;
    public static HoleManager holeManager;
    public static InventoryManager inventoryManager;
    public static PotionManager potionManager;
    public static RotationManager rotationManager;
    public static PositionManager positionManager;
    public static SpeedManager speedManager;
    public static ReloadManager reloadManager;
    public static FileManager fileManager;
    public static ConfigManager configManager;
    public static ServerManager serverManager;
    public static EventManager eventManager;
    public static TargetManager targetManager;
    public static TextManager textManager;
    @Mod.Instance
    public static lover INSTANCE;
    private static boolean unloaded;
    public SystemTray tray;
    public static Image image;
    public static TrayIcon trayIcon;
    
    public lover() {
        this.tray = SystemTray.getSystemTray();
    }
    
    public static void load() {
        lover.LOGGER.info("\n\nLoading lover by blackhig");
        lover.unloaded = false;
        if (lover.reloadManager != null) {
            lover.reloadManager.unload();
            lover.reloadManager = null;
        }
        lover.notificationsManager = new NotificationsManager();
        lover.textManager = new TextManager();
        lover.commandManager = new CommandManager();
        lover.friendManager = new FriendManager();
        lover.moduleManager = new ModuleManager();
        lover.rotationManager = new RotationManager();
        lover.packetManager = new PacketManager();
        lover.eventManager = new EventManager();
        lover.speedManager = new SpeedManager();
        lover.potionManager = new PotionManager();
        lover.inventoryManager = new InventoryManager();
        lover.serverManager = new ServerManager();
        lover.fileManager = new FileManager();
        lover.colorManager = new ColorManager();
        lover.positionManager = new PositionManager();
        lover.configManager = new ConfigManager();
        lover.holeManager = new HoleManager();
        lover.targetManager = new TargetManager();
        lover.LOGGER.info("Managers loaded.");
        lover.moduleManager.init();
        lover.LOGGER.info("Modules loaded.");
        lover.configManager.init();
        lover.eventManager.init();
        lover.LOGGER.info("EventManager loaded.");
        lover.textManager.init(true);
        lover.moduleManager.onLoad();
        lover.LOGGER.info("lover successfully loaded!\n");
    }
    
    public static void unload(final boolean unload) {
        lover.LOGGER.info("\n\nUnloading lover by zhebushigudu");
        if (unload) {
            (lover.reloadManager = new ReloadManager()).init((lover.commandManager != null) ? lover.commandManager.getPrefix() : ".");
        }
        onUnload();
        lover.notificationsManager = null;
        lover.eventManager = null;
        lover.friendManager = null;
        lover.speedManager = null;
        lover.holeManager = null;
        lover.positionManager = null;
        lover.rotationManager = null;
        lover.configManager = null;
        lover.commandManager = null;
        lover.colorManager = null;
        lover.serverManager = null;
        lover.fileManager = null;
        lover.potionManager = null;
        lover.inventoryManager = null;
        lover.moduleManager = null;
        lover.textManager = null;
        lover.LOGGER.info("lover unloaded!\n");
    }
    
    public static void reload() {
        unload(false);
        load();
    }
    
    public static void onUnload() {
        if (!lover.unloaded) {
            lover.eventManager.onUnload();
            lover.moduleManager.onUnload();
            lover.configManager.saveConfig(lover.configManager.config.replaceFirst("Lover/", ""));
            lover.moduleManager.onUnloadPost();
            lover.unloaded = true;
        }
    }
    
    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        Display.setTitle("Lover v0.8.0");
        load();
    }
    
    static {
        LOGGER = LogManager.getLogger("Lover");
        lover.unloaded = false;
        lover.image = Toolkit.getDefaultToolkit().createImage(lover.class.getResource("/assets/minecraft/textures/icon-32x.png"));
        lover.trayIcon = new TrayIcon(lover.image, "Lover");
    }
}
