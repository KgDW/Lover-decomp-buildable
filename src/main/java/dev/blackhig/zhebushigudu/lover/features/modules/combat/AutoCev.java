package dev.blackhig.zhebushigudu.lover.features.modules.combat;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBlock;
import net.minecraft.block.Block;
import dev.blackhig.zhebushigudu.lover.features.command.Command;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import dev.blackhig.zhebushigudu.lover.util.CrystalUtil;
import dev.blackhig.zhebushigudu.lover.util.BlockUtils;
import net.minecraft.util.math.Vec3i;
import java.util.Arrays;
import net.minecraft.entity.MoverType;
import net.minecraft.item.ItemStack;
import dev.blackhig.zhebushigudu.lover.lover;
import net.minecraft.entity.player.EntityPlayer;
import dev.blackhig.zhebushigudu.lover.util.InventoryUtil;
import net.minecraft.init.Blocks;
import dev.blackhig.zhebushigudu.lover.util.Util;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.Entity;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import dev.blackhig.zhebushigudu.lover.features.modules.Module;

public class AutoCev extends Module
{
    private final Setting<Double> range;
    Entity currentEntity;
    boolean flag;
    int progress;
    int sleep;
    int civCounter;
    boolean breakFlag;
    private BlockPos breakPos;
    
    public AutoCev() {
        super("HighPlaceCev", "AutoCev", Category.COMBAT, true, false, false);
        this.range = this.register(new Setting<>("Range", 4.9, 0.0, 6.0));
        this.progress = 0;
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
    }
    
    private int findItem(final Item item) {
        if (item == Items.END_CRYSTAL && Util.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
            return 999;
        }
        for (int i = 0; i < 9; ++i) {
            if (Util.mc.player.inventory.getStackInSlot(i).getItem() == item) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public void onEnable() {
        this.findTarget();
        this.progress = 0;
        this.breakFlag = false;
        this.flag = false;
        this.civCounter = 0;
        this.sleep = 0;
        super.onEnable();
    }
    
    @Override
    public void onTick() {
        final int n = this.findItem(Items.DIAMOND_PICKAXE);
        final int n2 = this.findItem(Items.END_CRYSTAL);
        final int n3 = this.findMaterials(Blocks.OBSIDIAN);
        final BlockPos[] objectArray = { new BlockPos(0, 0, 1), new BlockPos(0, 1, 1), new BlockPos(0, 2, 1), new BlockPos(0, 3, 1), new BlockPos(0, 3, 0), new BlockPos(1, 2, 0), new BlockPos(-1, 2, 0), new BlockPos(0, 2, -1), new BlockPos(0, 1, -1), new BlockPos(0, 1, 1), new BlockPos(1, 1, 0), new BlockPos(-1, 1, 0) };
        final int n4 = InventoryUtil.getSlot();
        if (n != -1 && n2 != -1 && n3 != -1) {
            if (this.currentEntity == null || this.currentEntity.getDistance((Entity)Util.mc.player) > this.range.getValue()) {
                this.findTarget();
            }
            if (this.currentEntity != null) {
                final Entity entity = this.currentEntity;
                if (entity instanceof EntityPlayer && !lover.friendManager.isFriend(entity.getName())) {
                    if (n2 == -1 || n2 != -1 || !Util.mc.player.inventory.offHandInventory.get(0).getItem().getClass().equals(Item.getItemById(426).getClass())) {}
                    if (this.sleep > 0) {
                        --this.sleep;
                    }
                    else {
                        entity.move(MoverType.SELF, 0.0, -2.0, 0.0);
                        switch (this.progress) {
                            case 0: {
                                final BlockPos blockPos = new BlockPos(entity);
                                final BlockPos[] var16 = objectArray;
                                for (int var17 = objectArray.length, var18 = 0; var18 < var17; ++var18) {
                                    final BlockPos blockPos2 = var16[var18];
                                    if (Arrays.asList(objectArray).indexOf(blockPos2) != -1 && this.civCounter < 1) {
                                        this.flag = true;
                                        InventoryUtil.setSlot(n3);
                                    }
                                    else {
                                        InventoryUtil.setSlot(n3);
                                    }
                                    final BlockUtils blockUtils = BlockUtils.isPlaceable(blockPos.add((Vec3i)blockPos2), 0.0, true);
                                    if (blockUtils != null) {
                                        blockUtils.doPlace(true);
                                    }
                                }
                                InventoryUtil.setSlot(n2);
                                CrystalUtil.placeCrystal(new BlockPos(entity.posX, entity.posY + 4.0, entity.posZ));
                                ++this.progress;
                                break;
                            }
                            case 1: {
                                InventoryUtil.setSlot(n);
                                Util.mc.playerController.onPlayerDamageBlock(new BlockPos(entity).add(0, 3, 0), EnumFacing.UP);
                                Util.mc.getConnection().sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, new BlockPos(entity).add(0, 3, 0), EnumFacing.UP));
                                if (Util.mc.world.isAirBlock(new BlockPos(entity).add(0, 3, 0))) {
                                    for (final Entity entity2 : Util.mc.world.loadedEntityList) {
                                        if (entity.getDistance(entity2) <= this.range.getValue() && entity2 instanceof EntityEnderCrystal) {
                                            Util.mc.playerController.attackEntity(Util.mc.player, entity2);
                                        }
                                    }
                                    this.breakFlag = true;
                                }
                                if (this.civCounter < 1) {
                                    Util.mc.playerController.onPlayerDamageBlock(new BlockPos(entity).add(0, 3, 0), EnumFacing.UP);
                                    this.sleep += 30;
                                }
                                ++this.progress;
                                break;
                            }
                            case 2: {
                                int n5 = 0;
                                for (final Entity entity3 : Util.mc.world.loadedEntityList) {
                                    if (entity.getDistance(entity3) <= this.range.getValue() && entity3 instanceof EntityEnderCrystal) {
                                        Util.mc.playerController.attackEntity((EntityPlayer)Util.mc.player, entity3);
                                        ++n5;
                                    }
                                }
                                if (n5 == 0 || this.flag) {
                                    ++this.progress;
                                    break;
                                }
                                break;
                            }
                            case 3: {
                                BlockUtils.doPlace(BlockUtils.isPlaceable(new BlockPos(entity.posX, entity.posY + 3.0, entity.posZ), 0.0, true), true);
                                InventoryUtil.setSlot(n3);
                                this.progress = 0;
                                ++this.civCounter;
                                break;
                            }
                        }
                    }
                    InventoryUtil.setSlot(n4);
                    return;
                }
                InventoryUtil.setSlot(n4);
            }
        }
        else {
            Command.sendMessage("Pix or Crystal or Obsidian No Material");
            this.disable();
        }
    }
    
    private int findMaterials(final Block block) {
        for (int i = 0; i < 9; ++i) {
            if (Util.mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemBlock && ((ItemBlock)Util.mc.player.inventory.getStackInSlot(i).getItem()).getBlock() == block) {
                return i;
            }
        }
        return -1;
    }
    
    public void findTarget() {
        this.currentEntity = Util.mc.world.loadedEntityList.stream().filter(entity -> entity != Util.mc.player && entity instanceof EntityLivingBase && entity.getDistance((Entity)Util.mc.player) < this.range.getValue() && !lover.friendManager.isFriend(entity.getName())).findFirst().orElse(null);
    }
}
