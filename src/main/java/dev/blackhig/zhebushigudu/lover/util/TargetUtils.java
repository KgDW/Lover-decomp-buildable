package dev.blackhig.zhebushigudu.lover.util;

import net.minecraft.util.math.Vec3d;
import javax.annotation.Nullable;
import java.util.Iterator;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import java.util.Comparator;
import net.minecraft.entity.Entity;
import net.minecraft.client.Minecraft;

public class TargetUtils
{
    protected static Minecraft mc;
    public static Entity currentTarget;
    public static boolean Player;
    public static boolean Animal;
    
    public static boolean findTarget(final double range) {
        TargetUtils.mc.world.loadedEntityList.sort(new Comparator<Entity>() {
            @Override
            public int compare(final Entity a, final Entity b) {
                if (a == null && b == null) {
                    return 0;
                }
                if (a.getDistance((Entity)TargetUtils.mc.player) < b.getDistance((Entity)TargetUtils.mc.player)) {
                    return -1;
                }
                if (a.getDistance((Entity)TargetUtils.mc.player) > b.getDistance((Entity)TargetUtils.mc.player)) {
                    return 1;
                }
                return 0;
            }
        });
        for (final Entity e : TargetUtils.mc.world.loadedEntityList) {
            if (e == TargetUtils.mc.player) {
                continue;
            }
            if (e.getDistance((Entity)TargetUtils.mc.player) > range) {
                continue;
            }
            if (!(e instanceof EntityLivingBase)) {
                continue;
            }
            if (e instanceof EntityPlayer && !TargetUtils.Player) {
                continue;
            }
            if (e instanceof EntityAnimal && !TargetUtils.Animal) {
                continue;
            }
            if (!(e instanceof EntityPlayer)) {
                continue;
            }
            TargetUtils.currentTarget = e;
            return true;
        }
        return false;
    }
    
    public static double getDistance(@Nullable final Entity target) {
        return (TargetUtils.currentTarget == null) ? 0.0 : TargetUtils.currentTarget.getDistance((Entity)((target == null) ? TargetUtils.mc.player : target));
    }
    
    public static boolean canAttack(final Vec3d vec, final Vec3d pos) {
        final boolean flag = TargetUtils.mc.world.rayTraceBlocks(vec, pos, false, true, false) == null;
        double d0 = 36.0;
        if (!flag) {
            d0 = 9.0;
        }
        return vec.squareDistanceTo(pos) < d0;
    }
    
    static {
        TargetUtils.mc = Minecraft.getMinecraft();
        TargetUtils.Player = true;
        TargetUtils.Animal = false;
    }
}
