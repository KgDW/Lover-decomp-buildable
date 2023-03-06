package dev.blackhig.zhebushigudu.lover.features.modules.combat;

import dev.blackhig.zhebushigudu.lover.util.Util;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPistonExtension;
import dev.blackhig.zhebushigudu.lover.util.SeijaUtil.CombatUtil;
import java.util.List;
import java.util.Comparator;
import java.util.Map;
import java.util.HashMap;
import net.minecraft.entity.item.EntityEnderCrystal;
import java.util.ArrayList;

import dev.blackhig.zhebushigudu.lover.util.SeijaUtil.SeijaDistanceUtil;
import dev.blackhig.zhebushigudu.lover.util.SeijaUtil.TargetUtil;
import dev.blackhig.zhebushigudu.lover.util.EntityUtil;
import dev.blackhig.zhebushigudu.lover.lover;
import dev.blackhig.zhebushigudu.lover.util.HoleFillPlus.HoleUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import dev.blackhig.zhebushigudu.lover.util.SeijaUtil.SeijaInvUtil;
import net.minecraft.init.Items;
import dev.blackhig.zhebushigudu.lover.features.modules.misc.InstantMine;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.Vec3d;
import dev.blackhig.zhebushigudu.lover.util.SeijaUtil.SeijaBlockUtil;
import dev.blackhig.zhebushigudu.lover.util.RotationUtil;
import dev.blackhig.zhebushigudu.lover.util.SeijaUtil.BlockUtil;
import net.minecraft.util.EnumHand;
import dev.blackhig.zhebushigudu.lover.features.command.Command;
import net.minecraft.block.Block;
import dev.blackhig.zhebushigudu.lover.util.InventoryUtil;
import net.minecraft.item.Item;
import net.minecraft.init.Blocks;
import dev.blackhig.zhebushigudu.lover.util.render.RenderUtil;
import java.awt.Color;
import net.minecraft.util.math.AxisAlignedBB;
import dev.blackhig.zhebushigudu.lover.event.events.Render3DEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import dev.blackhig.zhebushigudu.lover.util.Timer;
import dev.blackhig.zhebushigudu.lover.features.setting.Bind;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.features.modules.Module;

public class AutoHoleKick extends Module
{
    private final Setting<page> pageSetting;
    private final Setting<Integer> renderTime;
    private final Setting<Boolean> render;
    private final Setting<Integer> colorR;
    private final Setting<Integer> colorG;
    private final Setting<Integer> colorB;
    private final Setting<Integer> colorA;
    private final Setting<Double> renderSpeed;
    private final Setting<Boolean> renderText;
    private final Setting<Float> targetRange;
    private final Setting<Double> maxTargetSpeed;
    private final Setting<surCheckMode> surCheck;
    private final Setting<Boolean> burCheck;
    private final Setting<Bind> forcePlaceBind;
    private final Setting<Integer> minArmorPieces;
    private final Setting<Boolean> onlyPushOnGround;
    private final Setting<Double> targetMinHP;
    private final Setting<Integer> delay;
    private final Setting<Double> circulateDelay;
    private final Setting<FeetPlaceMode> feetPlace;
    private final Setting<Double> placeRange;
    private final Setting<Double> minRange;
    private final Setting<Boolean> noOutOfDistancePlace;
    private final Setting<Boolean> checkPlaceable;
    private final Setting<Boolean> farPlace;
    private final Setting<Boolean> noPlacePisOnBreakPos;
    private final Setting<Boolean> noPlaceRstOnBreakPos;
    private final Setting<Boolean> attackCry;
    private final Setting<Integer> attackRange;
    private final Setting<Integer> noSuicide;
    private final Setting<Boolean> disableOnNoBlock;
    private final Setting<Boolean> onGroundCheck;
    private final Setting<Integer> count;
    private final Setting<Boolean> speedCheck;
    private final Setting<Double> maxSpeed;
    private final Setting<Boolean> noPushSelf;
    private final Setting<Boolean> onUpdateMode;
    private final Setting<Double> cryRange;
    private final Setting<Integer> maxCount;
    private final Setting<Integer> balance;
    private final Setting<Integer> cryWeight;
    private final Setting<Boolean> raytrace;
    private final Setting<Boolean> strictRotate;
    private final Setting<Boolean> newRotate;
    private final Setting<Boolean> packetMine;
    private final Setting<Integer> packSwichCount;
    private final Setting<Double> mineDelay;
    private final Setting<Boolean> alwaysMine;
    private final Setting<Integer> advanceMine;
    private final Setting<Boolean> deBugMode;
    Timer mineTimer;
    Timer timer;
    PushInfo info;
    BlockPos piston;
    BlockPos rst;
    boolean pull;
    EnumFacing facing;
    int stage;
    int ct;
    int ct1;
    EntityPlayer target;
    private BlockPos renderPos;
    Timer dynamicRenderingTimer;
    double renderCount;
    Timer renderTimer;
    Timer circulateTimer;
    boolean canRender;
    int mineCount;
    static int rotate;
    public static boolean hasCry;
    public static boolean oldCry;
    public static int var;
    
