package dev.blackhig.zhebushigudu.lover.features.modules.combat;

import dev.blackhig.zhebushigudu.lover.event.events.PacketEvent;
import dev.blackhig.zhebushigudu.lover.features.command.Command;
import dev.blackhig.zhebushigudu.lover.features.modules.Module;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.util.Util;
import net.minecraft.item.*;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EliteBow extends Module
{
    private long lastShootTime;
    public Setting<Boolean> Bows;
    public Setting<Boolean> pearls;
    public Setting<Boolean> eggs;
    public Setting<Boolean> snowballs;
    public Setting<Integer> Timeout;
    public Setting<Integer> spoofs;
    public Setting<Boolean> bypass;
    public Setting<Boolean> debug;
    
    public EliteBow() {
        super("EliteBow", "Uno hitter w bows", Category.MISC, true, false, false);
        this.Bows = this.register(new Setting<>("Bows", true));
        this.pearls = this.register(new Setting<>("Pearls", true));
        this.eggs = this.register(new Setting<>("Eggs", true));
        this.snowballs = this.register(new Setting<>("SnowBallz", true));
        this.Timeout = this.register(new Setting<>("Timeout", 5000, 100, 20000));
        this.spoofs = this.register(new Setting<>("Spoofs", 10, 1, 300));
        this.bypass = this.register(new Setting<>("Bypass", false));
        this.debug = this.register(new Setting<>("Debug", false));
    }
    
    @Override
    public void onEnable() {
        if (this.isEnabled()) {
            this.lastShootTime = System.currentTimeMillis();
        }
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (event.getStage() != 0) {
            return;
        }
        if (event.getPacket() instanceof CPacketPlayerDigging) {
            final CPacketPlayerDigging packet = event.getPacket();
            if (packet.getAction() == CPacketPlayerDigging.Action.RELEASE_USE_ITEM) {
                final ItemStack handStack = Util.mc.player.getHeldItem(EnumHand.MAIN_HAND);
                if (!handStack.isEmpty()) {
                    handStack.getItem();
                    if (handStack.getItem() instanceof ItemBow && this.Bows.getValue()) {
                        this.doSpoofs();
                        if (this.debug.getValue()) {
                            Command.sendMessage("trying to spoof");
                        }
                    }
                }
            }
        }
        else if (event.getPacket() instanceof CPacketPlayerTryUseItem) {
            final CPacketPlayerTryUseItem packet2 = event.getPacket();
            if (packet2.getHand() == EnumHand.MAIN_HAND) {
                final ItemStack handStack = Util.mc.player.getHeldItem(EnumHand.MAIN_HAND);
                if (!handStack.isEmpty()) {
                    handStack.getItem();
                    if (handStack.getItem() instanceof ItemEgg && this.eggs.getValue()) {
                        this.doSpoofs();
                    } else if (handStack.getItem() instanceof ItemEnderPearl && this.pearls.getValue()) {
                        this.doSpoofs();
                    } else if (handStack.getItem() instanceof ItemSnowball && this.snowballs.getValue()) {
                        this.doSpoofs();
                    }
                }
            }
        }
    }
    
    private void doSpoofs() {
        if (System.currentTimeMillis() - this.lastShootTime >= this.Timeout.getValue()) {
            this.lastShootTime = System.currentTimeMillis();
            Util.mc.player.connection.sendPacket(new CPacketEntityAction(Util.mc.player, CPacketEntityAction.Action.START_SPRINTING));
            for (int index = 0; index < this.spoofs.getValue(); ++index) {
                if (this.bypass.getValue()) {
                    Util.mc.player.connection.sendPacket(new CPacketPlayer.Position(Util.mc.player.posX, Util.mc.player.posY + 1.0E-10, Util.mc.player.posZ, false));
                    Util.mc.player.connection.sendPacket(new CPacketPlayer.Position(Util.mc.player.posX, Util.mc.player.posY - 1.0E-10, Util.mc.player.posZ, true));
                }
                else {
                    Util.mc.player.connection.sendPacket(new CPacketPlayer.Position(Util.mc.player.posX, Util.mc.player.posY - 1.0E-10, Util.mc.player.posZ, true));
                    Util.mc.player.connection.sendPacket(new CPacketPlayer.Position(Util.mc.player.posX, Util.mc.player.posY + 1.0E-10, Util.mc.player.posZ, false));
                }
            }
            if (this.debug.getValue()) {
                Command.sendMessage("Spoofed");
            }
        }
    }
}
