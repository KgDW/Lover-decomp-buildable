package dev.blackhig.zhebushigudu.lover.util;

import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos;

public class BlockRenderSmooth implements Util
{
    private BlockPos lastPos;
    private BlockPos newPos;
    private final FadeUtils fade;
    public static Timer timer;
    
    public BlockRenderSmooth(final BlockPos pos, final long smoothLength) {
        this.lastPos = pos;
        this.newPos = pos;
        this.fade = new FadeUtils(smoothLength);
    }
    
    public void setNewPos(final BlockPos pos) {
        if (!pos.equals((Object)this.newPos) && BlockRenderSmooth.timer.passedMs(200L)) {
            this.lastPos = this.newPos;
            this.newPos = pos;
            this.fade.reset();
            BlockRenderSmooth.timer.reset();
        }
    }
    
    public Vec3d getRenderPos() {
        return Lerp(PosToVec(this.lastPos), PosToVec(this.newPos), (float)this.fade.easeOutQuad());
    }
    
    public static Vec3d Lerp(final Vec3d frome, final Vec3d to, final float t) {
        return new Vec3d(t * to.x + (1.0f - t) * frome.x, t * to.y + (1.0f - t) * frome.y, t * to.z + (1.0f - t) * frome.z);
    }
    
    private static Vec3d PosToVec(final BlockPos pos) {
        return new Vec3d((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
    }
    
    static {
        BlockRenderSmooth.timer = new Timer();
    }
}
