package dev.blackhig.zhebushigudu.lover.features.modules.misc;

import dev.blackhig.zhebushigudu.lover.event.events.BlockEvent;
import dev.blackhig.zhebushigudu.lover.event.events.PacketEvent;
import dev.blackhig.zhebushigudu.lover.event.events.Render3DEvent;
import dev.blackhig.zhebushigudu.lover.features.Feature;
import dev.blackhig.zhebushigudu.lover.features.modules.Module;
import dev.blackhig.zhebushigudu.lover.features.modules.combat.AntiBurrow;
import dev.blackhig.zhebushigudu.lover.features.setting.Bind;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.util.BlockRenderSmooth;
import dev.blackhig.zhebushigudu.lover.util.FadeUtils;
import dev.blackhig.zhebushigudu.lover.util.MathUtil;
import dev.blackhig.zhebushigudu.lover.util.RenderUtils3D;
import dev.blackhig.zhebushigudu.lover.util.Timer;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.minecraft.block.Block;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketPlayerDigging.Action;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class InstantMine extends Module {
    public static BlockRenderSmooth blockRenderSmooth = new BlockRenderSmooth(new BlockPos(0, 0, 0), 500L);
    public static FadeUtils fadeBlockSize = new FadeUtils(2000L);
    public static FadeUtils pos2FadeBlockSize = new FadeUtils(2000L);
    Timer timer = new Timer();
    Setting<Boolean> haste = this.register(new Setting<>("Haste", true));
    Setting<Boolean> ghostHand;
    Setting<Boolean> fuck;
    public static Setting<Boolean> doubleBreak;
    Setting<Boolean> crystal;
    Setting<Boolean> attackcrystal;
    Setting<Bind> bind;
    Setting<Float> range = this.register(new Setting<>("Range", 256.0F, 1.0F, 256.0F));
    Setting<Boolean> instant;
    Setting<Boolean> render;
    Setting<Boolean> boxRender;
    Setting<Integer> boxAlpha;
    Setting<Integer> red;
    Setting<Integer> green;
    Setting<Integer> blue;
    Setting<Integer> alpha;
    Setting<InstantMine.RenderMode> renderMode;
    Setting<Boolean> render2;
    Setting<Boolean> render3;
    Setting<Boolean> boxRender2;
    Setting<Integer> boxAlpha2;
    Setting<Integer> red2;
    Setting<Integer> green2;
    Setting<Integer> blue2;
    Setting<Integer> alpha2;
    Setting<InstantMine.RenderMode> renderMode2;
    public static final List<Block> godBlocks = Arrays.asList(Blocks.AIR, Blocks.FLOWING_LAVA, Blocks.LAVA, Blocks.FLOWING_WATER, Blocks.WATER, Blocks.BEDROCK);
    private static boolean cancelStart = false;
    private static boolean empty = false;
    private static EnumFacing facing;
    public static BlockPos breakPos;
    public static BlockPos breakPos2;
    private static final Timer breakSuccess = new Timer();
    public static int tickCount;
    public static int tickCount2;
    public static double time;
    public static double time2;
    private static InstantMine INSTANCE = new InstantMine();
    private Block block;

    public InstantMine() {
        super("LoverMine", "breaking blocks", Module.Category.MISC, true, false, false);
        this.ghostHand = this.register(new Setting<>("GhostHand", true));
        this.fuck = this.register(new Setting<>("Super Ghost hand", false));
        doubleBreak = this.register(new Setting<>("DoubleBreak", false));
        this.crystal = this.register(new Setting<>("Crystal", false));
        this.attackcrystal = this.register(new Setting("Attack Crystal", false, (v) -> this.crystal.getValue()));
        this.bind = this.register(new Setting<>("ObsidianBind", new Bind(-1), (Predicate<Bind>)((v) -> this.crystal.getValue())));
        this.instant = this.register(new Setting<>("Instant", true));
        this.render = this.register(new Setting<>("Render", true));
        this.render2 = this.register(new Setting("FirstRender", true, (v) -> this.render.getValue()));
        this.boxRender = this.register(new Setting("FirstBox", true, (v) -> this.render.getValue()));
        this.boxAlpha = this.register(new Setting("FirstBoxAlpha", 85, 0, 255, (v) -> this.boxRender.getValue() && this.render.getValue()));
        this.red = this.register(new Setting("FirstRed", 255, 0, 255, (v) -> this.render.getValue()));
        this.green = this.register(new Setting("FirstGreen", 255, 0, 255, (v) -> this.render.getValue()));
        this.blue = this.register(new Setting("FirstBlue", 255, 0, 255, (v) -> this.render.getValue()));
        this.alpha = this.register(new Setting("FirstAlpha", 60, 0, 255, (v) -> this.render.getValue()));
        this.renderMode = this.register(new Setting<>("FirstRenderMode", InstantMine.RenderMode.Outline));
        this.render3 = this.register(new Setting("SecondRender", true, (v) -> this.render.getValue()));
        this.boxRender2 = this.register(new Setting("SecondBox", true, (v) -> this.render.getValue()));
        this.boxAlpha2 = this.register(new Setting("SecondBoxAlpha", 85, 0, 255, (v) -> this.render.getValue()));
        this.red2 = this.register(new Setting("SecondRed", 255, 0, 255, (v) -> this.render.getValue()));
        this.green2 = this.register(new Setting("SecondGreen", 255, 0, 255, (v) -> this.render.getValue()));
        this.blue2 = this.register(new Setting("SecondBlue", 255, 0, 255, (v) -> this.render.getValue()));
        this.alpha2 = this.register(new Setting("SecondAlpha", 60, 0, 255, (v) -> this.render.getValue()));
    }

    public static InstantMine getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new InstantMine();
        }

        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static void attackcrystal() {
        for(Object crystal : (List)mc.world.loadedEntityList.stream().filter((e) -> e instanceof EntityEnderCrystal && !e.isDead).sorted(Comparator.comparing((e) -> mc.player.getDistance(e))).collect(Collectors.toList())) {
            //if (crystal instanceof EntityEnderCrystal && crystal.getDistanceSq(breakPos) <= 2.0D) {
            {
                mc.player.connection.sendPacket(new CPacketUseEntity((Entity) crystal));
                mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.OFF_HAND));
            }
        }

    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (Feature.fullNullCheck()) {
            rend();
        } else if (breakPos != null && mc.player != null && mc.player.getDistanceSq(breakPos) > MathUtil.square((double)((Float)this.range.getValue()).floatValue())) {
            breakPos = null;
            breakPos2 = null;
            cancelStart = false;
        } else if (!this.isEnabled()) {
            rend();
        } else if (event.getPacket() instanceof CPacketPlayerDigging) {
            CPacketPlayerDigging packet = event.getPacket();
            if (packet.getAction() == Action.START_DESTROY_BLOCK) {
                event.setCanceled(cancelStart);
            }
        }
    }

    public int getBestAvailableToolSlot(IBlockState blockState) {
        int toolSlot = -1;
        double max = 0.0D;

        for(int i = 0; i < 9; ++i) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            float speed;
            int eff;
            if (!stack.isEmpty && (speed = stack.getDestroySpeed(blockState)) > 1.0F && (double)(speed = (float)((double)speed + ((eff = EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack)) > 0 ? Math.pow((double)eff, 2.0D) + 1.0D : 0.0D))) > max) {
                max = (double)speed;
                toolSlot = i;
            }
        }

        return toolSlot;
    }

    public static void ondeve(BlockPos pos) {
        if (!Feature.fullNullCheck()) {
            if (dev.blackhig.zhebushigudu.lover.util.BlockUtil.canBreak(pos)) {
                if (breakPos == null || !breakPos.equals(pos)) {
                    rend();
                    blockRenderSmooth.setNewPos(pos);
                    fadeBlockSize.reset();
                    empty = false;
                    cancelStart = false;
                    breakPos = pos;
                    tickCount = 0;
                    breakSuccess.reset();
                    facing = EnumFacing.UP;
                    if (breakPos != null) {
                        mc.player.swingArm(EnumHand.MAIN_HAND);
                        mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.START_DESTROY_BLOCK, breakPos, facing));
                        cancelStart = true;
                        mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.STOP_DESTROY_BLOCK, breakPos, facing));
                    }
                }
            }
        }
    }

    public static void ondeve2(BlockPos pos) {
        if (!Feature.fullNullCheck()) {
            if (dev.blackhig.zhebushigudu.lover.util.BlockUtil.canBreak(pos)) {
                if (breakPos == null || !breakPos.equals(pos)) {
                    rend();
                    blockRenderSmooth.setNewPos(pos);
                    pos2FadeBlockSize.reset();
                    empty = false;
                    cancelStart = false;
                    breakPos2 = pos;
                    tickCount2 = 0;
                    breakSuccess.reset();
                    facing = EnumFacing.UP;
                    if (breakPos != null) {
                        mc.player.swingArm(EnumHand.MAIN_HAND);
                        mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.START_DESTROY_BLOCK, breakPos, facing));
                        cancelStart = true;
                        mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.STOP_DESTROY_BLOCK, breakPos, facing));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onBlockEvent(BlockEvent event) {
        if (Feature.fullNullCheck()) {
            rend();
        } else if (this.isEnabled()) {
            if (dev.blackhig.zhebushigudu.lover.util.BlockUtil.canBreak(event.pos)) {
                if (breakPos == null || !breakPos.equals(event.pos)) {
                    if (mc.world.getBlockState(new BlockPos(blockRenderSmooth.getRenderPos())).getBlock().material != Material.AIR) {
                        pos2FadeBlockSize.reset();
                    }

                    blockRenderSmooth.setNewPos(event.pos);
                    fadeBlockSize.reset();
                    empty = false;
                    cancelStart = false;
                    breakPos2 = breakPos;
                    breakPos = event.pos;
                    tickCount2 = tickCount;
                    tickCount = 0;
                    breakSuccess.reset();
                    facing = event.facing;
                    if (breakPos != null) {
                        mc.player.swingArm(EnumHand.MAIN_HAND);
                        mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.START_DESTROY_BLOCK, breakPos, facing));
                        cancelStart = true;
                        mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.STOP_DESTROY_BLOCK, breakPos, facing));
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    public Block whatBlock(BlockPos pos) {
        return mc.world.getBlockState(pos).getBlock();
    }

    public void onRender3D(Render3DEvent event) {
        if (Feature.fullNullCheck()) {
            rend();
        } else if (cancelStart) {
            if ((breakPos != null || this.instant.getValue() && mc.world.getBlockState(breakPos).getBlock() == Blocks.AIR) && mc.player != null && mc.player.getDistanceSq(breakPos) > MathUtil.square((double)((Float)this.range.getValue()).floatValue())) {
                breakPos = null;
                breakPos2 = null;
                cancelStart = false;
            } else if (dev.blackhig.zhebushigudu.lover.util.InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE) != -1) {
                if (this.fuck.getValue() || dev.blackhig.zhebushigudu.lover.util.InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE) != -1) {
                    if (doubleBreak.getValue() && breakPos2 != null) {
                        int slotMains = mc.player.inventory.currentItem;
                        if (mc.world.getBlockState(breakPos2).getBlock() != Blocks.AIR && dev.blackhig.zhebushigudu.lover.util.InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE) != -1) {
                            if (mc.world.getBlockState(breakPos2).getBlock() == Blocks.OBSIDIAN && !breakSuccess.passedMs(1234L)) {
                                return;
                            }

                            mc.player.connection.sendPacket(new CPacketHeldItemChange(dev.blackhig.zhebushigudu.lover.util.InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE)));
                            mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.STOP_DESTROY_BLOCK, breakPos2, facing));
                        }

                        if (mc.world.getBlockState(breakPos2).getBlock() == Blocks.AIR) {
                            breakPos2 = null;
                            mc.player.connection.sendPacket(new CPacketHeldItemChange(slotMains));
                        }
                    }

                    if (!godBlocks.contains(mc.world.getBlockState(breakPos).getBlock())) {
                        if (this.ghostHand.getValue() && (this.fuck.getValue() || dev.blackhig.zhebushigudu.lover.util.InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE) != -1) && dev.blackhig.zhebushigudu.lover.util.InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE) != -1) {
                            int slotMain = mc.player.inventory.currentItem;
                            if (mc.world.getBlockState(breakPos).getBlock() == Blocks.OBSIDIAN) {
                                if (!breakSuccess.passedMs(1234L)) {
                                    return;
                                }

                                if (this.fuck.getValue() && dev.blackhig.zhebushigudu.lover.util.InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE) == -1) {
                                    for(int i = 9; i < 36; ++i) {
                                        if (mc.player.inventory.getStackInSlot(i).getItem() == Items.DIAMOND_PICKAXE) {
                                            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, i, mc.player.inventory.currentItem, ClickType.SWAP, mc.player);
                                            mc.playerController.updateController();
                                            mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.STOP_DESTROY_BLOCK, breakPos, facing));
                                            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, i, mc.player.inventory.currentItem, ClickType.SWAP, mc.player);
                                            mc.playerController.updateController();
                                            return;
                                        }
                                    }

                                    return;
                                }

                            } else {
                                if (this.fuck.getValue() && dev.blackhig.zhebushigudu.lover.util.InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE) == -1) {
                                    for(int i = 9; i < 35; ++i) {
                                        if (mc.player.inventory.getStackInSlot(i).getItem() == Items.DIAMOND_PICKAXE) {
                                            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, i, mc.player.inventory.currentItem, ClickType.SWAP, mc.player);
                                            mc.playerController.updateController();
                                            mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.STOP_DESTROY_BLOCK, breakPos, facing));
                                            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, i, mc.player.inventory.currentItem, ClickType.SWAP, mc.player);
                                            mc.playerController.updateController();
                                            return;
                                        }
                                    }

                                    return;
                                }

                            }
                            mc.player.inventory.currentItem = dev.blackhig.zhebushigudu.lover.util.InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE);
                            mc.playerController.updateController();
                            mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.STOP_DESTROY_BLOCK, breakPos, facing));
                            mc.player.inventory.currentItem = slotMain;
                            mc.playerController.updateController();

                        } else {
                            mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.STOP_DESTROY_BLOCK, breakPos, facing));
                            if (doubleBreak.getValue()) {
                                mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.STOP_DESTROY_BLOCK, breakPos2, facing));
                            }

                        }
                    }
                }
            }
        }
    }

    public boolean check() {
        return breakPos.equals(new BlockPos(mc.player.posX, mc.player.posY + 2.0D, mc.player.posZ)) || breakPos.equals(new BlockPos(mc.player.posX, mc.player.posY + 3.0D, mc.player.posZ)) || breakPos.equals(new BlockPos(mc.player.posX, mc.player.posY - 1.0D, mc.player.posZ)) || breakPos.equals(new BlockPos(mc.player.posX + 1.0D, mc.player.posY, mc.player.posZ)) || breakPos.equals(new BlockPos(mc.player.posX - 1.0D, mc.player.posY, mc.player.posZ)) || breakPos.equals(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ + 1.0D)) || breakPos.equals(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ - 1.0D)) || breakPos.equals(new BlockPos(mc.player.posX + 1.0D, mc.player.posY + 1.0D, mc.player.posZ)) || breakPos.equals(new BlockPos(mc.player.posX - 1.0D, mc.player.posY + 1.0D, mc.player.posZ)) || breakPos.equals(new BlockPos(mc.player.posX, mc.player.posY + 1.0D, mc.player.posZ + 1.0D)) || breakPos.equals(new BlockPos(mc.player.posX, mc.player.posY + 1.0D, mc.player.posZ - 1.0D));
    }

    public void onTick() {
        if (!fullNullCheck()) {
            if (!mc.player.capabilities.isCreativeMode) {
                if (cancelStart) {
                    if (this.crystal.getValue() && this.attackcrystal.getValue() && mc.world.getBlockState(breakPos).getBlock() == Blocks.AIR) {
                        attackcrystal();
                    }

                    if (this.bind.getValue().isDown() && this.crystal.getValue() && dev.blackhig.zhebushigudu.lover.util.InventoryUtil.findHotbarBlock(BlockObsidian.class) != -1 && mc.world.getBlockState(breakPos).getBlock() == Blocks.AIR) {
                        int obbySlot = dev.blackhig.zhebushigudu.lover.util.InventoryUtil.findHotbarBlock(BlockObsidian.class);
                        int old = mc.player.inventory.currentItem;
                        this.switchToSlot(obbySlot);
                        dev.blackhig.zhebushigudu.lover.util.BlockUtil.placeBlock(breakPos, EnumHand.MAIN_HAND, false, true, false);
                        this.switchToSlot(old);
                    }

                    if (breakPos != null && mc.player != null && mc.player.getDistanceSq(breakPos) > MathUtil.square((double)((Float)this.range.getValue()).floatValue())) {
                        breakPos = null;
                        breakPos2 = null;
                        cancelStart = false;
                    } else if (mc.world.getBlockState(breakPos).getBlock() == Blocks.AIR && !this.instant.getValue()) {
                        breakPos = null;
                        breakPos2 = null;
                        cancelStart = false;
                    } else if (!godBlocks.contains(mc.world.getBlockState(breakPos).getBlock())) {
                        if (dev.blackhig.zhebushigudu.lover.util.InventoryUtil.getItemHotbar(Items.END_CRYSTAL) != -1 && this.crystal.getValue() && mc.world.getBlockState(breakPos).getBlock() == Blocks.OBSIDIAN && !this.check() && !breakPos.equals(AntiBurrow.pos)) {
                            dev.blackhig.zhebushigudu.lover.util.BlockUtil.placeCrystalOnBlock(breakPos, EnumHand.MAIN_HAND, true, false, true);
                        }

                        if (this.ghostHand.getValue() || this.ghostHand.getValue() && this.fuck.getValue()) {
                            float breakTime = mc.world.getBlockState(breakPos).getBlockHardness(mc.world, breakPos);
                            int slotMain = mc.player.inventory.currentItem;
                            if (!breakSuccess.passedMs((long)((int)breakTime))) {
                                return;
                            }

                            if (this.fuck.getValue() && dev.blackhig.zhebushigudu.lover.util.InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE) == -1) {
                                for(int i = 9; i < 36; ++i) {
                                    if (mc.player.inventory.getStackInSlot(i).getItem() == Items.DIAMOND_PICKAXE) {
                                        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, i, mc.player.inventory.currentItem, ClickType.SWAP, mc.player);
                                        mc.playerController.updateController();
                                        mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.STOP_DESTROY_BLOCK, breakPos, facing));
                                        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, i, mc.player.inventory.currentItem, ClickType.SWAP, mc.player);
                                        mc.playerController.updateController();
                                        return;
                                    }
                                }
                            }

                            try {
                                this.block = mc.world.getBlockState(breakPos).getBlock();
                            } catch (Exception var4) {
                            }

                            int toolSlot = this.getBestAvailableToolSlot(this.block.getBlockState().getBaseState());
                            if (mc.player.inventory.currentItem != toolSlot && toolSlot != -1) {
                                mc.player.inventory.currentItem = toolSlot;
                                mc.playerController.updateController();
                                mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.STOP_DESTROY_BLOCK, breakPos, facing));
                                mc.player.inventory.currentItem = slotMain;
                                mc.playerController.updateController();
                                return;
                            }
                        }

                        mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.STOP_DESTROY_BLOCK, breakPos, facing));
                        ++tickCount;
                        ++tickCount2;
                    }
                }
            }
        }
    }

    private static void rend() {
        time = 0.0D;
        time2 = 0.0D;
        empty = false;
        cancelStart = false;
        breakPos = null;
    }

    public void onDisable() {
        if (!Feature.fullNullCheck()) {
            rend();
            fadeBlockSize.reset();
            if (this.haste.getValue()) {
                mc.player.removePotionEffect(MobEffects.HASTE);
            }

        }
    }

    public void onEnable() {
        if (Feature.fullNullCheck()) {
            rend();
        }

        fadeBlockSize.reset();
    }

    private double normalize(double value, double max, double min) {
        return 0.5D * ((value - min) / (max - min)) + 0.5D;
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (fullNullCheck()) {
            rend();
        } else if (!this.isEnabled()) {
            rend();
        } else if (this.render.getValue()) {
            if (this.render.getValue() && breakPos != null && cancelStart && this.render2.getValue()) {
                Vec3d interpolateEntity = MathUtil.interpolateEntity(mc.player, mc.getRenderPartialTicks());
                AxisAlignedBB pos = (new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)).offset(blockRenderSmooth.getRenderPos());
                pos = pos.grow((double)0.002F).offset(-interpolateEntity.x, -interpolateEntity.y, -interpolateEntity.z);
                this.renderESP1(pos, (float)fadeBlockSize.easeOutQuad());
            }

            if (breakPos2 != null && this.render3.getValue()) {
                Vec3d interpolateEntity = MathUtil.interpolateEntity(mc.player, mc.getRenderPartialTicks());
                AxisAlignedBB pos = (new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)).offset(breakPos2);
                pos = pos.grow((double)0.002F).offset(-interpolateEntity.x, -interpolateEntity.y, -interpolateEntity.z);
                this.renderESP2(pos, (float)pos2FadeBlockSize.easeOutQuad());
            }

        }
    }

    public void renderESP1(AxisAlignedBB axisAlignedBB, float size) {
        double centerX = axisAlignedBB.minX + (axisAlignedBB.maxX - axisAlignedBB.minX) / 2.0D;
        double centerY = axisAlignedBB.minY + (axisAlignedBB.maxY - axisAlignedBB.minY) / 2.0D;
        double centerZ = axisAlignedBB.minZ + (axisAlignedBB.maxZ - axisAlignedBB.minZ) / 2.0D;
        double full = axisAlignedBB.maxX - centerX;
        double progressValX = full * (double)size;
        double progressValY = full * (double)size;
        double progressValZ = full * (double)size;
        AxisAlignedBB axisAlignedBB2 = new AxisAlignedBB(centerX - progressValX, centerY - progressValY, centerZ - progressValZ, centerX + progressValX, centerY + progressValY, centerZ + progressValZ);
        if (axisAlignedBB2 != null) {
            RenderUtils3D.drawBoxTest((float)axisAlignedBB2.minX, (float)axisAlignedBB2.minY, (float)axisAlignedBB2.minZ, (float)axisAlignedBB2.maxX - (float)axisAlignedBB2.minX, (float)axisAlignedBB2.maxY - (float)axisAlignedBB2.minY, (float)axisAlignedBB2.maxZ - (float)axisAlignedBB2.minZ, this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue(), 63);
        }

    }

    public void renderESP2(AxisAlignedBB axisAlignedBB, float size) {
        double centerX = axisAlignedBB.minX + (axisAlignedBB.maxX - axisAlignedBB.minX) / 2.0D;
        double centerY = axisAlignedBB.minY + (axisAlignedBB.maxY - axisAlignedBB.minY) / 2.0D;
        double centerZ = axisAlignedBB.minZ + (axisAlignedBB.maxZ - axisAlignedBB.minZ) / 2.0D;
        double full = axisAlignedBB.maxX - centerX;
        double progressValX = full * (double)size;
        double progressValY = full * (double)size;
        double progressValZ = full * (double)size;
        AxisAlignedBB axisAlignedBB2 = new AxisAlignedBB(centerX - progressValX, centerY - progressValY, centerZ - progressValZ, centerX + progressValX, centerY + progressValY, centerZ + progressValZ);
        if (axisAlignedBB2 != null) {
            RenderUtils3D.drawBoxTest((float)axisAlignedBB2.minX, (float)axisAlignedBB2.minY, (float)axisAlignedBB2.minZ, (float)axisAlignedBB2.maxX - (float)axisAlignedBB2.minX, (float)axisAlignedBB2.maxY - (float)axisAlignedBB2.minY, (float)axisAlignedBB2.maxZ - (float)axisAlignedBB2.minZ, this.red2.getValue(), this.green2.getValue(), this.blue2.getValue(), this.alpha2.getValue(), 63);
        }

    }

    public String getDisplayInfo() {
        return "Instant";
    }

    private void switchToSlot(int slot) {
        mc.player.inventory.currentItem = slot;
        mc.playerController.updateController();
    }

    public static enum RenderMode {
        Fill,
        Outline,
        Both;
    }
}
 