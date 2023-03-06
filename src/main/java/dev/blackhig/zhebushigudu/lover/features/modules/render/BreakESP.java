package dev.blackhig.zhebushigudu.lover.features.modules.render;

import dev.blackhig.zhebushigudu.lover.util.FadeUtils;
import dev.blackhig.zhebushigudu.lover.util.RenderUtils3D;
import java.awt.Color;
import net.minecraft.util.math.Vec3d;
import dev.blackhig.zhebushigudu.lover.util.RenderUtil;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.entity.Entity;
import dev.blackhig.zhebushigudu.lover.util.MathUtil;
import dev.blackhig.zhebushigudu.lover.event.events.Render3DEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import dev.blackhig.zhebushigudu.lover.features.modules.misc.InstantMine;
import dev.blackhig.zhebushigudu.lover.event.events.BlockBreakEvent;
import java.util.concurrent.atomic.AtomicReference;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.player.EntityPlayer;
import java.util.HashMap;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.features.modules.Module;

public class BreakESP extends Module
{
    private final Setting<Double> range;
    private final Setting<Boolean> render;
    private final Setting<Integer> red;
    private final Setting<Integer> green;
    private final Setting<Integer> blue;
    private final Setting<Integer> alpha;
    private final Setting<Boolean> playername;
    private final Setting<Boolean> render1;
    private final Setting<Integer> red1;
    private final Setting<Integer> green1;
    private final Setting<Integer> blue1;
    private final Setting<Integer> alpha1;
    private final Setting<Boolean> playername1;
    static HashMap<EntityPlayer, InstantMiner> miners;
    
    public BreakESP() {
        super("BreakESP", "Render the miners' breaking position", Category.RENDER, true, false, false);
        this.range = this.register(new Setting<>("Range", 5.5, 0.0, 10.0));
        this.render = this.register(new Setting<>("FirstRender", true));
        this.red = this.register(new Setting<>("Red", 255, 0, 255));
        this.green = this.register(new Setting<>("Green", 255, 0, 255));
        this.blue = this.register(new Setting<>("Blue", 255, 0, 255));
        this.alpha = this.register(new Setting<>("Alpha", 100, 20, 255));
        this.playername = this.register(new Setting<>("FirstPlayerName", true));
        this.render1 = this.register(new Setting<>("SecondRender", true));
        this.red1 = this.register(new Setting<>("Red", 255, 0, 255));
        this.green1 = this.register(new Setting<>("Green", 255, 0, 255));
        this.blue1 = this.register(new Setting<>("Blue", 255, 0, 255));
        this.alpha1 = this.register(new Setting<>("Alpha", 100, 20, 255));
        this.playername1 = this.register(new Setting<>("SecondPlayerName", true));
    }
    
    public static boolean isBreaking(final BlockPos pos) {
        final AtomicReference<InstantMiner> lastMiner = new AtomicReference<>(null);
        BreakESP.miners.values().forEach(e -> {
            try {
                if (pos.getDistance(e.first.getX(), e.first.getY(), e.first.getZ()) < 1.0 || pos.getDistance(e.second.getX(), e.second.getY(), e.second.getZ()) < 1.0) {
                    lastMiner.set(e);
                }
            }
            catch (final Exception ignored) {}
        });
        return lastMiner.get() != null;
    }
    
    @SubscribeEvent
    public void onBreaking(final BlockBreakEvent e) {
        if (e.getBreaker() == BreakESP.mc.player) {
            return;
        }
        if (InstantMine.godBlocks.contains(BreakESP.mc.world.getBlockState(e.getPosition()).getBlock())) {
            return;
        }
        final EntityPlayer breaker = (EntityPlayer)BreakESP.mc.world.getEntityByID(e.getBreakId());
        if (!BreakESP.miners.containsKey(breaker)) {
            BreakESP.miners.put(breaker, new InstantMiner(breaker));
        }
        BreakESP.miners.get(breaker).updateFirst(e.getPosition());
    }
    
