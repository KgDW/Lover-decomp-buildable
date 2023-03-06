package dev.blackhig.zhebushigudu.lover.features.modules.combat;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.math.AxisAlignedBB;
import dev.blackhig.zhebushigudu.lover.util.BlockUtil;
import net.minecraft.util.EnumHand;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.math.Vec3d;
import net.minecraft.init.Blocks;
import net.minecraft.block.BlockEnderChest;
import dev.blackhig.zhebushigudu.lover.features.command.Command;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.block.BlockObsidian;
import dev.blackhig.zhebushigudu.lover.lover;
import net.minecraft.entity.Entity;
import dev.blackhig.zhebushigudu.lover.util.Anti.EntityUtil;
import java.util.HashMap;
import net.minecraft.util.math.BlockPos;
import java.util.Map;
import dev.blackhig.zhebushigudu.lover.util.Timer;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.features.modules.Module;

public class SmartAntiCity extends Module
{
    private final Setting<Boolean> rotate;
    public Setting<Boolean> packet;
    private final int obsidian;
    private final int enderchest;
    private final Timer timer;
    private final Timer retryTimer;
    private final float yaw;
    private final float pitch;
    private final boolean rotating;
    private final Setting<Boolean> center;
    private final Setting<BlockMode> mode;
    private final Setting<Integer> delay;
    private final Setting<Boolean> crystal;
    private final Map<BlockPos, Integer> retries;
    private BlockPos startPos;
    
    public SmartAntiCity() {
        super("SmartAntiCity", "SmartAntiCity", Category.COMBAT, true, false, false);
        this.rotate = this.register(new Setting<>("Rotate", false));
        this.packet = this.register(new Setting<>("Packet", true));
        this.obsidian = -1;
        this.enderchest = -1;
        this.timer = new Timer();
        this.retryTimer = new Timer();
        this.yaw = 0.0f;
        this.pitch = 0.0f;
        this.rotating = false;
        this.center = this.register(new Setting<>("TPCenter", true));
        this.mode = this.register(new Setting<>("BlockMode", BlockMode.Smart));
        this.delay = this.register(new Setting<>("Delay", 0, 0, 300));
        this.crystal = this.register(new Setting<>("BreakCrystal", true));
        this.retries = new HashMap<BlockPos, Integer>();
    }
    
    @Override
    public void onEnable() {
        this.startPos = EntityUtil.getRoundedBlockPos(Surround.mc.player);
        if (this.center.getValue()) {
            lover.positionManager.setPositionPacket(this.startPos.getX() + 0.5, this.startPos.getY(), this.startPos.getZ() + 0.5, true, true, true);
        }
    }
    
    @Override
    public void onTick() {
        if (!this.startPos.equals(EntityUtil.getRoundedBlockPos(Surround.mc.player))) {
            this.toggle();
        }
    }
    
