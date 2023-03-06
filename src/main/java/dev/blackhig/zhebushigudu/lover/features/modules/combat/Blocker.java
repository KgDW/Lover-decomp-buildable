package dev.blackhig.zhebushigudu.lover.features.modules.combat;

import java.util.Comparator;
import net.minecraft.entity.item.EntityEnderCrystal;
import dev.blackhig.zhebushigudu.lover.util.Anti.BlockUtil;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.Entity;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.features.modules.Module;

public class Blocker extends Module
{
    Setting<Boolean> piston;
    Setting<Boolean> cev;
    Setting<Boolean> packetPlace;
    Setting<Integer> range;
    private BlockPos b_piston;
    private BlockPos b_cev;
    
    public Blocker() {
        super("Blocker", "Blocked piston and cev", Category.COMBAT, true, false, false);
        this.piston = this.register(new Setting<>("Piston", true));
        this.cev = this.register(new Setting<>("CevBreaker", true));
        this.packetPlace = this.register(new Setting<>("SpeedUP Place", true));
        this.range = this.register(new Setting<>("Range", 6, 1, 10));
        this.b_piston = null;
        this.b_cev = null;
    }

    private IBlockState getBlock(final BlockPos blockPos) {
        return Blocker.mc.world.getBlockState(blockPos);
    }
    
