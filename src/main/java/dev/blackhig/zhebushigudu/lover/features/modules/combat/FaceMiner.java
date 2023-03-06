package dev.blackhig.zhebushigudu.lover.features.modules.combat;

import dev.blackhig.zhebushigudu.lover.features.modules.Module;
import dev.blackhig.zhebushigudu.lover.features.modules.misc.InstantMine;
import dev.blackhig.zhebushigudu.lover.features.setting.Bind;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.lover;
import dev.blackhig.zhebushigudu.lover.util.EntityUtil;
import dev.blackhig.zhebushigudu.lover.util.SeijaUtil.SeijaBlockUtil;
import dev.blackhig.zhebushigudu.lover.util.SeijaUtil.SeijaDistanceUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.util.*;

public class FaceMiner extends Module
{
    private final Setting<Double> targetMaxSpeed;
    private final Setting<Double> targetRange;
    private final Setting<Integer> blockerCount;
    private final Setting<Boolean> precedenceHead;
    private final Setting<Boolean> onlyOnPressBind;
    Setting<Bind> mineKey;
    ArrayList<BlockPos> minePosList;
    int blockerCount1;
    
    public FaceMiner() {
        super("FaceMiner", "AntiAntiPiston", Category.COMBAT, true, false, false);
        this.targetMaxSpeed = this.register(new Setting<>("TargetMaxSpeed", 3.6, 0.0, 15.0));
        this.targetRange = this.register(new Setting<>("TargetRange", 3.6, 0.0, 7.0));
        this.blockerCount = this.register(new Setting<>("blockerCount", 20, 0, 100));
        this.precedenceHead = this.register(new Setting<>("PrecedenceHead", true));
        this.onlyOnPressBind = this.register(new Setting<>("onlyOnPressBind", true));
        this.mineKey = this.register(new Setting("MineKey", new Bind(0), v -> this.onlyOnPressBind.getValue()));
        this.blockerCount1 = 10;
    }
    
    @Override
    public void onUpdate() {
        if (InstantMine.breakPos == null || this.airCheck(InstantMine.breakPos)) {
            --this.blockerCount1;
        }
        else {
            this.blockerCount1 = this.blockerCount.getValue();
        }
        if (this.onlyOnPressBind.getValue() && !this.mineKey.getValue().isDown()) {
            return;
        }
        final EntityPlayer target = this.getTarget(this.targetRange.getValue());
        if (target == null || this.minePosList.size() == 0) {
            return;
        }
        final HashMap<BlockPos, Double> blockMap = new HashMap<>();
        for (final BlockPos minePos : this.minePosList) {
            blockMap.put(minePos, SeijaDistanceUtil.distanceToXZ(minePos.getX(), minePos.getZ()));
        }
        final List<Map.Entry<BlockPos, Double>> list = new ArrayList<>(blockMap.entrySet());
        list.sort(Map.Entry.comparingByValue());
        if (this.precedenceHead.getValue() && !this.airCheck(SeijaBlockUtil.getFlooredPosition(target).add(0, 2, 0))) {
            final HashMap<BlockPos, Double> blockMap2 = new HashMap<>();
            blockMap2.put(SeijaBlockUtil.getFlooredPosition(target).add(0, 2, 0), SeijaDistanceUtil.distanceToXZ(SeijaBlockUtil.getFlooredPosition(target).add(0, 2, 0)));
            final List<Map.Entry<BlockPos, Double>> list2 = new ArrayList<>(blockMap2.entrySet());
            list.set(0, list2.get(0));
        }
        if (list.size() >= 2 && InstantMine.breakPos2 == null && this.blockerCount1 < 0) {
            SeijaBlockUtil.mine(list.get(0).getKey(), list.get(1).getKey());
        }
        if (list.size() == 1 && InstantMine.breakPos2 == null && this.blockerCount1 < 0) {
            SeijaBlockUtil.mine(list.get(0).getKey());
        }
    }
    
    private ArrayList<BlockPos> getMineBlock(final BlockPos pos) {
        final ArrayList<BlockPos> posList = new ArrayList<>();
        if (!this.airCheck(pos.add(0, 2, 0))) {
            posList.add(pos.add(0, 2, 0));
        }
        if (!this.airCheck(pos.add(1, 2, 0))) {
            posList.add(pos.add(1, 2, 0));
        }
        if (!this.airCheck(pos.add(-1, 2, 0))) {
            posList.add(pos.add(-1, 2, 0));
        }
        if (!this.airCheck(pos.add(0, 2, 1))) {
            posList.add(pos.add(0, 2, 1));
        }
        if (!this.airCheck(pos.add(0, 2, -1))) {
            posList.add(pos.add(0, 2, -1));
        }
        if (!this.airCheck(pos.add(1, 1, 0))) {
            posList.add(pos.add(1, 1, 0));
        }
        if (!this.airCheck(pos.add(-1, 1, 0))) {
            posList.add(pos.add(-1, 1, 0));
        }
        if (!this.airCheck(pos.add(0, 1, 1))) {
            posList.add(pos.add(0, 1, 1));
        }
        if (!this.airCheck(pos.add(0, 1, -1))) {
            posList.add(pos.add(0, 1, -1));
        }
        return posList;
    }
    
    private EntityPlayer getTarget(final double range) {
        EntityPlayer target = null;
        double distance = range;
        for (final EntityPlayer player : FaceMiner.mc.world.playerEntities) {
            if (!EntityUtil.isntValid(player, range) && !lover.friendManager.isFriend(player.getName()) && FaceMiner.mc.player.posY - player.posY < 5.0 && lover.speedManager.getPlayerSpeed(player) <= this.targetMaxSpeed.getValue()) {
                if (this.getMineBlock(SeijaBlockUtil.getFlooredPosition(player)).size() == 0) {
                    continue;
                }
                if (target == null) {
                    target = player;
                    distance = EntityUtil.mc.player.getDistanceSq(player);
                }
                else {
                    if (EntityUtil.mc.player.getDistanceSq(player) >= distance) {
                        continue;
                    }
                    target = player;
                    distance = EntityUtil.mc.player.getDistanceSq(player);
                }
            }
        }
        if (target != null) {
            this.minePosList = this.getMineBlock(SeijaBlockUtil.getFlooredPosition(target));
        }
        return target;
    }
    
    public boolean airCheck(final BlockPos pos) {
        return FaceMiner.mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR) || FaceMiner.mc.world.getBlockState(pos).getBlock().equals(Blocks.PISTON) || FaceMiner.mc.world.getBlockState(pos).getBlock().equals(Blocks.STICKY_PISTON) || FaceMiner.mc.world.getBlockState(pos).getBlock().equals(Blocks.REDSTONE_BLOCK);
    }
}
