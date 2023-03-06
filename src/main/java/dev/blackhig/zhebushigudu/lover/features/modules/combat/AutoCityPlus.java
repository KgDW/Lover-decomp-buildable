package dev.blackhig.zhebushigudu.lover.features.modules.combat;

import dev.blackhig.zhebushigudu.lover.features.modules.Module;
import dev.blackhig.zhebushigudu.lover.features.modules.misc.InstantMine;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.lover;
import dev.blackhig.zhebushigudu.lover.util.BlockUtil;
import dev.blackhig.zhebushigudu.lover.util.EntityUtil;
import dev.blackhig.zhebushigudu.lover.util.InventoryUtil;
import dev.blackhig.zhebushigudu.lover.util.Timer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.math.BlockPos;

import java.util.Arrays;
import java.util.List;

public class AutoCityPlus extends Module
{
    public static EntityPlayer target;
    private static AutoCityPlus INSTANCE;
    private final Setting<Float> range;
    private final Setting<Boolean> toggle;
    private final List<Block> godBlocks;
    private final Timer timer;
    
    public AutoCityPlus() {
        super("AutoCity+", "AutoCityPlus", Category.COMBAT, true, false, false);
        this.range = this.register(new Setting<>("Range", 5.0f, 1.0f, 8.0f));
        this.toggle = this.register(new Setting<>("AutoToggle", false));
        this.godBlocks = Arrays.asList(Blocks.OBSIDIAN, Blocks.BEDROCK);
        this.timer = new Timer();
        this.setInstance();
    }
    
    public static AutoCityPlus getInstance() {
        if (AutoCityPlus.INSTANCE == null) {
            AutoCityPlus.INSTANCE = new AutoCityPlus();
        }
        return AutoCityPlus.INSTANCE;
    }
    
    private void setInstance() {
        AutoCityPlus.INSTANCE = this;
    }
    