    public AutoHoleKick() {
        super("AutoHoleKick", "by KijinSeija", Category.COMBAT, true, false, false);
        this.pageSetting = this.register(new Setting<>("page", page.render));
        this.renderTime = this.register(new Setting("RenderTime", 200, 0, 1000, v -> this.pageSetting.getValue() == page.render));
        this.render = this.register(new Setting("render", true, v -> this.pageSetting.getValue() == page.render));
        this.colorR = this.register(new Setting("R", 232, 0, 255, v -> this.render.getValue() && this.pageSetting.getValue() == page.render));
        this.colorG = this.register(new Setting("G", 226, 0, 255, v -> this.render.getValue() && this.pageSetting.getValue() == page.render));
        this.colorB = this.register(new Setting("B", 2, 0, 255, v -> this.render.getValue() && this.pageSetting.getValue() == page.render));
        this.colorA = this.register(new Setting("Alpha", 100, 0, 255, v -> this.render.getValue() && this.pageSetting.getValue() == page.render));
        this.renderSpeed = this.register(new Setting("RenderSpeed", 5.0, 0.0, 10.0, v -> this.pageSetting.getValue() == page.render));
        this.renderText = this.register(new Setting("RenderText", true, v -> this.pageSetting.getValue() == page.render));
        this.targetRange = this.register(new Setting("TargetRange", 4.0f, 1.0f, 6.0f, v -> this.pageSetting.getValue() == page.target));
        this.maxTargetSpeed = this.register(new Setting("MaxTargetSpeed", 4.0, 0.0, 15.0, v -> this.pageSetting.getValue() == page.target));
        this.surCheck = this.register(new Setting("OnlyPushSurrounded", surCheckMode.normal, v -> this.pageSetting.getValue() == page.target));
        this.burCheck = this.register(new Setting("BurCheck", true, v -> this.pageSetting.getValue() == page.target));
        this.forcePlaceBind = this.register(new Setting("ForcePlace", new Bind(0), v -> this.pageSetting.getValue() == page.target && this.surCheck.getValue() != surCheckMode.off));
        this.minArmorPieces = this.register(new Setting("MinArmorPieces", 3, 0, 4, v -> this.pageSetting.getValue() == page.target));
        this.onlyPushOnGround = this.register(new Setting("onlyPushOnGround", true, v -> this.pageSetting.getValue() == page.target));
        this.targetMinHP = this.register(new Setting("targetMinHP", 11.0, 0.0, 36.0, v -> this.pageSetting.getValue() == page.target));
        this.delay = this.register(new Setting("Delay", 200, 0, 1000, v -> this.pageSetting.getValue() == page.place));
        this.circulateDelay = this.register(new Setting("circulateDelay", 0.0, 0.0, 200.0, v -> this.pageSetting.getValue() == page.place));
        this.feetPlace = this.register(new Setting("FeetPlace", FeetPlaceMode.Obsidian, v -> this.pageSetting.getValue() == page.place));
        this.placeRange = this.register(new Setting("PlaceRange", 5.0, 0.0, 6.0, v -> this.pageSetting.getValue() == page.place));
        this.minRange = this.register(new Setting("AntiStickRange", 2.6, 0.0, 4.0, v -> this.pageSetting.getValue() == page.place));
        this.noOutOfDistancePlace = this.register(new Setting("NoOutOfDistancePlace", true, v -> this.pageSetting.getValue() == page.place));
        this.checkPlaceable = this.register(new Setting("CheckPlaceable", false, v -> this.pageSetting.getValue() == page.place));
        this.farPlace = this.register(new Setting("FarPlace", false, v -> this.pageSetting.getValue() == page.place));
        this.noPlacePisOnBreakPos = this.register(new Setting("NoPlacePisOnBreakPos", true, v -> this.pageSetting.getValue() == page.place));
        this.noPlaceRstOnBreakPos = this.register(new Setting("NoPlaceRstOnBreakPos", true, v -> this.pageSetting.getValue() == page.place));
        this.attackCry = this.register(new Setting("AttackCrystal", true, v -> this.pageSetting.getValue() == page.breakCry));
        this.attackRange = this.register(new Setting("AttackCryRange", 5, 0, 7, v -> this.attackCry.getValue() && this.pageSetting.getValue() == page.breakCry));
        this.noSuicide = this.register(new Setting("NoSuicideHealth", 5, 0, 20, v -> this.attackCry.getValue() && this.pageSetting.getValue() == page.breakCry));
        this.disableOnNoBlock = this.register(new Setting("DisableOnNoBlock", true, v -> this.pageSetting.getValue() == page.selfCheck));
        this.onGroundCheck = this.register(new Setting("OnGroundCheck", true, v -> this.pageSetting.getValue() == page.selfCheck));
        this.count = this.register(new Setting("AntiStickCount", 20, 0, 200, v -> this.pageSetting.getValue() == page.selfCheck));
        this.speedCheck = this.register(new Setting("SpeedCheck", true, v -> this.pageSetting.getValue() == page.selfCheck));
        this.maxSpeed = this.register(new Setting("SelfMaxSpeed", 4.0, 0.0, 20.0, v -> this.speedCheck.getValue() && this.pageSetting.getValue() == page.selfCheck));
        this.noPushSelf = this.register(new Setting("NoPushSelf", true, v -> this.pageSetting.getValue() == page.selfCheck));
        this.onUpdateMode = this.register(new Setting("OnUpdateMode", true, v -> this.pageSetting.getValue() == page.crySpeedCheck));
        this.cryRange = this.register(new Setting("CryRange", 5.0, 0.0, 8.0, v -> this.pageSetting.getValue() == page.crySpeedCheck));
        this.maxCount = this.register(new Setting("MaxCount", 30, 2, 50, v -> this.pageSetting.getValue() == page.crySpeedCheck && this.cryRange.getValue() > this.cryRange.getMin()));
        this.balance = this.register(new Setting("Balance", 17, 2, this.maxCount.getMax(), v -> this.pageSetting.getValue() == page.crySpeedCheck && this.cryRange.getValue() > this.cryRange.getMin()));
        this.cryWeight = this.register(new Setting("CryWeight", 5, 1, 10, v -> this.pageSetting.getValue() == page.crySpeedCheck && this.cryRange.getValue() > this.cryRange.getMin()));
        this.raytrace = this.register(new Setting("rayTrace", false, v -> this.pageSetting.getValue() == page.bypass));
        this.strictRotate = this.register(new Setting("UselessMode", false, v -> this.pageSetting.getValue() == page.bypass));
        this.newRotate = this.register(new Setting("newRotate", false, v -> this.pageSetting.getValue() == page.bypass && !this.strictRotate.getValue()));
        this.packetMine = this.register(new Setting("PacketMine", true, v -> this.pageSetting.getValue() == page.mine));
        this.packSwichCount = this.register(new Setting("packSwichCount", 5, 0, 20, v -> this.pageSetting.getValue() == page.mine && this.packetMine.getValue()));
        this.mineDelay = this.register(new Setting("mineDelay", 20.0, 0.0, 400.0, v -> this.pageSetting.getValue() == page.mine));
        this.alwaysMine = this.register(new Setting("AlwaysMine", true, v -> this.pageSetting.getValue() == page.mine));
        this.advanceMine = this.register(new Setting("AdvanceMineOnStage", 2, 0, 3, v -> this.pageSetting.getValue() == page.mine && this.alwaysMine.getValue()));
        this.deBugMode = this.register(new Setting<>("Debug", false));
        this.mineTimer = new Timer();
        this.timer = new Timer();
        this.stage = 0;
        this.ct = 0;
        this.ct1 = 0;
        this.dynamicRenderingTimer = new Timer();
        this.renderTimer = new Timer();
        this.circulateTimer = new Timer();
        this.canRender = false;
        this.mineCount = 0;
    }
    
    @Override
    public void onEnable() {
        this.piston = null;
        this.rst = null;
        this.target = null;
        this.renderPos = null;
        this.stage = 0;
        this.ct = 0;
        this.ct1 = 0;
        this.circulateTimer.setMs(10000L);
    }
    
