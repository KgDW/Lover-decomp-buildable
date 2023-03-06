package dev.blackhig.zhebushigudu.lover.features.modules.combat;

import dev.blackhig.zhebushigudu.lover.event.events.EventMotion;
import dev.blackhig.zhebushigudu.lover.event.events.Render3DEvent;
import dev.blackhig.zhebushigudu.lover.features.command.Command;
import dev.blackhig.zhebushigudu.lover.features.modules.Module;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.lover;
import dev.blackhig.zhebushigudu.lover.util.BlockUtils;
import dev.blackhig.zhebushigudu.lover.util.RenderUtils;
import dev.blackhig.zhebushigudu.lover.util.TargetUtils;
import dev.blackhig.zhebushigudu.lover.util.Util;
import dev.blackhig.zhebushigudu.lover.util.e.InventoryUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEmptyDrops;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PistonAura extends Module
{
    Setting<Double> range;
    Setting<Integer> delay1;
    Setting<Integer> delay2;
    Setting<Integer> min;
    Setting<Integer> thread;
    int progress;
    EnumFacing facing;
    int sleep;
    List<PA> attackable;
    List<PA> attack;
    public static final BlockPos[] pistonoff;
    
    public PistonAura() {
        super("PistonAura", "PistonAura", Category.COMBAT, true, false, false);
        this.range = this.register(new Setting<Number>("Range", 5.2, 0, 15));
        this.delay1 = this.register(new Setting<>("ChangeDelay", 5, 0, 20));
        this.delay2 = this.register(new Setting<>("PlaceDelay", 2, (-1), 100));
        this.min = this.register(new Setting<>("MinDamage", 21, 0, 100));
        this.thread = this.register(new Setting<>("Thread", 1, 0, 10));
        this.progress = 0;
    }
    
    @Override
    public void onEnable() {
        this.progress = 0;
        this.attackable = new ArrayList<>();
        this.attack = new CopyOnWriteArrayList<>();
        super.onEnable();
    }
    
    @Override
    public void onTick() {
        final int pitem = InventoryUtil.pickItem(33, false);
        final int cryst = InventoryUtil.pickItem(426, false);
        final int powtem1 = InventoryUtil.pickItem(152, false);
        final int powtem2 = InventoryUtil.pickItem(76, false);
        if (pitem == -1 || cryst == -1 || (powtem1 == -1 && powtem2 == -1)) {
            Command.sendMessage("Item Not Found ");
            this.toggle();
        }
        if (!TargetUtils.findTarget(this.range.getValue())) {
            return;
        }
        final Entity entity = TargetUtils.currentTarget;
        final Entity player = TargetUtils.currentTarget;
        final BlockPos playerPos = new BlockPos(TargetUtils.currentTarget);
        final Double range = this.range.getValue();
        if (this.attackable.isEmpty() || PistonAura.mc.player.ticksExisted % Math.max(1, this.delay1.getValue() / Math.max(1, this.thread.getValue())) == 0) {
            this.attackable = new ArrayList<PA>();
            for (int dx = (int) -range; dx <= range; ++dx) {
                for (int dy = (int) -range; dy <= range; ++dy) {
                    for (int dz = (int) -range; dz <= range; ++dz) {
                        final BlockPos pos = new BlockPos(PistonAura.mc.player).add(dx, dy, dz);
                        if (player.getDistanceSq(pos) <= range * range) {
                            boolean b = false;
                            for (final BlockPos off : PistonAura.pistonoff) {
                                final Block block = PistonAura.mc.world.getBlockState(pos.add(off)).getBlock();
                                if (block instanceof BlockObsidian) {
                                    b = true;
                                    break;
                                }
                                if (block instanceof BlockEmptyDrops) {
                                    b = true;
                                    break;
                                }
                            }
                            if (b) {
                                final double damage = getDamage(new Vec3d(pos).add(0.5, 0.0, 0.5), TargetUtils.currentTarget);
                                if (damage >= this.min.getValue()) {
                                    final PA pa = new PA(pos, damage);
                                    if (pa.canPA()) {
                                        this.attackable.add(pa);
                                    }
                                }
                            }
                        }
                    }
                }
                this.attackable.sort((a, b) -> {
                    if (a == null && b == null) {
                        return 0;
                    }
                    if (a.damage < b.damage) {
                        return 1;
                    }
                    if (a.damage > b.damage) {
                        return -1;
                    }
                    return 0;
                });
            }
        }
    }
    
    @SubscribeEvent
    public void onmotion(final EventMotion e) {
        if (!TargetUtils.findTarget(this.range.getValue())) {
            return;
        }
        if (lover.friendManager.isFriend((EntityPlayer)TargetUtils.currentTarget)) {
            return;
        }
        final EventMotion event = e;
        if (!this.attackable.isEmpty() && this.attack.size() < this.thread.getValue() && PistonAura.mc.player.ticksExisted % Math.max(1, this.delay1.getValue() / Math.max(1, this.thread.getValue())) == 0) {
            this.attack.add(this.attackable.get(0));
        }
        for (final PA pa : this.attack) {
            pa.updatePA(event);
            if (pa.stage > this.delay1.getValue() + 1) {
                this.attack.remove(pa);
            }
        }
        for (final Entity et : PistonAura.mc.world.loadedEntityList) {
            if (et instanceof EntityEnderCrystal) {
                if (!TargetUtils.canAttack(PistonAura.mc.player.getPositionVector().add(0.0, PistonAura.mc.player.getEyeHeight(), 0.0), et.getPositionVector().add(0.0, 1.7, 0.0))) {
                    continue;
                }
                PistonAura.mc.playerController.attackEntity(PistonAura.mc.player, et);
                PistonAura.mc.player.swingArm(EnumHand.MAIN_HAND);
                PistonAura.mc.world.removeEntity(et);
            }
        }
    }
    
    public EnumFacing getFacing(final BlockPos position) {
        for (final EnumFacing f : EnumFacing.values()) {
            if (f.getAxis() != EnumFacing.Axis.Y) {
                final BlockPos pos = new BlockPos(position).offset(f, 2);
                if (PistonAura.mc.world.isAirBlock(pos)) {
                    return f;
                }
            }
        }
        return null;
    }
    
    @Override
    public void onRender3D(final Render3DEvent event) {
        for (final PA pa : this.attack) {
            final Color col = new Color(3619901);
            RenderUtils.drawBlockBox(pa.crystal, alpha(col, 64));
            RenderUtils.drawBlockBox(pa.piston, alpha(col, 64));
            if (pa.power != null) {
                RenderUtils.drawBlockBox(pa.power, alpha(col, 64));
            }
            RenderUtils.drawBlockBox(pa.crystal.offset(pa.pistonFacing), alpha(new Color(16777215), 32));
        }
    }
    
    public static Color alpha(final Color color, final int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
    
    public EnumFacing rotateHantaigawa(final EnumFacing f) {
        switch (f) {
            case WEST: {
                return EnumFacing.EAST;
            }
            case EAST: {
                return EnumFacing.WEST;
            }
            case SOUTH: {
                return EnumFacing.NORTH;
            }
            case NORTH: {
                return EnumFacing.SOUTH;
            }
            case UP: {
                return EnumFacing.DOWN;
            }
            case DOWN: {
                return EnumFacing.UP;
            }
            default: {
                throw new IllegalStateException("Unable to get CCW facing of " + this);
            }
        }
    }
    
    public static boolean placeCrystalSilent(final BlockPos pos) {
        pos.offset(EnumFacing.DOWN);
        final double dx = pos.getX() + 0.5 - PistonAura.mc.player.posX;
        final double dy = pos.getY() + 0.5 - PistonAura.mc.player.posY - 0.5 - PistonAura.mc.player.getEyeHeight();
        final double dz = pos.getZ() + 0.5 - PistonAura.mc.player.posZ;
        final double x = BlockUtils.getDirection2D(dz, dx);
        final double y = BlockUtils.getDirection2D(dy, Math.sqrt(dx * dx + dz * dz));
        final int slot = InventoryUtil.pickItem(426, false);
        if (slot == -1 && ((ItemStack)PistonAura.mc.player.inventory.offHandInventory.get(0)).getItem() != Items.END_CRYSTAL) {
            return false;
        }
        final Vec3d vec = getVectorForRotation(-y, x - 90.0);
        if (((ItemStack)PistonAura.mc.player.inventory.offHandInventory.get(0)).getItem() == Items.END_CRYSTAL) {
            PistonAura.mc.getConnection().sendPacket(new CPacketPlayerTryUseItemOnBlock(pos.offset(EnumFacing.DOWN), EnumFacing.UP, EnumHand.OFF_HAND, 0.0f, 0.0f, 0.0f));
        }
        else if (InventoryUtil.pickItem(426, false) != -1) {
            PistonAura.mc.getConnection().sendPacket(new CPacketHeldItemChange(slot));
            PistonAura.mc.getConnection().sendPacket(new CPacketPlayerTryUseItemOnBlock(pos.offset(EnumFacing.DOWN), EnumFacing.UP, EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
        }
        return true;
    }
    
    protected static final Vec3d getVectorForRotation(final double pitch, final double yaw) {
        final float f = MathHelper.cos((float)(-yaw * 0.01745329238474369 - 3.1415927410125732));
        final float f2 = MathHelper.sin((float)(-yaw * 0.01745329238474369 - 3.1415927410125732));
        final float f3 = -MathHelper.cos((float)(-pitch * 0.01745329238474369));
        final float f4 = MathHelper.sin((float)(-pitch * 0.01745329238474369));
        return new Vec3d((f2 * f3), f4, (f * f3));
    }
    
    public static boolean canAttack(final Vec3d vec, final Vec3d pos) {
        final boolean flag = PistonAura.mc.world.rayTraceBlocks(vec, pos, false, true, false) == null;
        double d0 = 36.0;
        if (!flag) {
            d0 = 9.0;
        }
        return vec.squareDistanceTo(pos) < d0;
    }
    
    public static double getDamage(final Vec3d pos, @Nullable final Entity target) {
        final Entity entity = ((target == null) ? PistonAura.mc.player : target);
        final float damage = 6.0f;
        final float f3 = damage * 2.0f;
        if (!entity.isImmuneToExplosions()) {
            final double d12 = entity.getDistance(pos.x, pos.y, pos.z) / f3;
            if (d12 <= 1.0) {
                double d13 = entity.posX - pos.x;
                double d14 = entity.posY + entity.getEyeHeight() - pos.y;
                double d15 = entity.posZ - pos.z;
                final double d16 = MathHelper.sqrt(d13 * d13 + d14 * d14 + d15 * d15);
                if (d16 != 0.0) {
                    final double d17 = PistonAura.mc.world.getBlockDensity(pos, entity.getEntityBoundingBox());
                    final double d18 = (1.0 - d12) * d17;
                    return (float)(int)((d18 * d18 + d18) / 2.0 * 7.0 * f3 + 1.0);
                }
            }
        }
        return 0.0;
    }
    
    static {
        pistonoff = new BlockPos[] { new BlockPos(-1, -1, -1), new BlockPos(0, -1, -1), new BlockPos(1, -1, -1), new BlockPos(-1, -1, 0), new BlockPos(0, -1, 0), new BlockPos(1, -1, 0), new BlockPos(-1, -1, 1), new BlockPos(0, -1, 1), new BlockPos(1, -1, 1), new BlockPos(-1, 0, -1), new BlockPos(0, 0, -1), new BlockPos(1, 0, -1), new BlockPos(-1, 0, 0), new BlockPos(0, 0, 0), new BlockPos(1, 0, 0), new BlockPos(-1, 0, 1), new BlockPos(0, 0, 1), new BlockPos(1, 0, 1), new BlockPos(-1, 1, -1), new BlockPos(0, 1, -1), new BlockPos(1, 1, -1), new BlockPos(-1, 1, 0), new BlockPos(0, 1, 0), new BlockPos(1, 1, 0), new BlockPos(-1, 1, 1), new BlockPos(0, 1, 1), new BlockPos(1, 1, 1) };
    }
    
    public class PA
    {
        public BlockPos pos;
        public BlockPos crystal;
        public BlockPos power;
        public EnumFacing pistonFacing;
        public BlockPos piston;
        public double damage;
        public int stage;
        
        public PA(final BlockPos pos, final double damage) {
            this.pos = pos;
            this.damage = damage;
            this.stage = 0;
        }
        
        public boolean canPA() {
            final long time = System.nanoTime();
            final boolean isTorch = InventoryUtil.pickItem(76, false) != -1;
            final float pist = 0.5f;
            for (final EnumFacing f : EnumFacing.values()) {
                final BlockPos crypos = this.pos.offset(f);
                if (Util.mc.world.isAirBlock(crypos)) {
                    final BlockPos crystalhead = crypos.offset(EnumFacing.UP);
                    this.pistonFacing = PistonAura.this.rotateHantaigawa(f);
                    if (this.pistonFacing != EnumFacing.DOWN) {
                        final BlockPos crystallook = crypos.offset(this.pistonFacing);
                        if (Util.mc.world.isAirBlock(crystallook)) {
                            if (Util.mc.world.isAirBlock(crystalhead)) {
                                if (PistonAura.canAttack(Util.mc.player.getPositionVector().add(0.0, Util.mc.player.getEyeHeight(), 0.0), new Vec3d(crypos).add(0.5, 1.7, 0.5))) {
                                    final Block b1 = Util.mc.world.getBlockState(crypos.offset(EnumFacing.DOWN)).getBlock();
                                    if (b1 instanceof BlockObsidian || b1 instanceof BlockEmptyDrops) {
                                        if (Util.mc.player.getDistanceSqToCenter(crypos) < 64.0) {
                                            if (Util.mc.world.checkNoEntityCollision(Block.FULL_BLOCK_AABB.offset(crypos))) {
                                                this.crystal = crypos;
                                                for (final BlockPos off : PistonAura.pistonoff) {
                                                    final BlockPos pispos = this.crystal.add(off);
                                                    final BlockPos pistonhead = pispos.offset(this.pistonFacing);
                                                    Label_1198: {
                                                        if (!pispos.equals(crypos)) {
                                                            if (!crystalhead.equals(pispos)) {
                                                                if (!crystallook.equals(pispos)) {
                                                                    if (Util.mc.world.isAirBlock(pispos)) {
                                                                        if (Util.mc.world.isAirBlock(pistonhead)) {
                                                                            if (pispos.getY() >= this.crystal.getY() || this.pistonFacing.getAxis() == EnumFacing.Axis.Y) {
                                                                                if (Util.mc.player.getDistanceSqToCenter(pispos) < 64.0) {
                                                                                    final EnumFacing sfac = EnumFacing.getDirectionFromEntityLiving(pispos, (EntityLivingBase)Util.mc.player);
                                                                                    if (this.pistonFacing.getAxis() != EnumFacing.Axis.Y || this.pistonFacing == sfac) {
                                                                                        if (sfac.getAxis() != EnumFacing.Axis.Y || this.pistonFacing == sfac) {
                                                                                            if (!Util.mc.world.getEntitiesWithinAABBExcludingEntity(null, Block.FULL_BLOCK_AABB.offset(pistonhead)).contains(Util.mc.player)) {
                                                                                                this.power = null;
                                                                                                final Vec3i pv = this.pistonFacing.getDirectionVec();
                                                                                                if (Util.mc.world.isBlockPowered(pispos)) {
                                                                                                    if (dev.blackhig.zhebushigudu.lover.util.n.BlockUtils.isPlaceable(pispos, 0.0, true) == null) {
                                                                                                        break Label_1198;
                                                                                                    }
                                                                                                }
                                                                                                else {
                                                                                                    for (final EnumFacing fa : EnumFacing.values()) {
                                                                                                        final BlockPos powpos = pispos.offset(fa);
                                                                                                        if (!pispos.equals(powpos)) {
                                                                                                            if (!pistonhead.equals(powpos)) {
                                                                                                                if (!crypos.equals(powpos)) {
                                                                                                                    if (!crystalhead.equals(powpos)) {
                                                                                                                        if (Util.mc.world.isAirBlock(powpos)) {
                                                                                                                            if (Util.mc.player.getDistanceSq(powpos.getX() + 0.5, powpos.getY() + 0.5, powpos.getZ() + 0.5) < 64.0) {
                                                                                                                                if (pv.getX() <= 0 || powpos.getX() - pist <= crypos.getX()) {
                                                                                                                                    if (pv.getY() <= 0 || powpos.getY() - pist <= crypos.getY()) {
                                                                                                                                        if (pv.getZ() <= 0 || powpos.getZ() - pist <= crypos.getZ()) {
                                                                                                                                            if (pv.getX() >= 0 || powpos.getX() + pist >= crypos.getX()) {
                                                                                                                                                if (pv.getY() >= 0 || powpos.getY() + pist >= crypos.getY()) {
                                                                                                                                                    if (pv.getZ() >= 0 || powpos.getZ() + pist >= crypos.getZ()) {
                                                                                                                                                        if (dev.blackhig.zhebushigudu.lover.util.n.BlockUtils.isPlaceable(powpos, 0.0, true) != null) {
                                                                                                                                                            this.power = powpos;
                                                                                                                                                        }
                                                                                                                                                    }
                                                                                                                                                }
                                                                                                                                            }
                                                                                                                                        }
                                                                                                                                    }
                                                                                                                                }
                                                                                                                            }
                                                                                                                        }
                                                                                                                    }
                                                                                                                }
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                    if (this.power == null) {
                                                                                                        break Label_1198;
                                                                                                    }
                                                                                                }
                                                                                                if (Util.mc.world.checkNoEntityCollision(Block.FULL_BLOCK_AABB.offset(pispos))) {
                                                                                                    if (pv.getX() <= 0 || pispos.getX() - pist <= crypos.getX()) {
                                                                                                        if (pv.getY() <= 0 || pispos.getY() - pist <= crypos.getY()) {
                                                                                                            if (pv.getZ() <= 0 || pispos.getZ() - pist <= crypos.getZ()) {
                                                                                                                if (pv.getX() >= 0 || pispos.getX() + pist >= crypos.getX()) {
                                                                                                                    if (pv.getY() >= 0 || pispos.getY() + pist >= crypos.getY()) {
                                                                                                                        if (pv.getZ() >= 0 || pispos.getZ() + pist >= crypos.getZ()) {
                                                                                                                            this.piston = pispos;
                                                                                                                            return true;
                                                                                                                        }
                                                                                                                    }
                                                                                                                }
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            System.out.println("a" + (System.nanoTime() - time));
            return false;
        }
        
        public void updatePA(final EventMotion event) {
            final int pitem = InventoryUtil.pickItem(33, false);
            final int powtem1 = InventoryUtil.pickItem(152, false);
            final int powtem2 = InventoryUtil.pickItem(76, false);
            final int cryst = InventoryUtil.pickItem(426, false);
            switch (this.pistonFacing) {
                case SOUTH: {
                    event.yaw = 180.0f;
                    event.pitch = 0.0f;
                    break;
                }
                case NORTH: {
                    event.yaw = 0.0f;
                    event.pitch = 0.0f;
                    break;
                }
                case EAST: {
                    event.yaw = 90.0f;
                    event.pitch = 0.0f;
                    break;
                }
                case WEST: {
                    event.yaw = -90.0f;
                    event.pitch = 0.0f;
                    break;
                }
                case UP: {
                    event.pitch = 90.0f;
                    break;
                }
                case DOWN: {
                    event.pitch = 90.0f;
                    break;
                }
            }
            if (PistonAura.this.delay2.getValue() == -1) {
                Util.mc.getConnection().sendPacket(new CPacketPlayer.Rotation(event.yaw, event.pitch, Util.mc.player.onGround));
            }
            if (this.stage == PistonAura.this.delay2.getValue() || (this.stage == 0 && PistonAura.this.delay2.getValue() == -1)) {
                final int pow = (powtem1 == -1) ? powtem2 : powtem1;
                if (this.power != null && pow == -1) {
                    return;
                }
                if (pitem == -1 || cryst == -1) {
                    return;
                }
                dev.blackhig.zhebushigudu.lover.util.n.BlockUtils.doPlaceSilent(dev.blackhig.zhebushigudu.lover.util.n.BlockUtils.isPlaceable(this.piston, 0.0, false), pitem, false);
                if (this.power != null) {
                    dev.blackhig.zhebushigudu.lover.util.n.BlockUtils.doPlaceSilent(dev.blackhig.zhebushigudu.lover.util.n.BlockUtils.isPlaceable(this.power, 0.0, false), pow, false);
                }
                dev.blackhig.zhebushigudu.lover.util.n.BlockUtils.doPlaceSilent(dev.blackhig.zhebushigudu.lover.util.n.BlockUtils.isPlaceable(this.piston, 0.0, false), pitem, false);
                PistonAura.placeCrystalSilent(this.crystal);
                if (this.power != null) {
                    dev.blackhig.zhebushigudu.lover.util.n.BlockUtils.doPlaceSilent(dev.blackhig.zhebushigudu.lover.util.n.BlockUtils.isPlaceable(this.power, 0.0, false), pow, false);
                }
            }
            Util.mc.world.setBlockToAir(this.piston);
            Util.mc.getConnection().sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.piston, EnumFacing.UP));
            if (this.power != null) {
                Util.mc.world.setBlockToAir(this.power);
                Util.mc.getConnection().sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.power, EnumFacing.UP));
            }
            ++this.stage;
            Util.mc.getConnection().sendPacket(new CPacketHeldItemChange(InventoryUtil.getSlot()));
        }
    }
}
