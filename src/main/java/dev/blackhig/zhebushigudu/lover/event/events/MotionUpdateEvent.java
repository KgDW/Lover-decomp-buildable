package dev.blackhig.zhebushigudu.lover.event.events;

import dev.blackhig.zhebushigudu.lover.event.EventStage;
import dev.blackhig.zhebushigudu.lover.util.Location;
import net.minecraft.util.math.Vec2f;

public final class MotionUpdateEvent extends EventStage
{
    private Location location;
    private Vec2f rotation;
    private Vec2f prevRotation;
    private boolean rotating;
    
    public MotionUpdateEvent(final Location location, final Vec2f rotation, final Vec2f prevRotation) {
        this.location = location;
        this.rotation = rotation;
        this.prevRotation = prevRotation;
    }
    
    public final Location getLocation() {
        return this.location;
    }
    
    public final void setLocation(final Location location) {
        this.location = location;
    }
    
    public final Vec2f getRotation() {
        return this.rotation;
    }
    
    public final void setRotation(final Vec2f vec2f) {
        this.rotation = vec2f;
    }
    
    public final Vec2f getPrevRotation() {
        return this.prevRotation;
    }
    
    public final boolean getRotating() {
        return this.rotating;
    }
    
    public final void setRotating(final boolean bl) {
        this.rotating = bl;
    }
    
    public class FastTick
    {
    }
}
