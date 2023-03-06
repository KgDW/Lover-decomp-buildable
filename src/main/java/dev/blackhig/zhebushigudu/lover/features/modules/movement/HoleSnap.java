package dev.blackhig.zhebushigudu.lover.features.modules.movement;

import dev.blackhig.zhebushigudu.lover.event.events.MoveEvent2;
import dev.blackhig.zhebushigudu.lover.event.events.PacketEvent;
import dev.blackhig.zhebushigudu.lover.features.modules.Module;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.util.holesnap.*;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraft.util.Timer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class HoleSnap extends Module
{
    private final Setting<Integer> range;
    private final Setting<Float> timer;
    private final Setting<Integer> timeoutTicks;
    private BlockPos holePos;
    private int stuckTicks;
    private int enabledTicks;
    
    public HoleSnap() {
        super("HoleSnap", "IQ", Category.MOVEMENT, true, false, false);
        this.range = this.register(new Setting<>("Range", 5, 1, 50));
        this.timer = this.register(new Setting<>("TimerVal", 3.4f, 1.0f, 4.0f));
        this.timeoutTicks = this.register(new Setting<>("TimeOutTicks", 60, 0, 1000));
    }
    
    @Override
    public void onDisable() {
        this.holePos = null;
        this.stuckTicks = 0;
        this.enabledTicks = 0;
        Module.mc.timer.tickLength = 50.0f;
    }
    
    private double getSpeed(final Entity $this$speed) {
        return Math.hypot($this$speed.motionX, $this$speed.motionZ);
    }
    
    private boolean isFlying(final EntityPlayer $this$isFlying) {
        return $this$isFlying.isElytraFlying() || $this$isFlying.capabilities.isFlying;
    }
    
    @SubscribeEvent
    public void onLagBack(final PacketEvent.Receive event) {
        if (Module.fullNullCheck()) {
            return;
        }
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            this.disable();
        }
    }
    
    @SubscribeEvent
    public void onInput(final InputUpdateEvent event) {
        if (Module.fullNullCheck()) {
            return;
        }
        if (event.getMovementInput() instanceof MovementInputFromOptions && this.holePos != null) {
            final MovementInput movementInput = event.getMovementInput();
            this.resetMove(movementInput);
        }
    }
    
    private void resetMove(final MovementInput $this$resetMove) {
        $this$resetMove.moveForward = 0.0f;
        $this$resetMove.moveStrafe = 0.0f;
        $this$resetMove.forwardKeyDown = false;
        $this$resetMove.backKeyDown = false;
        $this$resetMove.leftKeyDown = false;
        $this$resetMove.rightKeyDown = false;
    }
    
    private boolean isCentered(final Entity $this$isCentered, final BlockPos pos) {
        double d = pos.getX() + 0.31;
        double d2 = pos.getX() + 0.69;
        double d3 = $this$isCentered.posX;
        if (d > d3) {
            return false;
        }
        if (d3 > d2) {
            return false;
        }
        d = pos.getZ() + 0.31;
        d2 = pos.getZ() + 0.69;
        d3 = $this$isCentered.posZ;
        return d <= d3 && d3 <= d2;
    }
    
    @SubscribeEvent
    public void onMove(final MoveEvent2 event) {
        if (Module.fullNullCheck()) {
            return;
        }
        ++this.enabledTicks;
        final Integer n = this.timeoutTicks.getValue();
        if (this.enabledTicks > n) {
            this.disable();
            return;
        }
        if (Module.mc.player.isEntityAlive()) {
            if (!this.isFlying(HoleSnap.mc.player)) {
                final EntityPlayerSP entityPlayerSP = Module.mc.player;
                final double currentSpeed = this.getSpeed(entityPlayerSP);
                if (this.shouldDisable(currentSpeed)) {
                    this.disable();
                    return;
                }
                final BlockPos blockPos = this.getHole();
                if (blockPos != null) {
                    final Timer timer = Module.mc.timer;
                    final Float f = this.timer.getValue();
                    timer.tickLength = 50.0f / f.floatValue();
                    if (!this.isCentered(HoleSnap.mc.player, blockPos)) {
                        final Vec3d playerPos = Module.mc.player.getPositionVector();
                        final Vec3d targetPos = new Vec3d(blockPos.getX() + 0.5, Module.mc.player.posY, blockPos.getZ() + 0.5);
                        final float $this$toRadian$iv = this.getRotationTo(playerPos, targetPos).getX();
                        final float yawRad = $this$toRadian$iv / 180.0f * 3.1415927f;
                        final double dist = playerPos.distanceTo(targetPos);
                        final EntityPlayerSP entityPlayerSP2 = Module.mc.player;
                        final double baseSpeed = this.applySpeedPotionEffects(entityPlayerSP2);
                        final double speed = Module.mc.player.onGround ? baseSpeed : Math.max(currentSpeed + 0.02, baseSpeed);
                        final double cappedSpeed = Math.min(speed, dist);
                        event.setX(-(float)Math.sin(yawRad) * cappedSpeed);
                        event.setZ((float)Math.cos(yawRad) * cappedSpeed);
                        if (Module.mc.player.collidedHorizontally) {
                        }
                        else {
                            this.stuckTicks = 0;
                        }
                    }
                }
            }
        }
    }
    
    private double applySpeedPotionEffects(final EntityLivingBase $this$applySpeedPotionEffects) {
        final PotionEffect potionEffect = $this$applySpeedPotionEffects.getActivePotionEffect(MobEffects.SPEED);
        double d;
        if (potionEffect == null) {
            d = 0.2873;
        }
        else {
            d = 0.2873 * this.getSpeedEffectMultiplier($this$applySpeedPotionEffects);
        }
        return d;
    }
    
    private double getSpeedEffectMultiplier(final EntityLivingBase $this$speedEffectMultiplier) {
        final PotionEffect potionEffect = $this$speedEffectMultiplier.getActivePotionEffect(MobEffects.SPEED);
        double d;
        if (potionEffect == null) {
            d = 1.0;
        }
        else {
            d = 1.0 + (potionEffect.getAmplifier() + 1.0) * 0.2;
        }
        return d;
    }
    
    private Vec2f getRotationTo(final Vec3d posFrom, final Vec3d posTo) {
        final Vec3d vec3d = posTo.subtract(posFrom);
        return this.getRotationFromVec(vec3d);
    }
    
    private Vec2f getRotationFromVec(final Vec3d vec) {
        final double d = vec.x;
        double d2 = vec.z;
        final double xz = Math.hypot(d, d2);
        d2 = vec.z;
        final double d3 = vec.x;
        final double yaw = this.normalizeAngle(Math.toDegrees(Math.atan2(d2, d3)) - 90.0);
        final double pitch = this.normalizeAngle(Math.toDegrees(-Math.atan2(vec.y, xz)));
        return new Vec2f((float)yaw, (float)pitch);
    }
    
    private double normalizeAngle(final double angleIn) {
        double angle = angleIn;
        if ((angle %= 360.0) >= 180.0) {
            angle -= 360.0;
        }
        if (angle < -180.0) {
            angle += 360.0;
        }
        return angle;
    }
    
    private boolean shouldDisable(final double currentSpeed) {
        final BlockPos blockPos = this.holePos;
        if (blockPos != null) {
            if (Module.mc.player.posY < blockPos.getY()) {
                return true;
            }
            if (HoleUtil.is2HoleB(blockPos)) {
                final BlockPos blockPos2 = Module.mc.player.getPosition();
                if (VectorUtils.INSTANCE.toBlockPos(VectorUtils.INSTANCE.toVec3dCenter(blockPos2)).equals(blockPos)) {
                    return true;
                }
            }
        }
        if (this.stuckTicks > 5 && currentSpeed < 0.1) {
            return true;
        }
        if (currentSpeed >= 0.01) {
            return false;
        }
        final EntityPlayerSP entityPlayerSP = Module.mc.player;
        return SurroundUtils.INSTANCE.checkHole(entityPlayerSP) != SurroundUtils.HoleType.NONE;
    }
    
    private BlockPos getHole() {
        if (Module.mc.player.ticksExisted % 10 == 0) {
            final EntityPlayerSP entityPlayerSP = Module.mc.player;
            if (!SurroundUtils.INSTANCE.getFlooredPosition(entityPlayerSP).equals(this.holePos)) {
                return this.findHole();
            }
        }
        final BlockPos blockPos2 = this.holePos;
        BlockPos blockPos;
        if ((blockPos = blockPos2) != null) {
            return blockPos;
        }
        blockPos = this.findHole();
        return blockPos;
    }
    
    private BlockPos findHole() {
        Pair<Double, BlockPos> closestHole = new Pair<>(69.69, BlockPos.ORIGIN);
        final EntityPlayerSP entityPlayerSP = Module.mc.player;
        final BlockPos playerPos = SurroundUtils.INSTANCE.getFlooredPosition(entityPlayerSP);
        final Integer ceilRange = this.range.getValue();
        final BlockPos blockPos2 = playerPos.add(ceilRange, -1, ceilRange);
        BlockPos object = playerPos.add(-ceilRange, -1, -ceilRange);
        final List<BlockPos> posList = VectorUtils.INSTANCE.getBlockPositionsInArea(blockPos2, object);
        for (final BlockPos posXZ : posList) {
            final EntityPlayerSP entityPlayerSP2 = Module.mc.player;
            final double dist = VectorUtils.INSTANCE.distanceTo(entityPlayerSP2, posXZ);
            final Integer n = this.range.getValue();
            if (dist <= ((n == null) ? null : Double.valueOf(n))) {
                if (dist > closestHole.getLeft()) {
                    continue;
                }
                int n2 = 0;
                BlockPos pos;
                while (n2 < 6 && Module.mc.world.isAirBlock((pos = posXZ.add(0, -(n2++), 0)).up())) {
                    if (HoleUtil.is2HoleB(pos)) {
                        closestHole = new Pair<>(dist, pos);
                    }
                    else {
                        if (SurroundUtils.INSTANCE.checkHole(pos) == SurroundUtils.HoleType.NONE) {
                            continue;
                        }
                        closestHole = new Pair<>(dist, pos);
                    }
                }
            }
        }
        BlockPos blockPos3;
        if (closestHole.getRight() != BlockPos.ORIGIN) {
            object = closestHole.getRight();
            this.holePos = object;
            blockPos3 = object;
        }
        else {
            blockPos3 = null;
        }
        return blockPos3;
    }
}
