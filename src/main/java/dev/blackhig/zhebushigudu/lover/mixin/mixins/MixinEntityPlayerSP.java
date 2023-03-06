package dev.blackhig.zhebushigudu.lover.mixin.mixins;

import dev.blackhig.zhebushigudu.lover.event.events.MoveEvent;
import net.minecraft.entity.MoverType;
import org.spongepowered.asm.mixin.injection.Redirect;
import dev.blackhig.zhebushigudu.lover.util.e.Util;
import dev.blackhig.zhebushigudu.lover.features.modules.movement.Sprint;
import dev.blackhig.zhebushigudu.lover.event.events.PlayerInteractEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.InputUpdateEvent;
import dev.blackhig.zhebushigudu.lover.event.events.PushEvent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import dev.blackhig.zhebushigudu.lover.event.events.EventMotion;
import dev.blackhig.zhebushigudu.lover.event.events.UpdateWalkingPlayerEvent;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import dev.blackhig.zhebushigudu.lover.event.events.ChatEvent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.stats.RecipeBook;
import net.minecraft.stats.StatisticsManager;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.world.World;
import net.minecraft.client.Minecraft;
import dev.blackhig.zhebushigudu.lover.event.events.PlayerMotionUpdateEvent;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.util.MovementInput;
import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.entity.AbstractClientPlayer;

@Mixin(value = { EntityPlayerSP.class }, priority = Integer.MAX_VALUE)
public abstract class MixinEntityPlayerSP extends AbstractClientPlayer
{
    @Shadow
    public MovementInput movementInput;
    PlayerMotionUpdateEvent _event;
    
    public MixinEntityPlayerSP(final Minecraft p_i47378_1_, final World p_i47378_2_, final NetHandlerPlayClient p_i47378_3_, final StatisticsManager p_i47378_4_, final RecipeBook p_i47378_5_) {
        super(p_i47378_2_, p_i47378_3_.getGameProfile());
    }
    
    @Inject(method = { "sendChatMessage" }, at = { @At("HEAD") }, cancellable = true)
    public void sendChatMessage(final String message, final CallbackInfo ci) {
        final ChatEvent chatEvent = new ChatEvent(message);
        MinecraftForge.EVENT_BUS.post((Event)chatEvent);
    }
    
    @Inject(method = { "onUpdateWalkingPlayer" }, at = { @At("HEAD") }, cancellable = true)
    private void preMotion(final CallbackInfo ci) {
        final UpdateWalkingPlayerEvent event = new UpdateWalkingPlayerEvent(0);
        MinecraftForge.EVENT_BUS.post((Event)event);
        final EventMotion event2 = new EventMotion(this.posX, this.getEntityBoundingBox().minY, this.posZ, this.rotationYaw, this.rotationPitch, this.onGround);
        MinecraftForge.EVENT_BUS.post((Event)event2);
        if (!event.isCanceled()) {
            return;
        }
        ci.cancel();
    }
    
    @Inject(method = { "onUpdateWalkingPlayer" }, at = { @At("RETURN") }, cancellable = true)
    private void postMotion(final CallbackInfo ci) {
        final UpdateWalkingPlayerEvent event = new UpdateWalkingPlayerEvent(1);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (!event.isCanceled()) {
            return;
        }
        ci.cancel();
    }
    
    @Inject(method = { "pushOutOfBlocks" }, at = { @At("HEAD") }, cancellable = true)
    private void pushOutOfBlocksHook(final double x, final double y, final double z, final CallbackInfoReturnable<Boolean> ci) {
        final PushEvent event = new PushEvent(1);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (!event.isCanceled()) {
            return;
        }
        ci.setReturnValue((Boolean) false);
    }
    
    @Redirect(method = { "onLivingUpdate" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;setSprinting(Z)V", ordinal = 2))
    public void onLivingUpdate(final EntityPlayerSP entityPlayerSP, final boolean sprinting) {
        new InputUpdateEvent((EntityPlayer)this, this.movementInput);
        final PlayerInteractEvent event = new PlayerInteractEvent((EntityPlayer)this, this.movementInput);
        Label_0107: {
            if (Sprint.getInstance().isOn() && Sprint.getInstance().mode.getValue() == Sprint.Mode.RAGE) {
                if (Util.mc.player.moveForward == 0.0f && Util.mc.player.moveStrafing == 0.0f) {
                    break Label_0107;
                }
            }
            else if (Util.mc.player.movementInput.moveStrafe == 0.0f) {
                break Label_0107;
            }
            entityPlayerSP.setSprinting(true);
            return;
        }
        entityPlayerSP.setSprinting(sprinting);
    }
    
    @Inject(method = { "move" }, at = { @At("HEAD") }, cancellable = true)
    public void move(final MoverType moverType, final double n, final double n2, final double n3, final CallbackInfo ci) {
        if (Minecraft.getMinecraft() == null) {
            return;
        }
        final MoveEvent event = new MoveEvent(moverType, n, n2, n3);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (!event.isCanceled()) {
            return;
        }
        super.move(moverType, event.getX(), event.getY(), event.getZ());
        ci.cancel();
    }
}
