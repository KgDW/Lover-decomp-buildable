package dev.blackhig.zhebushigudu.lover.features.modules.combat;

import net.minecraft.util.math.AxisAlignedBB;
import java.util.HashSet;
import dev.blackhig.zhebushigudu.lover.util.HoleFillPlus.HoleUtil;
import net.minecraft.init.Blocks;
import dev.blackhig.zhebushigudu.lover.util.HoleFillPlus.EntityUtil;
import com.google.common.collect.Sets;

import java.util.List;
import dev.blackhig.zhebushigudu.lover.util.HoleFillPlus.PlayerUtil;
import dev.blackhig.zhebushigudu.lover.lover;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import dev.blackhig.zhebushigudu.lover.util.HoleFillPlus.BlockUtil;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.features.modules.Module;

public class HoleFillPlus extends Module
{
    Setting<Integer> range;
    Setting<Integer> holesPerSecond;
    Setting<fillModea> fillMode;
    Setting<Integer> smartRange;
    Setting<Boolean> doubleHoles;
    Setting<Boolean> rotate;
    Setting<Boolean> toggle;
    Setting<SwingMode> swing;
    
    public HoleFillPlus() {
        super("HoleFill+", "Fill Hole around you ee.", Category.COMBAT, true, false, false);
        this.range = this.register(new Setting<>("Range", 3, 1, 6));
        this.holesPerSecond = this.register(new Setting<>("HPS", 3, 1, 6));
        this.fillMode = this.register(new Setting<>("Mode", fillModea.Normal));
        this.smartRange = this.register(new Setting<>("Auto Range", 2, 1, 5));
        this.doubleHoles = this.register(new Setting<>("DoubleFill", false));
        this.rotate = this.register(new Setting<>("Rotate", true));
        this.toggle = this.register(new Setting<>("Toggle", false));
        this.swing = this.register(new Setting<>("Swing", SwingMode.Mainhand));
    }
    
    @Override
    public void onTick() {
        this.DoFill();
    }
    
    public void DoFill() {
        System.out.println("outputs");
        final List<BlockPos> holes = this.findHoles();
        BlockPos posToFill = null;
        if (holes.isEmpty() && this.toggle.getValue()) {
            this.disable();
            return;
        }
        for (int i = 0; i < this.holesPerSecond.getValue(); ++i) {
            final double bestDistance = 10.0;
            for (final BlockPos pos : new ArrayList<>(holes)) {
                final BlockUtil.ValidResult result = BlockUtil.valid(pos);
                if (result != BlockUtil.ValidResult.Ok) {
                    holes.remove(pos);
                }
                else if (this.fillMode.getValue() != fillModea.Normal) {
                    for (final EntityPlayer target : HoleFillPlus.mc.world.playerEntities) {
                        if (this.fillMode.getValue() == fillModea.Smart) {
                            final double distance2 = Minecraft.getMinecraft().player.getDistance(pos.getX(), pos.getY(), pos.getZ());
                            final double distance3 = target.getDistance(pos.getX(), pos.getY(), pos.getZ());
                            if (target == HoleFillPlus.mc.player) {
                                continue;
                            }
                            if (lover.friendManager.isFriend(target.getName())) {
                                continue;
                            }
                            if (distance3 > ((this.fillMode.getValue() == fillModea.Auto) ? this.smartRange.getValue() : 5)) {
                                continue;
                            }
                            if (distance3 > distance2) {
                                continue;
                            }
                            if (distance3 >= bestDistance) {
                                continue;
                            }
                            posToFill = pos;
                        }
                        else {
                            final double distance4 = target.getDistance(pos.getX(), pos.getY(), pos.getZ());
                            if (target == HoleFillPlus.mc.player) {
                                continue;
                            }
                            if (lover.friendManager.isFriend(target.getName())) {
                                continue;
                            }
                            if (distance4 > ((this.fillMode.getValue() == fillModea.Auto) ? this.smartRange.getValue() : 5)) {
                                continue;
                            }
                            if (distance4 >= bestDistance) {
                                continue;
                            }
                            posToFill = pos;
                        }
                    }
                }
                else {
                    posToFill = pos;
                }
            }
            if (PlayerUtil.findObiInHotbar() == -1) {
                return;
            }
            if (posToFill != null) {
                BlockUtil.placeBlock(posToFill, PlayerUtil.findObiInHotbar(), this.rotate.getValue(), this.rotate.getValue(), this.swing.getValue().toString());
                holes.remove(posToFill);
            }
        }
    }
    
    public List<BlockPos> findHoles() {
        final int range = this.range.getValue();
        final HashSet<BlockPos> possibleHoles = Sets.newHashSet();
        final List<BlockPos> blockPosList = EntityUtil.getSphere(PlayerUtil.getPlayerPos(), (float)range, range, false, true, 0);
        final List<BlockPos> holes = new ArrayList<BlockPos>();
        for (final BlockPos pos : blockPosList) {
            if (!HoleFillPlus.mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR)) {
                continue;
            }
            if (HoleFillPlus.mc.world.getBlockState(pos.add(0, -1, 0)).getBlock().equals(Blocks.AIR)) {
                continue;
            }
            if (!HoleFillPlus.mc.world.getBlockState(pos.add(0, 1, 0)).getBlock().equals(Blocks.AIR)) {
                continue;
            }
            if (!HoleFillPlus.mc.world.getBlockState(pos.add(0, 2, 0)).getBlock().equals(Blocks.AIR)) {
                continue;
            }
            possibleHoles.add(pos);
        }
        for (final BlockPos pos : possibleHoles) {
            final HoleUtil.HoleInfo holeInfo = HoleUtil.isHole(pos, false, false);
            final HoleUtil.HoleType holeType = holeInfo.getType();
            if (holeType != HoleUtil.HoleType.NONE) {
                final AxisAlignedBB centreBlocks = holeInfo.getCentre();
                if (centreBlocks == null) {
                    continue;
                }
                if (holeType == HoleUtil.HoleType.DOUBLE && !this.doubleHoles.getValue()) {
                    continue;
                }
                holes.add(pos);
            }
        }
        return holes;
    }
    
    public enum SwingMode
    {
        Mainhand, 
        Offhand, 
        None;
    }
    
    public enum fillModea
    {
        Normal, 
        Smart, 
        Auto;
    }
}