    private int findMaterials() {
        for (int i = 0; i < 9; ++i) {
            if (Blocker.mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemBlock && ((ItemBlock)Blocker.mc.player.inventory.getStackInSlot(i).getItem()).getBlock() == Blocks.OBSIDIAN) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public void onTick() {
        if (Blocker.mc.player != null) {
            try {
                final int n2 = this.findMaterials();
                if (n2 == -1) {
                    return;
                }
                final BlockPos blockPos = new BlockPos(Blocker.mc.player.posX, Blocker.mc.player.posY, Blocker.mc.player.posZ);
                if (this.piston.getValue()) {
                    final BlockPos[] blockPosArray = { new BlockPos(2, 1, 0), new BlockPos(-2, 1, 0), new BlockPos(0, 1, 2), new BlockPos(0, 1, -2) };
                    for (int n3 = 0; n3 < 4; ++n3) {
                        for (BlockPos pos : blockPosArray) {
                            final BlockPos blockPos2 = blockPos.add((Vec3i) pos.add(0, n3, 0));
                            if (this.getBlock(blockPos2).getBlock() == Blocks.PISTON || this.getBlock(blockPos2).getBlock() == Blocks.STICKY_PISTON) {
                                this.b_piston = blockPos2;
                            }
                        }
                    }
                    if (this.b_piston != null) {
                        if (this.getBlock(this.b_piston).getBlock() == Blocks.AIR) {
                            if (Blocker.mc.player.getDistance(this.b_piston.getX(), this.b_piston.getY(), this.b_piston.getZ()) > this.range.getValue()) {
                                return;
                            }
                            final int n3 = Blocker.mc.player.inventory.currentItem;
                            Blocker.mc.player.inventory.currentItem = n2;
                            Blocker.mc.playerController.updateController();
                            Blocker.mc.playerController.updateController();
                            Blocker.mc.playerController.onPlayerDamageBlock(this.b_piston, EnumFacing.DOWN);
                            BlockUtil.placeBlock(this.b_piston, EnumHand.MAIN_HAND, true, this.packetPlace.getValue(), false);
                            Blocker.mc.player.inventory.currentItem = n3;
                            Blocker.mc.playerController.updateController();
                        }
                        if (this.getBlock(this.b_piston).getBlock() == Blocks.OBSIDIAN || Blocker.mc.player.getDistance(this.b_piston.getX(), this.b_piston.getY(), (double)this.b_piston.getZ()) > this.range.getValue()) {
                            this.b_piston = null;
                        }
                    }
                }
                if (this.cev.getValue()) {
                    final BlockPos blockPos3 = new BlockPos(Blocker.mc.player.posX, (double)Math.round(Blocker.mc.player.posY), Blocker.mc.player.posZ);
                    final Entity entity3 = (Entity)Blocker.mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityEnderCrystal).filter(entity -> Blocker.mc.player.getDistance(entity.posX, Blocker.mc.player.posY, entity.posZ) < 1.0).min(Comparator.comparing(entity -> Blocker.mc.player.getDistanceSq(entity.posX, entity.posY, entity.posZ))).orElse(null);
                    if (this.getBlock(new BlockPos(blockPos3.getX(), blockPos3.getY() + 2, blockPos3.getZ())).getBlock() == Blocks.OBSIDIAN && entity3 != null) {
                        this.b_cev = new BlockPos(entity3.posX, entity3.posY, entity3.posZ);
                    }
                    if (this.getBlock(new BlockPos(blockPos3.getX() + 1, blockPos3.getY() + 1, blockPos3.getZ())).getBlock() == Blocks.OBSIDIAN && entity3 != null) {
                        this.b_cev = new BlockPos(entity3.posX, entity3.posY, entity3.posZ);
                    }
                    if (this.getBlock(new BlockPos(blockPos3.getX() - 1, blockPos3.getY() + 1, blockPos3.getZ())).getBlock() == Blocks.OBSIDIAN && entity3 != null) {
                        this.b_cev = new BlockPos(entity3.posX, entity3.posY, entity3.posZ);
                    }
                    if (this.getBlock(new BlockPos(blockPos3.getX(), blockPos3.getY() + 1, blockPos3.getZ() + 1)).getBlock() == Blocks.OBSIDIAN && entity3 != null) {
                        this.b_cev = new BlockPos(entity3.posX, entity3.posY, entity3.posZ);
                    }
                    if (this.getBlock(new BlockPos(blockPos3.getX(), blockPos3.getY() + 1, blockPos3.getZ() - 1)).getBlock() == Blocks.OBSIDIAN && entity3 != null) {
                        this.b_cev = new BlockPos(entity3.posX, entity3.posY, entity3.posZ);
                    }
                    if (this.b_cev != null && this.getBlock(this.b_cev).getBlock() == Blocks.AIR) {
                        if (Blocker.mc.player.getDistance(this.b_cev.getX(), this.b_cev.getY(), this.b_cev.getZ()) > this.range.getValue()) {
                            return;
                        }
                        if (entity3 == null) {
                            final BlockPos blockPos4 = new BlockPos(Blocker.mc.player.posX, this.b_cev.getY(), Blocker.mc.player.posZ);
                            if (blockPos4.getDistance(this.b_cev.getX(), this.b_cev.getY(), this.b_cev.getZ()) < 1.0) {
                                final int n4 = Blocker.mc.player.inventory.currentItem;
                                Blocker.mc.player.inventory.currentItem = n2;
                                Blocker.mc.playerController.updateController();
                                BlockUtil.placeBlock(this.b_cev.add(0, -1, 0), EnumHand.MAIN_HAND, true, false, false);
                                BlockUtil.placeBlock(this.b_cev, EnumHand.MAIN_HAND, true, false, false);
                                Blocker.mc.player.inventory.currentItem = n4;
                                Blocker.mc.playerController.updateController();
                                this.b_cev = null;
                            }
                        }
                    }
                }
                if (this.cev.getValue()) {
                    final BlockPos blockPos3 = new BlockPos(Blocker.mc.player.posX, Blocker.mc.player.posY, Blocker.mc.player.posZ);
                    final Entity entity3 = Blocker.mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityEnderCrystal).filter(entity -> Blocker.mc.player.getDistance(entity.posX, Blocker.mc.player.posY, entity.posZ) < 1.0).min(Comparator.comparing(entity -> Blocker.mc.player.getDistanceSq(entity.posX, entity.posY, entity.posZ))).orElse(null);
                    if (this.getBlock(new BlockPos(blockPos3.getX(), blockPos3.getY() + 2, blockPos3.getZ())).getBlock() == Blocks.OBSIDIAN && entity3 != null) {
                        this.b_cev = new BlockPos(entity3.posX, entity3.posY, entity3.posZ);
                    }
                    if (this.b_cev != null && this.getBlock(this.b_cev).getBlock() == Blocks.AIR) {
                        if (Blocker.mc.player.getDistance(this.b_cev.getX(), this.b_cev.getY(), this.b_cev.getZ()) > this.range.getValue()) {
                            return;
                        }
                        if (entity3 == null) {
                            final BlockPos blockPos4 = new BlockPos(Blocker.mc.player.posX, this.b_cev.getY(), Blocker.mc.player.posZ);
                            if (blockPos4.getDistance(this.b_cev.getX(), this.b_cev.getY(), this.b_cev.getZ()) < 1.0) {
                                final int n4 = Blocker.mc.player.inventory.currentItem;
                                Blocker.mc.player.inventory.currentItem = n2;
                                Blocker.mc.playerController.updateController();
                                BlockUtil.placeBlock(this.b_cev.add(0, -1, 0), EnumHand.MAIN_HAND, true, false, false);
                                BlockUtil.placeBlock(this.b_cev, EnumHand.MAIN_HAND, true, false, false);
                                Blocker.mc.player.inventory.currentItem = n4;
                                Blocker.mc.playerController.updateController();
                                this.b_cev = null;
                            }
                        }
                    }
                }
            }
            catch (final Exception var8) {
                this.b_cev = null;
                this.b_piston = null;
            }
        }
    }
}