    @Override
    public void onRender3D(final Render3DEvent event) {
        final EntityPlayer[] array;
        final EntityPlayer[] s = array = BreakESP.miners.keySet().toArray(new EntityPlayer[0]);
        for (final EntityPlayer entityPlayer : array) {
            if (entityPlayer != null) {
                if (!entityPlayer.isEntityAlive()) {
                    BreakESP.miners.remove(entityPlayer);
                }
            }
        }
        BreakESP.miners.values().forEach(miner -> {
            if (miner.first.getDistance((int)BreakESP.mc.player.posX, (int)BreakESP.mc.player.posY, (int)BreakESP.mc.player.posZ) < this.range.getValue()) {
                final Vec3d interpolateEntity = MathUtil.interpolateEntity(BreakESP.mc.player, BreakESP.mc.getRenderPartialTicks());
                if (this.render.getValue()) {
                    final AxisAlignedBB axisAlignedBB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0).offset(miner.first);
                    final AxisAlignedBB axisAlignedBB2 = axisAlignedBB.grow(0.0020000000949949026).offset(-interpolateEntity.x, -interpolateEntity.y, -interpolateEntity.z);
                    final double size = miner.firstFade.easeOutQuad();
                    this.draw(axisAlignedBB2, size);
                }
                if (this.playername.getValue()) {
                    RenderUtil.drawText(miner.first, (miner.player == null) ? "Unknown" : miner.player.getName());
                }
            }
            if (!miner.secondFinished && miner.second != null) {
                if (BreakESP.mc.world.isAirBlock(miner.second)) {
                    miner.finishSecond();
                }
                else if (miner.second.getDistance((int)BreakESP.mc.player.posX, (int)BreakESP.mc.player.posY, (int)BreakESP.mc.player.posZ) < this.range.getValue()) {
                    final Vec3d interpolateEntity2 = MathUtil.interpolateEntity(BreakESP.mc.player, BreakESP.mc.getRenderPartialTicks());
                    if (this.render1.getValue()) {
                        final AxisAlignedBB axisAlignedBB3 = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0).offset(miner.second);
                        final AxisAlignedBB axisAlignedBB4 = axisAlignedBB3.grow(0.0020000000949949026).offset(-interpolateEntity2.x, -interpolateEntity2.y, -interpolateEntity2.z);
                        final double size2 = miner.secondFade.easeOutQuad();
                        this.draw1(axisAlignedBB4, size2);
                    }
                    if (this.playername1.getValue()) {
                        RenderUtil.drawText(miner.second, (miner.player == null) ? "Unknown" : miner.player.getName());
                    }
                }
            }
        });
    }
    
    public void draw(final AxisAlignedBB axisAlignedBB, final double size) {
        final double centerX = axisAlignedBB.minX + (axisAlignedBB.maxX - axisAlignedBB.minX) / 2.0;
        final double centerY = axisAlignedBB.minY + (axisAlignedBB.maxY - axisAlignedBB.minY) / 2.0;
        final double centerZ = axisAlignedBB.minZ + (axisAlignedBB.maxZ - axisAlignedBB.minZ) / 2.0;
        final double full = axisAlignedBB.maxX - centerX;
        final double progressValX = full * size;
        final double progressValY = full * size;
        final double progressValZ = full * size;
        final AxisAlignedBB axisAlignedBB2 = new AxisAlignedBB(centerX - progressValX, centerY - progressValY, centerZ - progressValZ, centerX + progressValX, centerY + progressValY, centerZ + progressValZ);
        RenderUtils3D.drawBlockOutline(axisAlignedBB2, new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), 1.5f);
        RenderUtil.drawFilledBox(axisAlignedBB2, new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()).getRGB());
    }
    
    public void draw1(final AxisAlignedBB axisAlignedBB, final double size) {
        final double centerX = axisAlignedBB.minX + (axisAlignedBB.maxX - axisAlignedBB.minX) / 2.0;
        final double centerY = axisAlignedBB.minY + (axisAlignedBB.maxY - axisAlignedBB.minY) / 2.0;
        final double centerZ = axisAlignedBB.minZ + (axisAlignedBB.maxZ - axisAlignedBB.minZ) / 2.0;
        final double full = axisAlignedBB.maxX - centerX;
        final double progressValX = full * size;
        final double progressValY = full * size;
        final double progressValZ = full * size;
        final AxisAlignedBB axisAlignedBB2 = new AxisAlignedBB(centerX - progressValX, centerY - progressValY, centerZ - progressValZ, centerX + progressValX, centerY + progressValY, centerZ + progressValZ);
        RenderUtils3D.drawBlockOutline(axisAlignedBB2, new Color(this.red1.getValue(), this.green1.getValue(), this.blue1.getValue(), this.alpha1.getValue()), 1.5f);
        RenderUtil.drawFilledBox(axisAlignedBB2, new Color(this.red1.getValue(), this.green1.getValue(), this.blue1.getValue(), this.alpha1.getValue()).getRGB());
    }
    
    static {
        BreakESP.miners = new HashMap<>();
    }
    
    private static class InstantMiner
    {
        public EntityPlayer player;
        public FadeUtils firstFade;
        public FadeUtils secondFade;
        public BlockPos first;
        public BlockPos second;
        public boolean secondFinished;
        
        public InstantMiner(final EntityPlayer player) {
            this.firstFade = new FadeUtils(2000L);
            this.secondFade = new FadeUtils(2000L);
            this.player = player;
            this.secondFinished = true;
        }
        
        public void finishSecond() {
            this.secondFinished = true;
        }
        
        public void updateFirst(final BlockPos pos) {
            if (this.first == pos || this.second == pos) {
                return;
            }
            this.second = this.first;
            this.first = pos;
            this.secondFinished = false;
            this.firstFade.reset();
            this.secondFade.reset();
        }
    }
}