    @Override
    public void onRender3D(final Render3DEvent event) {
        if (this.piston != null) {
            this.renderPos = this.piston;
        }
        if (this.renderPos == null) {
            return;
        }
        if (this.canRender) {
            if (this.renderCount / 100.0 < 0.5 && this.dynamicRenderingTimer.passedDms(0.5)) {
                this.renderCount += this.renderSpeed.getValue();
                this.dynamicRenderingTimer.reset();
            }
            final AxisAlignedBB axisAlignedBB = new AxisAlignedBB(this.renderPos.getX() + 0.5 - this.renderCount / 100.0, this.renderPos.getY() + 0.5 - this.renderCount / 100.0, this.renderPos.getZ() + 0.5 - this.renderCount / 100.0, this.renderPos.getX() + 0.5 + this.renderCount / 100.0, this.renderPos.getY() + 0.5 + this.renderCount / 100.0, this.renderPos.getZ() + 0.5 + this.renderCount / 100.0);
            if (this.render.getValue()) {
                RenderUtil.drawBBFill(axisAlignedBB, new Color(this.colorR.getValue(), this.colorG.getValue(), this.colorB.getValue()), this.colorA.getValue());
            }
            if (this.renderText.getValue()) {
                RenderUtil.drawText(this.renderPos, "PUSH!");
            }
        }
        else {
            this.renderCount = 0.0;
        }
    }
    
    @Override
    public void onTick() {
        if (!this.onUpdateMode.getValue()) {
            caCheck(this.cryRange.getValue(), 0, this.maxCount.getValue(), this.balance.getValue(), this.cryWeight.getValue(), false);
        }
    }
    
