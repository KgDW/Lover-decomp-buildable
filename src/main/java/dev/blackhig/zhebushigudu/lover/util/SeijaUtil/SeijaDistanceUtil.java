package dev.blackhig.zhebushigudu.lover.util.SeijaUtil;

import net.minecraft.util.math.BlockPos;
import net.minecraft.client.Minecraft;

public class SeijaDistanceUtil
{
    public static Minecraft mc;
    
    public static double distanceToXZ(final double x, final double z) {
        final double dx = SeijaDistanceUtil.mc.player.posX - x;
        final double dz = SeijaDistanceUtil.mc.player.posZ - z;
        return Math.sqrt(dx * dx + dz * dz);
    }
    
    public static double distanceToXZ(final double x1, final double z1, final double x2, final double z2) {
        final double dx = x1 - x2;
        final double dz = z1 - z2;
        return Math.sqrt(dx * dx + dz * dz);
    }
    
    public static double distanceToXZ(final BlockPos pos) {
        final double dx = SeijaDistanceUtil.mc.player.posX - pos.getX();
        final double dz = SeijaDistanceUtil.mc.player.posZ - pos.getZ();
        return Math.sqrt(dx * dx + dz * dz);
    }
    
    public static double distanceToXZ(final BlockPos pos1, final BlockPos pos2) {
        final double dx = pos1.getX() - pos2.getX();
        final double dz = pos1.getZ() - pos2.getZ();
        return Math.sqrt(dx * dx + dz * dz);
    }
    
    static {
        SeijaDistanceUtil.mc = Minecraft.getMinecraft();
    }
}
