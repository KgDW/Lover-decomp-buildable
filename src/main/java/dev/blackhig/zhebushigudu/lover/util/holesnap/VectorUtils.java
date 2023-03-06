package dev.blackhig.zhebushigudu.lover.util.holesnap;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.Vec3d;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.math.BlockPos;

public final class VectorUtils
{
    public static final VectorUtils INSTANCE;
    
    private VectorUtils() {
    }
    
    public final List<BlockPos> getBlockPositionsInArea(final BlockPos pos1, final BlockPos pos2) {
        final int minX = Math.min(pos1.getX(), pos2.getX());
        final int maxX = Math.max(pos1.getX(), pos2.getX());
        final int minY = Math.min(pos1.getY(), pos2.getY());
        final int maxY = Math.max(pos1.getY(), pos2.getY());
        final int minZ = Math.min(pos1.getZ(), pos2.getZ());
        final int maxZ = Math.max(pos1.getZ(), pos2.getZ());
        return this.getBlockPos(minX, maxX, minY, maxY, minZ, maxZ);
    }
    
    private final List<BlockPos> getBlockPos(final int minX, final int maxX, final int minY, final int maxY, final int minZ, final int maxZ) {
        final ArrayList<BlockPos> returnList = new ArrayList<BlockPos>();
        int n = minX;
        if (n <= maxX) {
            int x;
            do {
                x = n++;
                int n2 = minZ;
                if (n2 > maxZ) {
                    continue;
                }
                int z;
                do {
                    z = n2++;
                    int n3 = minY;
                    if (n3 > maxY) {
                        continue;
                    }
                    int y;
                    do {
                        y = n3++;
                        returnList.add(new BlockPos(x, y, z));
                    } while (y != maxY);
                } while (z != maxZ);
            } while (x != maxX);
        }
        return returnList;
    }
    
    public final ArrayList<BlockPos> getBlockPosInSphere(final Vec3d center, final float radius) {
        final float squaredRadius = (float)Math.pow(radius, 2.0);
        final ArrayList<BlockPos> posList = new ArrayList<BlockPos>();
        final Pair<Integer, Integer> intRange = this.getAxisRange(center.x, radius);
        int n = intRange.getLeft();
        final int n2 = intRange.getRight();
        if (n <= n2) {
            int x;
            do {
                x = n++;
                final Pair<Integer, Integer> intRange2 = this.getAxisRange(center.y, radius);
                int n3 = intRange2.getLeft();
                final int n4;
                if (n3 > (n4 = intRange2.getRight())) {
                    continue;
                }
                int y;
                do {
                    y = n3++;
                    final Pair<Integer, Integer> intRange3 = this.getAxisRange(center.z, radius);
                    int n5 = intRange3.getLeft();
                    final int n6;
                    if (n5 > (n6 = intRange3.getRight())) {
                        continue;
                    }
                    int z;
                    do {
                        final BlockPos blockPos;
                        if ((blockPos = new BlockPos(x, y, z = n5++)).distanceSqToCenter(center.x, center.y, center.z) > squaredRadius) {
                            continue;
                        }
                        posList.add(blockPos);
                    } while (z != n6);
                } while (y != n4);
            } while (x != n2);
        }
        return posList;
    }
    
    private final Pair<Integer, Integer> getAxisRange(final double d1, final float d2) {
        return new Pair<Integer, Integer>((int)Math.floor(d1 - d2), (int)Math.ceil(d1 + d2));
    }
    
    public final BlockPos toBlockPos(final Vec3d vec) {
        final int n = (int)Math.floor(vec.x);
        final int n2 = (int)Math.floor(vec.y);
        return new BlockPos(n, n2, (int)Math.floor(vec.z));
    }
    
    public final Vec3d toVec3d(final Vec3i $this$toVec3d) {
        return this.toVec3d($this$toVec3d, 0.0, 0.0, 0.0);
    }
    
    public final Vec3d toVec3d(final Vec3i $this$toVec3d, final Vec3d offSet) {
        return new Vec3d($this$toVec3d.getX() + offSet.x, $this$toVec3d.getY() + offSet.y, $this$toVec3d.getZ() + offSet.z);
    }
    