    @Override
    public void onUpdate() {
        if (this.onUpdateMode.getValue()) {
            caCheck(this.cryRange.getValue(), 0, this.maxCount.getValue(), this.balance.getValue(), this.cryWeight.getValue(), false);
        }
        if (!this.circulateTimer.passedDms(this.circulateDelay.getValue())) {
            return;
        }
        if (this.renderTimer.passedDms(this.renderTime.getValue())) {
            this.canRender = false;
        }
        final int oldSlot = AutoHoleKick.mc.player.inventory.currentItem;
        final int obbySlot = InventoryUtil.getItemHotbar(Item.getItemFromBlock(Blocks.OBSIDIAN));
        int pisSlot = InventoryUtil.getItemHotbar(Item.getItemFromBlock(Blocks.PISTON));
        if (pisSlot == -1) {
            pisSlot = InventoryUtil.getItemHotbar(Item.getItemFromBlock(Blocks.STICKY_PISTON));
            if (pisSlot == -1) {
                if (this.disableOnNoBlock.getValue()) {
                    Command.sendMessage("NoPiston");
                    this.disable();
                }
                return;
            }
        }
        final int rstSlot = InventoryUtil.getItemHotbar(Item.getItemFromBlock(Blocks.REDSTONE_BLOCK));
        if (rstSlot == -1) {
            if (this.disableOnNoBlock.getValue()) {
                Command.sendMessage("NoRedStoneBlock");
                this.disable();
            }
            return;
        }
        if (this.stage == 0) {
            if (!this.timer.passedDms(this.delay.getValue())) {
                return;
            }
            final EntityPlayer target = this.getTarget(this.targetRange.getValue());
            this.target = target;
            if (this.getPistonPos(target, this.raytrace.getValue()) == null) {
                return;
            }
            this.info = this.getPistonPos(target, this.raytrace.getValue());
            if (this.info.nullCheck()) {
                return;
            }
            this.pull = this.info.pullMode;
            this.piston = this.info.pistonPos;
            this.facing = this.info.pisFac;
            this.rst = this.info.rstPos;
            this.ct = 0;
            this.ct1 = 0;
            ++this.stage;
        }
        if (this.stage == 1) {
            if (!this.timer.passedDms(this.delay.getValue())) {
                return;
            }
            this.timer.reset();
            if (this.attackCry.getValue()) {
                this.attackCrystal();
            }
            if (this.feetPlace.getValue().equals(FeetPlaceMode.RedStone) && AutoHoleKick.mc.world.getBlockState(this.piston.add(0, -1, 0)).getBlock().equals(Blocks.AIR) && isNoBBoxBlocked(this.piston.add(0, -1, 0))) {
                if (this.noOutOfDistancePlace.getValue() && Math.sqrt(AutoHoleKick.mc.player.getDistanceSq(this.piston.add(0, -1, 0))) > this.placeRange.getValue()) {
                    this.stage = 0;
                    return;
                }
                InventoryUtil.switchToHotbarSlot(rstSlot, false);
                BlockUtil.placeBlock(this.piston.add(0, -1, 0), EnumHand.MAIN_HAND, false, false, false);
                InventoryUtil.switchToHotbarSlot(oldSlot, false);
            }
            if (this.feetPlace.getValue().equals(FeetPlaceMode.Obsidian) && AutoHoleKick.mc.world.getBlockState(this.piston.add(0, -1, 0)).getBlock().equals(Blocks.AIR) && isNoBBoxBlocked(this.piston.add(0, -1, 0))) {
                if (obbySlot == -1) {
                    this.stage = 2;
                    return;
                }
                if (this.noOutOfDistancePlace.getValue() && Math.sqrt(AutoHoleKick.mc.player.getDistanceSq(this.piston.add(0, -1, 0))) > this.placeRange.getValue()) {
                    this.stage = 0;
                    return;
                }
                InventoryUtil.switchToHotbarSlot(obbySlot, false);
                BlockUtil.placeBlock(this.piston.add(0, -1, 0), EnumHand.MAIN_HAND, false, false);
                InventoryUtil.switchToHotbarSlot(oldSlot, false);
            }
            if (this.advanceMine.getValue() == 1 && this.alwaysMine.getValue()) {
                this.minePos(this.rst);
            }
            ++this.stage;
        }
        if (this.stage == 2) {
            if (!this.timer.passedDms(this.delay.getValue())) {
                return;
            }
            this.timer.reset();
            this.canRender = true;
            this.renderTimer.reset();
            if (AutoHoleKick.mc.world.getBlockState(this.piston).getBlock().equals(Blocks.PISTON) || AutoHoleKick.mc.world.getBlockState(this.piston).getBlock().equals(Blocks.STICKY_PISTON)) {
                ++this.stage;
                return;
            }
            if (!AutoHoleKick.mc.world.getBlockState(this.piston).getBlock().equals(Blocks.AIR) && !AutoHoleKick.mc.world.getBlockState(this.piston).getBlock().equals(Blocks.PISTON) && !AutoHoleKick.mc.world.getBlockState(this.piston).getBlock().equals(Blocks.STICKY_PISTON)) {
                this.stage = 0;
                return;
            }
            if (this.noOutOfDistancePlace.getValue() && Math.sqrt(AutoHoleKick.mc.player.getDistanceSq(this.piston)) > this.placeRange.getValue()) {
                this.stage = 0;
                return;
            }
            if (!this.strictRotate.getValue() && !this.newRotate.getValue()) {
                if (this.info.pisFac == EnumFacing.EAST) {
                    if (!this.newRotate.getValue()) {
                        RotationUtil.faceYawAndPitch(90.0f, 0.0f);
                    }
                    else {
                        AutoHoleKick.mc.player.rotationYawHead = 90.0f;
                        AutoHoleKick.mc.player.renderYawOffset = 90.0f;
                    }
                }
                else if (this.info.pisFac == EnumFacing.WEST) {
                    if (!this.newRotate.getValue()) {
                        RotationUtil.faceYawAndPitch(-90.0f, 0.0f);
                    }
                    else {
                        AutoHoleKick.mc.player.rotationYawHead = -90.0f;
                        AutoHoleKick.mc.player.renderYawOffset = -90.0f;
                    }
                }
                else if (this.info.pisFac == EnumFacing.NORTH) {
                    if (!this.newRotate.getValue()) {
                        RotationUtil.faceYawAndPitch(0.0f, 0.0f);
                    }
                    else {
                        AutoHoleKick.mc.player.rotationYawHead = 0.0f;
                        AutoHoleKick.mc.player.renderYawOffset = 0.0f;
                    }
                }
                else if (this.info.pisFac == EnumFacing.SOUTH) {
                    if (!this.newRotate.getValue()) {
                        RotationUtil.faceYawAndPitch(180.0f, 0.0f);
                    }
                    else {
                        AutoHoleKick.mc.player.rotationYawHead = 180.0f;
                        AutoHoleKick.mc.player.renderYawOffset = 180.0f;
                    }
                }
            }
            InventoryUtil.switchToHotbarSlot(pisSlot, false);
            SeijaBlockUtil.placeBlock(this.piston, EnumHand.MAIN_HAND, false, false, EnumFacing.DOWN);
            InventoryUtil.switchToHotbarSlot(oldSlot, false);
            if ((AutoHoleKick.mc.world.getBlockState(this.piston).getBlock().equals(Blocks.PISTON) || AutoHoleKick.mc.world.getBlockState(this.piston).getBlock().equals(Blocks.STICKY_PISTON) || !this.checkPlaceable.getValue()) && isNoBBoxBlocked(this.piston)) {
                ++this.stage;
                if (this.advanceMine.getValue() == 2 && this.alwaysMine.getValue()) {
                    this.minePos(this.rst);
                }
            }
            else {
                ++this.ct1;
                if (this.ct1 > this.count.getValue()) {
                    this.stage = 0;
                }
            }
        }
        if (this.stage == 3) {
            if (SeijaBlockUtil.haveNeighborBlock(this.piston, Blocks.REDSTONE_BLOCK).size() > 0) {
                this.mineTimer.reset();
                this.stage = 4;
                return;
            }
            if (isNoBBoxBlocked(this.rst) && (!this.checkPlaceable.getValue() || AutoHoleKick.mc.world.getBlockState(this.piston).getBlock().equals(Blocks.PISTON) || AutoHoleKick.mc.world.getBlockState(this.piston).getBlock().equals(Blocks.STICKY_PISTON))) {
                if (!AutoHoleKick.mc.world.getBlockState(this.rst).getBlock().equals(Blocks.AIR) && !AutoHoleKick.mc.world.getBlockState(this.rst).getBlock().equals(Blocks.REDSTONE_BLOCK)) {
                    this.stage = 0;
                    return;
                }
                InventoryUtil.switchToHotbarSlot(rstSlot, false);
                final Vec3d hitVec = new Vec3d((Vec3i)this.piston).add(0.5, 0.5, 0.5).add(new Vec3d(getRstFac(this.piston, this.rst).getOpposite().getDirectionVec()).scale(0.5));
                SeijaBlockUtil.sneak(this.piston);
                BlockUtil.rightClickBlock(this.piston, hitVec, EnumHand.MAIN_HAND, getRstFac(this.piston, this.rst).getOpposite(), true);
                InventoryUtil.switchToHotbarSlot(oldSlot, false);
                if (AutoHoleKick.mc.world.getBlockState(this.rst).getBlock().equals(Blocks.REDSTONE_BLOCK)) {
                    this.mineTimer.reset();
                    this.stage = 4;
                    if (this.advanceMine.getValue() == 3 && this.alwaysMine.getValue()) {
                        this.minePos(this.rst);
                    }
                }
                ++this.ct;
                if (this.ct > this.count.getValue()) {
                    this.stage = 0;
                }
            }
            else {
                this.stage = 0;
            }
        }
        if (this.stage == 4) {
            if (this.alwaysMine.getValue()) {
                this.minePos(this.rst);
            }
            if (!this.pull) {
                this.stage = 0;
                return;
            }
            if (!this.mineTimer.passedDms(this.mineDelay.getValue())) {
                return;
            }
            this.mineRst(this.target, this.piston);
            this.stage = 0;
        }
        this.circulateTimer.reset();
    }
    
    public void mineRst(final EntityPlayer target, final BlockPos piston) {
        if (AutoHoleKick.mc.world.getBlockState(SeijaBlockUtil.getFlooredPosition(target).add(0, 2, 0)).getBlock().equals(Blocks.AIR) && !AutoHoleKick.mc.world.getBlockState(piston.add(0, -1, 0)).getBlock().equals(Blocks.AIR) && SeijaBlockUtil.haveNeighborBlock(piston, Blocks.REDSTONE_BLOCK).size() == 1) {
            final BlockPos minePos = SeijaBlockUtil.haveNeighborBlock(piston, Blocks.REDSTONE_BLOCK).get(0);
            if (minePos != null && (InstantMine.breakPos == null || !InstantMine.breakPos.equals(minePos))) {
                if (this.packetMine.getValue()) {
                    if (AutoHoleKick.mc.world.getBlockState(minePos).getBlock().equals(Blocks.REDSTONE_BLOCK)) {
                        ++this.mineCount;
                    }
                    else {
                        this.mineCount = 0;
                    }
                    if (this.mineCount >= this.packSwichCount.getValue()) {
                        final int oldslot = AutoHoleKick.mc.player.inventory.currentItem;
                        SeijaInvUtil.switchToItem(Items.DIAMOND_PICKAXE);
                        pMine(minePos);
                        InventoryUtil.switchToHotbarSlot(oldslot, false);
                        return;
                    }
                    pMine(minePos);
                }
                else {
                    AutoHoleKick.mc.playerController.onPlayerDamageBlock(minePos, BlockUtil.getRayTraceFacing(minePos));
                }
            }
        }
    }
    
