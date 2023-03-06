package dev.blackhig.zhebushigudu.lover.features.modules.misc;

import dev.blackhig.zhebushigudu.lover.features.modules.Module;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class AutoGG extends Module
{
    private static AutoGG INSTANCE;
    public Setting<String> custom;
    public Setting<String> test;
    private ConcurrentHashMap<String, Integer> targetedPlayers;
    
    public AutoGG() {
        super("AutoGG", "Sends msg after you kill someone", Category.MISC, true, false, false);
        this.custom = this.register(new Setting("Custom", "Nigga-Hack.me"));
        this.test = this.register(new Setting("Test", "null"));
        this.targetedPlayers = null;
        this.setInstance();
    }
    
    private void setInstance() {
        AutoGG.INSTANCE = this;
    }
    
    @Override
    public void onEnable() {
        this.targetedPlayers = new ConcurrentHashMap<>();
    }
    
    @Override
    public void onDisable() {
        this.targetedPlayers = null;
    }
    
    @Override
    public void onUpdate() {
        if (nullCheck()) {
            return;
        }
        if (this.targetedPlayers == null) {
            this.targetedPlayers = new ConcurrentHashMap<>();
        }
        for (final Entity entity : AutoGG.mc.world.getLoadedEntityList()) {
            final EntityPlayer player;
            if (entity instanceof EntityPlayer && (player = (EntityPlayer)entity).getHealth() <= 0.0f) {
                final String name2;
                if (!this.shouldAnnounce(name2 = player.getName())) {
                    continue;
                }
                this.doAnnounce(name2);
                break;
            }
        }
        this.targetedPlayers.forEach((name, timeout) -> {
            if (timeout <= 0) {
                this.targetedPlayers.remove(name);
            }
            else {
                this.targetedPlayers.put(name, timeout - 1);
            }
        });
    }
    
    @SubscribeEvent
    public void onLeavingDeathEvent(final LivingDeathEvent event) {
        if (AutoGG.mc.player == null) {
            return;
        }
        if (this.targetedPlayers == null) {
            this.targetedPlayers = new ConcurrentHashMap<>();
        }
        final EntityLivingBase entity;
        if ((entity = event.getEntityLiving()) == null) {
            return;
        }
        if (!(entity instanceof EntityPlayer)) {
            return;
        }
        final EntityPlayer player = (EntityPlayer)entity;
        if (player.getHealth() > 0.0f) {
            return;
        }
        final String name = player.getName();
        if (this.shouldAnnounce(name)) {
            this.doAnnounce(name);
        }
    }
    
    private boolean shouldAnnounce(final String name) {
        return this.targetedPlayers.containsKey(name);
    }
    
    private void doAnnounce(final String name) {
        this.targetedPlayers.remove(name);
        AutoGG.mc.player.connection.sendPacket(new CPacketChatMessage(this.custom.getValue()));
        int u = 0;
        for (int i = 0; i < 10; ++i) {
            ++u;
        }
        if (!this.test.getValue().equalsIgnoreCase("null")) {
            AutoGG.mc.player.connection.sendPacket(new CPacketChatMessage(this.test.getValue()));
        }
    }
    
    public void addTargetedPlayer(final String name) {
        if (Objects.equals(name, AutoGG.mc.player.getName())) {
            return;
        }
        if (this.targetedPlayers == null) {
            this.targetedPlayers = new ConcurrentHashMap<>();
        }
        this.targetedPlayers.put(name, 20);
    }
    
    static {
        AutoGG.INSTANCE = new AutoGG();
    }
}
