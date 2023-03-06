package dev.blackhig.zhebushigudu.lover.features.modules.movement;

import net.minecraft.util.math.Vec3d;
import net.minecraft.init.Blocks;
import net.minecraft.entity.Entity;
import dev.blackhig.zhebushigudu.lover.util.SeijaUtil.SeijaBlockUtil;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.features.modules.Module;

public class PositionBug extends Module
{
    public final Setting<Integer> offset;
    public final Setting<Boolean> onBurrow;
    public final Setting<Boolean> toggle;
    
    public PositionBug() {
        super("PositionBug", "", Category.MOVEMENT, true, false, false);
        this.offset = this.register(new Setting<>("Offset", 1, 0, 10));
        this.onBurrow = this.register(new Setting<>("OnBurrow", false));
        this.toggle = this.register(new Setting<>("AutoToggle", false));
    }
    
    @Override
    public void onTick() {
        final double x = Math.abs(PositionBug.mc.player.posX) - Math.floor(Math.abs(PositionBug.mc.player.posX));
        final double z = Math.abs(PositionBug.mc.player.posZ) - Math.floor(Math.abs(PositionBug.mc.player.posZ));
        if (x == 0.7 || x == 0.3 || z == 0.7 || z == 0.3 || (!this.onBurrow.getValue() && !PositionBug.mc.world.getBlockState(SeijaBlockUtil.getFlooredPosition(PositionBug.mc.player)).getBlock().equals(Blocks.AIR))) {
            return;
        }
        final Vec3d playerVec = PositionBug.mc.player.getPositionVector();
        if (!PositionBug.mc.world.getBlockState(SeijaBlockUtil.vec3toBlockPos(playerVec.add(new Vec3d(0.3 + this.offset.getValue() / 100.0, 0.2, 0.0)))).getBlock().equals(Blocks.AIR)) {
            PositionBug.mc.player.setPosition(PositionBug.mc.player.posX + this.offset.getValue() / 100.0, PositionBug.mc.player.posY, PositionBug.mc.player.posZ);
            if (this.toggle.getValue()) {
                this.disable();
            }
            return;
        }
        if (!PositionBug.mc.world.getBlockState(SeijaBlockUtil.vec3toBlockPos(playerVec.add(new Vec3d(-0.3 - this.offset.getValue() / 100.0, 0.2, 0.0)))).getBlock().equals(Blocks.AIR)) {
            PositionBug.mc.player.setPosition(PositionBug.mc.player.posX - this.offset.getValue() / 100.0, PositionBug.mc.player.posY, PositionBug.mc.player.posZ);
            if (this.toggle.getValue()) {
                this.disable();
            }
            return;
        }
        if (!PositionBug.mc.world.getBlockState(SeijaBlockUtil.vec3toBlockPos(playerVec.add(new Vec3d(0.0, 0.2, 0.3 + this.offset.getValue() / 100.0)))).getBlock().equals(Blocks.AIR)) {
            PositionBug.mc.player.setPosition(PositionBug.mc.player.posX, PositionBug.mc.player.posY, PositionBug.mc.player.posZ + this.offset.getValue() / 100.0);
            if (this.toggle.getValue()) {
                this.disable();
            }
            return;
        }
        if (!PositionBug.mc.world.getBlockState(SeijaBlockUtil.vec3toBlockPos(playerVec.add(new Vec3d(0.0, 0.2, -0.3 - this.offset.getValue() / 100.0)))).getBlock().equals(Blocks.AIR)) {
            PositionBug.mc.player.setPosition(PositionBug.mc.player.posX, PositionBug.mc.player.posY, PositionBug.mc.player.posZ - this.offset.getValue() / 100.0);
            if (this.toggle.getValue()) {
                this.disable();
            }
        }
    }
}
