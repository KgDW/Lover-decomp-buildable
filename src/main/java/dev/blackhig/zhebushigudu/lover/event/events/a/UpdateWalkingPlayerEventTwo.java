package dev.blackhig.zhebushigudu.lover.event.events.a;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import dev.blackhig.zhebushigudu.lover.event.EventStage;

@Cancelable
public class UpdateWalkingPlayerEventTwo extends EventStage
{
    public static UpdateWalkingPlayerEventTwo INSTANCE;
    protected float yaw;
    protected float pitch;
    protected double x;
    protected double y;
    protected double z;
    protected boolean onGround;
    
    public UpdateWalkingPlayerEventTwo(final int stage, final double posX, final double posY, final double posZ, final float y, final float p, final boolean pOnGround) {
        super(stage);
        UpdateWalkingPlayerEventTwo.INSTANCE = this;
        this.x = posX;
        this.y = posY;
        this.z = posZ;
        this.yaw = y;
        this.pitch = p;
        this.onGround = pOnGround;
    }
    
    public UpdateWalkingPlayerEventTwo(final int stage, final UpdateWalkingPlayerEventTwo event) {
        this(stage, event.x, event.y, event.z, event.yaw, event.pitch, event.onGround);
    }
    
    public void setRotation(final float yaw, final float pitch) {
        if (Minecraft.getMinecraft().player != null) {
            Minecraft.getMinecraft().player.rotationYawHead = yaw;
            Minecraft.getMinecraft().player.renderYawOffset = yaw;
        }
        this.setYaw(yaw);
        this.setPitch(pitch);
    }
    
    public void setPostion(final double x, final double y, final double z, final boolean onGround) {
        this.setX(x);
        this.setY(y);
        this.setZ(z);
        this.setOnGround(onGround);
    }
    
    public void setPostion(final double x, final double y, final double z, final float yaw, final float pitch, final boolean onGround) {
        this.setX(x);
        this.setY(y);
        this.setZ(z);
        this.setYaw(yaw);
        this.setPitch(pitch);
        this.setOnGround(onGround);
    }
    
    public float getYaw() {
        return this.yaw;
    }
    
    public void setYaw(final float yaw) {
        this.yaw = yaw;
    }
    
    public void setYaw(final double yaw) {
        this.yaw = (float)yaw;
    }
    
    public float getPitch() {
        return this.pitch;
    }
    
    public void setPitch(final float pitch) {
        this.pitch = pitch;
    }
    
    public void setPitch(final double pitch) {
        this.pitch = (float)pitch;
    }
    
    public double getX() {
        return this.x;
    }
    
    public void setX(final double posX) {
        this.x = posX;
    }
    
    public double getY() {
        return this.y;
    }
    
    public void setY(final double d) {
        this.y = d;
    }
    
    public double getZ() {
        return this.z;
    }
    
    public void setZ(final double posZ) {
        this.z = posZ;
    }
    
    public boolean isOnGround() {
        return this.onGround;
    }
    
    public void setOnGround(final boolean b) {
        this.onGround = b;
    }
}
