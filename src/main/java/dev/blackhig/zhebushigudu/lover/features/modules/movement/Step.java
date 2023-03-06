package dev.blackhig.zhebushigudu.lover.features.modules.movement;

import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import dev.blackhig.zhebushigudu.lover.util.Util;
import dev.blackhig.zhebushigudu.lover.features.Feature;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.features.modules.Module;

public class Step extends Module
{
    public Setting<Mode> mode;
    private final Setting<Double> height;
    private final Setting<Double> timerTicks;
    public Setting<Boolean> useTimer;
    public Setting<Boolean> entityStep;
    public Setting<Boolean> sneakPause;
    public Setting<Boolean> waterPause;
    public Setting<Modes> disable;
    double[] forwardStep;
    double originalHeight;
    
    public Step() {
        super("Step", "placeholder", Category.MOVEMENT, false, false, false);
        this.mode = this.register(new Setting<>("Mode", Mode.Teleport));
        this.height = this.register(new Setting<>("Height", 2.0, 0.0, 10.0));
        this.timerTicks = this.register(new Setting<>("Timer Speed", 0.5, 0.1, 2.0));
        this.useTimer = this.register(new Setting<>("use Timer", false));
        this.entityStep = this.register(new Setting<>("Entity Step", false));
        this.sneakPause = this.register(new Setting<>("When Sneaking", false));
        this.waterPause = this.register(new Setting<>("When in Liquid", true));
        this.disable = this.register(new Setting<>("Disable", Modes.Never));
    }
    
    @Override
    public void onEnable() {
        if (Feature.nullCheck()) {
            return;
        }
        this.originalHeight = Util.mc.player.posY;
    }
    
    @Override
    public void onUpdate() {
        if (Feature.nullCheck()) {
            return;
        }
        if (!Util.mc.player.collidedHorizontally) {
            return;
        }
        if (Util.mc.player.isOnLadder() || Util.mc.player.movementInput.jump) {
            return;
        }
        if ((Util.mc.player.isInWater() || Util.mc.player.isInLava()) && this.waterPause.getValue()) {
            return;
        }
        if (!Util.mc.player.onGround) {
            return;
        }
        if (this.useTimer.getValue()) {
            Util.mc.timer.tickLength = (float)(50.0 / this.timerTicks.getValue());
        }
        if (this.entityStep.getValue() && Util.mc.player.isRiding()) {
            Util.mc.player.ridingEntity.stepHeight = (float)(this.height.getValue() / 1.0);
        }
        if (Util.mc.player.isSneaking() && this.sneakPause.getValue()) {
            return;
        }
        this.forwardStep = getMoveSpeed(0.1);
        if (this.getStepHeight().equals(StepHeight.Unsafe)) {
            if (this.disable.getValue() == Modes.Never) {
                this.disable();
            }
            return;
        }
        if (this.mode.getValue() == Mode.Teleport) {
            this.stepTeleport();
        }
        if (this.mode.getValue() == Mode.Spider) {
            this.stepSpider();
        }
        if (this.mode.getValue() == Mode.Vanilla) {
            this.stepVanilla();
        }
        if (Util.mc.player.posY > this.originalHeight + this.getStepHeight().height && this.disable.getValue() == Modes.Completion) {
            this.disable();
        }
    }
    
