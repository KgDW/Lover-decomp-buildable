package dev.blackhig.zhebushigudu.lover.features.modules.movement;

import java.util.Objects;

import net.minecraft.init.MobEffects;
import net.minecraft.entity.Entity;
import dev.blackhig.zhebushigudu.lover.event.events.MoveEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import dev.blackhig.zhebushigudu.lover.util.Util;
import dev.blackhig.zhebushigudu.lover.features.Feature;
import dev.blackhig.zhebushigudu.lover.event.events.UpdateWalkingPlayerEvent;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.features.modules.Module;

public class Strafe extends Module
{
    private static Strafe INSTANCE;
    private double lastDist;
    public Setting<Mode> mode;
    private double moveSpeed;
    int stage;
    
    public Strafe() {
        super("Strafe", "Modifies sprinting", Category.MOVEMENT, true, false, false);
        this.mode = this.register(new Setting<>("Mode", Mode.NORMAL));
        this.setInstance();
    }
    
    private void setInstance() {
        Strafe.INSTANCE = this;
    }
    
    public static Strafe getInstance() {
        if (Strafe.INSTANCE == null) {
            Strafe.INSTANCE = new Strafe();
        }
        return Strafe.INSTANCE;
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayerEvent(final UpdateWalkingPlayerEvent event) {
        if (event.getStage() != 1 || !Feature.fullNullCheck()) {
            this.lastDist = Math.sqrt((Util.mc.player.posX - Util.mc.player.prevPosX) * (Util.mc.player.posX - Util.mc.player.prevPosX) + (Util.mc.player.posZ - Util.mc.player.prevPosZ) * (Util.mc.player.posZ - Util.mc.player.prevPosZ));
        }
    }
    
    @SubscribeEvent
    public void onStrafe(final MoveEvent event) {
        if (!Feature.fullNullCheck() && !Util.mc.player.isInWater() && !Util.mc.player.isInLava()) {
            if (Util.mc.player.onGround) {
                this.stage = 2;
            }
            switch (this.stage) {
                case 0: {
                    ++this.stage;
                    this.lastDist = 0.0;
                    break;
                }
                default: {
                    if ((Util.mc.world.getCollisionBoxes(Util.mc.player, Util.mc.player.getEntityBoundingBox().offset(0.0, Util.mc.player.motionY, 0.0)).size() > 0 || Util.mc.player.collidedVertically) && this.stage > 0) {
                        this.stage = ((Util.mc.player.moveForward != 0.0f || Util.mc.player.moveStrafing != 0.0f) ? 1 : 0);
                    }
                    this.moveSpeed = this.lastDist - this.lastDist / ((this.mode.getValue() == Mode.NORMAL) ? 730.0 : 159.0);
                    break;
                }
                case 2: {
                    double motionY = 0.40123128;
                    if (Util.mc.player.onGround && Util.mc.gameSettings.keyBindJump.isKeyDown()) {
                        if (Util.mc.player.isPotionActive(MobEffects.JUMP_BOOST)) {
                            motionY = 0.40123128 + (Objects.requireNonNull(Util.mc.player.getActivePotionEffect(MobEffects.JUMP_BOOST)).getAmplifier() + 1) * 0.1f;
                        }
                        event.setY(Util.mc.player.motionY = motionY);
                        this.moveSpeed *= ((this.mode.getValue() == Mode.NORMAL) ? 1.67 : 2.149);
                        break;
                    }
                }
                case 3: {
                    this.moveSpeed = this.lastDist - ((this.mode.getValue() == Mode.NORMAL) ? 0.6896 : 0.795) * (this.lastDist - this.getBaseMoveSpeed());
                    break;
                }
            }
            if (Util.mc.gameSettings.keyBindJump.isKeyDown() || !Util.mc.player.onGround) {
                this.moveSpeed = Math.max(this.moveSpeed, this.getBaseMoveSpeed());
            }
            else {
                this.moveSpeed = this.getBaseMoveSpeed();
            }
            double n = Util.mc.player.movementInput.moveForward;
            double n2 = Util.mc.player.movementInput.moveStrafe;
            final double n3 = Util.mc.player.rotationYaw;
            if (n == 0.0 && n2 == 0.0) {
                event.setX(0.0);
                event.setZ(0.0);
            }
            else if (n != 0.0 && n2 != 0.0) {
                n *= Math.sin(0.7853981633974483);
                n2 *= Math.cos(0.7853981633974483);
            }
            final double n4 = (this.mode.getValue() == Mode.NORMAL) ? 0.993 : 0.99;
            event.setX((this.moveSpeed * n * -Math.sin(Math.toRadians(n3)) + this.moveSpeed * n2 * Math.cos(Math.toRadians(n3))) * n4);
            event.setZ((this.moveSpeed * n * Math.cos(Math.toRadians(n3)) - this.moveSpeed * n2 * -Math.sin(Math.toRadians(n3))) * n4);
            ++this.stage;
            event.setCanceled(true);
        }
    }
    
    public double getBaseMoveSpeed() {
        if (Util.mc.player.isPotionActive(MobEffects.SPEED)) {
            return 0.2873 * ((Objects.requireNonNull(Util.mc.player.getActivePotionEffect(MobEffects.SPEED)).getAmplifier() + 1) * 0.2 + 1.0);
        }
        return 0.2873;
    }
    
    @Override
    public String getDisplayInfo() {
        return this.mode.currentEnumName();
    }
    
    static {
        Strafe.INSTANCE = new Strafe();
    }
    
    public enum Mode
    {
        NORMAL, 
        Strict
    }
}
