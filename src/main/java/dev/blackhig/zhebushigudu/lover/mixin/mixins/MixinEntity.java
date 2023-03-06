package dev.blackhig.zhebushigudu.lover.mixin.mixins;

import net.minecraft.entity.MoverType;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.math.AxisAlignedBB;
import java.util.Random;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ Entity.class })
public abstract class MixinEntity
{
    @Shadow
    public double posX;
    @Shadow
    public double posY;
    @Shadow
    public double posZ;
    @Shadow
    public float rotationPitch;
    @Shadow
    public float rotationYaw;
    @Shadow
    public Entity ridingEntity;
    @Shadow
    public double motionX;
    @Shadow
    public double motionY;
    @Shadow
    public double motionZ;
    @Shadow
    public boolean onGround;
    @Shadow
    public boolean isAirBorne;
    @Shadow
    public boolean noClip;
    @Shadow
    public boolean isInWeb;
    @Shadow
    public float stepHeight;
    @Shadow
    public float distanceWalkedModified;
    @Shadow
    public float distanceWalkedOnStepModified;
    @Shadow
    public float width;
    @Shadow
    public Random rand;
    @Shadow
    public int nextStepDistance;
    public int fire;
    
    @Shadow
    public boolean isRiding() {
        return this.getRidingEntity() != null;
    }
    
    @Shadow
    public Entity getRidingEntity() {
        return this.ridingEntity;
    }
    
    @Shadow
    public abstract boolean isSprinting();
    
    @Shadow
    public abstract AxisAlignedBB getEntityBoundingBox();
    
    @Shadow
    public abstract void setEntityBoundingBox(final AxisAlignedBB p0);
    
    @Shadow
    protected abstract boolean canTriggerWalking();
    
    @Shadow
    public abstract boolean isInWater();
    
    @Shadow
    protected abstract void dealFireDamage(final int p0);
    
    @Shadow
    public abstract boolean isWet();
    
    @Shadow
    public abstract void addEntityCrashInfo(final CrashReportCategory p0);
    
    @Shadow
    protected abstract void doBlockCollisions();
    
    @Shadow
    protected abstract void playStepSound(final BlockPos p0, final Block p1);
    
    @Shadow
    public abstract boolean isSneaking();
    
    public int getNextStepDistance() {
        return this.nextStepDistance;
    }
    
    public void setNextStepDistance(final int nextStepDistance) {
        this.nextStepDistance = nextStepDistance;
    }
    
    public int getFire() {
        return this.fire;
    }
    
    @Shadow
    public abstract void setFire(final int p0);
    
    public void moveEntity(final MoverType type, final double x, final double y, final double z) {
    }
}