    @Override
    public void onUpdate() {
        final Vec3d a = Blocker.mc.player.getPositionVector();
        if (!dev.blackhig.zhebushigudu.lover.util.e.Util.mc.world.isBlockLoaded(dev.blackhig.zhebushigudu.lover.util.e.Util.mc.player.getPosition())) {
            return;
        }
        BlockPos pos = new BlockPos(SmartAntiCity.mc.player.posX, SmartAntiCity.mc.player.posY, SmartAntiCity.mc.player.posZ);
        if (this.mode.getValue() == BlockMode.Obsidian && dev.blackhig.zhebushigudu.lover.util.e.Util.findHotbarBlock(BlockObsidian.class) == -1) {
            Command.sendMessage(ChatFormatting.RED + "Obsidian ?");
            this.disable();
            return;
        }
        if (this.mode.getValue() == BlockMode.Chest && dev.blackhig.zhebushigudu.lover.util.e.Util.findHotbarBlock(BlockEnderChest.class) == -1) {
            Command.sendMessage(ChatFormatting.RED + "Ender Chest ?");
            this.disable();
            return;
        }
        if (this.mode.getValue() == BlockMode.Smart && dev.blackhig.zhebushigudu.lover.util.e.Util.findHotbarBlock(BlockEnderChest.class) == -1 && dev.blackhig.zhebushigudu.lover.util.e.Util.findHotbarBlock(BlockObsidian.class) == -1) {
            Command.sendMessage(ChatFormatting.RED + "Obsidian/Ender Chest ?");
            this.disable();
            return;
        }
        pos = new BlockPos(SmartAntiCity.mc.player.posX, SmartAntiCity.mc.player.posY, SmartAntiCity.mc.player.posZ);
        if (this.checkCrystal(a, EntityUtil.getVarOffsets(0, 1, 1)) != null) {
            if (this.crystal.getValue()) {
                EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(0, 1, 1)), true);
            }
            this.place(a, EntityUtil.getVarOffsets(0, 1, 1));
        }
        if (this.checkCrystal(a, EntityUtil.getVarOffsets(0, 1, -1)) != null) {
            this.place(a, EntityUtil.getVarOffsets(0, 1, -1));
        }
        if (this.checkCrystal(a, EntityUtil.getVarOffsets(1, 1, 0)) != null) {
            if (this.crystal.getValue()) {
                EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(1, 1, 0)), true);
            }
            this.place(a, EntityUtil.getVarOffsets(1, 1, 0));
        }
        if (this.checkCrystal(a, EntityUtil.getVarOffsets(-1, 1, 0)) != null) {
            if (this.crystal.getValue()) {
                EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(-1, 1, 0)), true);
            }
            this.place(a, EntityUtil.getVarOffsets(-1, 1, 0));
        }
        if (this.checkCrystal(a, EntityUtil.getVarOffsets(0, 0, 1)) != null) {
            if (this.crystal.getValue()) {
                EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(0, 0, 1)), true);
            }
            this.place(a, EntityUtil.getVarOffsets(0, 0, 1));
        }
        if (this.checkCrystal(a, EntityUtil.getVarOffsets(0, 1, -1)) != null) {
            this.place(a, EntityUtil.getVarOffsets(0, 0, -1));
        }
        if (this.checkCrystal(a, EntityUtil.getVarOffsets(0, 2, 1)) != null) {
            if (this.crystal.getValue()) {
                EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(0, 2, 1)), true);
            }
            this.place(a, EntityUtil.getVarOffsets(0, 2, 1));
        }
        if (this.checkCrystal(a, EntityUtil.getVarOffsets(0, 2, -1)) != null) {
            if (this.crystal.getValue()) {
                EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(0, 2, -1)), true);
            }
            this.place(a, EntityUtil.getVarOffsets(0, 2, -1));
        }
        if (this.checkCrystal(a, EntityUtil.getVarOffsets(1, 2, 0)) != null) {
            if (this.crystal.getValue()) {
                EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(1, 2, 0)), true);
            }
            this.place(a, EntityUtil.getVarOffsets(1, 2, 0));
        }
        if (this.checkCrystal(a, EntityUtil.getVarOffsets(-1, 2, 0)) != null) {
            if (this.crystal.getValue()) {
                EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(-1, 2, 0)), true);
            }
            this.place(a, EntityUtil.getVarOffsets(-1, 2, 0));
        }
        if (this.checkCrystal(a, EntityUtil.getVarOffsets(0, 4, 0)) != null) {
            this.place(a, EntityUtil.getVarOffsets(0, 4, 0));
        }
        if (this.checkCrystal(a, EntityUtil.getVarOffsets(0, 5, 0)) != null) {
            this.place(a, EntityUtil.getVarOffsets(0, 5, 0));
        }
        if (this.checkCrystal(a, EntityUtil.getVarOffsets(0, 3, 0)) != null) {
            this.place(a, EntityUtil.getVarOffsets(0, 3, 0));
        }
        if (this.checkCrystal(a, EntityUtil.getVarOffsets(0, 3, 1)) != null) {
            this.place(a, EntityUtil.getVarOffsets(0, 3, 1));
        }
        if (this.checkCrystal(a, EntityUtil.getVarOffsets(0, 3, -1)) != null) {
            this.place(a, EntityUtil.getVarOffsets(0, 3, -1));
        }
        if (this.checkCrystal(a, EntityUtil.getVarOffsets(1, 3, 0)) != null) {
            this.place(a, EntityUtil.getVarOffsets(1, 3, 0));
        }
        if (this.checkCrystal(a, EntityUtil.getVarOffsets(-1, 3, 0)) != null) {
            this.place(a, EntityUtil.getVarOffsets(-1, 3, 0));
        }
        if (this.checkCrystal(a, EntityUtil.getVarOffsets(1, 0, 0)) != null) {
            if (this.crystal.getValue()) {
                EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(1, 0, 0)), true);
            }
            this.place(a, EntityUtil.getVarOffsets(1, 0, 0));
        }
        if (this.checkCrystal(a, EntityUtil.getVarOffsets(-1, 0, 0)) != null) {
            if (this.crystal.getValue()) {
                EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(-1, 0, 0)), true);
            }
            this.place(a, EntityUtil.getVarOffsets(-1, 0, 0));
        }
        if (this.getBlock(pos.add(1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(2, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(2, 1, 0)).getBlock() == Blocks.AIR) {
            if (this.checkCrystal(a, EntityUtil.getVarOffsets(2, 1, 0)) != null && this.crystal.getValue()) {
                EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(2, 1, 0)), true);
            }
            this.perform(pos.add(2, 1, 0));
        }
        if (this.getBlock(pos.add(-1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(-2, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(-2, 1, 0)).getBlock() == Blocks.AIR) {
            if (this.checkCrystal(a, EntityUtil.getVarOffsets(-2, 1, 0)) != null && this.crystal.getValue()) {
                EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(-2, 1, 0)), true);
            }
            this.perform(pos.add(-2, 1, 0));
        }
        if (this.getBlock(pos.add(0, 0, 1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(0, 0, 2)).getBlock() == Blocks.AIR && this.getBlock(pos.add(0, 1, 2)).getBlock() == Blocks.AIR) {
            if (this.checkCrystal(a, EntityUtil.getVarOffsets(0, 1, 2)) != null && this.crystal.getValue()) {
                EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(0, 1, 2)), true);
            }
            this.perform(pos.add(0, 1, 2));
        }
        if (this.getBlock(pos.add(0, 0, -1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(0, 0, -2)).getBlock() == Blocks.AIR && this.getBlock(pos.add(0, 1, -2)).getBlock() == Blocks.AIR) {
            if (this.checkCrystal(a, EntityUtil.getVarOffsets(0, 1, -2)) != null && this.crystal.getValue()) {
                EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(0, 1, -2)), true);
            }
            this.perform(pos.add(0, 1, -2));
        }
        if (this.getBlock(pos.add(0, 0, 1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(0, -1, 1)).getBlock() == Blocks.AIR) {
            if (this.checkCrystal(a, EntityUtil.getVarOffsets(0, 0, 1)) != null && this.crystal.getValue()) {
                EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(0, 0, 1)), true);
            }
            this.perform(pos.add(0, -1, 1));
            this.perform(pos.add(0, 0, 1));
        }
        if (this.getBlock(pos.add(0, 0, -1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(0, -1, -1)).getBlock() == Blocks.AIR) {
            if (this.checkCrystal(a, EntityUtil.getVarOffsets(0, 0, -1)) != null && this.crystal.getValue()) {
                EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(0, 0, -1)), true);
            }
            this.perform(pos.add(0, -1, -1));
            this.perform(pos.add(0, 0, -1));
        }
        if (this.getBlock(pos.add(1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(1, -1, 0)).getBlock() == Blocks.AIR) {
            if (this.checkCrystal(a, EntityUtil.getVarOffsets(1, 0, 0)) != null && this.crystal.getValue()) {
                EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(1, 0, 0)), true);
            }
            this.perform(pos.add(1, -1, 0));
            this.perform(pos.add(1, 0, 0));
        }
        if (this.getBlock(pos.add(-1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(-1, -1, 0)).getBlock() == Blocks.AIR) {
            if (this.checkCrystal(a, EntityUtil.getVarOffsets(-1, 0, 0)) != null && this.crystal.getValue()) {
                EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(-1, 0, 0)), true);
            }
            this.perform(pos.add(-1, -1, 0));
            this.perform(pos.add(-1, 0, 0));
        }
        if (this.getBlock(pos.add(0, 0, 1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(0, 0, 2)).getBlock() == Blocks.AIR) {
            if (this.checkCrystal(a, EntityUtil.getVarOffsets(0, 0, 2)) != null && this.crystal.getValue()) {
                EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(0, 0, 2)), true);
            }
            this.perform(pos.add(0, 0, 1));
            this.perform(pos.add(0, 0, 2));
            this.perform(pos.add(0, -1, 2));
        }
        if (this.getBlock(pos.add(0, 0, -1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(0, 0, -2)).getBlock() == Blocks.AIR) {
            if (this.checkCrystal(a, EntityUtil.getVarOffsets(0, 0, -2)) != null && this.crystal.getValue()) {
                EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(0, 0, -2)), true);
            }
            this.perform(pos.add(0, 0, -1));
            this.perform(pos.add(0, 0, -2));
            this.perform(pos.add(0, -1, -2));
        }
        if (this.getBlock(pos.add(1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(2, 0, 0)).getBlock() == Blocks.AIR) {
            if (this.checkCrystal(a, EntityUtil.getVarOffsets(2, 0, 0)) != null && this.crystal.getValue()) {
                EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(2, 0, 0)), true);
            }
            this.perform(pos.add(1, 0, 0));
            this.perform(pos.add(2, 0, 0));
            this.perform(pos.add(2, -1, 0));
        }
        if (this.getBlock(pos.add(-1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(-2, 0, 0)).getBlock() == Blocks.AIR) {
            if (this.checkCrystal(a, EntityUtil.getVarOffsets(-2, 0, 0)) != null && this.crystal.getValue()) {
                EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(-2, 0, 0)), true);
            }
            this.perform(pos.add(-1, 0, 0));
            this.perform(pos.add(-2, 0, 0));
            this.perform(pos.add(-2, -1, 0));
        }
        if (this.getBlock(pos.add(0, 0, 1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(0, 1, 1)).getBlock() == Blocks.AIR) {
            this.perform(pos.add(0, 0, 1));
            this.perform(pos.add(0, 1, 1));
            this.perform(pos.add(0, 1, 2));
        }
        if (this.getBlock(pos.add(0, 0, -1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(0, 1, -1)).getBlock() == Blocks.AIR) {
            this.perform(pos.add(0, 0, -1));
            this.perform(pos.add(0, 1, -1));
            this.perform(pos.add(0, 1, -2));
        }
        if (this.getBlock(pos.add(1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(1, 1, 0)).getBlock() == Blocks.AIR) {
            this.perform(pos.add(1, 0, 0));
            this.perform(pos.add(1, 1, 0));
            this.perform(pos.add(2, 1, 0));
        }
        if (this.getBlock(pos.add(-1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(-1, 1, 0)).getBlock() == Blocks.AIR) {
            this.perform(pos.add(-1, 0, 0));
            this.perform(pos.add(-1, 1, 0));
            this.perform(pos.add(-2, 1, 0));
        }
        if (this.getBlock(pos.add(1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(1, 0, 1)).getBlock() == Blocks.AIR) {
            if (this.checkCrystal(a, EntityUtil.getVarOffsets(1, 0, 1)) != null && this.crystal.getValue()) {
                EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(1, 0, 1)), true);
            }
            this.perform(pos.add(1, 0, 0));
            this.perform(pos.add(1, 0, 1));
        }
        if (this.getBlock(pos.add(0, 0, 1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(1, 0, 1)).getBlock() == Blocks.AIR) {
            if (this.checkCrystal(a, EntityUtil.getVarOffsets(1, 0, 1)) != null && this.crystal.getValue()) {
                EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(1, 0, 1)), true);
            }
            this.perform(pos.add(0, 0, 1));
            this.perform(pos.add(1, 0, 1));
        }
        if (this.getBlock(pos.add(-1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(-1, 0, -1)).getBlock() == Blocks.AIR) {
            if (this.checkCrystal(a, EntityUtil.getVarOffsets(-1, 0, -1)) != null && this.crystal.getValue()) {
                EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(-1, 0, -1)), true);
            }
            this.perform(pos.add(-1, 0, 0));
            this.perform(pos.add(-1, 0, -1));
        }
        if (this.getBlock(pos.add(0, 0, -1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(-1, 0, -1)).getBlock() == Blocks.AIR) {
            if (this.checkCrystal(a, EntityUtil.getVarOffsets(-1, 0, -1)) != null && this.crystal.getValue()) {
                EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(-1, 0, -1)), true);
            }
            this.perform(pos.add(0, 0, -1));
            this.perform(pos.add(-1, 0, -1));
        }
        if (this.getBlock(pos.add(-1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(-1, 0, 1)).getBlock() == Blocks.AIR) {
            if (this.checkCrystal(a, EntityUtil.getVarOffsets(-1, 0, 1)) != null && this.crystal.getValue()) {
                EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(-1, 0, 1)), true);
            }
            this.perform(pos.add(-1, 0, 0));
            this.perform(pos.add(-1, 0, 1));
        }
        if (this.getBlock(pos.add(0, 0, 1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(-1, 0, 1)).getBlock() == Blocks.AIR) {
            if (this.checkCrystal(a, EntityUtil.getVarOffsets(-1, 0, 1)) != null && this.crystal.getValue()) {
                EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(-1, 0, 1)), true);
            }
            this.perform(pos.add(0, 0, 1));
            this.perform(pos.add(-1, 0, 1));
        }
        if (this.getBlock(pos.add(1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(1, 0, -1)).getBlock() == Blocks.AIR) {
            if (this.checkCrystal(a, EntityUtil.getVarOffsets(1, 0, -1)) != null && this.crystal.getValue()) {
                EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(1, 0, -1)), true);
            }
            this.perform(pos.add(1, 0, 0));
            this.perform(pos.add(1, 0, -1));
        }
        if (this.getBlock(pos.add(0, 0, -1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(1, 0, -1)).getBlock() == Blocks.AIR) {
            if (this.checkCrystal(a, EntityUtil.getVarOffsets(1, 0, -1)) != null && this.crystal.getValue()) {
                EntityUtil.attackEntity(this.checkCrystal(a, EntityUtil.getVarOffsets(1, 0, -1)), true);
            }
            this.perform(pos.add(0, 0, -1));
            this.perform(pos.add(1, 0, -1));
        }
        if (this.getBlock(pos.add(1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(1, 0, 1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(1, 1, 1)).getBlock() == Blocks.AIR) {
            this.perform(pos.add(1, 0, 0));
            this.perform(pos.add(1, 0, 1));
            this.perform(pos.add(1, 1, 1));
        }
        if (this.getBlock(pos.add(0, 0, 1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(1, 0, 1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(1, 1, 1)).getBlock() == Blocks.AIR) {
            this.perform(pos.add(0, 0, 1));
            this.perform(pos.add(1, 0, 1));
            this.perform(pos.add(1, 1, 1));
        }
        if (this.getBlock(pos.add(-1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(-1, 0, -1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(-1, 1, -1)).getBlock() == Blocks.AIR) {
            this.perform(pos.add(-1, 0, 0));
            this.perform(pos.add(-1, 0, -1));
            this.perform(pos.add(-1, 1, -1));
        }
        if (this.getBlock(pos.add(0, 0, -1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(-1, 0, -1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(-1, 1, -1)).getBlock() == Blocks.AIR) {
            this.perform(pos.add(0, 0, -1));
            this.perform(pos.add(-1, 0, -1));
            this.perform(pos.add(-1, 1, -1));
        }
        if (this.getBlock(pos.add(-1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(-1, 0, 1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(-1, 1, 1)).getBlock() == Blocks.AIR) {
            this.perform(pos.add(-1, 0, 0));
            this.perform(pos.add(-1, 0, 1));
            this.perform(pos.add(-1, 1, 1));
        }
        if (this.getBlock(pos.add(0, 0, 1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(-1, 0, 1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(-1, 1, 1)).getBlock() == Blocks.AIR) {
            this.perform(pos.add(0, 0, 1));
            this.perform(pos.add(-1, 0, 1));
            this.perform(pos.add(-1, 1, 1));
        }
        if (this.getBlock(pos.add(1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(pos.add(1, 0, -1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(1, 1, -1)).getBlock() == Blocks.AIR) {
            this.perform(pos.add(1, 0, 0));
            this.perform(pos.add(1, 0, -1));
            this.perform(pos.add(1, 1, -1));
        }
        if (this.getBlock(pos.add(0, 0, -1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(1, 0, -1)).getBlock() == Blocks.AIR && this.getBlock(pos.add(1, 1, -1)).getBlock() == Blocks.AIR) {
            this.perform(pos.add(0, 0, -1));
            this.perform(pos.add(1, 0, -1));
            this.perform(pos.add(1, 1, -1));
        }
    }
    
    private void switchToSlot(final int slot) {
        SmartAntiCity.mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
        SmartAntiCity.mc.player.inventory.currentItem = slot;
        SmartAntiCity.mc.playerController.updateController();
    }
    
    private Boolean place(final Vec3d pos, final Vec3d[] list) {
        for (int var4 = list.length, var5 = 0; var5 < var4; ++var5) {
            final Vec3d vec3d = list[var5];
            final BlockPos position = new BlockPos(pos).add(vec3d.x, vec3d.y, vec3d.z);
            final int a = Blocker.mc.player.inventory.currentItem;
            if (this.retryTimer.passedMs(2500L)) {
                this.retries.clear();
                this.retryTimer.reset();
            }
            if (this.mode.getValue() == BlockMode.Obsidian) {
                dev.blackhig.zhebushigudu.lover.util.e.Util.mc.player.connection.sendPacket(new CPacketHeldItemChange(dev.blackhig.zhebushigudu.lover.util.e.Util.findHotbarBlock(BlockObsidian.class)));
                dev.blackhig.zhebushigudu.lover.util.e.Util.mc.player.inventory.currentItem = dev.blackhig.zhebushigudu.lover.util.e.Util.findHotbarBlock(BlockObsidian.class);
            }
            if (this.mode.getValue() == BlockMode.Chest) {
                dev.blackhig.zhebushigudu.lover.util.e.Util.mc.player.connection.sendPacket(new CPacketHeldItemChange(dev.blackhig.zhebushigudu.lover.util.e.Util.findHotbarBlock(BlockEnderChest.class)));
                dev.blackhig.zhebushigudu.lover.util.e.Util.mc.player.inventory.currentItem = dev.blackhig.zhebushigudu.lover.util.e.Util.findHotbarBlock(BlockEnderChest.class);
            }
            if (this.mode.getValue() == BlockMode.Smart) {
                if (dev.blackhig.zhebushigudu.lover.util.e.Util.findHotbarBlock(BlockObsidian.class) != -1) {
                    dev.blackhig.zhebushigudu.lover.util.e.Util.mc.player.connection.sendPacket(new CPacketHeldItemChange(dev.blackhig.zhebushigudu.lover.util.e.Util.findHotbarBlock(BlockObsidian.class)));
                    dev.blackhig.zhebushigudu.lover.util.e.Util.mc.player.inventory.currentItem = dev.blackhig.zhebushigudu.lover.util.e.Util.findHotbarBlock(BlockObsidian.class);
                }
                else if (dev.blackhig.zhebushigudu.lover.util.e.Util.findHotbarBlock(BlockEnderChest.class) != -1) {
                    dev.blackhig.zhebushigudu.lover.util.e.Util.mc.player.connection.sendPacket(new CPacketHeldItemChange(dev.blackhig.zhebushigudu.lover.util.e.Util.findHotbarBlock(BlockEnderChest.class)));
                    dev.blackhig.zhebushigudu.lover.util.e.Util.mc.player.inventory.currentItem = dev.blackhig.zhebushigudu.lover.util.e.Util.findHotbarBlock(BlockEnderChest.class);
                }
            }
            Blocker.mc.playerController.updateController();
            boolean isSneaking = BlockUtil.placeBlock(position, EnumHand.MAIN_HAND, false, this.packet.getValue(), true);
            Blocker.mc.player.inventory.currentItem = a;
            Blocker.mc.playerController.updateController();
        }
        return !this.timer.passedMs(this.delay.getValue());
    }
    
    Entity checkCrystal(final Vec3d pos, final Vec3d[] list) {
        Entity crystal = null;
        final Vec3d[] var4 = list;
        for (int var5 = list.length, var6 = 0; var6 < var5; ++var6) {
            final Vec3d vec3d = var4[var6];
            final BlockPos position = new BlockPos(pos).add(vec3d.x, vec3d.y, vec3d.z);
            for (final Entity entity : Blocker.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(position))) {
                if (entity instanceof EntityEnderCrystal) {
                    if (crystal != null) {
                        continue;
                    }
                    crystal = entity;
                }
            }
        }
        return crystal;
    }
    
    private IBlockState getBlock(final BlockPos block) {
        return SmartAntiCity.mc.world.getBlockState(block);
    }
    
    private void perform(final BlockPos pos) {
        final int old = SmartAntiCity.mc.player.inventory.currentItem;
        if (this.mode.getValue() == BlockMode.Obsidian) {
            dev.blackhig.zhebushigudu.lover.util.e.Util.mc.player.connection.sendPacket(new CPacketHeldItemChange(dev.blackhig.zhebushigudu.lover.util.e.Util.findHotbarBlock(BlockObsidian.class)));
            dev.blackhig.zhebushigudu.lover.util.e.Util.mc.player.inventory.currentItem = dev.blackhig.zhebushigudu.lover.util.e.Util.findHotbarBlock(BlockObsidian.class);
        }
        if (this.mode.getValue() == BlockMode.Chest) {
            dev.blackhig.zhebushigudu.lover.util.e.Util.mc.player.connection.sendPacket(new CPacketHeldItemChange(dev.blackhig.zhebushigudu.lover.util.e.Util.findHotbarBlock(BlockEnderChest.class)));
            dev.blackhig.zhebushigudu.lover.util.e.Util.mc.player.inventory.currentItem = dev.blackhig.zhebushigudu.lover.util.e.Util.findHotbarBlock(BlockEnderChest.class);
        }
        if (this.mode.getValue() == BlockMode.Smart) {
            if (dev.blackhig.zhebushigudu.lover.util.e.Util.findHotbarBlock(BlockObsidian.class) != -1) {
                dev.blackhig.zhebushigudu.lover.util.e.Util.mc.player.connection.sendPacket(new CPacketHeldItemChange(dev.blackhig.zhebushigudu.lover.util.e.Util.findHotbarBlock(BlockObsidian.class)));
                dev.blackhig.zhebushigudu.lover.util.e.Util.mc.player.inventory.currentItem = dev.blackhig.zhebushigudu.lover.util.e.Util.findHotbarBlock(BlockObsidian.class);
            }
            else if (dev.blackhig.zhebushigudu.lover.util.e.Util.findHotbarBlock(BlockEnderChest.class) != -1) {
                dev.blackhig.zhebushigudu.lover.util.e.Util.mc.player.connection.sendPacket(new CPacketHeldItemChange(dev.blackhig.zhebushigudu.lover.util.e.Util.findHotbarBlock(BlockEnderChest.class)));
                dev.blackhig.zhebushigudu.lover.util.e.Util.mc.player.inventory.currentItem = dev.blackhig.zhebushigudu.lover.util.e.Util.findHotbarBlock(BlockEnderChest.class);
            }
        }
        BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, this.rotate.getValue(), true, false);
        this.switchToSlot(old);
    }
    
    enum BlockMode
    {
        Obsidian, 
        Chest, 
        Smart
    }
}