    public void minePos(final BlockPos pos) {
        AutoHoleKick.mc.playerController.onPlayerDamageBlock(pos, BlockUtil.getRayTraceFacing(pos));
    }
    
    public static void pMine(final BlockPos minePos) {
        InstantMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, minePos, BlockUtil.getRayTraceFacing(minePos)));
        InstantMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, minePos, BlockUtil.getRayTraceFacing(minePos)));
    }
    
    public boolean isSur(final Entity player, final surCheckMode checkMode) {
        final BlockPos playerPos = SeijaBlockUtil.getFlooredPosition(player);
        if (checkMode == surCheckMode.test) {
            return ((!AutoHoleKick.mc.world.getBlockState(playerPos.add(1, 0, 0)).getBlock().equals(Blocks.AIR) || !AutoHoleKick.mc.world.getBlockState(playerPos.add(1, 1, 0)).getBlock().equals(Blocks.AIR)) && (!AutoHoleKick.mc.world.getBlockState(playerPos.add(-1, 0, 0)).getBlock().equals(Blocks.AIR) || !AutoHoleKick.mc.world.getBlockState(playerPos.add(-1, 1, 0)).getBlock().equals(Blocks.AIR)) && (!AutoHoleKick.mc.world.getBlockState(playerPos.add(0, 0, 1)).getBlock().equals(Blocks.AIR) || !AutoHoleKick.mc.world.getBlockState(playerPos.add(0, 1, 1)).getBlock().equals(Blocks.AIR)) && (!AutoHoleKick.mc.world.getBlockState(playerPos.add(0, 0, -1)).getBlock().equals(Blocks.AIR) || !AutoHoleKick.mc.world.getBlockState(playerPos.add(0, 0, -1)).getBlock().equals(Blocks.AIR))) || HoleUtil.isHole(playerPos, false, false).getType() != HoleUtil.HoleType.NONE;
        }
        if (checkMode == surCheckMode.normal && !AutoHoleKick.mc.world.getBlockState(playerPos.add(1, 0, 0)).getBlock().equals(Blocks.AIR) && !AutoHoleKick.mc.world.getBlockState(playerPos.add(-1, 0, 0)).getBlock().equals(Blocks.AIR) && !AutoHoleKick.mc.world.getBlockState(playerPos.add(0, 0, 1)).getBlock().equals(Blocks.AIR) && !AutoHoleKick.mc.world.getBlockState(playerPos.add(0, 0, -1)).getBlock().equals(Blocks.AIR)) {
            return true;
        }
        if (checkMode == surCheckMode.center) {
            final double x = Math.abs(player.posX) - Math.floor(Math.abs(player.posX));
            final double z = Math.abs(player.posZ) - Math.floor(Math.abs(player.posZ));
            if (x <= 0.7 && x >= 0.3 && z <= 0.7 && z >= 0.3) {
                return true;
            }
        }
        if (checkMode == surCheckMode.smart) {
            return (!AutoHoleKick.mc.world.getBlockState(playerPos.add(1, 0, 0)).getBlock().equals(Blocks.AIR) || !AutoHoleKick.mc.world.getBlockState(playerPos.add(1, 1, 0)).getBlock().equals(Blocks.AIR)) && (!AutoHoleKick.mc.world.getBlockState(playerPos.add(-1, 0, 0)).getBlock().equals(Blocks.AIR) || !AutoHoleKick.mc.world.getBlockState(playerPos.add(-1, 1, 0)).getBlock().equals(Blocks.AIR)) && (!AutoHoleKick.mc.world.getBlockState(playerPos.add(0, 0, 1)).getBlock().equals(Blocks.AIR) || !AutoHoleKick.mc.world.getBlockState(playerPos.add(0, 1, 1)).getBlock().equals(Blocks.AIR)) && (!AutoHoleKick.mc.world.getBlockState(playerPos.add(0, 0, -1)).getBlock().equals(Blocks.AIR) || !AutoHoleKick.mc.world.getBlockState(playerPos.add(0, 0, -1)).getBlock().equals(Blocks.AIR));
        }
        return checkMode == surCheckMode.off;
    }
    
    public boolean helpingBlockCheck(final BlockPos pos) {
        return !AutoHoleKick.mc.world.getBlockState(pos.add(1, 0, 0)).getBlock().equals(Blocks.AIR) || !AutoHoleKick.mc.world.getBlockState(pos.add(-1, 0, 0)).getBlock().equals(Blocks.AIR) || !AutoHoleKick.mc.world.getBlockState(pos.add(0, 1, 0)).getBlock().equals(Blocks.AIR) || !AutoHoleKick.mc.world.getBlockState(pos.add(0, -1, 0)).getBlock().equals(Blocks.AIR) || !AutoHoleKick.mc.world.getBlockState(pos.add(0, 0, -1)).getBlock().equals(Blocks.AIR) || !AutoHoleKick.mc.world.getBlockState(pos.add(0, 0, 1)).getBlock().equals(Blocks.AIR);
    }
    
    private EntityPlayer getTarget(final double range) {
        if (!caCheck(this.cryRange.getValue(), 0, this.maxCount.getValue(), this.balance.getValue(), this.cryWeight.getValue(), true)) {
            if (this.deBugMode.getValue()) {
                Command.sendMessage("crystalIsFast" + AutoHoleKick.var);
            }
            return null;
        }
        if (this.onGroundCheck.getValue() && !AutoHoleKick.mc.player.onGround) {
            if (this.deBugMode.getValue()) {
                Command.sendMessage("You notOnGround");
            }
            return null;
        }
        EntityPlayer target = null;
        double distance = range;
        if (this.speedCheck.getValue() && lover.speedManager.getPlayerSpeed(AutoHoleKick.mc.player) > this.maxSpeed.getValue()) {
            if (this.deBugMode.getValue()) {
                Command.sendMessage("YouFast" + lover.speedManager.getPlayerSpeed(AutoHoleKick.mc.player));
            }
            return null;
        }
        for (final EntityPlayer player : AutoHoleKick.mc.world.playerEntities) {
            if (this.getPistonPos(player, this.raytrace.getValue()) == null) {
                continue;
            }
            final BlockPos pistonPos = this.getPistonPos(player, this.raytrace.getValue()).pistonPos;
            final BlockPos rstPos = this.getPistonPos(player, this.raytrace.getValue()).rstPos;
            if (EntityUtil.isntValid(player, range) || lover.friendManager.isFriend(player.getName()) || lover.speedManager.getPlayerSpeed(player) > this.maxTargetSpeed.getValue() || pistonPos == null) {
                continue;
            }
            if (rstPos == null) {
                continue;
            }
            if (TargetUtil.getArmorPieces(player) < this.minArmorPieces.getValue()) {
                if (!this.deBugMode.getValue()) {
                    continue;
                }
                Command.sendMessage("LowArmor" + player.getName() + TargetUtil.getArmorPieces(player));
            }
            else if (this.onlyPushOnGround.getValue() && !player.onGround) {
                if (!this.deBugMode.getValue()) {
                    continue;
                }
                Command.sendMessage("noGround" + player.getName());
            }
            else if (this.targetMinHP.getValue() > player.getHealth()) {
                if (!this.deBugMode.getValue()) {
                    continue;
                }
                Command.sendMessage("LowHp" + player.getName() + player.getHealth());
            }
            else {
                if (this.surCheck.getValue() != surCheckMode.off && !this.forcePlaceBind.getValue().isDown()) {
                    Boolean p = true;
                    if (!this.isSur(player, this.surCheck.getValue())) {
                        if (this.burCheck.getValue()) {
                            if (AutoHoleKick.mc.world.getBlockState(SeijaBlockUtil.getFlooredPosition(player)).getBlock().equals(Blocks.AIR)) {
                                if (this.deBugMode.getValue()) {
                                    Command.sendMessage("noBurAndSur" + player.getName());
                                    continue;
                                }
                                continue;
                            }
                            else {
                                p = false;
                            }
                        }
                        if (p) {
                            if (this.deBugMode.getValue()) {
                                Command.sendMessage("NOSur" + player.getName());
                                continue;
                            }
                            continue;
                        }
                    }
                }
                if (this.noPushSelf.getValue() && SeijaBlockUtil.getFlooredPosition(player).equals(SeijaBlockUtil.getFlooredPosition(AutoHoleKick.mc.player))) {
                    if (!this.deBugMode.getValue()) {
                        continue;
                    }
                    Command.sendMessage("CanPushSelf" + player.getName());
                }
                else if ((AutoHoleKick.mc.player.posY - player.posY <= -1.0 || AutoHoleKick.mc.player.posY - player.posY >= 2.0) && SeijaDistanceUtil.distanceToXZ(pistonPos.getX() + 0.5, pistonPos.getZ() + 0.5) < this.minRange.getValue()) {
                    if (!this.deBugMode.getValue()) {
                        continue;
                    }
                    Command.sendMessage("Can't Place true Facing" + player.getName());
                }
                else if (target == null) {
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
        return target;
    }
    
    public void attackCrystal() {
        if (AutoHoleKick.mc.player.getHealth() < this.noSuicide.getValue()) {
            return;
        }
        final ArrayList<Entity> crystalList = new ArrayList<Entity>();
        for (final Entity entity : AutoHoleKick.mc.world.loadedEntityList) {
            if (entity instanceof EntityEnderCrystal) {
                crystalList.add(entity);
            }
        }
        if (crystalList.size() == 0) {
            return;
        }
        final HashMap<Entity, Double> distantMap = new HashMap<Entity, Double>();
        for (final Entity crystal : crystalList) {
            if (AutoHoleKick.mc.player.getDistance(crystal.posX, crystal.posY, crystal.posZ) < this.attackRange.getValue()) {
                distantMap.put(crystal, AutoHoleKick.mc.player.getDistance(crystal.posX, crystal.posY, crystal.posZ));
            }
        }
        final List<Map.Entry<Entity, Double>> list = new ArrayList<Map.Entry<Entity, Double>>(distantMap.entrySet());
        list.sort(Map.Entry.comparingByValue());
        if (list.size() == 0) {
            return;
        }
        if (list.get(0).getValue() < 5.0) {
            EntityUtil.attackEntity(list.get(list.size() - 1).getKey(), true, true);
        }
    }
    
    public BlockPos getRSTPos2(final BlockPos pistonPos, final double range, final boolean rayTrace, final boolean instaMineCheck, final boolean helpBlockCheck) {
        if (pistonPos == null) {
            return null;
        }
        if (SeijaBlockUtil.haveNeighborBlock(pistonPos, Blocks.REDSTONE_BLOCK).size() > 0 && isNoBBoxBlocked(pistonPos)) {
            return SeijaBlockUtil.haveNeighborBlock(pistonPos, Blocks.REDSTONE_BLOCK).get(0);
        }
        final ArrayList<BlockPos> placePosList = new ArrayList<BlockPos>();
        placePosList.add(pistonPos.add(0, 1, 0));
        placePosList.add(pistonPos.add(-1, 0, 0));
        placePosList.add(pistonPos.add(1, 0, 0));
        placePosList.add(pistonPos.add(0, 0, -1));
        placePosList.add(pistonPos.add(0, 0, 1));
        final HashMap<BlockPos, Double> distantMap = new HashMap<BlockPos, Double>();
        for (final BlockPos rSTPos : placePosList) {
            if (AutoHoleKick.mc.world.getBlockState(rSTPos).getBlock().equals(Blocks.AIR) && isNoBBoxBlocked(rSTPos)) {
                if (Math.sqrt(AutoHoleKick.mc.player.getDistanceSq(rSTPos)) > range) {
                    continue;
                }
                if (rayTrace && !CombatUtil.rayTraceRangeCheck(rSTPos, 0.0, 0.0)) {
                    continue;
                }
                if (instaMineCheck && InstantMine.breakPos != null && InstantMine.breakPos.equals((Object)rSTPos)) {
                    continue;
                }
                if (helpBlockCheck && !this.helpingBlockCheck(rSTPos)) {
                    continue;
                }
                distantMap.put(rSTPos, AutoHoleKick.mc.player.getDistanceSq(rSTPos));
            }
        }
        final List<Map.Entry<BlockPos, Double>> list = new ArrayList<Map.Entry<BlockPos, Double>>(distantMap.entrySet());
        list.sort(Map.Entry.comparingByValue());
        if (list.size() == 0) {
            return null;
        }
        return list.get(0).getKey();
    }
    
    public static boolean headCheck(final BlockPos playerPos) {
        return AutoHoleKick.mc.world.getBlockState(playerPos.add(0, 1, 0)).getBlock().equals(Blocks.AIR) && AutoHoleKick.mc.world.getBlockState(playerPos.add(0, 2, 0)).getBlock().equals(Blocks.AIR);
    }
    
    public static boolean caCheck(final double checkRange, final int min, final int max, final int baseValue, final int weight, final boolean onlyGet) {
        if (onlyGet) {
            return AutoHoleKick.var <= baseValue;
        }
        if (min >= max || baseValue >= max || baseValue <= min) {
            return false;
        }
        final ArrayList<Entity> crystalList = new ArrayList<Entity>();
        for (final Entity entity : AutoHoleKick.mc.world.loadedEntityList) {
            if (entity instanceof EntityEnderCrystal && AutoHoleKick.mc.player.getDistance(entity.posX, entity.posY, entity.posZ) < checkRange) {
                crystalList.add(entity);
            }
        }
        if (crystalList.size() == 0) {
            AutoHoleKick.hasCry = false;
        }
        else {
            AutoHoleKick.hasCry = true;
        }
        if (AutoHoleKick.hasCry != AutoHoleKick.oldCry) {
            AutoHoleKick.oldCry = AutoHoleKick.hasCry;
            AutoHoleKick.var += weight;
        }
        else {
            --AutoHoleKick.var;
        }
        if (AutoHoleKick.var >= max) {
            AutoHoleKick.var = max;
        }
        if (AutoHoleKick.var <= min) {
            AutoHoleKick.var = min;
        }
        return AutoHoleKick.var <= baseValue;
    }
    
    public PushInfo getPistonPos(final EntityPlayer player, final boolean raytrace) {
        if (player == null || player.equals(AutoHoleKick.mc.player)) {
            return null;
        }
        final BlockPos playerPos = SeijaBlockUtil.getFlooredPosition((Entity)player);
        if (AutoHoleKick.mc.world.getBlockState(playerPos.add(0, 1, 0)).getBlock() instanceof BlockPistonExtension) {
            if (!AutoHoleKick.mc.world.getBlockState(playerPos.add(0, 2, 0)).getBlock().equals(Blocks.AIR)) {
                return null;
            }
            final EnumFacing headFac = SeijaBlockUtil.getFacing(playerPos.add(0, 1, 0));
            BlockPos pisPos = null;
            switch (headFac) {
                case EAST: {
                    pisPos = playerPos.add(-1, 1, 0);
                    break;
                }
                case WEST: {
                    pisPos = playerPos.add(1, 1, 0);
                    break;
                }
                case NORTH: {
                    pisPos = playerPos.add(0, 1, 1);
                    break;
                }
                case SOUTH: {
                    pisPos = playerPos.add(0, 1, -1);
                    break;
                }
            }
            if (pisPos != null && AutoHoleKick.mc.world.getBlockState(pisPos).getBlock() instanceof BlockPistonBase) {
                final ArrayList<BlockPos> l = SeijaBlockUtil.haveNeighborBlock(pisPos, Blocks.REDSTONE_BLOCK);
                if (l.size() == 1) {
                    final BlockPos rstPos = l.get(0);
                    if (raytrace && !CombatUtil.rayTraceRangeCheck(rstPos, 0.0, 0.0)) {
                        return null;
                    }
                    if (Math.sqrt(AutoHoleKick.mc.player.getDistanceSq(rstPos)) > 6.0) {
                        return null;
                    }
                    return new PushInfo(pisPos, rstPos, headFac, true);
                }
            }
            return null;
        }
        else {
            if (!AutoHoleKick.mc.world.getBlockState(playerPos.add(0, 1, 0)).getBlock().equals(Blocks.AIR)) {
                return null;
            }
            final HashMap<PushInfo, Double> distantMap = new HashMap<PushInfo, Double>();
            for (int i = 0; i < 4; ++i) {
                int xOffSet = 0;
                int zOffSet = 0;
                EnumFacing Pisfac = EnumFacing.UP;
                if (i == 0) {
                    xOffSet = 1;
                    zOffSet = 0;
                    Pisfac = EnumFacing.WEST;
                }
                else if (i == 1) {
                    xOffSet = -1;
                    zOffSet = 0;
                    Pisfac = EnumFacing.EAST;
                }
                else if (i == 2) {
                    xOffSet = 0;
                    zOffSet = 1;
                    Pisfac = EnumFacing.NORTH;
                }
                else if (i == 3) {
                    xOffSet = 0;
                    zOffSet = -1;
                    Pisfac = EnumFacing.SOUTH;
                }
                if (this.fakeBBoxCheck(player, new Vec3d((double)(-xOffSet), 1.0, (double)(-zOffSet)), true) && !AutoHoleKick.mc.world.getBlockState(playerPos.add(-xOffSet, 0, -zOffSet)).getBlock().equals(Blocks.AIR)) {
                    final PushInfo pushInfo = new PushInfo(playerPos.add(xOffSet, 1, zOffSet), raytrace, this.noPlaceRstOnBreakPos.getValue(), Pisfac, false);
                    if (pushInfo.check()) {
                        distantMap.put(pushInfo, Math.sqrt(AutoHoleKick.mc.player.getDistanceSq(pushInfo.pistonPos)));
                    }
                }
                else if (!AutoHoleKick.mc.world.getBlockState(playerPos).getBlock().equals(Blocks.AIR) && !AutoHoleKick.mc.world.getBlockState(playerPos).getBlock().equals(Blocks.WEB) && AutoHoleKick.mc.world.getBlockState(playerPos.add(0, 2, 0)).getBlock().equals(Blocks.AIR) && AutoHoleKick.mc.world.getBlockState(playerPos.add(xOffSet, 2, zOffSet)).getBlock().equals(Blocks.AIR)) {
                    final PushInfo pushInfo = new PushInfo(playerPos.add(xOffSet, 1, zOffSet), raytrace, this.noPlaceRstOnBreakPos.getValue(), Pisfac, false);
                    if (pushInfo.check()) {
                        distantMap.put(pushInfo, Math.sqrt(AutoHoleKick.mc.player.getDistanceSq(pushInfo.pistonPos)));
                    }
                }
            }
            final List<Map.Entry<PushInfo, Double>> list = new ArrayList<Map.Entry<PushInfo, Double>>(distantMap.entrySet());
            list.sort(Map.Entry.comparingByValue());
            int a = 0;
            if (this.farPlace.getValue()) {
                for (a = list.size() - 1; a >= 0; --a) {
                    if ((!this.noPlacePisOnBreakPos.getValue() || InstantMine.breakPos == null || !list.get(a).getKey().pistonPos.equals(InstantMine.breakPos)) && (double)list.get(a).getValue() < this.placeRange.getValue()) {
                        if (!raytrace) {
                            break;
                        }
                        if (CombatUtil.rayTraceRangeCheck(list.get(a).getKey().pistonPos, 0.0, 0.0)) {
                            break;
                        }
                    }
                }
            }
            else {
                for (a = 0; a < list.size(); ++a) {
                    if ((!this.noPlacePisOnBreakPos.getValue() || InstantMine.breakPos == null || !list.get(a).getKey().pistonPos.equals(InstantMine.breakPos)) && (double)list.get(a).getValue() < this.placeRange.getValue()) {
                        if (!raytrace) {
                            break;
                        }
                        if (CombatUtil.rayTraceRangeCheck(list.get(a).getKey().pistonPos, 0.0, 0.0)) {
                            break;
                        }
                    }
                }
            }
            if (a <= -1 || list.size() == 0 || list.size() <= a) {
                return null;
            }
            return list.get(a).getKey();
        }
    }
    
    public static EnumFacing getRstFac(final BlockPos pistonPos, final BlockPos rstPos) {
        for (final EnumFacing facing : EnumFacing.values()) {
            if (rstPos.offset(facing).equals(pistonPos)) {
                return facing;
            }
        }
        return null;
    }
    
    public static boolean isNoBBoxBlocked(final BlockPos pos) {
        final AxisAlignedBB axisAlignedBB = new AxisAlignedBB(pos);
        final List<Entity> l = AutoHoleKick.mc.world.getEntitiesWithinAABBExcludingEntity(null, axisAlignedBB);
        for (final Entity entity : l) {
            if (!(entity instanceof EntityEnderCrystal) && !(entity instanceof EntityItem) && !(entity instanceof EntityArrow) && !(entity instanceof EntityTippedArrow) && !(entity instanceof EntityArrow)) {
                if (entity instanceof EntityXPOrb) {
                    continue;
                }
                return false;
            }
        }
        return true;
    }
    
    public boolean fakeBBoxCheck(final EntityPlayer player, final Vec3d offset, final boolean headcheck) {
        final Vec3d actualPos = player.getPositionVector().add(offset);
        if (headcheck) {
            final Vec3d playerPos = player.getPositionVector();
            return this.isAir(actualPos.add(0.3, 0.0, 0.3)) && this.isAir(actualPos.add(-0.3, 0.0, 0.3)) && this.isAir(actualPos.add(0.3, 0.0, -0.3)) && this.isAir(actualPos.add(-0.3, 0.0, 0.3)) && this.isAir(actualPos.add(0.3, 1.8, 0.3)) && this.isAir(actualPos.add(-0.3, 1.8, 0.3)) && this.isAir(actualPos.add(0.3, 1.8, -0.3)) && this.isAir(actualPos.add(-0.3, 1.8, 0.3)) && this.isAir(playerPos.add(0.3, 2.8, 0.3)) && this.isAir(playerPos.add(-0.3, 2.8, 0.3)) && this.isAir(playerPos.add(-0.3, 2.8, -0.3)) && this.isAir(playerPos.add(0.3, 2.8, -0.3));
        }
        return this.isAir(actualPos.add(0.3, 0.0, 0.3)) && this.isAir(actualPos.add(-0.3, 0.0, 0.3)) && this.isAir(actualPos.add(0.3, 0.0, -0.3)) && this.isAir(actualPos.add(-0.3, 0.0, 0.3)) && this.isAir(actualPos.add(0.3, 1.8, 0.3)) && this.isAir(actualPos.add(-0.3, 1.8, 0.3)) && this.isAir(actualPos.add(0.3, 1.8, -0.3)) && this.isAir(actualPos.add(-0.3, 1.8, 0.3));
    }
    
    public boolean isAir(final Vec3d vec3d) {
        return AutoHoleKick.mc.world.getBlockState(this.vec3toBlockPos(vec3d, true)).getBlock().equals(Blocks.AIR);
    }
    
    public BlockPos vec3toBlockPos(final Vec3d vec3d, final boolean Yfloor) {
        if (Yfloor) {
            return new BlockPos(Math.floor(vec3d.x), Math.floor(vec3d.y), Math.floor(vec3d.z));
        }
        return new BlockPos(Math.floor(vec3d.x), (double)Math.round(vec3d.y), Math.floor(vec3d.z));
    }
    
    static {
        AutoHoleKick.hasCry = false;
        AutoHoleKick.oldCry = false;
        AutoHoleKick.var = 0;
    }
    
    public enum FeetPlaceMode
    {
        Obsidian, 
        RedStone;
    }
    
    private enum page
    {
        render, 
        target, 
        place, 
        breakCry, 
        selfCheck, 
        crySpeedCheck, 
        mine, 
        bypass;
    }
    
    private enum surCheckMode
    {
        off, 
        normal, 
        center, 
        smart, 
        test;
    }
    
    public class PushInfo
    {
        public BlockPos pistonPos;
        public BlockPos rstPos;
        public EnumFacing pisFac;
        public boolean pullMode;
        
        public boolean nullCheck() {
            return this.pistonPos == null || this.rstPos == null || this.pisFac == null;
        }
        
        public PushInfo(final BlockPos pistonPos, final BlockPos rstPos, final EnumFacing pisFac, final boolean pullMode) {
            this.pistonPos = pistonPos;
            this.rstPos = rstPos;
            this.pisFac = pisFac;
            this.pullMode = pullMode;
        }
        
        public PushInfo(final BlockPos pistonPos, final boolean rayTrace, final boolean instaMineC, final EnumFacing pisFac, final boolean pullMode) {
            this.pistonPos = pistonPos;
            this.rstPos = AutoHoleKick.this.getRSTPos2(pistonPos, AutoHoleKick.this.placeRange.getValue(), rayTrace, instaMineC, false);
            this.pisFac = pisFac;
            this.pullMode = pullMode;
        }
        
        public boolean check() {
            return this.rstPos != null && this.pistonPos != null && ((Util.mc.world.getBlockState(this.pistonPos).getBlock().equals(Blocks.AIR) || ((Util.mc.world.getBlockState(this.pistonPos).getBlock().equals(Blocks.PISTON) || Util.mc.world.getBlockState(this.pistonPos).getBlock().equals(Blocks.STICKY_PISTON)) && SeijaBlockUtil.isFacing(this.pistonPos, this.pisFac)) || (Util.mc.world.getBlockState(this.pistonPos).getBlock().equals(Blocks.STICKY_PISTON) && SeijaBlockUtil.isFacing(this.pistonPos, this.pisFac))) && SeijaBlockUtil.isNoBBoxBlocked(this.pistonPos, true) && SeijaBlockUtil.isNoBBoxBlocked(this.rstPos, true) && (Util.mc.world.getBlockState(this.rstPos).getBlock().equals(Blocks.AIR) || Util.mc.world.getBlockState(this.rstPos).getBlock().equals(Blocks.REDSTONE_BLOCK)));
        }
    }
}
