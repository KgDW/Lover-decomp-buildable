package dev.blackhig.zhebushigudu.lover.util.holesnap;

import com.sun.javafx.geom.Vec2d;
import net.minecraft.entity.Entity;

public class Vec2f
{
    private float x;
    private float y;
    public static final Vec2f ZERO;
    
    public Vec2f(final float x, final float y) {
        this.x = x;
        this.y = y;
    }
    
    public final float getX() {
        return this.x;
    }
    
    public final float getY() {
        return this.y;
    }
    
    public Vec2f(final Entity entity) {
        this(entity.rotationYaw, entity.rotationPitch);
    }
    
    public Vec2f(final double x, final double y) {
        this((float)x, (float)y);
    }
    
    public final Vec2f toRadians() {
        return new Vec2f(this.x / 180.0f * 3.1415927f, this.y / 180.0f * 3.1415927f);
    }
    
    public final float length() {
        return (float)Math.hypot(this.x, this.y);
    }
    
    public final float lengthSquared() {
        return (float)Math.pow(this.x, 2.0) + (float)Math.pow(this.y, 2.0);
    }
    
    public final Vec2f div(final Vec2f vec2f) {
        return this.div(vec2f.x, vec2f.y);
    }
    
    public final Vec2f div(final float divider) {
        return this.div(divider, divider);
    }
    
    public final Vec2f div(final float x, final float y) {
        return new Vec2f(this.x / x, this.y / y);
    }
    
    public final Vec2f times(final Vec2f vec2f) {
        return this.times(vec2f.x, vec2f.y);
    }
    
    public final Vec2f times(final float multiplier) {
        return this.times(multiplier, multiplier);
    }
    
    public final Vec2f times(final float x, final float y) {
        return new Vec2f(this.x * x, this.y * y);
    }
    
    public final Vec2f minus(final Vec2f vec2f) {
        return this.minus(vec2f.x, vec2f.y);
    }
    
    public final Vec2f minus(final float value) {
        return this.minus(value, value);
    }
    
    public final Vec2f minus(final float x, final float y) {
        return this.plus(-x, -y);
    }
    
    public final Vec2f plus(final Vec2f vec2f) {
        return this.plus(vec2f.x, vec2f.y);
    }
    
    public final Vec2f plus(final float value) {
        return this.plus(value, value);
    }
    
    public final Vec2f plus(final float x, final float y) {
        return new Vec2f(this.x + x, this.y + y);
    }
    
    public final Vec2d toVec2d() {
        return new Vec2d(this.x, this.y);
    }
    
    static {
        ZERO = new Vec2f(0.0f, 0.0f);
    }
}