    public static double[] getMoveSpeed(final double speed) {
        float forward = Util.mc.player.movementInput.moveForward;
        float strafe = Util.mc.player.movementInput.moveStrafe;
        float yaw = Util.mc.player.rotationYaw;
        if (forward != 0.0f) {
            if (strafe >= 1.0f) {
                yaw += ((forward > 0.0f) ? -45 : 45);
                strafe = 0.0f;
            }
            else if (strafe <= -1.0f) {
                yaw += ((forward > 0.0f) ? 45 : -45);
                strafe = 0.0f;
            }
            if (forward > 0.0f) {
                forward = 1.0f;
            }
            else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        final double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        final double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        double motionX = forward * speed * cos + strafe * speed * sin;
        double motionZ = forward * speed * sin - strafe * speed * cos;
        if (!isMoving()) {
            motionX = 0.0;
            motionZ = 0.0;
        }
        return new double[] { motionX, motionZ };
    }
    
    public static boolean isMoving() {
        return Util.mc.player.moveForward != 0.0 || Util.mc.player.moveStrafing != 0.0;
    }
    
    public void stepTeleport() {
        this.updateStepPackets(this.getStepHeight().stepArray);
        Util.mc.player.setPosition(Util.mc.player.posX, Util.mc.player.posY + this.getStepHeight().height, Util.mc.player.posZ);
    }
    
    public void stepSpider() {
        this.updateStepPackets(this.getStepHeight().stepArray);
        Util.mc.player.motionY = 0.2;
        Util.mc.player.fallDistance = 0.0f;
    }
    
    public void stepVanilla() {
        Util.mc.player.setPosition(Util.mc.player.posX, Util.mc.player.posY + this.getStepHeight().height, Util.mc.player.posZ);
    }
    
    public void updateStepPackets(final double[] stepArray) {
        for (final double v : stepArray) {
            Util.mc.player.connection.sendPacket((Packet<net.minecraft.network.play.INetHandlerPlayServer>)new CPacketPlayer.Position(Util.mc.player.posX, Util.mc.player.posY + v, Util.mc.player.posZ, Util.mc.player.onGround));
        }
    }
    
    public StepHeight getStepHeight() {
        if (Util.mc.world.getCollisionBoxes((Entity)Util.mc.player, Util.mc.player.getEntityBoundingBox().offset(this.forwardStep[0], 1.0, this.forwardStep[1])).isEmpty() && !Util.mc.world.getCollisionBoxes((Entity)Util.mc.player, Util.mc.player.getEntityBoundingBox().offset(this.forwardStep[0], 0.6, this.forwardStep[1])).isEmpty()) {
            return StepHeight.One;
        }
        if (Util.mc.world.getCollisionBoxes((Entity)Util.mc.player, Util.mc.player.getEntityBoundingBox().offset(this.forwardStep[0], 1.6, this.forwardStep[1])).isEmpty() && !Util.mc.world.getCollisionBoxes((Entity)Util.mc.player, Util.mc.player.getEntityBoundingBox().offset(this.forwardStep[0], 1.4, this.forwardStep[1])).isEmpty()) {
            return StepHeight.OneHalf;
        }
        if (Util.mc.world.getCollisionBoxes((Entity)Util.mc.player, Util.mc.player.getEntityBoundingBox().offset(this.forwardStep[0], 2.1, this.forwardStep[1])).isEmpty() && !Util.mc.world.getCollisionBoxes((Entity)Util.mc.player, Util.mc.player.getEntityBoundingBox().offset(this.forwardStep[0], 1.9, this.forwardStep[1])).isEmpty()) {
            return StepHeight.Two;
        }
        if (Util.mc.world.getCollisionBoxes((Entity)Util.mc.player, Util.mc.player.getEntityBoundingBox().offset(this.forwardStep[0], 2.6, this.forwardStep[1])).isEmpty() && !Util.mc.world.getCollisionBoxes((Entity)Util.mc.player, Util.mc.player.getEntityBoundingBox().offset(this.forwardStep[0], 2.4, this.forwardStep[1])).isEmpty()) {
            return StepHeight.TwoHalf;
        }
        return StepHeight.Unsafe;
    }
    
    public enum Mode
    {
        Teleport, 
        Spider, 
        Vanilla;
    }
    
    public enum Modes
    {
        Never, 
        Completion;
    }
    
    public enum StepHeight
    {
        One(1.0, new double[] { 0.42, 0.753 }), 
        OneHalf(1.5, new double[] { 0.42, 0.75, 1.0, 1.16, 1.23, 1.2 }), 
        Two(2.0, new double[] { 0.42, 0.78, 0.63, 0.51, 0.9, 1.21, 1.45, 1.43 }), 
        TwoHalf(2.5, new double[] { 0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869, 2.019, 1.907 }), 
        Unsafe(3.0, new double[] { 0.0 });
        
        double[] stepArray;
        double height;
        
        StepHeight(final double height, final double[] stepArray) {
            this.height = height;
            this.stepArray = stepArray;
        }
    }
}
