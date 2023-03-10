package dev.blackhig.zhebushigudu.lover.mixin.mixins;

import org.spongepowered.asm.mixin.Overwrite;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;
import dev.blackhig.zhebushigudu.lover.event.events.a.UpdateWalkingPlayerEventTwo;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MovementInput;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ EntityPlayerSP.class })
public abstract class MixinEntityPlayerSPTwo extends MixinEntity
{
    @Shadow
    @Final
    public NetHandlerPlayClient connection;
    @Shadow
    public boolean serverSprintState;
    @Shadow
    public boolean serverSneakState;
    @Shadow
    public double lastReportedPosX;
    @Shadow
    public double lastReportedPosY;
    @Shadow
    public double lastReportedPosZ;
    @Shadow
    public float lastReportedYaw;
    @Shadow
    public float lastReportedPitch;
    @Shadow
    public int positionUpdateTicks;
    @Shadow
    public boolean autoJumpEnabled;
    @Shadow
    public boolean prevOnGround;
    @Shadow
    public MovementInput movementInput;
    @Shadow
    protected Minecraft mc;
    
    public MixinEntityPlayerSPTwo() {
        this.autoJumpEnabled = true;
    }
    
    @Shadow
    public boolean isCurrentViewEntity() {
        return false;
    }
    
    @Overwrite
    public void onUpdateWalkingPlayer() {
        final boolean flag = this.isSprinting();
        final UpdateWalkingPlayerEventTwo pre = new UpdateWalkingPlayerEventTwo(0, this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch, this.onGround);
        MinecraftForge.EVENT_BUS.post((Event)pre);
        if (pre.isCanceled()) {
            final UpdateWalkingPlayerEventTwo post = new UpdateWalkingPlayerEventTwo(1, pre.getX(), pre.getY(), pre.getZ(), pre.getYaw(), pre.getPitch(), pre.isOnGround());
            MinecraftForge.EVENT_BUS.post((Event)post);
            return;
        }
        if (flag != this.serverSprintState) {
            if (flag) {
                this.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Minecraft.getMinecraft().player, CPacketEntityAction.Action.START_SPRINTING));
            }
            else {
                this.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Minecraft.getMinecraft().player, CPacketEntityAction.Action.STOP_SPRINTING));
            }
            this.serverSprintState = flag;
        }
        final boolean flag2 = this.isSneaking();
        if (flag2 != this.serverSneakState) {
            if (flag2) {
                this.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Minecraft.getMinecraft().player, CPacketEntityAction.Action.START_SNEAKING));
            }
            else {
                this.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Minecraft.getMinecraft().player, CPacketEntityAction.Action.STOP_SNEAKING));
            }
            this.serverSneakState = flag2;
        }
        if (this.isCurrentViewEntity()) {
            final double d0 = this.posX - this.lastReportedPosX;
            final double d2 = this.getEntityBoundingBox().minY - this.lastReportedPosY;
            final double d3 = this.posZ - this.lastReportedPosZ;
            final double d4 = pre.getYaw() - this.lastReportedYaw;
            final double d5 = pre.getPitch() - this.lastReportedPitch;
            boolean flag3 = d0 * d0 + d2 * d2 + d3 * d3 > 9.0E-4 || this.positionUpdateTicks >= 20;
            final boolean flag4 = d4 != 0.0 || d5 != 0.0;
            if (this.ridingEntity == null) {
                if (flag3 && flag4) {
                    this.connection.sendPacket((Packet)new CPacketPlayer.PositionRotation(pre.getX(), pre.getY(), pre.getZ(), pre.getYaw(), pre.getPitch(), pre.isOnGround()));
                }
                else if (flag3) {
                    this.connection.sendPacket((Packet)new CPacketPlayer.Position(pre.getX(), pre.getY(), pre.getZ(), pre.isOnGround()));
                }
                else if (flag4) {
                    this.connection.sendPacket((Packet)new CPacketPlayer.Rotation(pre.getYaw(), pre.getPitch(), pre.isOnGround()));
                }
                else {
                    this.connection.sendPacket((Packet)new CPacketPlayer(pre.isOnGround()));
                }
            }
            else {
                this.connection.sendPacket((Packet)new CPacketPlayer.PositionRotation(this.motionX, -999.0, this.motionZ, pre.getYaw(), pre.getPitch(), pre.isOnGround()));
                flag3 = false;
            }
            ++this.positionUpdateTicks;
            if (flag3) {
                this.lastReportedPosX = pre.getX();
                this.lastReportedPosY = pre.getY();
                this.lastReportedPosZ = pre.getZ();
                this.positionUpdateTicks = 0;
            }
            if (flag4) {
                this.lastReportedYaw = pre.getYaw();
                this.lastReportedPitch = pre.getPitch();
            }
            this.prevOnGround = this.onGround;
            this.autoJumpEnabled = this.mc.gameSettings.autoJump;
            final UpdateWalkingPlayerEventTwo post2 = new UpdateWalkingPlayerEventTwo(1, pre.getX(), pre.getY(), pre.getZ(), pre.getYaw(), pre.getPitch(), pre.isOnGround());
            MinecraftForge.EVENT_BUS.post((Event)post2);
        }
    }
}
