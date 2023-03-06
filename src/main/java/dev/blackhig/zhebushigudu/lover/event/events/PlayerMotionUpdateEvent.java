package dev.blackhig.zhebushigudu.lover.event.events;

import net.minecraft.client.entity.EntityPlayerSP;
import java.util.function.Consumer;
import dev.blackhig.zhebushigudu.lover.event.EventStage;

public class PlayerMotionUpdateEvent extends EventStage
{
    protected float _yaw;
    protected float _pitch;
    protected double x;
    protected double y;
    protected double z;
    protected boolean onGround;
    private Consumer<EntityPlayerSP> _funcToCall;
    private boolean _isForceCancelled;
    
    public PlayerMotionUpdateEvent(final double posX, final double posY, final double posZ, final boolean pOnGround) {
        this._funcToCall = null;
        this.x = posX;
        this.y = posY;
        this.z = posZ;
        this.onGround = pOnGround;
    }
    
    public Consumer<EntityPlayerSP> getFunc() {
        return this._funcToCall;
    }
    
    public void setFunct(final Consumer<EntityPlayerSP> post) {
        this._funcToCall = post;
    }
    
    public float getYaw() {
        return this._yaw;
    }
    
    public void setYaw(final float yaw) {
        this._yaw = yaw;
    }
    
    public float getPitch() {
        return this._pitch;
    }
    
    public void setPitch(final float pitch) {
        this._pitch = pitch;
    }
    
    public void setYaw(final double yaw) {
        this._yaw = (float)yaw;
    }
    
    public void setPitch(final double pitch) {
        this._pitch = (float)pitch;
    }
    
    public void forceCancel() {
        this._isForceCancelled = true;
    }
    
    public boolean isForceCancelled() {
        return this._isForceCancelled;
    }
    
    public void setX(final double posX) {
        this.x = posX;
    }
    
    public void setY(final double d) {
        this.y = d;
    }
    
    public void setZ(final double posZ) {
        this.z = posZ;
    }
    
    public void setOnGround(final boolean b) {
        this.onGround = b;
    }
    
    public double getX() {
        return this.x;
    }
    
    public double getY() {
        return this.y;
    }
    
    public double getZ() {
        return this.z;
    }
    
    public boolean getOnGround() {
        return this.onGround;
    }
}