    public final Vec3d toVec3d(final Vec3i $this$toVec3d, final double xOffset, final double yOffset, final double zOffset) {
        return new Vec3d($this$toVec3d.getX() + xOffset, $this$toVec3d.getY() + yOffset, $this$toVec3d.getZ() + zOffset);
    }
    
    public final Vec3d toVec3dCenter(final Vec3i $this$toVec3dCenter) {
        return this.toVec3dCenter($this$toVec3dCenter, 0.0, 0.0, 0.0);
    }
    
    public final Vec3d toVec3dCenter(final Vec3i $this$toVec3dCenter, final Vec3d offSet) {
        return new Vec3d($this$toVec3dCenter.getX() + 0.5 + offSet.x, $this$toVec3dCenter.getY() + 0.5 + offSet.y, $this$toVec3dCenter.getZ() + 0.5 + offSet.z);
    }
    
    public final Vec3d toVec3dCenter(final Vec3i $this$toVec3dCenter, final double xOffset, final double yOffset, final double zOffset) {
        return new Vec3d($this$toVec3dCenter.getX() + 0.5 + xOffset, $this$toVec3dCenter.getY() + 0.5 + yOffset, $this$toVec3dCenter.getZ() + 0.5 + zOffset);
    }
    
    public final double distanceTo(final Vec3i $this$distanceTo, final Vec3i vec3i) {
        final int xDiff = vec3i.getX() - $this$distanceTo.getX();
        final int yDiff = vec3i.getY() - $this$distanceTo.getY();
        final int zDiff = vec3i.getZ() - $this$distanceTo.getZ();
        return Math.sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff);
    }
    
    public final double distanceTo(final Vec3d $this$distanceTo, final Vec3i vec3i) {
        final double xDiff = vec3i.getX() + 0.5 - $this$distanceTo.x;
        final double yDiff = vec3i.getY() + 0.5 - $this$distanceTo.y;
        final double zDiff = vec3i.getZ() + 0.5 - $this$distanceTo.z;
        return Math.sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff);
    }
    
    public final double distanceTo(final Entity $this$distanceTo, final Vec3i vec3i) {
        final double xDiff = vec3i.getX() + 0.5 - $this$distanceTo.posX;
        final double yDiff = vec3i.getY() + 0.5 - $this$distanceTo.posY;
        final double zDiff = vec3i.getZ() + 0.5 - $this$distanceTo.posZ;
        return Math.sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff);
    }
    
    public final double distanceTo(final Entity $this$distanceTo, final Vec3d vec3d) {
        final double xDiff = vec3d.x - $this$distanceTo.posX;
        final double yDiff = vec3d.y - $this$distanceTo.posY;
        final double zDiff = vec3d.z - $this$distanceTo.posZ;
        return Math.sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff);
    }
    
    public final double distanceTo(final Entity $this$distanceTo, final ChunkPos chunkPos) {
        return Math.hypot(chunkPos.x * 16 + 8 - $this$distanceTo.posX, chunkPos.z * 16 + 8 - $this$distanceTo.posZ);
    }
    
    public final Vec3i multiply(final Vec3i $this$multiply, final int multiplier) {
        return new Vec3i($this$multiply.getX() * multiplier, $this$multiply.getY() * multiplier, $this$multiply.getZ() * multiplier);
    }
    
    public final Vec3d times(final Vec3d $this$times, final Vec3d vec3d) {
        return new Vec3d($this$times.x * vec3d.x, $this$times.y * vec3d.y, $this$times.z * vec3d.z);
    }
    
    public final Vec3d times(final Vec3d $this$times, final double multiplier) {
        return new Vec3d($this$times.x * multiplier, $this$times.y * multiplier, $this$times.z * multiplier);
    }
    
    public final Vec3d plus(final Vec3d $this$plus, final Vec3d vec3d) {
        final Vec3d vec3d2 = $this$plus.add(vec3d);
        return vec3d2;
    }
    
    public final Vec3d minus(final Vec3d $this$minus, final Vec3d vec3d) {
        final Vec3d vec3d2 = $this$minus.subtract(vec3d);
        return vec3d2;
    }
    
    static {
        INSTANCE = new VectorUtils();
    }
}
