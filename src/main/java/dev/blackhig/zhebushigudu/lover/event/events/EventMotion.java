package dev.blackhig.zhebushigudu.lover.event.events;

import net.minecraft.util.math.BlockPos;
import dev.blackhig.zhebushigudu.lover.event.EventStage;

public class EventMotion extends EventStage
{
    public double x;
    public double y;
    public double z;
    public float yaw;
    public float pitch;
    public boolean onGround;
    private double lastX;
    private double lastY;
    private double lastZ;
    public float lastYaw;
    public float lastPitch;
    public boolean lastOnGround;
    
    public boolean isModded() {
        return this.lastX != this.x || this.lastY != this.y || this.lastZ != this.z || this.lastYaw != this.yaw || this.lastPitch != this.pitch || this.lastOnGround != this.onGround;
    }
    
    public EventMotion(final double x, final double y, final double z, final float yaw, final float pitch, final boolean onGround) {
        super(0);
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
        this.lastX = x;
        this.lastY = y;
        this.lastZ = z;
        this.lastYaw = yaw;
        this.lastPitch = pitch;
        this.lastOnGround = onGround;
    }
    
    public double getX() {
        return this.x;
    }
    
    public void setX(final double x) {
        this.x = x;
    }
    
    public double getY() {
        return this.y;
    }
    
    public void setY(final double y) {
        this.y = y;
    }
    
    public double getZ() {
        return this.z;
    }
    
    public void setZ(final double z) {
        this.z = z;
    }
    
    public float getYaw() {
        return this.yaw;
    }
    
    public void setYaw(final float yaw) {
        this.yaw = yaw;
    }
    
    public float getPitch() {
        return this.pitch;
    }
    
    public void setPitch(final float pitch) {
        this.pitch = pitch;
    }
    
    public boolean isOnGround() {
        return this.onGround;
    }
    
    public void setOnGround(final boolean onGround) {
        this.onGround = onGround;
    }
    
    public void setPosition(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public void setPosition(final BlockPos pos) {
        this.x = pos.getX() + 0.5;
        this.y = pos.getY();
        this.z = pos.getZ() + 0.5;
    }
}
