package dev.blackhig.zhebushigudu.lover.util.HoleFillPlus;

import net.minecraft.entity.player.EntityPlayer;
import java.math.RoundingMode;
import java.math.BigDecimal;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class MathsUtil implements Globals
{
    public static int clamp(final int num, final int min, final int max) {
        return (num < min) ? min : Math.min(num, max);
    }
    
    public static float clamp(final float num, final float min, final float max) {
        return (num < min) ? min : Math.min(num, max);
    }
    
    public static double clamp(final double num, final double min, final double max) {
        return (num < min) ? min : Math.min(num, max);
    }
    
    public static float[] calcAngle(final Vec3d from, final Vec3d to) {
        final double difX = to.x - from.x;
        final double difY = (to.y - from.y) * -1.0;
        final double difZ = to.z - from.z;
        final double dist = MathHelper.sqrt(difX * difX + difZ * difZ);
        return new float[] { (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0), (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difY, dist))) };
    }
    
    public static Vec3d roundVec(final Vec3d vec3d, final int places) {
        return new Vec3d(round(vec3d.x, places), round(vec3d.y, places), round(vec3d.z, places));
    }
    
    public static double round(final double value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.FLOOR);
        return bd.doubleValue();
    }
    
    public static int random(final int min, final int max) {
        return MathsUtil.random.nextInt(max - min) + min;
    }
    
    public static int floor(final double value) {
        final int i = (int)value;
        return (value < i) ? (i - 1) : i;
    }
    
    public static Vec3d extrapolatePlayerPosition(final EntityPlayer player, final int ticks) {
        final Vec3d lastPos = new Vec3d(player.lastTickPosX, player.lastTickPosY, player.lastTickPosZ);
        final Vec3d currentPos = new Vec3d(player.posX, player.posY, player.posZ);
        final double distance = square((float)player.motionX) + square((float)player.motionY) + square((float)player.motionZ);
        final Vec3d tempVec = calculateLine(lastPos, currentPos, distance * ticks);
        return new Vec3d(tempVec.x, player.posY, tempVec.z);
    }
    
    public static Vec3d calculateLine(final Vec3d x1, final Vec3d x2, final double distance) {
        final double length = Math.sqrt(square((float)(x2.x - x1.x)) + square((float)(x2.y - x1.y)) + square((float)(x2.z - x1.z)));
        final double unitSlopeX = (x2.x - x1.x) / length;
        final double unitSlopeY = (x2.y - x1.y) / length;
        final double unitSlopeZ = (x2.z - x1.z) / length;
        final double x3 = x1.x + unitSlopeX * distance;
        final double y = x1.y + unitSlopeY * distance;
        final double z = x1.z + unitSlopeZ * distance;
        return new Vec3d(x3, y, z);
    }
    
    public static double square(final float input) {
        return input * input;
    }
    
    public static double[] directionSpeed(final double speed) {
        float forward = MathsUtil.mc.player.movementInput.moveForward;
        float side = MathsUtil.mc.player.movementInput.moveStrafe;
        float yaw = MathsUtil.mc.player.prevRotationYaw + (MathsUtil.mc.player.rotationYaw - MathsUtil.mc.player.prevRotationYaw) * MathsUtil.mc.getRenderPartialTicks();
        if (forward != 0.0f) {
            if (side > 0.0f) {
                yaw += ((forward > 0.0f) ? -45 : 45);
            }
            else if (side < 0.0f) {
                yaw += ((forward > 0.0f) ? 45 : -45);
            }
            side = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            }
            else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        final double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        final double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        final double posX = forward * speed * cos + side * speed * sin;
        final double posZ = forward * speed * sin - side * speed * cos;
        return new double[] { posX, posZ };
    }
    
    public static double degToRad(final double deg) {
        return deg * 0.01745329238474369;
    }
}
