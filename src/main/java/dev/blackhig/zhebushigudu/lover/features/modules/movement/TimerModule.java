package dev.blackhig.zhebushigudu.lover.features.modules.movement;

import dev.blackhig.zhebushigudu.lover.util.RandomUtil;
import dev.blackhig.zhebushigudu.lover.util.EntityUtil;
import dev.blackhig.zhebushigudu.lover.event.events.a.UpdateWalkingPlayerEventTwo;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.network.play.client.CPacketPlayer;
import dev.blackhig.zhebushigudu.lover.event.events.PacketEvent;
import dev.blackhig.zhebushigudu.lover.util.Timer;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.features.modules.Module;

public class TimerModule extends Module
{
    Setting<Double> tickNormal;
    Setting<Boolean> packetControl;
    Timer packetListReset;
    private int normalLookPos;
    private int rotationMode;
    private int normalPos;
    private float lastPitch;
    private float lastYaw;
    
    public TimerModule() {
        super("Timer", "Timer", Category.MOVEMENT, true, false, false);
        this.tickNormal = this.register(new Setting<>("Speed", 1.2, 1.0, 10.0));
        this.packetControl = this.register(new Setting<>("PacketControl", true));
        this.packetListReset = new Timer();
    }
    
    @SubscribeEvent
    public final void onPacketSend(final PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketPlayer.Position && this.rotationMode == 1) {
            if (this.normalPos > 20) {
                this.rotationMode = 2;
            }
        }
        else if (event.getPacket() instanceof CPacketPlayer.PositionRotation && this.rotationMode == 2) {
            if (this.normalLookPos > 20) {
                this.rotationMode = 1;
            }
        }
    }
    
    @Override
    public void onDisable() {
        if (Module.fullNullCheck()) {
            return;
        }
        Module.mc.timer.tickLength = 50.0f;
        this.packetListReset.reset();
    }
    
    @Override
    public void onEnable() {
        if (Module.fullNullCheck()) {
            return;
        }
        Module.mc.timer.tickLength = 50.0f;
        this.packetListReset.reset();
        this.lastYaw = Module.mc.player.rotationYaw;
        this.lastPitch = Module.mc.player.rotationPitch;
    }
    
    @SubscribeEvent
    public final void onUpdate(final UpdateWalkingPlayerEventTwo event) {
        if (Module.fullNullCheck()) {
            return;
        }
        if (this.packetListReset.passedMs(1000L)) {
            this.normalPos = 0;
            this.normalLookPos = 0;
            this.rotationMode = 1;
            this.lastYaw = Module.mc.player.rotationYaw;
            this.lastPitch = Module.mc.player.rotationPitch;
            this.packetListReset.reset();
        }
        if (this.packetControl.getValue()) {
            switch (this.rotationMode) {
                case 1: {
                    if (!EntityUtil.isMoving()) {
                        break;
                    }
                    event.setRotation(this.lastYaw, this.lastPitch);
                    break;
                }
                case 2: {
                    event.setRotation(this.lastYaw + RandomUtil.nextFloat(1.0f, 3.0f), this.lastPitch + RandomUtil.nextFloat(1.0f, 3.0f));
                    break;
                }
            }
        }
        TimerModule.mc.timer.tickLength = (float)(50.0 / this.tickNormal.getValue());
    }
}
