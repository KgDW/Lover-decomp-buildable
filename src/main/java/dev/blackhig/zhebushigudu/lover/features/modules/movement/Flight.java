package dev.blackhig.zhebushigudu.lover.features.modules.movement;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.client.Minecraft;
import dev.blackhig.zhebushigudu.lover.util.EntityUtil;
import dev.blackhig.zhebushigudu.lover.util.Util;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.features.modules.Module;

import java.util.Objects;

public class Flight extends Module
{
    public Setting<Float> speed;
    public Setting<FlightMode> mode;
    
    public Flight() {
        super("Flight", "Flight.", Category.MOVEMENT, true, false, false);
        this.speed = this.register(new Setting<>("Speed", 10.0f, 0.0f, 50.0f));
        this.mode = this.register(new Setting<>("Mode", FlightMode.VANILLA));
    }
    
    @Override
    public void onEnable() {
        if (Util.mc.player == null) {
            return;
        }
        if (Objects.requireNonNull(this.mode.getValue()) == FlightMode.VANILLA) {
            Util.mc.player.capabilities.isFlying = true;
            if (Util.mc.player.capabilities.isCreativeMode) {
                return;
            }
            Util.mc.player.capabilities.allowFlying = true;
        }
    }
    
    @Override
    public void onUpdate() {
        switch (this.mode.getValue()) {
            case STATIC: {
                Util.mc.player.capabilities.isFlying = false;
                Util.mc.player.motionX = 0.0;
                Util.mc.player.motionY = 0.0;
                Util.mc.player.motionZ = 0.0;
                Util.mc.player.jumpMovementFactor = this.speed.getValue();
                if (Util.mc.gameSettings.keyBindJump.isKeyDown()) {
                    final EntityPlayerSP player = Util.mc.player;
                    player.motionY += this.speed.getValue();
                }
                if (Util.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    final EntityPlayerSP player2 = Util.mc.player;
                    player2.motionY -= this.speed.getValue();
                    break;
                }
                break;
            }
            case VANILLA: {
                Util.mc.player.capabilities.setFlySpeed(this.speed.getValue() / 100.0f);
                Util.mc.player.capabilities.isFlying = true;
                if (Util.mc.player.capabilities.isCreativeMode) {
                    return;
                }
                Util.mc.player.capabilities.allowFlying = true;
                break;
            }
            case PACKET: {
                final boolean forward = Util.mc.gameSettings.keyBindForward.isKeyDown();
                final boolean left = Util.mc.gameSettings.keyBindLeft.isKeyDown();
                final boolean right = Util.mc.gameSettings.keyBindRight.isKeyDown();
                final boolean back = Util.mc.gameSettings.keyBindBack.isKeyDown();
                int angle;
                if (left && right) {
                    angle = (forward ? 0 : (back ? 180 : -1));
                }
                else if (forward && back) {
                    angle = (left ? -90 : (right ? 90 : -1));
                }
                else {
                    angle = (left ? -90 : (right ? 90 : 0));
                    if (forward) {
                        angle /= 2;
                    }
                    else if (back) {
                        angle = 180 - angle / 2;
                    }
                }
                if (angle != -1 && (forward || left || right || back)) {
                    final float yaw = Util.mc.player.rotationYaw + angle;
                    Util.mc.player.motionX = EntityUtil.getRelativeX(yaw) * 0.20000000298023224;
                    Util.mc.player.motionZ = EntityUtil.getRelativeZ(yaw) * 0.20000000298023224;
                }
                Util.mc.player.motionY = 0.0;
                Util.mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(Util.mc.player.posX + Util.mc.player.motionX, Util.mc.player.posY + (Minecraft.getMinecraft().gameSettings.keyBindJump.isKeyDown() ? 0.0622 : 0.0) - (Minecraft.getMinecraft().gameSettings.keyBindSneak.isKeyDown() ? 0.0622 : 0.0), Util.mc.player.posZ + Util.mc.player.motionZ, Util.mc.player.rotationYaw, Util.mc.player.rotationPitch, false));
                Util.mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(Util.mc.player.posX + Util.mc.player.motionX, Util.mc.player.posY - 42069.0, Util.mc.player.posZ + Util.mc.player.motionZ, Util.mc.player.rotationYaw, Util.mc.player.rotationPitch, true));
                break;
            }
        }
    }
    
    @Override
    public void onDisable() {
        if (Objects.requireNonNull(this.mode.getValue()) == FlightMode.VANILLA) {
            Util.mc.player.capabilities.isFlying = false;
            Util.mc.player.capabilities.setFlySpeed(0.05f);
            if (Util.mc.player.capabilities.isCreativeMode) {
                return;
            }
            Util.mc.player.capabilities.allowFlying = false;
        }
    }

    public enum FlightMode
    {
        VANILLA, 
        STATIC, 
        PACKET
    }
}
