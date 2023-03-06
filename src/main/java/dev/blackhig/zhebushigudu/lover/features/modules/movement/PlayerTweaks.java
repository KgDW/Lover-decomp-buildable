package dev.blackhig.zhebushigudu.lover.features.modules.movement;

import dev.blackhig.zhebushigudu.lover.event.events.PushEvent;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import dev.blackhig.zhebushigudu.lover.event.events.PacketEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.MovementInput;
import net.minecraftforge.client.event.InputUpdateEvent;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.features.modules.Module;

public class PlayerTweaks extends Module
{
    public static PlayerTweaks INSTANCE;
    public Setting<Boolean> noSlow;
    public Setting<Boolean> antiKnockBack;
    public Setting<Boolean> noEntityPush;
    public Setting<Boolean> noBlockPush;
    public Setting<Boolean> noWaterPush;
    public Setting<Boolean> Sprint;
    public Setting<Boolean> guiMove;
    
    public PlayerTweaks() {
        super("PlayerTweaks", "tweaks", Category.MOVEMENT, true, false, false);
        this.noSlow = this.register(new Setting<>("No Slow", true));
        this.antiKnockBack = this.register(new Setting<>("Velocity", true));
        this.noEntityPush = this.register(new Setting<>("No PlayerPush", true));
        this.noBlockPush = this.register(new Setting<>("No BlockPush", true));
        this.noWaterPush = this.register(new Setting<>("No LiquidPush", true));
        this.Sprint = this.register(new Setting<>("Sprint", true));
        this.guiMove = this.register(new Setting<>("Gui Move", true));
        this.setInstance();
    }
    
    public static PlayerTweaks getInstance() {
        if (PlayerTweaks.INSTANCE == null) {
            PlayerTweaks.INSTANCE = new PlayerTweaks();
        }
        return PlayerTweaks.INSTANCE;
    }
    
    private void setInstance() {
        PlayerTweaks.INSTANCE = this;
    }
    
    @SubscribeEvent
    public void Slow(final InputUpdateEvent event) {
        if (this.noSlow.getValue() && PlayerTweaks.mc.player.isHandActive() && !PlayerTweaks.mc.player.isRiding()) {
            final MovementInput movementInput = event.getMovementInput();
            movementInput.moveStrafe *= 5.0f;
            final MovementInput movementInput2 = event.getMovementInput();
            movementInput2.moveForward *= 5.0f;
        }
    }
    
    @SubscribeEvent
    public void onPacketReceived(final PacketEvent.Receive event) {
        if (fullNullCheck()) {
            return;
        }
        if (this.antiKnockBack.getValue()) {
            if (event.getPacket() instanceof SPacketEntityVelocity && ((SPacketEntityVelocity) event.getPacket()).getEntityID() == PlayerTweaks.mc.player.getEntityId()) {
                event.setCanceled(true);
            }
            if (event.getPacket() instanceof SPacketExplosion) {
                event.setCanceled(true);
            }
        }
    }
    
    @Override
    public void onTick() {
        if (this.Sprint.getValue() && (PlayerTweaks.mc.player.moveForward != 0.0f || PlayerTweaks.mc.player.moveStrafing != 0.0f) && !PlayerTweaks.mc.player.isSprinting()) {
            PlayerTweaks.mc.player.setSprinting(true);
        }
    }
    
    @SubscribeEvent
    public void onPush(final PushEvent event) {
        if (fullNullCheck()) {
            return;
        }
        if (event.getStage() == 0 && this.noEntityPush.getValue() && event.entity.equals(PlayerTweaks.mc.player)) {
            event.x = -event.x * 0.0;
            event.y = -event.y * 0.0;
            event.z = -event.z * 0.0;
        }
        else if (event.getStage() == 1 && this.noBlockPush.getValue()) {
            event.setCanceled(true);
        }
        else if (event.getStage() == 2 && this.noWaterPush.getValue() && PlayerTweaks.mc.player != null && PlayerTweaks.mc.player.equals(event.entity)) {
            event.setCanceled(true);
        }
    }
    
    static {
        PlayerTweaks.INSTANCE = new PlayerTweaks();
    }
}
