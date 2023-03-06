package dev.blackhig.zhebushigudu.lover.features.modules.combat;

import dev.blackhig.zhebushigudu.lover.event.events.PacketEvent;
import dev.blackhig.zhebushigudu.lover.features.modules.Module;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.util.EntityUtil;
import dev.blackhig.zhebushigudu.lover.util.Timer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;

public class Criticals extends Module
{
    private final Setting<Integer> packets;
    private final Setting<Integer> desyncDelay;
    private final Setting<Boolean> onlySharp;
    private final Timer timer;

    public Criticals() {
        super("Criticals", "Scores criticals for you", Category.COMBAT, true, false, false);
        this.packets = this.register(new Setting<>("Packets", 2, 1, 5));
        this.desyncDelay = this.register(new Setting<>("DesyncDelay", 10, 0, 500));
        this.onlySharp = this.register(new Setting<>("SwordOnly", true));
        this.timer = new Timer();
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        final CPacketUseEntity packet;
        if (event.getPacket() instanceof CPacketUseEntity && (packet = event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK) {
            if (fullNullCheck()) {
                return;
            }
            if (this.onlySharp.getValue() && !EntityUtil.holdingWeapon(Criticals.mc.player)) {
                return;
            }
            this.getClass();
            if (!this.timer.passedMs(this.desyncDelay.getValue())) {
                return;
            }
            if (Criticals.mc.player.onGround && !Criticals.mc.gameSettings.keyBindJump.isKeyDown() && packet.getEntityFromWorld(Criticals.mc.world) instanceof EntityLivingBase && !Criticals.mc.player.isInWater() && !Criticals.mc.player.isInLava()) {
                switch (this.packets.getValue()) {
                    case 1: {
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + 0.10000000149011612, Criticals.mc.player.posZ, false));
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY, Criticals.mc.player.posZ, false));
                        break;
                    }
                    case 2: {
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + 0.0625101, Criticals.mc.player.posZ, false));
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY, Criticals.mc.player.posZ, false));
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + 1.1E-5, Criticals.mc.player.posZ, false));
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY, Criticals.mc.player.posZ, false));
                        break;
                    }
                    case 3: {
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + 0.0625101, Criticals.mc.player.posZ, false));
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY, Criticals.mc.player.posZ, false));
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + 0.0125, Criticals.mc.player.posZ, false));
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY, Criticals.mc.player.posZ, false));
                        break;
                    }
                    case 4: {
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + 0.05, Criticals.mc.player.posZ, false));
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY, Criticals.mc.player.posZ, false));
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + 0.03, Criticals.mc.player.posZ, false));
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY, Criticals.mc.player.posZ, false));
                        break;
                    }
                    case 5: {
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + 0.1625, Criticals.mc.player.posZ, false));
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY, Criticals.mc.player.posZ, false));
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + 4.0E-6, Criticals.mc.player.posZ, false));
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY, Criticals.mc.player.posZ, false));
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + 1.0E-6, Criticals.mc.player.posZ, false));
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY, Criticals.mc.player.posZ, false));
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer());
                        Criticals.mc.player.onCriticalHit(Objects.requireNonNull(packet.getEntityFromWorld(Criticals.mc.world)));
                        break;
                    }
                }
                this.timer.reset();
            }
        }
    }
    
    @Override
    public String getDisplayInfo() {
        return "Packet";
    }
}
