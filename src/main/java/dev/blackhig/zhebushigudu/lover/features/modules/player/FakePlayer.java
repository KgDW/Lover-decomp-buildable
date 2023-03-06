package dev.blackhig.zhebushigudu.lover.features.modules.player;

import com.mojang.authlib.GameProfile;
import dev.blackhig.zhebushigudu.lover.features.modules.Module;
import dev.blackhig.zhebushigudu.lover.features.setting.Setting;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.GameType;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.UUID;

public class FakePlayer extends Module
{
    private EntityOtherPlayerMP clonedPlayer;
    private final Setting<Integer> setHealth;
    private final Setting<String> playername;
    private final Setting<Boolean> iscopyplayerstack;
    private final Setting<Boolean> runner;
    private ItemStack[] playerarmor;
    
    public FakePlayer() {
        super("FakePlayer", "Spawns a FakePlayer for testing", Category.PLAYER, true, false, false);
        this.setHealth = this.register(new Setting<>("SetHealth", 20, 1, 20));
        this.playername = this.register(new Setting<>("", "LoverHack"));
        this.iscopyplayerstack = this.register(new Setting<>("CopyInventory", true));
        this.runner = this.register(new Setting<>("Runner", false));
        this.playerarmor = null;
        this.playerarmor = new ItemStack[] { new ItemStack((Item)Items.DIAMOND_BOOTS), new ItemStack((Item)Items.DIAMOND_LEGGINGS), new ItemStack((Item)Items.DIAMOND_CHESTPLATE), new ItemStack((Item)Items.DIAMOND_HELMET) };
    }
    
    @Override
    public void onEnable() {
        if (FakePlayer.mc.player == null || FakePlayer.mc.player.isDead) {
            this.disable();
            return;
        }
        final InventoryPlayer a = FakePlayer.mc.player.inventory;
        (this.clonedPlayer = new EntityOtherPlayerMP(FakePlayer.mc.world, new GameProfile(UUID.fromString("a3ca166d-c5f1-3d5a-baac-b18a5b38d4cd"), this.playername.getValue()))).copyLocationAndAnglesFrom(FakePlayer.mc.player);
        this.clonedPlayer.rotationYawHead = FakePlayer.mc.player.rotationYawHead;
        this.clonedPlayer.rotationYaw = FakePlayer.mc.player.rotationYaw;
        this.clonedPlayer.rotationPitch = FakePlayer.mc.player.rotationPitch;
        this.clonedPlayer.setGameType(GameType.SURVIVAL);
        this.clonedPlayer.setHealth((float)this.setHealth.getValue());
        FakePlayer.mc.world.addEntityToWorld(-114514, this.clonedPlayer);
        if (this.iscopyplayerstack.getValue()) {
            this.clonedPlayer.inventory.copyInventory(a);
        }
        this.clonedPlayer.onLivingUpdate();
    }
    
    @SubscribeEvent
    public void input(final InputUpdateEvent e) {
        System.out.println("e.getMovementInput() = " + e.getMovementInput());
    }
    
    @Override
    public void onDisable() {
        if (FakePlayer.mc.world != null) {
            FakePlayer.mc.world.removeEntityFromWorld(-114514);
        }
    }
    
    private double roundValueToCenter(final double inputVal) {
        double roundVal = (double)Math.round(inputVal);
        if (roundVal > inputVal) {
            roundVal -= 0.5;
        }
        else if (roundVal <= inputVal) {
            roundVal += 0.5;
        }
        return roundVal;
    }
}