    @Override
    public void onTick() {
        if (fullNullCheck()) {
            return;
        }
        if (lover.moduleManager.isModuleEnabled("AutoCev")) {
            return;
        }
        if (lover.moduleManager.isModuleEnabled("AutoCev1")) {
            return;
        }
        if (InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE) == -1) {
            return;
        }
        AutoCityPlus.target = this.getTarget(this.range.getValue());
        this.surroundMine();
        if (this.toggle.getValue()) {
            this.disable();
        }
    }
    
    @Override
    public String getDisplayInfo() {
        if (AutoCityPlus.target != null) {
            return AutoCityPlus.target.getName();
        }
        return null;
    }
    
    private void surroundMine() {
        if (AutoCityPlus.target == null) {
            return;
        }
        final BlockPos feet = new BlockPos(AutoCityPlus.target.posX, AutoCityPlus.target.posY, AutoCityPlus.target.posZ);
        if (!this.detection(AutoCityPlus.target)) {
            if (this.getBlock(feet.add(1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(1, 0, 0)).getBlock() != Blocks.AIR && this.godBlocks.contains(this.getBlock(feet.add(2, -1, 0)).getBlock()) && this.getBlock(feet.add(2, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(feet.add(2, 1, 0)).getBlock() == Blocks.AIR) {
                this.surroundMine(feet.add(1, 0, 0));
            }
            else if (this.getBlock(feet.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(-1, 0, 0)).getBlock() != Blocks.AIR && this.godBlocks.contains(this.getBlock(feet.add(-2, -1, 0)).getBlock()) && this.getBlock(feet.add(-2, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(feet.add(-2, 1, 0)).getBlock() == Blocks.AIR) {
                this.surroundMine(feet.add(-1, 0, 0));
            }
            else if (this.getBlock(feet.add(0, 0, 1)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(0, 0, 1)).getBlock() != Blocks.AIR && this.godBlocks.contains(this.getBlock(feet.add(0, -1, 2)).getBlock()) && this.getBlock(feet.add(0, 0, 2)).getBlock() == Blocks.AIR && this.getBlock(feet.add(0, 1, 2)).getBlock() == Blocks.AIR) {
                this.surroundMine(feet.add(0, 0, 1));
            }
            else if (this.getBlock(feet.add(0, 0, -1)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(0, 0, -1)).getBlock() != Blocks.AIR && this.godBlocks.contains(this.getBlock(feet.add(0, -1, -2)).getBlock()) && this.getBlock(feet.add(0, 0, -2)).getBlock() == Blocks.AIR && this.getBlock(feet.add(0, 1, -2)).getBlock() == Blocks.AIR) {
                this.surroundMine(feet.add(0, 0, -1));
            }
            else if (this.getBlock(feet.add(1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(1, 0, 0)).getBlock() != Blocks.AIR && this.godBlocks.contains(this.getBlock(feet.add(1, -1, 0)).getBlock()) && this.getBlock(feet.add(1, 1, 0)).getBlock() == Blocks.AIR) {
                this.surroundMine(feet.add(1, 0, 0));
            }
            else if (this.getBlock(feet.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(-1, 0, 0)).getBlock() != Blocks.AIR && this.godBlocks.contains(this.getBlock(feet.add(-1, -1, 0)).getBlock()) && this.getBlock(feet.add(-1, 1, 0)).getBlock() == Blocks.AIR) {
                this.surroundMine(feet.add(-1, 0, 0));
            }
            else if (this.getBlock(feet.add(0, 0, 1)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(0, 0, 1)).getBlock() != Blocks.AIR && this.godBlocks.contains(this.getBlock(feet.add(0, -1, 1)).getBlock()) && this.getBlock(feet.add(0, 1, 1)).getBlock() == Blocks.AIR) {
                this.surroundMine(feet.add(0, 0, 1));
            }
            else if (this.getBlock(feet.add(0, 0, -1)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(0, 0, -1)).getBlock() != Blocks.AIR && this.godBlocks.contains(this.getBlock(feet.add(0, -1, -1)).getBlock()) && this.getBlock(feet.add(0, 1, -1)).getBlock() == Blocks.AIR) {
                this.surroundMine(feet.add(0, 0, -1));
            }
            else if (this.getBlock(feet.add(1, 0, 0)).getBlock() != Blocks.BEDROCK && this.godBlocks.contains(this.getBlock(feet.add(1, -1, 0)).getBlock()) && this.getBlock(feet.add(1, 1, 0)).getBlock() != Blocks.AIR && this.getBlock(feet.add(1, 1, 0)).getBlock() != Blocks.BEDROCK) {
                this.surroundMine(feet.add(1, 1, 0));
            }
            else if (this.getBlock(feet.add(-1, 0, 0)).getBlock() != Blocks.BEDROCK && this.godBlocks.contains(this.getBlock(feet.add(-1, -1, 0)).getBlock()) && this.getBlock(feet.add(-1, 1, 0)).getBlock() != Blocks.AIR && this.getBlock(feet.add(-1, 1, 0)).getBlock() != Blocks.BEDROCK) {
                this.surroundMine(feet.add(-1, 1, 0));
            }
            else if (this.getBlock(feet.add(0, 0, 1)).getBlock() != Blocks.BEDROCK && this.godBlocks.contains(this.getBlock(feet.add(0, -1, 1)).getBlock()) && this.getBlock(feet.add(0, 1, 1)).getBlock() != Blocks.AIR && this.getBlock(feet.add(0, 1, 1)).getBlock() != Blocks.BEDROCK) {
                this.surroundMine(feet.add(0, 1, 1));
            }
            else if (this.getBlock(feet.add(0, 0, -1)).getBlock() != Blocks.BEDROCK && this.godBlocks.contains(this.getBlock(feet.add(0, -1, -1)).getBlock()) && this.getBlock(feet.add(0, 1, -1)).getBlock() != Blocks.AIR && this.getBlock(feet.add(0, 1, -1)).getBlock() != Blocks.BEDROCK) {
                this.surroundMine(feet.add(0, 1, -1));
            }
        }
        else if (this.getBlock(feet.add(1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(feet.add(1, 1, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(1, 1, 0)).getBlock() != Blocks.AIR && this.godBlocks.contains(this.getBlock(feet.add(1, -1, 0)).getBlock()) && (this.getBlock(feet.add(2, 0, 0)).getBlock() != Blocks.AIR || this.getBlock(feet.add(2, 1, 0)).getBlock() != Blocks.AIR)) {
            this.surroundMine(feet.add(1, 1, 0));
        }
        else if (this.getBlock(feet.add(-1, 0, 0)).getBlock() == Blocks.AIR && this.getBlock(feet.add(-1, 1, 0)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(-1, 1, 0)).getBlock() != Blocks.AIR && this.godBlocks.contains(this.getBlock(feet.add(-1, -1, 0)).getBlock()) && (this.getBlock(feet.add(-2, 0, 0)).getBlock() != Blocks.AIR || this.getBlock(feet.add(-2, 1, 0)).getBlock() != Blocks.AIR)) {
            this.surroundMine(feet.add(-1, 1, 0));
        }
        else if (this.getBlock(feet.add(0, 0, 1)).getBlock() == Blocks.AIR && this.getBlock(feet.add(0, 1, 1)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(0, 1, 1)).getBlock() != Blocks.AIR && this.godBlocks.contains(this.getBlock(feet.add(0, -1, 1)).getBlock()) && (this.getBlock(feet.add(0, 0, 2)).getBlock() != Blocks.AIR || this.getBlock(feet.add(0, 1, 2)).getBlock() != Blocks.AIR)) {
            this.surroundMine(feet.add(0, 1, 1));
        }
        else if (this.getBlock(feet.add(0, 0, -1)).getBlock() == Blocks.AIR && this.getBlock(feet.add(0, 1, -1)).getBlock() != Blocks.BEDROCK && this.getBlock(feet.add(0, 1, -1)).getBlock() != Blocks.AIR && this.godBlocks.contains(this.getBlock(feet.add(0, -1, -1)).getBlock()) && (this.getBlock(feet.add(0, 0, -2)).getBlock() != Blocks.AIR || this.getBlock(feet.add(0, 1, -2)).getBlock() != Blocks.AIR)) {
            this.surroundMine(feet.add(0, 1, -1));
        }
        else {
            AutoCityPlus.target = null;
        }
    }
    
    private void surroundMine(final BlockPos position) {
        if (InstantMine.breakPos != null) {
            if (InstantMine.breakPos.equals(position)) {
                return;
            }
            if (InstantMine.breakPos.equals(new BlockPos(AutoCityPlus.target.posX, AutoCityPlus.target.posY, AutoCityPlus.target.posZ)) && AutoCityPlus.mc.world.getBlockState(new BlockPos(AutoCityPlus.target.posX, AutoCityPlus.target.posY, AutoCityPlus.target.posZ)).getBlock() != Blocks.AIR) {
                return;
            }
            if (InstantMine.breakPos.equals(new BlockPos(AutoCityPlus.mc.player.posX, AutoCityPlus.mc.player.posY + 2.0, AutoCityPlus.mc.player.posZ))) {
                return;
            }
            if (AutoCityPlus.mc.player.rotationPitch <= 90.0f && AutoCityPlus.mc.player.rotationPitch >= 80.0f) {
                return;
            }
            if (AutoCityPlus.mc.world.getBlockState(InstantMine.breakPos).getBlock() == Blocks.WEB) {
                return;
            }
            if (lover.moduleManager.isModuleEnabled("Anti32k") && AutoCityPlus.mc.world.getBlockState(InstantMine.breakPos).getBlock() instanceof BlockHopper) {
                return;
            }
            if (lover.moduleManager.isModuleEnabled("AntiShulkerBox") && AutoCityPlus.mc.world.getBlockState(InstantMine.breakPos).getBlock() instanceof BlockShulkerBox) {
                return;
            }
        }
        AutoCityPlus.mc.playerController.onPlayerDamageBlock(position, BlockUtil.getRayTraceFacing(position));
    }
    
    private boolean detection(final EntityPlayer player) {
        return AutoCityPlus.mc.world.getBlockState(new BlockPos(player.posX + 1.2, player.posY, player.posZ)).getBlock() == Blocks.AIR || AutoCityPlus.mc.world.getBlockState(new BlockPos(player.posX - 1.2, player.posY, player.posZ)).getBlock() == Blocks.AIR || AutoCityPlus.mc.world.getBlockState(new BlockPos(player.posX, player.posY, player.posZ + 1.2)).getBlock() == Blocks.AIR || AutoCityPlus.mc.world.getBlockState(new BlockPos(player.posX, player.posY, player.posZ - 1.2)).getBlock() == Blocks.AIR;
    }
    
    private boolean detection2(final EntityPlayer player) {
        return (AutoCityPlus.mc.world.getBlockState(new BlockPos(player.posX + 1.2, player.posY, player.posZ)).getBlock() == Blocks.AIR && AutoCityPlus.mc.world.getBlockState(new BlockPos(player.posX + 1.2, player.posY + 1.0, player.posZ)).getBlock() == Blocks.AIR) || (AutoCityPlus.mc.world.getBlockState(new BlockPos(player.posX - 1.2, player.posY, player.posZ)).getBlock() == Blocks.AIR && AutoCityPlus.mc.world.getBlockState(new BlockPos(player.posX - 1.2, player.posY + 1.0, player.posZ)).getBlock() == Blocks.AIR) || (AutoCityPlus.mc.world.getBlockState(new BlockPos(player.posX, player.posY, player.posZ + 1.2)).getBlock() == Blocks.AIR && AutoCityPlus.mc.world.getBlockState(new BlockPos(player.posX, player.posY + 1.0, player.posZ + 1.2)).getBlock() == Blocks.AIR) || (AutoCityPlus.mc.world.getBlockState(new BlockPos(player.posX, player.posY, player.posZ - 1.2)).getBlock() == Blocks.AIR && AutoCityPlus.mc.world.getBlockState(new BlockPos(player.posX, player.posY + 1.0, player.posZ - 1.2)).getBlock() == Blocks.AIR);
    }
    
    private EntityPlayer getTarget(final double range) {
        EntityPlayer target = null;
        double distance = range;
        for (final EntityPlayer player : AutoCityPlus.mc.world.playerEntities) {
            if (EntityUtil.isntValid(player, range)) {
                continue;
            }
            if (this.detection2(player)) {
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
        return target;
    }
    
    private IBlockState getBlock(final BlockPos block) {
        return AutoCityPlus.mc.world.getBlockState(block);
    }
    
    static {
        AutoCityPlus.INSTANCE = new AutoCityPlus();
    }
}
